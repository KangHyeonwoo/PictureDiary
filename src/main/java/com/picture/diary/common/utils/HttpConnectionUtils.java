package com.picture.diary.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Data
@Builder
@Component
@Slf4j
public class HttpConnectionUtils {

    // PUSH
    @Builder.Default
    private String httpMethod = "GET";
    @Builder.Default
    private String protocol = "http";
    private String host;
    private String port;
    private String code;
    private String url; // 통합 인자값
    /*  @Param resource
     *  dataJson, dataMap 빌더로 둘중 하나의 데이터를 받아야한다.
     *  dataList -> applicatioin/json ,dataMap -> application/x-www-form-urlencoded
     *
     * */
    private List<JSONObject> dataList; // 리스트로된 json을 파라미터로 보낼경우
    private Map<String, Object> dataMap;

    @Builder.Default
    private boolean hasResponseBody = false;
    @Builder.Default
    private String filePath = "";

    /**
     * 통신 기능 시작
     *
     * @return ResultVO
     * @throws IOException
     */
    public String start() throws IOException {

        String sParam = this.makeParam();
        String sUrl = this.makeUrl(sParam);

        if (ObjectUtils.isEmpty(sUrl)) {
            return this.connection(sUrl, sParam);
        }

        throw new IOException();
    }

    /**
     * httpUrlConnection(결과데이터)
     *
     * @param sUrl
     * @param sParam
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private String connection(String sUrl, String sParam) throws IOException {
        BufferedReader rd = null;
        URL url = new URL(sUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setRequestMethod(this.httpMethod);
        conn.setDoOutput(true);

        // httpMethod process
        switch (this.httpMethod) {
            case "GET":
                break;
            case "POST":
                byte[] bParam = sParam.getBytes("UTF-8");
                if (dataMap != null) {
                    conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                } else {
                    //dataList으로 넣어줄 경우
                    conn.setRequestProperty("Content-type", "application/json");
                }
                conn.setRequestProperty("Content-Length", String.valueOf(bParam.length));
                conn.getOutputStream().write(bParam);
                break;
            default:
                break;
        }

        // Response Code
        int responseCode = conn.getResponseCode();

        if (responseCode >= 200 && responseCode <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        conn.disconnect();

        return sb.toString();
    }

    /**
     * param 생성
     *
     * dataMap or dataJson 하나만 받기.
     *
     * @return String
     */
    private String makeParam() {
        StringBuffer sbParam = new StringBuffer();

        if (dataMap == null && dataList == null) {
            return "";
        }
        if (dataMap != null) {
            for (Map.Entry<String, Object> entry : dataMap.entrySet())
                sbParam.append(sbParam.length() > 0 ? "&" : "").append(entry.getKey()).append("=")
                        .append(this.encode((String) entry.getValue())).toString();
        } else {
            sbParam.append(dataList);
        }
        return sbParam.toString();

    }

    /**
     * url 생성
     *
     * @param param
     * @return String
     */
    private String makeUrl(String param) {
        // make url
        StringBuffer sbUrl = !ObjectUtils.isEmpty(this.url) ? new StringBuffer().append(this.url)
                : new StringBuffer().append(this.protocol).append("://").append(this.host).append(":").append(this.port)
                .append(!ObjectUtils.isEmpty(code) ? code : "");

        // httpMethod process
        switch (this.httpMethod) {
            case "GET":
                sbUrl.append("?").append(param);
                break;
            case "POST":
                break;
            default:
                break;
        }
        return sbUrl.toString();
    }

    /**
     * get response body
     *
     * @param conn
     * @return String
     */
    private String getResponseBody(HttpURLConnection conn) throws IOException {

        int responseCode = conn.getResponseCode();

        BufferedReader rd;
        if (responseCode >= 200 && responseCode <= 300)
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        else
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null)
            sb.append(line);
        rd.close();

        if (!ObjectUtils.isEmpty(this.filePath))
            this.getResponseFile(conn, sb);

        return sb.toString();
    }

    /**
     * get response file
     *
     * @param conn, StringBuilder sb
     * @return void
     */
    private void getResponseFile(HttpURLConnection conn, StringBuilder sb) throws IOException {
        Map<String, List<String>> map = conn.getHeaderFields();

        if (map.containsKey("content-disposition")) {

            String value = map.get("content-disposition").toString();
            String fileName = value.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
            String path = this.filePath;
            String filePath = new StringBuffer().append(path).append(fileName).toString();
            File folder = new File(path);

            if (!folder.exists())
                folder.mkdir();

            FileWriter fw = new FileWriter(filePath);
            fw.write(sb.toString());
            fw.close();

        }

    }

    /**
     * encoding param
     *
     * @param in
     * @return String
     */
    private String encode(String in) {
        String out = "";
        try {
            out = URLEncoder.encode(in, "UTF-8");
        } catch (Exception e) {
            log.error("message", e.getMessage());
        }
        return out;
    }

    /**
     * 서버주소 만들기
     * @param scheme
     * @param ip
     * @param port
     * @return
     */
    public String ServerAddr(String scheme, String ip, String port) {
        String addr = "";

        if("".equals(port) || port == null) {
            addr = scheme + "://" + ip;
        }else{
            addr = scheme + "://" + ip + ":" + port;
        }

        return addr;
    }

}
