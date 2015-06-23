package pl.confitura.jelatyna.barcode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code")
public class BarCodeController {

    private BarCodeGenerator generator;

    @Autowired
    public BarCodeController(BarCodeGenerator generator) {
        this.generator = generator;
    }

    @RequestMapping("/{token}")
    public ResponseEntity<byte[]> getCodeFor(@PathVariable String token) {
        byte[] barcode = generator.generateFor(token);
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_PNG)
            .contentLength(barcode.length)
            .body(barcode);
    }
}
