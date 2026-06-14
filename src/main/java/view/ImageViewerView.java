package view;

import presenter.ImageViewerPresenter;

import javax.swing.*;
import java.awt.*;

public class ImageViewerView extends JFrame {
    private final SwingImageDisplay display = new SwingImageDisplay();  // ← interno, nadie de afuera lo toca
    private final JButton nextButton = new JButton("Next");
    private final JButton prevButton = new JButton("Prev");
    private final JButton addButton  = new JButton("Add");

    public ImageViewerView() {  // ← sin parámetros
        setTitle("Image Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        add(display, BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    public void setPresenter(ImageViewerPresenter presenter) {
        nextButton.addActionListener(e -> presenter.next());
        prevButton.addActionListener(e -> presenter.prev());
        addButton.addActionListener(e -> {});
    }

    public ImageDisplay getDisplay() {
        return display;
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel();
        panel.add(prevButton);
        panel.add(addButton);
        panel.add(nextButton);
        return panel;
    }
}