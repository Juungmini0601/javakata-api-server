package io.javakata.service.image;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 28.
 */
@Service
@RequiredArgsConstructor
public class ImageService {
	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.cloudfront.distribution-domain}")
	private String cloudfrontDomain;

	private final AmazonS3Client amazonS3Client;

	public String upload(MultipartFile file, String folder) {
		try {
			String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
			String key = folder + "/" + fileName;

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(file.getContentType());
			metadata.setContentLength(file.getSize());

			amazonS3Client.putObject(bucket, key, file.getInputStream(), metadata);

			return "https://" + cloudfrontDomain + "/" + key;
		} catch (Exception e) {
			throw new JavaKataException(ErrorType.DEFAULT_ERROR, e.getMessage());
		}
	}
}
