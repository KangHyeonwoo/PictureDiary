package com.picture.diary.common.connection;

import com.picture.diary.common.connection.response.ConnectionResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpConnectionUtils {

    public static <T extends ConnectionResponseEntity> ConnectionResponse getConnectionResponse(ConnectionInfo connectionInfo) {
        final String httpMethod = connectionInfo.getHttpMethod();
        String param = makeParam(connectionInfo);
        String sUrl = makeUrl(connectionInfo, param);
        HttpURLConnection conn = null;
        StringBuilder responseData = new StringBuilder();

        try {
            URL url = new URL(sUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setRequestMethod(httpMethod);
            conn.setDoOutput(true);

            // httpMethod process
            switch (httpMethod) {
                case "GET":
                    break;
                case "POST":
                    byte[] bParam = param.getBytes("UTF-8");
                    if (connectionInfo.getDataMap() != null) {
                        conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                    } else {
                        //dataList으로 넣어줄 경우
                        conn.setRequestProperty("Content-type", "application/json");
                    }
                    conn.setRequestProperty("Content-Length", String.valueOf(bParam.length));
                    conn.getOutputStream().write(bParam);
                    break;
                default:
                    throw new IllegalArgumentException("허용되지 않은 요청 타입입니다.");
            }

            // Response Code
            HttpStatus responseStatus = HttpStatus.valueOf(conn.getResponseCode());

            BufferedReader rd;

            if (responseStatus.is2xxSuccessful()) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while ((line = rd.readLine()) != null) {
                responseData.append(line);
            }

        } catch (IOException e) {
            return ConnectionResponse.createErrorInstance(e.getMessage());
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }

        return ConnectionResponse.createSuccessInstance(responseData.toString());
    }

    /**
     * url 생성
     */
    private static String makeUrl(ConnectionInfo connectionInfo, String param) {
        String url = connectionInfo.getUrl();
        String protocol = connectionInfo.getProtocol();
        String host = connectionInfo.getHost();
        String port = connectionInfo.getPort();
        String code = connectionInfo.getCode();

        // make url
        StringBuffer sbUrl = !ObjectUtils.isEmpty(url) ? new StringBuffer().append(url)
                : new StringBuffer().append(protocol).append("://").append(host).append(":").append(port)
                .append(!ObjectUtils.isEmpty(code) ? code : "");

        if("GET".equals(connectionInfo.getHttpMethod())) {
            sbUrl.append("?").append(param);
        }

        return sbUrl.toString();
    }

    /**
     * param 생성
     *
     * dataMap or dataJson 하나만 받기.
     *
     * @return String
     */
    private static String makeParam(ConnectionInfo connectionInfo) {
        StringBuffer sbParam = new StringBuffer();

        Map<String, Object> dataMap = connectionInfo.getDataMap();
        List<JSONObject> dataList = connectionInfo.getDataList();

        if (dataMap == null && dataList == null) {
            return "";
        }
        if (dataMap != null) {
            for (Map.Entry<String, Object> entry : dataMap.entrySet())
                sbParam.append(sbParam.length() > 0 ? "&" : "").append(entry.getKey()).append("=")
                        .append(encode((String) entry.getValue())).toString();
        } else {
            sbParam.append(dataList);
        }
        return sbParam.toString();

    }

    private static String encode(String in) {
        String out = "";
        try {
            out = URLEncoder.encode(in, "UTF-8");
        } catch (Exception e) {
            log.error("message", e.getMessage());
        }
        return out;
    }
}
