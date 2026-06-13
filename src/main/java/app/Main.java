package app;

import view.MainFrame;
import view.SwingImageDisplay;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingImageDisplay display = new SwingImageDisplay();
        MainFrame frame = new MainFrame(display);
        frame.setVisible(true);
    }
}
