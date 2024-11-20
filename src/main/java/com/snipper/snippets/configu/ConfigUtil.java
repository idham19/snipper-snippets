package com.snipper.snippets.configu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigUtil {
    @Autowired
    Environment environment;

    public String getDotEnvPath(String path){
        return  environment.getProperty(path);
    }
}
