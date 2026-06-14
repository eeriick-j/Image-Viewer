package io;

import model.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileImageLoader implements ImageLoader {
    private final File folder;

    public FileImageLoader(File folder) {
        this.folder = folder;
    }

    @Override
    public List<File> listImages() {
        File[] files = folder.listFiles(this::isImage);
        if (files == null) return List.of();
        return Arrays.asList(files);
    }

    @Override
    public Image load(File file) throws IOException {
        return new Image(ImageIO.read(file), file.getName());
    }

    private boolean isImage(File file) {
        String name = file.getName().toLowerCase();
        return file.isFile() &&
                (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));
    }
}