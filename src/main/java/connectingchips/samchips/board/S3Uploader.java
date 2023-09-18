package connectingchips.samchips.board;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Component
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

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //로컬 프로젝트에 사진 파일이 생성되지만, removeNewFile()을 통해서 바로 지워줄 예정
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
    private String putS3(File uploadfile, String fileName){
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadfile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File file) {
        if(file.delete()) System.out.println("파일삭제성공");
        else System.out.println("파일삭제실패");
    }

    //MultipartFile을 전달받아 File로 전환 후 S3에 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));

        return upload(uploadFile, dirName);
    }

    public String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        //로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)
        removeNewFile(uploadFile);

        return uploadImageUrl; //업로드된 파일의 S3 URL 주소 반환
    }
}
