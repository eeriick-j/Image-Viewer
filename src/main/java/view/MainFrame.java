package view;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JButton nextButton;
    private JButton prevButton;
    private JButton addButton;
    private SwingImageDisplay imageDisplay;

    public MainFrame(SwingImageDisplay imageDisplay) {
        this.imageDisplay = imageDisplay;

        setTitle("Image Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        add(imageDisplay, BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private JPanel createButtons() {
        JPanel panel = new JPanel();
        addButton = new JButton("Add");
        nextButton = new JButton("Next");
        prevButton = new JButton("Prev");

        panel.add(prevButton);
        panel.add(addButton);
        panel.add(nextButton);
        return panel;
    }
}