package com.behl.emulator.service;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.behl.emulator.configuration.properties.AwsConfigurationProperties;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(value = AwsConfigurationProperties.class)
public class StorageService {

	private final AmazonS3 amazonS3;
	private final AwsConfigurationProperties awsConfigurationProperties;

	public HttpStatus save(@NonNull final MultipartFile file) {
		final String bucketName = awsConfigurationProperties.getS3().getBucketName();
		final ObjectMetadata metadata = constructMetadata(file);
		try {
			var putObjectRequest = new PutObjectRequest(bucketName, file.getOriginalFilename(), file.getInputStream(),
					metadata);
			amazonS3.putObject(putObjectRequest);
		} catch (SdkClientException | IOException exception) {
			log.error("UNABLE TO STORE '{}' IN S3 Bucket {} : {}", file.getOriginalFilename(), bucketName,
					LocalDateTime.now(), exception);
			return HttpStatus.EXPECTATION_FAILED;
		}
		log.info("File '{}' successfully stored in S3 Bucket {} : {}", file.getOriginalFilename(), bucketName,
				LocalDateTime.now());
		return HttpStatus.OK;
	}

	public S3Object retrieve(@NonNull final String key) {
		final String bucketName = awsConfigurationProperties.getS3().getBucketName();
		final var getObjectRequest = new GetObjectRequest(bucketName, key);
		return amazonS3.getObject(getObjectRequest);
	}

	private ObjectMetadata constructMetadata(final MultipartFile file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentDisposition(file.getOriginalFilename());
		return metadata;
	}

}