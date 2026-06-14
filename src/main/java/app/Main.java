package app;

import io.FileImageLoader;
import io.ImageLoader;
import presenter.ImageViewerPresenter;
import view.ImageViewerView;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        ImageLoader loader = new FileImageLoader(new File("images"));
        ImageViewerView view = new ImageViewerView();
        ImageViewerPresenter presenter = new ImageViewerPresenter(loader, view.getDisplay());

        view.setPresenter(presenter);
        view.setVisible(true);
        presenter.init();
    }
}