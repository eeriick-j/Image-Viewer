package view;
import presenter.ImagePresenter;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final ImagePresenter presenter;
    private JButton nextButton;
    private JButton prevButton;
    private JButton addButton;
    private SwingImageDisplay imageDisplay;

    public MainFrame(ImagePresenter presenter, SwingImageDisplay display) {
        this.presenter = presenter;

        setTitle("Image Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        add(display, BorderLayout.CENTER);
        add(createButtons(), BorderLayout.SOUTH);

        setEventListeners();
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

    private void setEventListeners() {
        nextButton.addActionListener(e -> presenter.next());
        prevButton.addActionListener(e -> presenter.prev());
        // Implementar más tarde...
        addButton.addActionListener(e -> {});
    }
}