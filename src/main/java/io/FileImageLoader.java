package io;

import model.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileImageLoader implements ImageLoader {
    private final File folder;

    public FileImageLoader(File folder) {
        this.folder = folder;
    }

    @Override
    public List<Image> load() {
        List<Image> images = new ArrayList<>();
        File[] files = folder.listFiles();
        if (files == null) return images;

        for (File file : files) {
            try {
                if (isImage(file)) {
                    images.add(new Image(ImageIO.read(file), file.getName()));
                }
            } catch (IOException _) {}
        }
        return images;
    }

    private boolean isImage(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".png") ||
                name.endsWith(".jpg") ||
                name.endsWith(".jpeg");
    }
}