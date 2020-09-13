package com.campusamour.inoteblog;

import com.campusamour.inoteblog.thread.BillboardThread;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.campusamour.inoteblog.mapper")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        BillboardThread billboardThread = new BillboardThread();
        billboardThread.setDaemon(true);
        billboardThread.start();

        SpringApplication.run(Application.class, args);
    }

}

/* war包
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }
}
*/


