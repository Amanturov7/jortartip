package kg.amanturov.jortartip.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RequestMapping("/rest/")
@RestController
public class TileController {

    @Value("${tiles.storage}")
    private String tilesDirectory;

    @GetMapping("/{z}/{x}/{y}.png")
    public ResponseEntity<byte[]> getTile(
            @PathVariable int z,
            @PathVariable int x,
            @PathVariable int y
    ) {
        String tilePath = String.format("%s/%d/%d/%d.png", tilesDirectory, z, x, y);

        try (FileInputStream inputStream = new FileInputStream(new File(tilePath));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] tileBytes = outputStream.toByteArray();

            return ResponseEntity.ok()
                    .contentLength(tileBytes.length)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(tileBytes);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
