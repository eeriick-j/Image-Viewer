package presenter;

import io.ImageLoader;
import model.Image;
import view.ImageDisplay;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ImageViewerPresenter {
    private final ImageLoader loader;
    private final ImageDisplay view;
    private List<File> files;
    private int currentIndex;

    public ImageViewerPresenter(ImageLoader loader, ImageDisplay view) {
        this.loader = loader;
        this.view = view;
    }

    public void init() {
        files = loader.listImages();
        if (files.isEmpty()) {
            view.clear();
            return;
        }
        currentIndex = 0;
        showCurrent();
    }

    public void next() {
        if (files == null || files.isEmpty()) return;
        currentIndex = (currentIndex + 1) % files.size();
        showCurrent();
    }

    public void prev() {
        if (files == null || files.isEmpty()) return;
        currentIndex = (currentIndex - 1 + files.size()) % files.size();
        showCurrent();
    }

    public void add(File file) {
        if (file == null || !file.exists() || !isValidImage(file)) return;
        try {
            File saved = loader.save(file);
            files.add(saved);
            currentIndex = files.size() - 1;
            showCurrent();
        } catch (IOException e) {
            view.clear();
        }
    }

    private boolean isValidImage(File file) {
        if (file == null || !file.exists()) return false;
        try {
            String mime = Files.probeContentType(file.toPath());
            return mime != null && mime.startsWith("image/");
        } catch (IOException e) {
            return false;
        }
    }

    private void showCurrent() {
        try {
            Image image = loader.load(files.get(currentIndex));
            view.show(image);
        } catch (IOException e) {
            view.clear();
        }
    }
}