/*
 * Copyright (C) 2021 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia / South America
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.dynamia.modules.entityfiles.s3;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import tools.dynamia.commons.SimpleCache;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.io.ImageUtil;
import tools.dynamia.modules.entityfile.EntityFileException;
import tools.dynamia.modules.entityfile.EntityFileStorage;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.UploadedFileInfo;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.enums.EntityFileType;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * {@link EntityFileStorage} implementation that store files in Amazon S3 service.
 * The following environment variables are required AWS_ACCESS_KEY_ID, AWS_SECRET_KEY, AWS_S3_ENDPOINT, AWS_S3_BUCKET. The
 * values are loaded using Spring {@link Environment} service
 */
@Service
public class S3EntityFileStorage implements EntityFileStorage {

    public static final String AWS_ACCESS_KEY_ID = "AWS_ACCESS_KEY_ID";
    public static final String AWS_SECRET_KEY = "AWS_SECRET_KEY";
    public static final String AWS_S3_ENDPOINT = "AWS_S3_ENDPOINT";
    public static final String AWS_S3_BUCKET = "AWS_S3_BUCKET";
    private final LoggingService logger = new SLF4JLoggingService(S3EntityFileStorage.class);

    private final SimpleCache<String, String> URL_CACHE = new SimpleCache<>();

    public static final String ID = "AWSS3Storage";


    @Autowired
    private Environment environment;

