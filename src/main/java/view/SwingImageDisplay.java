package view;

import model.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    private BufferedImage currentImage;

    public SwingImageDisplay() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
    }

    @Override
    public void show(Image image) {
        if (image == null || image.data() == null) {
            clear();
            return;
        }
        currentImage = image.data();
        repaint();
    }

    @Override
    public void clear() {
        currentImage = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentImage == null) return;

        int panelW = getWidth();
        int panelH = getHeight();
        int imgW    = currentImage.getWidth();
        int imgH    = currentImage.getHeight();

        double scale = Math.min((double) panelW / imgW, (double) panelH / imgH);
        int drawW = (int) (imgW * scale);
        int drawH = (int) (imgH * scale);
        int x = (panelW - drawW) / 2;
        int y = (panelH - drawH) / 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.drawImage(currentImage, x, y, drawW, drawH, null);
    }
}