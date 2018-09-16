package com.sophon.summer.util;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Starter {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Starter.class)
                .properties("server.port=8081")
                .run(args);
    }
}
