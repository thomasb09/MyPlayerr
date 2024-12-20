package com.myplayerr.view.utils;

import javafx.concurrent.Task;
import javafx.scene.image.Image;

public class ImageLoaderView {

    public static Task<Image> loadImageTask(String imageUrl, double width, double height) {
        return new Task<>() {
            @Override
            protected Image call() {
                return new Image(imageUrl, width, height, true, true);
            }
        };
    }
}
