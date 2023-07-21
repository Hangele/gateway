package com.gateway.parse.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * NeGateWay
 * 高德地图逆地理编码
 *
 * @author Administrator
 * @date 2022-5-18 15:44
 */
public class GdRegeoUtil {
    /**
     * 个人账号 日5000次
     */
    private final static String KEY = "073d3c4697a7c9e2a0a66ed7a699a948";

    public static String getPositionAddress(String lon, String lat) {
        String urlStr = "https://restapi.amap.com/v3/geocode/regeo?output=xml&radius=1000&extensions=base&location=" +
                lon + "," + lat +
                "&key=" + KEY;
        StringBuilder responseStr = new StringBuilder();

        URL url;
        HttpURLConnection conn;
        BufferedReader in;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                responseStr.append(line).append("\n");
            }
            in.close();
            conn.disconnect();

            // XML解析具体位置
            Document document = DocumentHelper.parseText(responseStr.toString());
            Node rootNode = document.getRootElement();
            Node regeoCode = rootNode.selectSingleNode("regeocode");
            Node formattedAddress = regeoCode.selectSingleNode("formatted_address");
            if (formattedAddress.hasContent()) {
                return formattedAddress.getText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.err.println(getPositionAddress("113.028486", "28.233364"));
    }
}
