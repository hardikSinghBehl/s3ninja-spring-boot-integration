package com.behl.emulator.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "com.behl.aws")
public class AwsConfigurationProperties {

	private String accessKey;
	private String secretAccessKey;
	private S3 s3 = new S3();

	@Data
	public class S3 {
		private String bucketName;
	}

}
