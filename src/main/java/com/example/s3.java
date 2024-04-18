//package com.example;
//
//import org.springframework.web.bind.annotation.*;
//import com.amazonaws.HttpMethod;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//@RestController
//public class S3Controller {
//
//    private final AmazonS3 s3ClientA;
//    private final AmazonS3 s3ClientB;
//
//    public S3Controller(AmazonS3 s3ClientA, AmazonS3 s3ClientB) {
//        this.s3ClientA = s3ClientA;
//        this.s3ClientB = s3ClientB;
//    }
//
//    @GetMapping("/downloadAndUpload")
//    public String downloadAndUpload(@RequestParam String objectKey) {
//        // Generate pre-signed URL for downloading from Account A's S3
//        URL downloadUrlA = generatePreSignedUrl(s3ClientA, "bucket-A", objectKey, HttpMethod.GET);
//
//        // Generate pre-signed URL for uploading to Account B's S3
//        URL uploadUrlB = generatePreSignedUrl(s3ClientB, "bucket-B", "CDF", HttpMethod.PUT);
//
//        // Download from Account A's S3 and upload to Account B's S3
//        downloadAndUpload(downloadUrlA, uploadUrlB);
//
//        return "Downloaded from Account A as ABC and Uploaded to Account B as CDF successfully!";
//    }
//
//    private URL generatePreSignedUrl(AmazonS3 s3Client, String bucketName, String objectKey, HttpMethod method) {
//        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
//                .withMethod(method);
//        return s3Client.generatePresignedUrl(urlRequest);
//    }
//
//    private void downloadAndUpload(URL downloadUrl, URL uploadUrl) {
//        try {
//            // Download from the pre-signed URL
//            HttpURLConnection downloadConnection = (HttpURLConnection) downloadUrl.openConnection();
//            downloadConnection.setRequestMethod("GET");
//            InputStream inputStream = downloadConnection.getInputStream();
//
//            // Read the downloaded data into a byte array
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                byteArrayOutputStream.write(buffer, 0, bytesRead);
//            }
//
//            // Close the input stream
//            inputStream.close();
//
//            // Get the byte array
//            byte[] downloadedBytes = byteArrayOutputStream.toByteArray();
//
//            // Upload to the pre-signed URL
//            HttpURLConnection uploadConnection = (HttpURLConnection) uploadUrl.openConnection();
//            uploadConnection.setDoOutput(true);
//            uploadConnection.setRequestMethod("PUT");
//            OutputStream outputStream = uploadConnection.getOutputStream();
//            outputStream.write(downloadedBytes);
//            outputStream.close();
//        } catch (Exception e) {
//            // Handle exceptions
//            e.printStackTrace();
//        }
//    }
//}
//
