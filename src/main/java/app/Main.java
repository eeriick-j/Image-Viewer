package app;

import io.FileImageLoader;
import presenter.ImagePresenter;
import view.MainFrame;
import view.SwingImageDisplay;

import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        SwingImageDisplay display = new SwingImageDisplay();
        FileImageLoader loader = new FileImageLoader(new File("images"));
        ImagePresenter presenter = new ImagePresenter(loader, display);
        MainFrame frame = new MainFrame(presenter, display);

        frame.setVisible(true);
        presenter.init();
    }
}
