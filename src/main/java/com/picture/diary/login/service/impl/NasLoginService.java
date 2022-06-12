package com.picture.diary.login.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.service.LoginService;
import com.picture.diary.picture.file.data.NasProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
@Slf4j
public class NasLoginService implements LoginService {

    private final NasProperty nasProperty;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void login(LoginRequestDto loginRequestDto) throws IOException {
        final String userId = loginRequestDto.getUserId();
        final String password = loginRequestDto.getPassword();
        final String apiPath = nasProperty.getApiPath();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiPath);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        log.info("RESPONSE CODE : " + statusCode);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(httpResponse.getEntity().getContent())
        );

        String inputLine;
        StringBuffer response = new StringBuffer();

        while((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }

        reader.close();

        httpClient.close();
    }

    @Override
    public void logout(LoginRequestDto loginRequestDto) {

    }
}
