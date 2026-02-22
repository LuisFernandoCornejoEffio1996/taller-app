package com.taller.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${app.log-path}")
    private String logPath;

    public String getLogPath(){
        return logPath;
    }
}
