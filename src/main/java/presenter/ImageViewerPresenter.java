package presenter;

import io.ImageLoader;
import view.ImageDisplay;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ImageViewerPresenter {
    private final ImageLoader loader;
    private final ImageDisplay view;
    private final CircularIterator<File> iterator = new CircularIterator<>();

    public ImageViewerPresenter(ImageLoader loader, ImageDisplay view) {
        this.loader = loader;
        this.view = view;
    }

    public void init() {
        iterator.addAll(loader.listImages());
        if (iterator.isEmpty()) { view.clear(); return; }
        showCurrent();
    }

    public void next() {
        if (iterator.isEmpty()) return;
        iterator.next();
        showCurrent();
    }

    public void prev() {
        if (iterator.isEmpty()) return;
        iterator.prev();
        showCurrent();
    }

    public void add(File file) {
        if (file == null || !file.exists() || !isValidImage(file)) return;
        try {
            File saved = loader.save(file);
            iterator.add(saved);
            showCurrent();
        } catch (IOException e) {
            view.clear();
        }
    }

    private void showCurrent() {
        try {
            view.show(loader.load(iterator.current()));
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
}