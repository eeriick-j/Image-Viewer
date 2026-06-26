package presenter;

import io.ImageLoader;
import model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import view.ImageDisplay;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageViewerPresenterTest {
    @Mock private ImageLoader loader;
    @Mock private ImageDisplay display;
    @TempDir Path tempDir;

    private ImageViewerPresenter presenter;
    private final Image dummyImage =
            new Image(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), "dummy.jpg");

    @BeforeEach
    void setUp() {
        presenter = new ImageViewerPresenter(loader, display);
    }

    @Nested
    class Init {

        @Test
        void emptyList_callsClear() {
            when(loader.listImages()).thenReturn(Collections.emptyList());
            presenter.init();
            verify(display).clear();
            verify(display, never()).show(any());
        }

        @Test
        void withImages_showsFirst() throws IOException {
            File f = mock(File.class);
            when(loader.listImages()).thenReturn(List.of(f));
            when(loader.load(f)).thenReturn(dummyImage);

            presenter.init();

            verify(display).show(dummyImage);
            verify(display, never()).clear();
        }

        @Test
        void loadThrowsIOException_callsClear() throws IOException {
            File f = mock(File.class);
            when(loader.listImages()).thenReturn(List.of(f));
            when(loader.load(f)).thenThrow(new IOException("disk error"));
            presenter.init();
            verify(display).clear();
        }
    }

    @Nested
    class Next {
        @Test
        void beforeInit_doesNotThrow() {
            assertDoesNotThrow(() -> presenter.next());
        }

        @Test
        void emptyList_doesNothing() {
            when(loader.listImages()).thenReturn(Collections.emptyList());
            presenter.init();
            clearInvocations(display);

            presenter.next();
            verifyNoInteractions(display);
        }

        @Test
        void advancesToSecondImage() throws IOException {
            File f1 = mock(File.class);
            File f2 = mock(File.class);
            Image img2 = new Image(new BufferedImage(1, 1, 1), "b.jpg");
            when(loader.listImages()).thenReturn(List.of(f1, f2));
            when(loader.load(f1)).thenReturn(dummyImage);
            when(loader.load(f2)).thenReturn(img2);

            presenter.init();
            presenter.next();
            verify(display).show(img2);
        }

        @Test
        void wrapsAroundToFirst() throws IOException {
            File f = mock(File.class);
            when(loader.listImages()).thenReturn(List.of(f));
            when(loader.load(f)).thenReturn(dummyImage);

            presenter.init();
            presenter.next();

            verify(display, times(2)).show(dummyImage);
        }
    }

    @Nested
    class Prev {
        @Test
        void beforeInit_doesNotThrow() {
            assertDoesNotThrow(() -> presenter.prev());
        }

        @Test
        void emptyList_doesNothing() {
            when(loader.listImages()).thenReturn(Collections.emptyList());
            presenter.init();
            clearInvocations(display);

            presenter.prev();
            verifyNoInteractions(display);
        }

        @Test
        void fromFirstWrapsToLast() throws IOException {
            File f1 = mock(File.class);
            File f2 = mock(File.class);
            Image img2 = new Image(new BufferedImage(1, 1, 1), "b.jpg");
            when(loader.listImages()).thenReturn(List.of(f1, f2));
            when(loader.load(f1)).thenReturn(dummyImage);
            when(loader.load(f2)).thenReturn(img2);

            presenter.init();
            presenter.prev();
            verify(display).show(img2);
        }

        @Test
        void goesBackToPrevious() throws IOException {
            File f1 = mock(File.class);
            File f2 = mock(File.class);
            Image img2 = new Image(new BufferedImage(1, 1, 1), "b.jpg");
            when(loader.listImages()).thenReturn(List.of(f1, f2));
            when(loader.load(f1)).thenReturn(dummyImage);
            when(loader.load(f2)).thenReturn(img2);

            presenter.init();
            presenter.next();
            presenter.prev();

            verify(display, times(2)).show(dummyImage);
        }
    }

    @Nested
    class Add {
        @BeforeEach
        void initWithEmptyList() {
            when(loader.listImages()).thenReturn(new ArrayList<>());
            presenter.init();
            clearInvocations(display);
        }

        @Test
        void nullFile_ignored() throws IOException {
            presenter.add(null);
            verify(loader, never()).save(any());
            verifyNoInteractions(display);
        }

        @Test
        void nonExistentFile_ignored() throws IOException {
            presenter.add(new File("/nonexistent/photo.jpg"));
            verify(loader, never()).save(any());
            verifyNoInteractions(display);
        }

        @Test
        void nonImageFile_ignored() throws IOException {
            Path txt = tempDir.resolve("notes.txt");
            Files.writeString(txt, "hello");
            presenter.add(txt.toFile());
            verify(loader, never()).save(any());
            verifyNoInteractions(display);
        }

        @Test
        void validImage_delegatesToLoaderSave() throws IOException {
            Path png = createTempPng("photo.png");
            when(loader.save(png.toFile())).thenReturn(png.toFile());
            when(loader.load(png.toFile())).thenReturn(dummyImage);
            presenter.add(png.toFile());
            verify(loader).save(png.toFile());
        }

        @Test
        void validImage_showsAfterAdd() throws IOException {
            Path png = createTempPng("photo.png");
            when(loader.save(png.toFile())).thenReturn(png.toFile());
            when(loader.load(png.toFile())).thenReturn(dummyImage);
            presenter.add(png.toFile());
            verify(display).show(dummyImage);
        }

        @Test
        void validImage_becomesCurrentAfterAdd() throws IOException {
            File existing = mock(File.class);
            Image existingImg = new Image(new BufferedImage(1,1,1), "existing.jpg");
            when(loader.listImages()).thenReturn(new ArrayList<>(List.of(existing)));
            when(loader.load(existing)).thenReturn(existingImg);
            presenter.init();

            Path png = createTempPng("new.png");
            Image newImg = new Image(new BufferedImage(1,1,1), "new.png");
            when(loader.save(png.toFile())).thenReturn(png.toFile());
            when(loader.load(png.toFile())).thenReturn(newImg);

            presenter.add(png.toFile());
            verify(display).show(newImg);
        }

        @Test
        void saveThrowsIOException_fileNotAddedToList() throws IOException {
            Path png = createTempPng("photo.png");
            when(loader.save(png.toFile())).thenThrow(new IOException("disk full"));

            presenter.add(png.toFile());
            presenter.next();
            verify(loader, never()).load(any());
        }

        @Test
        void saveThrowsIOException_doesNotCorruptState() throws IOException {
            File existing = mock(File.class);
            when(loader.listImages()).thenReturn(new ArrayList<>(List.of(existing)));
            when(loader.load(existing)).thenReturn(dummyImage);
            presenter.init();
            clearInvocations(display);

            Path png = createTempPng("bad.png");
            when(loader.save(png.toFile())).thenThrow(new IOException("disk full"));

            presenter.add(png.toFile());

            presenter.next();
            verify(display).show(dummyImage);
        }
    }

    /**
     * Crea un fichero PNG mínimo en tempDir.
     * La firma PNG (primeros 4 bytes) garantiza que Files.probeContentType
     * devuelva "image/png" en sistemas que inspeccionan el contenido.
     * La extensión .png cubre los sistemas que solo miran el nombre.
     */
    private Path createTempPng(String name) throws IOException {
        Path path = tempDir.resolve(name);
        Files.write(path, new byte[]{
                (byte) 0x89, 0x50, 0x4E, 0x47,
                0x0D, 0x0A, 0x1A, 0x0A
        });
        return path;
    }
}