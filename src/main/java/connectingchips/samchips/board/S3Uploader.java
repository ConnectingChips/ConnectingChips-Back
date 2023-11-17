package connectingchips.samchips.board;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import connectingchips.samchips.global.exception.BadRequestException;
import connectingchips.samchips.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.*;

import static connectingchips.samchips.global.exception.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    public String uploadFile(MultipartFile multipartFile, String filePath) throws IOException {
        File uploadFile = convertMultipartFileToFile(multipartFile).orElseThrow(() -> new BadRequestException(NOT_FOUND_FILE));
        String uploadS3fileName = creatUniqueFileName(uploadFile.getName());
        String fileName = filePath + "/" + uploadS3fileName;
        uploadS3Bucket(uploadFile, fileName);
        deleteLocalFile(uploadFile);

        return getFileUrl(fileName);
    }

    /* MultiPartFile -> File로 전환 */
    private Optional<File> convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            throw new IOException("MultipartFile을 File로 변환하는 중 오류 발생", e);
        }
        return Optional.of(file);
    }

    //S3 파일 업로드
    private void uploadS3Bucket(File uploadFile, String fileName){
        amazonS3Client.putObject( new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }

    //로컬 파일 삭제
    private void deleteLocalFile(File file) {
        if(file.delete()) return;
        else throw new RestApiException(INVALID_REQUEST);
    }

    //파일 URL 반환
    public String getFileUrl(String fileName){
        // return amazonS3Client.getUrl(bucket, fileName).toString();
        // 임시로 cloudfront 도메인으로 URL 변경
        return "https://" + cloudFrontDomain + "/" + fileName;
    }

    //기존 확장자 명을 유지한 채, UUID 파일 생성
    private String creatUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        //이미지 확장자 제한하기 위한 코드
        if (!isImageExtension(extension)) throw new BadRequestException(INVALID_REQUEST);
        String uniqueName = UUID.randomUUID().toString();
        return uniqueName + "." + extension;
    }

    //확장자 추출
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    //파일 유효성 검사 (이미지 확장자만 업로드 가능)
    private boolean isImageExtension(String extension) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "heic", "bmp"};
        return Arrays.stream(imageExtensions).anyMatch(ext -> ext.equalsIgnoreCase(extension));
    }

    public List<String> find(String prefix){
        List<String> fileNames = new ArrayList<>();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(bucket);
        if(!prefix.isBlank()){
            listObjectsRequest.setPrefix(prefix);
        }
        listObjectsRequest.setDelimiter("/");

        ObjectListing s3Objects;

        do{
            s3Objects = amazonS3Client.listObjects(listObjectsRequest);
            for(S3ObjectSummary s3ObjectSummary : s3Objects.getObjectSummaries()){
                System.out.println(" - " + s3ObjectSummary.getKey() + "  " +
                        "(size = " + s3ObjectSummary.getSize() +
                        ")");
                fileNames.add(s3ObjectSummary.getKey());
            }
            listObjectsRequest.setMarker(s3Objects.getNextMarker());
        }while(s3Objects.isTruncated());

        return fileNames;
    }

    //S3 파일 삭제
    public void deleteS3File(String filePath) throws Exception {
        try {
            amazonS3Client.deleteObject(bucket, filePath);
        } catch (AmazonServiceException e) {
            throw new RestApiException(INVALID_REQUEST);
        }
    }

    //================================== 추가한 부분=========================
    public List<String> upload(List<MultipartFile> multipartFile) throws IOException {
        List<String> imgUrlList = new ArrayList<>();
        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 fileNameList에 추가
        for (int i = 0; i < multipartFile.size(); i++) {

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.get(i).getSize());
            objectMetadata.setContentType(multipartFile.get(i).getContentType());
            String dirName = "/";
            if( i== 0) dirName = "/introImage";
            if( i== 1) dirName = "/pageImage";
            if( i== 2) dirName = "/totalListImage";
            if( i== 3) dirName = "/myListImage";
            try (InputStream inputStream = multipartFile.get(i).getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket+dirName, multipartFile.get(i).getOriginalFilename(), inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                imgUrlList.add(amazonS3Client.getUrl(bucket+dirName,multipartFile.get(i).getOriginalFilename()).toString());
            } catch (IOException e) {
                throw new BadRequestException(INVALID_REQUEST);
            }
        }
        return imgUrlList;
    }
}