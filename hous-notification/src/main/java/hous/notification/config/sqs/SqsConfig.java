package hous.notification.config.sqs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SqsConfig {

	@Value("${cloud.aws.credentials.accessKey}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secretKey}")
	private String secretKey;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Primary
	@Bean
	public AmazonSQSAsync amazonSqsAws() {
		BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonSQSAsyncClientBuilder.standard()
			.withRegion(region)
			.withCredentials(new AWSStaticCredentialsProvider(basicAwsCredentials))
			.build();
	}
}
