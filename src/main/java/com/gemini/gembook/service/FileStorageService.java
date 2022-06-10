package com.gemini.gembook.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.gemini.gembook.Config.FileData;
import com.gemini.gembook.model.GemFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    private AmazonS3 amazonS3Client;

    private static final String BUCKET = "gembook";

    private final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Bean
    public AmazonS3 s3client() {

        Region region = Region.AP_SOUTH_1;
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("AKIA5ZJRWJYI7RNACI3L",
                "/kleJZuaFf2Xdyv6OfBNmJPc3t5sc72+8ar5asR5");
        AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(String.valueOf(region)))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        return amazonS3Client;
    }

    @Autowired
    public FileStorageService(FileData fileData) {
        this.fileStorageLocation = Paths.get(fileData.getDirectory())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                    "AKIA5ZJRWJYI7RNACI3L",
                    "/kleJZuaFf2Xdyv6OfBNmJPc3t5sc72+8ar5asR5");
            Region region = Region.AP_SOUTH_1;
            S3Client client = S3Client.builder()
                                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                                .region(region).build();

            PutObjectRequest request = PutObjectRequest.builder()
                                            .bucket(BUCKET)
                                            .key(fileName)
                                            .acl("public-read")
                                            .build();
            InputStream inputStream = file.getInputStream();
            client.putObject(request,
                    RequestBody.fromInputStream(inputStream, inputStream.available()));
            logger.info("Successfully added to AWS s3 bucket");

            return fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public ByteArrayOutputStream loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            S3Object s3object = amazonS3Client.getObject(new GetObjectRequest(BUCKET, fileName));

            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream;
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFiles(List<GemFiles> fileList) {
        fileList.forEach(uploadedFile -> {
            try {
                File file = new File(uploadedFile.getFileName());
                if (file.exists())
                    file.delete();
            } catch (Exception e) {
                logger.info("Exception while deleting file: {} , {}", uploadedFile.getFileName(), e.getMessage());
            }
        });
    }
}