package presenter;

import io.ImageLoader;
import model.Image;
import view.ImageDisplay;

import java.util.List;

public class ImagePresenter {
    private final ImageLoader loader;
    private final ImageDisplay view;
    private List<Image> images;
    private int currentIndex = 0;

    public ImagePresenter(ImageLoader loader, ImageDisplay view) {
        this.loader = loader;
        this.view = view;
    }

    public void init() {
        images = loader.load();
        if (images == null || images.isEmpty()) {
            view.clear();
            return;
        }

        currentIndex = 0;
        view.show(images.get(currentIndex));
    }

    public void next() {
        if (images == null || images.isEmpty()) return;

        currentIndex = (currentIndex + 1) % images.size();
        view.show(images.get(currentIndex));
    }

    public void prev() {
        if (images == null || images.isEmpty()) return;

        currentIndex--;
        if (currentIndex < 0) currentIndex = images.size() - 1;

        view.show(images.get(currentIndex));
    }
}