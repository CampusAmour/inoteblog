package com.campusamour.inoteblog.thread;

import com.campusamour.inoteblog.util.DateUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;


/**
 * @Author: CampusAmour
 * @Date: 2020/9/13 9:32
 */
public class BillboardThread extends Thread {

    @Override
    public void run() {
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = "http://localhost:8080/";
        // System.out.println("-------------守护线程启动-----------------");
        while (true) {
            try {
                crawl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sleep(DateUtil.getSecondsNextEarlyMorning()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void crawl(String url) throws IOException {
        HttpClient client =  HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        client.execute(get);
    }
}
