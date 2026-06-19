package io;

import model.Image;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImageLoader {
    Image load(File file) throws IOException;
    List<File> listImages();
    File save(File file) throws IOException;
}