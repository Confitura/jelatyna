package pl.confitura.jelatyna.qrcode;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QrCodeTest {

    @Test
    public void name() throws IOException {
        byte[] bytes = QRCode.from("123").to(ImageType.PNG).withSize(250,250).stream().toByteArray();
        Path path = Paths.get("/users/margielm/jelatyna/qrcode.png");
        Files.write(path, bytes);

    }
}
