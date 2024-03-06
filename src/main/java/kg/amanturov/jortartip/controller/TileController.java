package kg.amanturov.jortartip.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
@RequestMapping("/rest/")

@RestController
public class TileController {

    @Value("${tiles.storage}")
    private String tilesDirectory;

    @GetMapping("/tiles/{z}/{x}/{y}.png")
    public ResponseEntity<Resource> getTile(
            @PathVariable int z,
            @PathVariable int x,
            @PathVariable int y
    ) {
        String tilePath = String.format("%s/%d/%d/%d.png", tilesDirectory, z, x, y);

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(tilePath)) {
            if (inputStream != null) {
                byte[] tileBytes = inputStream.readAllBytes();
                ByteArrayResource resource = new ByteArrayResource(tileBytes);

                return ResponseEntity.ok()
                        .contentLength(tileBytes.length)
                        .contentType(MediaType.IMAGE_PNG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
