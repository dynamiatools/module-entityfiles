package tools.dynamia.modules.entityfiles.s3;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

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
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Permission;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3EntityFileStorage implements EntityFileStorage {

	private LoggingService logger = new SLF4JLoggingService(S3EntityFileStorage.class);

	private final Map<String, String> URL_CACHE = new HashMap<String, String>();

	public static final String ID = "AWSS3Storage";

	private final String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
	private final String secretKey = System.getenv("AWS_SECRET_KEY");
	private final String endpoint = System.getenv("AWS_S3_ENDPOINT");
	private final String bucketName = System.getenv("AWS_S3_BUCKET");

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
				URL_CACHE.put(urlKey, url);
			} else {
				url = generateSignedURL(getBucketName(), folder + fileName);
			}
		}

		StoredEntityFile storedEntityFile = new S3StoredEntityFile(entityFile, url, new File(fileName));

		return storedEntityFile;
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
		return String.format("http://%s.%s/%s", bucketName, endpoint, fileName);
	}

	private String getFileName(EntityFile entityFile) {
		return entityFile.getUuid() + "_" + entityFile.getName();
	}

	private AmazonS3 getConnection() {

		if (connection == null) {
			ClientConfiguration clientConfig = new ClientConfiguration();
			clientConfig.setProtocol(Protocol.HTTP);

			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			connection = new AmazonS3Client(credentials, clientConfig);
			connection.setEndpoint(endpoint);

			if (!connection.doesBucketExist(bucketName)) {
				connection.createBucket(bucketName);
			}

		}
		return connection;
	}

	private String getAccountFolderName(Long accountId) {
		String name = "account" + accountId + "/";
		return name;
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
				URL_CACHE.put(urlKey, url);
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

	private String getBucketName() {

		return bucketName;
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
