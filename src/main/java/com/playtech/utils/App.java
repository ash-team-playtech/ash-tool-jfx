package com.playtech.utils;

import com.playtech.utils.configs.ControllerConfig;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;

@SpringBootApplication
public class App extends Application {

    @Value("${application.title}")
    private String windowTitle;

    @Value("${application.icon}")
    private Resource icon;

    private ConfigurableApplicationContext context;

    @Autowired
    private ControllerConfig.ViewHolder view;

    @Override
    public void init() {
        context = SpringApplication.run(App.class);
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.getIcons().add(new Image(icon.getURI().toString()));
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(view.getView()));
        stage.sizeToScene();
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        stage.centerOnScreen();
    }

    @Override
    public void stop() {
        context.stop();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
