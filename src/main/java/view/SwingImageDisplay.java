package view;

import model.Image;

import javax.swing.*;
import java.awt.*;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    private JLabel label;

    public SwingImageDisplay() {
        setLayout(new BorderLayout());
        label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label, BorderLayout.CENTER);
    }

    @Override
    public void show(Image image) {
        if (image == null || image.data() == null) {
            clear();
            return;
        }
        label.setIcon(new ImageIcon(image.data()));
    }

    @Override
    public void clear() {
        label.setIcon(null);
    }
}
