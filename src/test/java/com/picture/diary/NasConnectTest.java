package com.picture.diary;

import com.picture.diary.picture.file.data.Picture;
import com.picture.diary.picture.file.service.PictureFileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class NasConnectTest {

    @Autowired
    PictureFileService pictureFileService;

    @Test
    @DisplayName("NAS 연결 잘 됐는지 테스트")
    void nasDriveConnectTest() throws IOException {
        String dir = "Z:/homes/hwkang/Photos/MobileBackup/iPhone (2)";

        Path path = Paths.get(dir);
        List<Path> paths = Files.walk(path).collect(Collectors.toList());

        Assertions.assertThat(paths.size()).isGreaterThan(0);
    }

    @Test
    void fileToPicture() throws IOException {
        List<Picture> fileList = pictureFileService.findListByUserId("hwkang");

        fileList.forEach(System.out::println);
    }


    @Test
    @DisplayName("NAS 로그인 API API 테스트")
    void nasLoginApiTest() throws IOException {
        String apiPath = "https://hwkang.synology.me:5001/webapi/entry.cgi";
        String userId = "hwkang";
        String password = "";
        WebClient webClient = WebClient.builder().build();

        //https://velog.io/@youmakemesmile/%EA%B8%89%ED%95%98%EA%B2%8C-RestTemplate%EB%A5%BC-%EB%8C%80%EC%B2%B4-%ED%95%98%EA%B8%B0-%EC%9C%84%ED%95%9C-Spring-WebFlux-WebClient-%EC%82%AC%EC%9A%A9%EC%84%A4%EB%AA%85%EC%84%9C-WebClient-Bean-%EC%83%9D%EC%84%B1
        String loginResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiPath)
                        .queryParam("api", "SYNO.API.Auth")
                        .queryParam("version", "6")
                        .queryParam("method", "login")
                        .queryParam("session", "FileStation")
                        .queryParam("format", "cookie")
                        .queryParam("account", userId)
                        .queryParam("password", password)
                        .build())
                .retrieve()
                .onStatus(
                        httpStatus -> !httpStatus.is2xxSuccessful(),
                        r -> Mono.empty()
                )
                .bodyToMono(String.class)
                .block();

        System.out.println(loginResponse);
    }
}
