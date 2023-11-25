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

    AttachmentResponseDto saveAttachment(MultipartFile file, AttachmentRequestDto dto) throws IOException;


    AttachmentResponseDto getAttachmentById(Long id);


//    void updateAttachments(FakeIrrigation fakeIrrigation, Long id);
//    void updateAttachments(SystemObject systemObject,Long id);
//    void updateAttachments(WaterUsers waterUsers,Long id);
//    void updateAttachments(Organization organization,Long id);

//    List<AttachmentResponseDto> findBySystemId(Long id);
//    List<AttachmentResponseDto> findByObjectId(Long id);
//
//    List<AttachmentResponseDto> findByWaterUserId(Long id);
//
//    List<AttachmentResponseDto> findByOrganizationId(Long id);

    Resource convertFileFromPath(Attachments attachments) throws IOException;

    AttachmentResponseDto getFileById(Long id) throws IOException;

    Resource load(String fileName, Path root);

    void deleteAll();

    Stream<Path> loadAll();


}
