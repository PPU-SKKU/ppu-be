package com.ppu.ppu.utils.s3;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    private final String accessKey;
    private final String secretKey;
    private final String region;

    S3Config() {
        accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        region = System.getenv("AWS_S3_REGION");
    }

    @Bean
    AmazonS3Client s3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
