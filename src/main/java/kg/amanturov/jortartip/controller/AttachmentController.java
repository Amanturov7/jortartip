package kg.amanturov.jortartip.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import kg.amanturov.jortartip.Exceptions.MyFileNotFoundException;
import kg.amanturov.jortartip.dto.AttachmentRequestDto;
import kg.amanturov.jortartip.dto.AttachmentResponseDto;
import kg.amanturov.jortartip.repository.AttachmentRepository;
import kg.amanturov.jortartip.service.FileStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/rest/attachment")
public class AttachmentController {

    private final FileStorageService service;
    private final AttachmentRepository repository;

    private final ObjectMapper objectMapper;

    public AttachmentController(FileStorageService service, AttachmentRepository repository, ObjectMapper objectMapper) {
        this.service = service;
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public AttachmentResponseDto uploadFile(@RequestPart String dto, @RequestPart MultipartFile file) throws IOException {
        try {
            AttachmentRequestDto attachmentRequestDto = objectMapper.readValue(dto, AttachmentRequestDto.class);
            return service.saveAttachment(file, attachmentRequestDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new AttachmentResponseDto();
        }
    }
//
//    @GetMapping(value = "/download/water-user/{id}")
//    public ResponseEntity<List<AttachmentResponseDto>> findByWaterUserId(@PathVariable Long id) throws IOException {
//        List<AttachmentResponseDto> attachments = service.findByWaterUserId(id);
//        if (attachments.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok().body(attachments);
//    }
//

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) throws Exception {
        AttachmentResponseDto attachment = service.getAttachmentById(id);
        if (attachment == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] fileContent;
        try {
            fileContent = readFileContent(attachment.getFilePath());
        } catch (IOException e) {
            throw new MyFileNotFoundException("Ошибка при чтении файла: " + e.getMessage());
        }
        String sanitizedFileName = attachment.getName();
        sanitizedFileName = sanitizedFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", sanitizedFileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileContent.length)
                .body(fileContent);
    }

//    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public @ResponseBody byte[] getFileByPath(@RequestParam("path") String path) throws IOException {
//        return readFileContent(path);
//    }
//
//
    private byte[] readFileContent(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }


}
