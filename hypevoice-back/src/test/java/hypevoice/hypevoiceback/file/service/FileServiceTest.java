package hypevoice.hypevoiceback.file.service;

import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.file.config.S3MockConfig;
import hypevoice.hypevoiceback.file.exception.FileErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(S3MockConfig.class)
@DisplayName("File [Service Layer] -> FileService 테스트")
class FileServiceTest extends ServiceTest {
    @Autowired
    private FileService fileService;

    @Autowired
    private S3Mock s3Mock;

    private final String FILE_PATH = "src/test/resources/files/";

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @Nested
    @DisplayName("Board 관련 파일 업로드")
    class uploadBoardFiles {
        @Test
        @DisplayName("빈 파일이면 업로드에 실패한다")
        void throwExceptionByEmptyFile() {
            // given
            MultipartFile nullFile = null;
            MultipartFile emptyFile = new MockMultipartFile("board", "empty.png", "image/png", new byte[]{});

            // when - then
            assertThatThrownBy(() -> fileService.uploadBoardFiles(nullFile))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(FileErrorCode.EMPTY_FILE.getMessage());
            assertThatThrownBy(() -> fileService.uploadBoardFiles(emptyFile))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(FileErrorCode.EMPTY_FILE.getMessage());
        }

        @Test
        @DisplayName("파일 업로드에 성공한다")
        void success() throws Exception {
            // given
            String fileName = "test.png";
            String contentType = "image/png";
            String dir = "board";
            MultipartFile file = createMockMultipartFile(dir, fileName, contentType);

            // when
            String fileKey = fileService.uploadBoardFiles(file);

            // then
            assertThat(fileKey).contains("test.png");
            assertThat(fileKey).contains(dir);
        }
    }

    @Nested
    @DisplayName("Voice 관련 파일 업로드")
    class uploadShareFiles {
        @Test
        @DisplayName("빈 파일이면 업로드에 실패한다")
        void throwExceptionByEmptyFile() {
            // given
            MultipartFile nullFile = null;
            MultipartFile emptyFile = new MockMultipartFile("voice", "empty.png", "image/png", new byte[]{});

            // when - then
            assertThatThrownBy(() -> fileService.uploadVoiceFiles(nullFile))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(FileErrorCode.EMPTY_FILE.getMessage());
            assertThatThrownBy(() -> fileService.uploadVoiceFiles(emptyFile))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(FileErrorCode.EMPTY_FILE.getMessage());
        }

        @Test
        @DisplayName("이미지 파일이 아니면 업로드에 실패한다")
        void throwExceptionByNotAnImage() throws IOException {
            // given
            String fileName = "test.txt";
            String contentType = "text/plain";
            String dir = "voice";
            MultipartFile file = createMockMultipartFile(dir, fileName, contentType);

            // when - then
            assertThatThrownBy(() -> fileService.uploadVoiceFiles(file))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(FileErrorCode.NOT_AN_IMAGE.getMessage());
        }

        @Test
        @DisplayName("파일 업로드에 성공한다")
        void success() throws Exception {
            // given
            String fileName = "test.png";
            String contentType = "image/png";
            String dir = "voice";
            MultipartFile file = createMockMultipartFile(dir, fileName, contentType);

            // when
            String fileKey = fileService.uploadVoiceFiles(file);

            // then
            assertThat(fileKey).contains("test.png");
            assertThat(fileKey).contains(dir);
        }
    }

    @Nested
    @DisplayName("Work 파일 업로드")
    class uploadProductFiles {
        @Test
        @DisplayName("빈 파일이면 업로드에 실패한다")
        void throwExceptionByEmptyFile() {
            // given
            MultipartFile nullFile = null;
            MultipartFile emptyFile = new MockMultipartFile("work", "empty.png", "image/png", new byte[]{});

            // when - then
            assertThatThrownBy(() -> fileService.uploadWorkFiles(nullFile))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(FileErrorCode.EMPTY_FILE.getMessage());
            assertThatThrownBy(() -> fileService.uploadWorkFiles(emptyFile))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(FileErrorCode.EMPTY_FILE.getMessage());
        }

        @Test
        @DisplayName("파일 업로드에 성공한다")
        void success() throws Exception {
            // given
            String fileName = "test.png";
            String contentType = "image/png";
            String dir = "work";
            MultipartFile file = createMockMultipartFile(dir, fileName, contentType);

            // when
            String fileKey = fileService.uploadWorkFiles(file);

            // then
            assertThat(fileKey).contains("test.png");
            assertThat(fileKey).contains(dir);
        }
    }

    private MultipartFile createMockMultipartFile(String dir, String fileName, String contentType) throws IOException {
        try (FileInputStream stream = new FileInputStream(FILE_PATH + fileName)) {
            return new MockMultipartFile(dir, fileName, contentType, stream);
        }
    }
}