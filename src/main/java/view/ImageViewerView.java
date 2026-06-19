package view;

import presenter.ImageViewerPresenter;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImageViewerView extends JFrame {
    private final SwingImageDisplay display = new SwingImageDisplay();
    private final JButton nextButton = new JButton("Next");
    private final JButton prevButton = new JButton("Prev");
    private final JButton addButton  = new JButton("Add");

    public ImageViewerView() {
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
        addButton.addActionListener(e -> {
            File file = chooseFile();
            presenter.add(file);}
        );
    }

    private File chooseFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) return chooser.getSelectedFile();
        return null;
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