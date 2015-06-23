package pl.confitura.jelatyna.barcode;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.stereotype.Service;

@Service
public class BarCodeGenerator {

    public byte[] generateFor(String token) {
        return QRCode
            .from(token)
            .to(ImageType.PNG)
            .withSize(250, 250)
            .stream().toByteArray();
    }
}
