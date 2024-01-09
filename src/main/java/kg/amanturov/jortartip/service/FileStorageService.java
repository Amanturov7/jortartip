package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.AttachmentRequestDto;
import kg.amanturov.jortartip.dto.AttachmentResponseDto;
import kg.amanturov.jortartip.model.Attachments;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FileStorageService {


    String save(MultipartFile file, String path);

    AttachmentResponseDto findByApplicationsId(Long id);

    AttachmentResponseDto findByReviewsId(Long id);


    AttachmentResponseDto saveAttachment(MultipartFile file, AttachmentRequestDto dto) throws IOException;


    void deleteByApplicationsId(Long id);

    AttachmentResponseDto getAttachmentById(Long id);

    void deleteAttachmentById(Long id);

    Resource convertFileFromPath(Attachments attachments) throws IOException;

    AttachmentResponseDto getFileById(Long id) throws IOException;

    Resource load(String fileName, Path root);

    void deleteAll();

    Stream<Path> loadAll();


}
