/*
 * Copyright (C) 2023 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import tools.dynamia.commons.SimpleCache;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.domain.query.ApplicationParameters;
import tools.dynamia.io.ImageUtil;
import tools.dynamia.modules.entityfile.EntityFileException;
import tools.dynamia.modules.entityfile.EntityFileStorage;
import tools.dynamia.modules.entityfile.StoredEntityFile;
import tools.dynamia.modules.entityfile.UploadedFileInfo;
import tools.dynamia.modules.entityfile.domain.EntityFile;
import tools.dynamia.modules.entityfile.enums.EntityFileType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
    private final SimpleCache<String, String> PARAMS_CACHE = new SimpleCache<>();

    public static final String ID = "AWSS3Storage";


    @Autowired
    private Environment environment;


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

            File tmpFile = null;
            long sourceLength = 0;
            long length = fileInfo.getLength();


            if (fileInfo.getSource() instanceof File file) {
                sourceLength = file.length();
                tmpFile = file;
            } else if (fileInfo.getInputStream() instanceof Path path) {
                sourceLength = path.toFile().length();
                tmpFile = path.toFile();
            }

            if (length <= 0 && sourceLength > 0) {
                length = sourceLength;
            }

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("accountId", entityFile.getAccountId().toString());
            metadata.addUserMetadata("uuid", entityFile.getUuid());
            metadata.addUserMetadata("creator", entityFile.getCreator());
            metadata.addUserMetadata("databaseId", String.valueOf(entityFile.getId()));
            metadata.setContentLength(length);
            metadata.setContentType(URLConnection.guessContentTypeFromName(entityFile.getName()));


            final var key = folder + fileName;
            final var bucket = getBucketName();
            PutObjectRequest request = new PutObjectRequest(bucket, key, fileInfo.getInputStream(), metadata);
            request.setCannedAcl(entityFile.isShared() ? CannedAccessControlList.PublicRead : CannedAccessControlList.Private);

            var result = getConnection().putObject(request);
            logger.info("EntityFile " + entityFile + " uploaded to S3 Bucket " + getBucketName() + " / " + key);


            if (tmpFile != null && tmpFile.delete()) {
                logger.info("Deleted temporal file: " + tmpFile);
            }


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
        fileName = fileName.replace(" ", "%20");
        return String.format("https://%s.%s/%s", bucketName, endpoint, fileName);
    }


    private String getFileName(EntityFile entityFile) {
        String subfolder = "";
        if (entityFile.getSubfolder() != null) {
            subfolder = entityFile.getSubfolder() + "/";
        }

        var name = entityFile.getName().toLowerCase().trim()
                .replace(" ", "_")
                .replace("-", "_")
                .replace("\u00F1", "n")
                .replace("\u00E1", "a")
                .replace("\u00E9", "e")
                .replace("\u00ED", "i")
                .replace("\u00F3", "o")
                .replace("\u00FA", "u");
        String storedFileName = entityFile.getUuid() + "_" + name;
        if (entityFile.getStoredFileName() != null && !entityFile.getStoredFileName().isEmpty()) {
            storedFileName = entityFile.getStoredFileName();
        }

        return subfolder + storedFileName;
    }

    private AmazonS3 getConnection() {
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTPS);

        AWSCredentials credentials = new BasicAWSCredentials(getAccessKey(), getSecretKey());

        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .withRegionalUsEast1EndpointEnabled(true)
                .withClientConfiguration(clientConfig)
                .build();
    }

    private String getAccountFolderName(Long accountId) {
        return "account" + accountId + "/";
    }

    private String generateThumbnailURL(EntityFile entityFile, int w, int h) {
        if (entityFile.getType() == EntityFileType.IMAGE || EntityFileType.getFileType(entityFile.getExtension()) == EntityFileType.IMAGE) {
            String urlKey = entityFile.getUuid() + w + "x" + h;
            String url = URL_CACHE.get(urlKey);
            if (url == null) {
                String folder = getAccountFolderName(entityFile.getAccountId());
                String fileName = getFileName(entityFile);
                String thumbfileName = w + "x" + h + "/" + fileName;

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
            final var key = folder + fileName;
            File localDestination = File.createTempFile(System.currentTimeMillis() + "file", entityFile.getName());
            File localThumbDestination = File.createTempFile(System.currentTimeMillis() + "thumb", entityFile.getName());


            var url = download(entityFile).getUrl();
            Files.copy(new URL(url).openStream(), localDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);

            ImageUtil.resizeImage(localDestination, localThumbDestination, entityFile.getExtension(), w, h);


            // metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("thumbnail", "true");
            metadata.addUserMetadata("description", entityFile.getDescription());
            metadata.addUserMetadata("uuid", entityFile.getUuid());
            metadata.addUserMetadata("width", String.valueOf(w));
            metadata.addUserMetadata("height", String.valueOf(h));
            metadata.setContentType("image/" + entityFile.getExtension());
            metadata.setContentLength(localThumbDestination.length());

            PutObjectRequest request = new PutObjectRequest(bucketName, folder + thumbfileName, localThumbDestination);
            request.setMetadata(metadata);
            request.setCannedAcl(CannedAccessControlList.PublicRead);

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

    }


    public String getBucketName() {
        return getParameter(AWS_S3_BUCKET);
    }

    public String getEndpoint() {
        return getParameter(AWS_S3_ENDPOINT);
    }

    public String getAccessKey() {
        return getParameter(AWS_ACCESS_KEY_ID);
    }

    private String getParameter(String name) {
        var param = PARAMS_CACHE.getOrLoad(name, s -> {
            var value = ApplicationParameters.get().getValue(name);
            if (value == null) {
                value = environment.getProperty(name);
            }
            return value;
        });
        return param;
    }

    public String getSecretKey() {
        return getParameter(AWS_SECRET_KEY);
    }

    @Override
    public void reloadParams() {
        PARAMS_CACHE.clear();
        URL_CACHE.clear();
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


    @Override
    public String toString() {
        return getName();
    }
}
