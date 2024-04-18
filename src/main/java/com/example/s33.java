//import org.springframework.web.bind.annotation.*;
//import com.amazonaws.HttpMethod;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//
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
//    public String downloadAndUpload() {
//        // Specify the object key
//        String objectKey = "bucker/abc.pdf";
//
//        // Generate pre-signed URL for downloading from Account A's S3
//        URL downloadUrlA = generatePreSignedUrl(s3ClientA, "bucket-A", objectKey, HttpMethod.GET);
//
//        // Download from Account A's S3
//        HttpURLConnection downloadConnection = null;
//        try {
//            downloadConnection = (HttpURLConnection) downloadUrlA.openConnection();
//            downloadConnection.setRequestMethod("GET");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed to download from Account A";
//        }
//
//        // Upload to Account B's S3
//        PutObjectRequest putRequest = new PutObjectRequest("bucket-B", objectKey, downloadConnection.getInputStream(), null);
//        s3ClientB.putObject(putRequest);
//
//        return "Downloaded from Account A and Uploaded to Account B successfully!";
//    }
//
//    private URL generatePreSignedUrl(AmazonS3 s3Client, String bucketName, String objectKey, HttpMethod method) {
//        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
//                .withMethod(method);
//        return s3Client.generatePresignedUrl(urlRequest);
//    }
//}
