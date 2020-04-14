package com.playtech.utils.configs;

import com.playtech.utils.controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class ControllerConfig {

    @Value("classpath:fxml/main.fxml")
    private Resource resource;

    @Bean
    public ViewHolder getMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(resource.getURL());
        loader.load();
        return new ViewHolder(loader.getRoot(), loader.getController());
    }

    @Bean
    public MainController getMainController() throws IOException {
        return (MainController) getMainView().getController();
    }

    public class ViewHolder {
        private Parent view;
        private Object controller;

        public ViewHolder(Parent view, Object controller) {
            this.view = view;
            this.controller = controller;
        }

        public Parent getView() {
            return view;
        }

        public void setView(Parent view) {
            this.view = view;
        }

        public Object getController() {
            return controller;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }
    }
}
