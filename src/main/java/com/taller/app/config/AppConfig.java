package com.taller.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private String logPath;
    private Storage storage = new Storage();

    @Getter @Setter
    public static class Storage {
        private Empresa empresa = new Empresa();
    }

    @Getter @Setter
    public static class Empresa {
        private String logoPath;
    }

}
