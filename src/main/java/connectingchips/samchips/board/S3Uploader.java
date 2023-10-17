package connectingchips.samchips.board;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import connectingchips.samchips.exception.BadRequestException;
import connectingchips.samchips.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static connectingchips.samchips.exception.CommonErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {
    /**
     * [코드의 순서]
     * 1. MultipartFile을 전달
     * 2. S3에 전달할 수 있도록 MultiPartFile을 File로 전환
     *      - S3에 Multipartfile 타입은 전송이 안 됨
     * 3. 전환된 File을 S3에 public 읽기 권한으로 put
     *      - 외부에서 정적 파일을 읽을 수 있도록 하기 위함
     * 4. 로컬에 생성된 File 삭제
     *      - Multipartfile -> File로 전환되면서 로컬에 파일 생성된 것을 삭제
     * 5. 업로드된 파일의 S3 URL 주소를 반환
     */
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일 업로드
     * @param uploadfile : 업로드할 파일
     * @param fileName : 업로드할 파일 이름
     * @return : 업로드 경로
     */
    private String putS3(File uploadfile, String fileName){
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadfile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * S3에 파일 삭제
     */
    public void deleteS3(String filePath) throws Exception {
        try {
            amazonS3Client.deleteObject(bucket, filePath);
        } catch (AmazonServiceException e) {
            throw new RestApiException(INVALID_REQUEST);
        }
    }

    /**
     * 로컬에 있는 파일 삭제하는 메서드
     * @param file : 삭제할 파일
     */
    private void removeLocalFile(File file) {
        if(file.delete()) System.out.println("파일 삭제 성공");
        else System.out.println("파일 삭제 실패");
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

    public String getFileUrl(String fileName){
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    public String upload(MultipartFile multipartFile, String filePath) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_FILE));
        return upload(uploadFile, filePath);
    }

    /**
     * MultiPartFile -> File로 전환
     */
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    public String upload(File uploadFile, String dirName) {
        //set UUID + 확장자 제한
        String uploadS3fileName = createFileName(uploadFile.getName());
        //set Directory
        String fileName = dirName + "/" + uploadS3fileName;
        //s3에 업로드
        String uploadImageUrl = putS3(uploadFile, fileName);
        //로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
        removeLocalFile(uploadFile);

        return uploadImageUrl; //업로드된 파일의 S3 URL 주소 반환
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

    //기존 확장자명을 유지한 채, 유니크한 파일의 이름을 생성
    private String createFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        if(!isImageExtension(extension)) throw new BadRequestException(INVALID_REQUEST);
        return UUID.randomUUID().toString().concat(".").concat(getFileExtension(originalFileName));
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
}