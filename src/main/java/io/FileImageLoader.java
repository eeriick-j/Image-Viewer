package io;

import model.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileImageLoader implements ImageLoader {
    private final File folder;

    public FileImageLoader(File folder) {
        this.folder = folder;
    }

    @Override
    public List<File> listImages() {
        File[] files = folder.listFiles(this::isImage);
        if (files == null) return new ArrayList<>();
        return new ArrayList<>(List.of(files));
    }

    @Override
    public Image load(File file) throws IOException {
        return new Image(ImageIO.read(file), file.getName());
    }

    @Override
    public File save(File file) throws IOException {
        if (!folder.exists()) folder.mkdirs();
        File target = new File(folder, file.getName());
        Files.copy(file.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return target;
    }

    private boolean isImage(File file) {
        String name = file.getName().toLowerCase();
        return file.isFile() &&
                (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"));
    }
}