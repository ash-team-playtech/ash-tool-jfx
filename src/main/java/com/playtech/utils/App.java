package com.playtech.utils;

import com.playtech.utils.configs.ControllerConfig;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App extends Application {

    @Value("${application.title}")
    private String windowTitle;

    private ConfigurableApplicationContext context;

    @Autowired
    private ControllerConfig.ViewHolder view;

    @Override
    public void init() {
        context = SpringApplication.run(App.class);
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(view.getView()));
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void stop() {
        context.stop();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