    private AmazonS3 connection;


    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "AWS S3 Storage";
    }

    @Override
    public void upload(EntityFile entityFile, UploadedFileInfo fileInfo) {
        try {

            String folder = getAccountFolderName(entityFile.getAccountId());
            String fileName = getFileName(entityFile);

            // Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("name", entityFile.getName());
            metadata.addUserMetadata("accountId", entityFile.getAccountId().toString());
            metadata.addUserMetadata("uuid", entityFile.getUuid());
            metadata.addUserMetadata("creator", entityFile.getCreator());
            metadata.addUserMetadata("description", entityFile.getDescription());


            PutObjectRequest request = new PutObjectRequest(getBucketName(), folder + fileName, fileInfo.getInputStream(), metadata);
            request.setCannedAcl(CannedAccessControlList.Private);
            AccessControlList acl = new AccessControlList();
            request.setAccessControlList(acl);

            // ACL
            if (entityFile.isShared()) {
                acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
                request.setCannedAcl(CannedAccessControlList.PublicRead);
            }

            getConnection().putObject(request);

        } catch (AmazonClientException amazonClientException) {
            logger.error("Error uploading entity file " + entityFile.getName() + " to S3", amazonClientException);
            throw new EntityFileException("Error uploading file " + entityFile.getName(), amazonClientException);
        }
    }

    @Override
    public StoredEntityFile download(EntityFile entityFile) {
        String urlKey = entityFile.getUuid();
        String url = URL_CACHE.get(urlKey);
        String fileName = getFileName(entityFile);

        if (url == null) {

            String folder = getAccountFolderName(entityFile.getAccountId());
            if (entityFile.isShared()) {
                url = generateStaticURL(getBucketName(), folder + fileName);
                URL_CACHE.add(urlKey, url);
            } else {
                url = generateSignedURL(getBucketName(), folder + fileName);
            }
        }

        return new S3StoredEntityFile(entityFile, url, new File(fileName));
    }

    private String generateSignedURL(String bucketName, String fileName) {
        try {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName);
            return getConnection().generatePresignedUrl(request).toString();

        } catch (AmazonClientException amazonClientException) {
            logger.error("Error generating URL for " + bucketName + " => " + fileName, amazonClientException);
            return "#";
        }
    }

    private String generateStaticURL(String bucketName, String fileName) {
        String endpoint = getEndpoint();
        return String.format("https://%s.%s/%s", bucketName, endpoint, fileName);
    }


    private String getFileName(EntityFile entityFile) {
        String subfolder = "";
        if (entityFile.getSubfolder() != null) {
            subfolder = entityFile.getSubfolder() + "/";
        }

        String storedFileName = entityFile.getUuid() + "_" + entityFile.getName();
        if (entityFile.getStoredFileName() != null && !entityFile.getStoredFileName().isEmpty()) {
            storedFileName = entityFile.getStoredFileName();
        }

        return subfolder + storedFileName;
    }

    private AmazonS3 getConnection() {

        if (connection == null) {
            ClientConfiguration clientConfig = new ClientConfiguration();
            clientConfig.setProtocol(Protocol.HTTPS);

            AWSCredentials credentials = new BasicAWSCredentials(getAccessKey(), getSecretKey());
            connection = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withClientConfiguration(clientConfig)
                    .build();

            connection.setEndpoint(getEndpoint());

            String bucketName = getBucketName();
            if (!connection.doesBucketExistV2(bucketName)) {
                connection.createBucket(bucketName);
            }

        }
        return connection;
    }

    private String getAccountFolderName(Long accountId) {
        return "account" + accountId + "/";
    }

    private String generateThumbnailURL(EntityFile entityFile, int w, int h) {
        if (entityFile.getType() == EntityFileType.IMAGE) {
            String urlKey = entityFile.getUuid() + w + "x" + h;
            String url = URL_CACHE.get(urlKey);
            if (url == null) {
                String folder = getAccountFolderName(entityFile.getAccountId());
                String fileName = getFileName(entityFile);
                String thumbfileName = "thumbs/" + w + "x" + h + "/" + fileName;

                if (!objectExists(getBucketName(), folder + thumbfileName)) {
                    createAndUploadThumbnail(entityFile, getBucketName(), folder, fileName, thumbfileName, w, h);
                }

                url = generateStaticURL(getBucketName(), folder + thumbfileName);
                URL_CACHE.add(urlKey, url);
            }
            return url;
        } else {
            return "#";
        }
    }

    private void createAndUploadThumbnail(EntityFile entityFile, String bucketName, String folder, String fileName, String thumbfileName,
                                          int w, int h) throws AmazonClientException {
        try {
            File localDestination = File.createTempFile(System.currentTimeMillis() + "file", entityFile.getName());
            File localThumbDestination = File.createTempFile(System.currentTimeMillis() + "thumb", entityFile.getName());

            getConnection().getObject(new GetObjectRequest(bucketName, folder + fileName), localDestination);

            ImageUtil.resizeImage(localDestination, localThumbDestination, entityFile.getExtension(), w, h);

            PutObjectRequest request = new PutObjectRequest(bucketName, folder + thumbfileName, localThumbDestination);

            // metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("thumbnail", "true");
            metadata.addUserMetadata("name", entityFile.getName());
            metadata.addUserMetadata("description", entityFile.getDescription());
            metadata.setContentType("image/" + entityFile.getExtension());
            request.setMetadata(metadata);

            // ACL
            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            request.setCannedAcl(CannedAccessControlList.PublicRead);
            request.setAccessControlList(acl);
            getConnection().putObject(request);

        } catch (Exception e) {
            logger.error("Error creating thumbnail for " + entityFile.getName() + "  " + w + "x" + h + "  " + fileName, e);
        }
    }

    public boolean objectExists(String bucketName, String key) {
        try {
            getConnection().getObjectMetadata(bucketName, key);
        } catch (AmazonServiceException e) {
            return false;
        }
        return true;
    }


    @Override
    public void delete(EntityFile entityFile) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void resetConnection() {
        if (connection != null) {
            connection.shutdown();
            connection = null;
        }
    }


    public String getBucketName() {
        return environment.getProperty(AWS_S3_BUCKET);
    }

    public String getEndpoint() {
        return environment.getProperty(AWS_S3_ENDPOINT);
    }

    public String getAccessKey() {
        return environment.getProperty(AWS_ACCESS_KEY_ID);
    }

    public String getSecretKey() {
        return environment.getProperty(AWS_SECRET_KEY);
    }

    class S3StoredEntityFile extends StoredEntityFile {

        public S3StoredEntityFile(EntityFile entityFile, String url, File realFile) {
            super(entityFile, url, realFile);
        }

        @Override
        public String getThumbnailUrl(int width, int height) {
            return generateThumbnailURL(getEntityFile(), width, width);
        }
    }


}
