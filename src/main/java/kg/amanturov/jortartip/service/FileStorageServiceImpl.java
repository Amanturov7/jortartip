package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.AttachmentRequestDto;
import kg.amanturov.jortartip.dto.AttachmentResponseDto;
import kg.amanturov.jortartip.model.Attachments;
import kg.amanturov.jortartip.repository.AttachmentRepository;

import kg.amanturov.jortartip.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;


@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final AttachmentRepository repository;
    private final UserService userService;
    private final ViolationsService violationsService;

    @Value("${file.storage.photos}")
    private String photoDirectory;
    @Value("${file.storage.videos}")
    private String videoDirectory;


    public FileStorageServiceImpl(AttachmentRepository repository, UserService userService, ViolationsService violationsService) {
        this.repository = repository;
        this.userService = userService;
        this.violationsService = violationsService;
    }


    @Override
    public String save(MultipartFile file, String extension) {

        String uuid = Objects.requireNonNull(UUID.randomUUID().toString()).concat("."+extension);
        switch(extension) {
            case "mp4":
            case "mov":
                try {
                    Path root = Paths.get(videoDirectory);
                    Files.copy(file.getInputStream(), root.resolve(uuid));
                    return root+"/"+uuid;
                } catch (Exception e) {
                    throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
                }
            case "jpg":
            case "jpeg":
            case "png":
            case "image":
                try {
                    Path root = Paths.get(photoDirectory);
                    Files.copy(file.getInputStream(), root.resolve(uuid));
                    return root+"/"+uuid;
                } catch (Exception e) {
                    throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
                }
        }
        return null;
    }


    @Override
    public AttachmentResponseDto saveAttachment(MultipartFile file, AttachmentRequestDto dto) throws IOException {
        AttachmentResponseDto responseDto = new AttachmentResponseDto();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String targetDirectory;
        switch (extension.toLowerCase()) {
            case "mp4":
            case "csv":
                targetDirectory = videoDirectory;
                break;
            case "jpg":
            case "jpeg":
            case "png":
            case "image":
                targetDirectory = photoDirectory;
                break;
            default:
                throw new RuntimeException("Не поддерживаемый тип файла: " + extension);
        }
        String fileName = Objects.requireNonNull(dto.getType()).concat("№" + UUID.randomUUID().toString()).concat("." + extension);
        Path root = Paths.get(targetDirectory);
        Files.createDirectories(root);
        Path filePath = root.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        responseDto.setFilePath(filePath.toString());
        responseDto.setType(dto.getType());
        responseDto.setExtension(extension);
        responseDto.setName(fileName);
        responseDto.setViolationsId(dto.getViolationsId());
        responseDto.setUserId(dto.getUserId());
        responseDto.setDescription(dto.getDescription());
        responseDto.setOriginName(dto.getOriginName());
        Attachments attachments = convertDtoToEntity(responseDto);
        Attachments saved = repository.save(attachments);
        responseDto.setAttachmentId(saved.getId());
        return responseDto;
    }

    private AttachmentResponseDto mapToAttachmentResponseDto(Attachments attachment) {
        AttachmentResponseDto responseDto = new AttachmentResponseDto();
        responseDto.setFilePath(attachment.getPath());
        responseDto.setType(attachment.getType());
        responseDto.setExtension(attachment.getExtension());
        responseDto.setName(attachment.getName());
        responseDto.setAttachmentId(attachment.getId());
        if(attachment.getUser() != null) {
            responseDto.setUserId(attachment.getUser().getId());
        }
        if(attachment.getViolationsId() != null) {
            responseDto.setViolationsId(attachment.getViolationsId().getId());
        }
        return responseDto;
    }

    @Override
    public AttachmentResponseDto getAttachmentById(Long id) {
        Attachments attachment = repository.findById(id).orElse(null);
        if (attachment != null) {
            AttachmentResponseDto responseDto = new AttachmentResponseDto();
            responseDto.setAttachmentId(attachment.getId());
            responseDto.setFilePath(attachment.getPath());
            responseDto.setType(attachment.getType());
            responseDto.setDescription(attachment.getDescription());
            responseDto.setOriginName(attachment.getName());
            responseDto.setExtension(attachment.getExtension());
            if(attachment.getUser() != null) {
                responseDto.setUserId(attachment.getUser().getId());
            }
            if(attachment.getViolationsId() != null) {
                responseDto.setViolationsId(attachment.getViolationsId().getId());
            }
            responseDto.setName(attachment.getName());
            return responseDto;
        } else {
            return null;
        }
    }

    private Attachments convertDtoToEntity(AttachmentResponseDto responseDto) {
        Attachments attachments = new Attachments();
        attachments.setExtension(responseDto.getExtension());
        attachments.setType(responseDto.getType());
        attachments.setName(responseDto.getName());
        attachments.setPath(responseDto.getFilePath());
        attachments.setDescription(responseDto.getDescription());
        if(responseDto.getUserId() != null) {
            attachments.setUser(userService.findById(responseDto.getUserId()));
        }
        if(responseDto.getViolationsId() != null) {
            attachments.setViolationsId(violationsService.findById(responseDto.getViolationsId()));
        }
        return attachments;
    }
    private AttachmentResponseDto convertDtoToEntity(Attachments attachments) {
        AttachmentResponseDto responseDto = new AttachmentResponseDto();
        responseDto.setExtension(attachments.getExtension());
        responseDto.setType(attachments.getType());
        responseDto.setName(attachments.getName());
        responseDto.setFilePath(attachments.getPath());
        responseDto.setAttachmentId(attachments.getId());
        responseDto.setDescription(attachments.getDescription());
        if(attachments.getUser() != null) {
            responseDto.setUserId(attachments.getUser().getId());
        }
        if(attachments.getViolationsId() != null) {
            responseDto.setViolationsId(attachments.getViolationsId().getId());
        }
        return responseDto;
    }

    @Override
    public Resource convertFileFromPath(Attachments attachments) throws IOException {
        Path filePath = Paths.get(attachments.getPath());
        byte[] fileData = Files.readAllBytes(filePath);
        return new ByteArrayResource(fileData);
    }

    @Override
    public AttachmentResponseDto getFileById(Long id) throws IOException {
        Attachments getById = repository.findById(id).get();
        AttachmentResponseDto responseDto = convertDtoToEntity(getById);
        responseDto.setFile(convertFileFromPath(getById));
        return responseDto;
    }

    @Override
    public Resource load(String fileName, Path root) {
        try {
            Path file = root.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }
}
