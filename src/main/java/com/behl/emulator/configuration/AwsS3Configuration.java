package com.behl.emulator.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.behl.emulator.configuration.properties.AwsConfigurationProperties;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(value = AwsConfigurationProperties.class)
public class AwsS3Configuration {

	private final AwsConfigurationProperties awsConfigurationProperties;

	@Bean
	@Profile(value = "!local")
	public AmazonS3 productionAmazonS3() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(awsConfigurationProperties.getAccessKey(),
				awsConfigurationProperties.getSecretAccessKey());
		return AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
	}

	@Bean
	@Profile(value = "local")
	public AmazonS3 localAmazonS3() {
		var endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
				awsConfigurationProperties.getS3().getEndpoint(), Regions.AP_SOUTH_1.getName());
		var awsCredentials = new BasicAWSCredentials(awsConfigurationProperties.getAccessKey(),
				awsConfigurationProperties.getSecretAccessKey());
		return AmazonS3ClientBuilder.standard().withEndpointConfiguration(endpointConfiguration)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withPathStyleAccessEnabled(true)
				.build();
	}

}