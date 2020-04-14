package com.playtech.utils;

import com.playtech.utils.configs.Config;
import com.playtech.utils.services.UtilFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        UtilFactory utilFactory = context.getBean(UtilFactory.class);
        utilFactory.getObject().execute();
    }
}
