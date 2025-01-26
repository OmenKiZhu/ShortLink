package com.OmenKi.shortlink.project.service.impl;

import com.OmenKi.shortlink.project.service.UrlTitleService;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: Masin_Zhu
 * @Date: 2025/1/26
 * @Description: 标题接口实现层
 */
@Service
public class UrlTitleServiceImpl implements UrlTitleService {
    @SneakyThrows
    @Override
    public String getTitleByUrl(String url) {
        URL targetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            Document document = Jsoup.connect(url).get();
            String title = document.title();
            return "Title of " + url + " is " + title;
        }
            return "Error while fetching title. Http responseCode:" + responseCode;
    }
}
