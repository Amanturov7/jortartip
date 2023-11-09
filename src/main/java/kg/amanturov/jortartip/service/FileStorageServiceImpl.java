//package kg.amanturov.jortartip.service;
//
//import kg.amanturov.jortartip.dto.AttachmentRequestDto;
//import kg.amanturov.jortartip.dto.AttachmentResponseDto;
//import kg.amanturov.jortartip.model.Attachments;
//import kg.amanturov.jortartip.repository.AttachmentRepository;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.UrlResource;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Objects;
//import java.util.UUID;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//
//@Service
//public class FileStorageServiceImpl implements FileStorageService {
//
//    private final AttachmentRepository repository;
//
//    @Value("${file.storage.docs}")
//    private String docDirectory;
//    @Value("${file.storage.pdf}")
//    private String pdfDirectory;
//    @Value("${file.storage.excel}")
//    private String excelDirectory;
//    @Value("${file.storage.png}")
//    private String pngDirectory;
//    @Value("${file.storage.image}")
//    private String imageDirectory;
//
//    public FileStorageServiceImpl(AttachmentRepository repository) {
//        this.repository = repository;
//    }
//
//
//    @Override
//    public String save(MultipartFile file, String extension) {
//
//        String uuid = Objects.requireNonNull(UUID.randomUUID().toString()).concat("."+extension);
//        switch(extension) {
//            case "docs":
//            case "doc":
//            case "docx":
//                try {
//                    Path root = Paths.get(docDirectory);
//                    Files.copy(file.getInputStream(), root.resolve(uuid));
//                    return root+"/"+uuid;
//                } catch (Exception e) {
//                    throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//                }
//            case "pdf":
//                try {
//                    Path root = Paths.get(pdfDirectory);
//                    Files.copy(file.getInputStream(), root.resolve(uuid));
//                    return root+"/"+uuid;
//                } catch (Exception e) {
//                    throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//                }
//            case "xlsx":
//            case "xls":
//                try {
//                    Path root = Paths.get(excelDirectory);
//                    Files.copy(file.getInputStream(), root.resolve(uuid));
//                    return root+"/"+uuid;
//                } catch (Exception e) {
//                    throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//                }
//            case "png":
//                try {
//                    Path root = Paths.get(pngDirectory);
//                    Files.copy(file.getInputStream(), root.resolve(uuid));
//                    return root+"/"+uuid;
//                } catch (Exception e) {
//                    throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//                }
//            case "image":
//                try {
//                    Path root = Paths.get(imageDirectory);
//                    Files.copy(file.getInputStream(), root.resolve(uuid));
//                    return root+"/"+uuid;
//                } catch (Exception e) {
//                    throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
//                }
//        }
//        return null;
//    }
//
//
//    @Override
//    public AttachmentResponseDto saveAttachment(MultipartFile file, AttachmentRequestDto dto) throws IOException {
//        AttachmentResponseDto responseDto = new AttachmentResponseDto();
//        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
//        String targetDirectory;
//        switch (extension.toLowerCase()) {
//            case "pdf":
//            case "csv":
//                targetDirectory = pdfDirectory;
//                break;
//            case "xlsx":
//            case "xls":
//                targetDirectory = excelDirectory;
//                break;
//            case "png":
//                targetDirectory = pngDirectory;
//                break;
//            case "image":
//                targetDirectory = imageDirectory;
//                break;
//            case "doc":
//            case "docx":
//                targetDirectory = docDirectory;
//                break;
//            default:
//                throw new RuntimeException("Не поддерживаемый тип файла: " + extension);
//        }
//        String fileName = Objects.requireNonNull(dto.getType()).concat("№" + UUID.randomUUID().toString()).concat("." + extension);
//        Path root = Paths.get(targetDirectory);
//        Files.createDirectories(root);
//        Path filePath = root.resolve(fileName);
//        Files.copy(file.getInputStream(), filePath);
//        responseDto.setFilePath(filePath.toString());
//        responseDto.setType(dto.getType());
//        responseDto.setExtension(extension);
//        responseDto.setName(fileName);
//        responseDto.setDescription(dto.getDescription());
//        responseDto.setOriginName(dto.getOriginName());
//        Attachments attachments = convertDtoToEntity(responseDto);
//        Attachments saved = repository.save(attachments);
//        responseDto.setAttachmentId(saved.getId());
//        return responseDto;
//    }
//
////
////    @Override
////    public void updateAttachments(FakeIrrigation fakeIrrigation,Long id) {
////        Attachments attachments = repository.findById(id).orElseThrow();
////        attachments.setIrrigationSystem(fakeIrrigation);
////        repository.save(attachments);
////    }
////
////    @Override
////    public void updateAttachments(SystemObject systemObject,Long id) {
////        Attachments attachments = repository.findById(id).orElseThrow();
////        attachments.setSystemObject(systemObject);
////        repository.save(attachments);
////    }
////
////    @Override
////    public void updateAttachments(WaterUsers waterUsers,Long id) {
////        Attachments attachments = repository.findById(id).orElseThrow();
////        attachments.setWaterUsers(waterUsers);
////        repository.save(attachments);
////    }
////
////    @Override
////    public void updateAttachments(Organization organization,Long id) {
////        Attachments attachments = repository.findById(id).orElseThrow();
////        attachments.setOrganization(organization);
////        repository.save(attachments);
////    }
//
//    private AttachmentResponseDto mapToAttachmentResponseDto(Attachments attachment) {
//        AttachmentResponseDto responseDto = new AttachmentResponseDto();
//        responseDto.setFilePath(attachment.getPath());
//        responseDto.setType(attachment.getType());
//        responseDto.setExtension(attachment.getExtension());
//        responseDto.setName(attachment.getName());
//        responseDto.setAttachmentId(attachment.getId());
////        if(attachment.getWaterUsers() != null) {
////            responseDto.setWaterUserId(attachment.getWaterUsers().getId());
////        }
////        if(attachment.getOrganization() != null) {
////            responseDto.setOrganizationId(attachment.getOrganization().getId());
////        }
////        if(attachment.getIrrigationSystem() != null) {
////            responseDto.setSystemId(attachment.getIrrigationSystem().getId());
////        }
////        if(attachment.getSystemObject() != null) {
////            responseDto.setObjectId(attachment.getSystemObject().getId());
////        }
//        return responseDto;
//    }
//
//    @Override
//    public AttachmentResponseDto getAttachmentById(Long id) {
//        Attachments attachment = repository.findById(id).orElse(null);
//        if (attachment != null) {
//            AttachmentResponseDto responseDto = new AttachmentResponseDto();
//            responseDto.setAttachmentId(attachment.getId());
//            responseDto.setFilePath(attachment.getPath());
//            responseDto.setType(attachment.getType());
//            responseDto.setDescription(attachment.getDescription());
//            responseDto.setOriginName(attachment.getName());
////
////            responseDto.setWaterUserId(attachment.getWaterUsers() != null ? attachment.getWaterUsers().getId() : null);
////            responseDto.setOrganizationId(attachment.getOrganization() != null ? attachment.getOrganization().getId() : null);
////            responseDto.setObjectId(attachment.getSystemObject() != null ? attachment.getSystemObject().getId() : null);
////            responseDto.setSystemId(attachment.getIrrigationSystem() != null ? attachment.getIrrigationSystem().getId() : null);
//
//            responseDto.setExtension(attachment.getExtension());
//            responseDto.setName(attachment.getName());
//            return responseDto;
//        } else {
//            return null;
//        }
//    }
////
////    @Override
////    public List<AttachmentResponseDto> findBySystemId(Long id) {
////        List<Attachments> findAllBySystem = repository.findAllByIrrigationSystemId(id);
////        List<AttachmentResponseDto> attachmentResponseDtos = findAllBySystem.stream()
////                .map(attachment -> {
////                    AttachmentResponseDto responseDto = mapToAttachmentResponseDto(attachment);
////                    String sanitizedFileName = responseDto.getName();
////                    sanitizedFileName = sanitizedFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
////                    responseDto.setDownloadUrl("http://localhost:8080/rest/attachment/download/" + responseDto.getAttachmentId());
////                    responseDto.setName(sanitizedFileName);
////                    return responseDto;
////                })
////                .collect(Collectors.toList());
////        return attachmentResponseDtos;
////    }
////
////    @Override
////    public List<AttachmentResponseDto> findByObjectId(Long id) {
////        List<Attachments> findAllBySystem = repository.findAllBySystemObjectId(id);
////        List<AttachmentResponseDto> attachmentResponseDtos = findAllBySystem.stream()
////                .map(attachment -> {
////                    AttachmentResponseDto responseDto = mapToAttachmentResponseDto(attachment);
////                    String sanitizedFileName = responseDto.getName();
////                    sanitizedFileName = sanitizedFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
////                    responseDto.setDownloadUrl("http://localhost:8080/rest/attachment/download/" + responseDto.getAttachmentId());
////                    responseDto.setName(sanitizedFileName);
////                    return responseDto;
////                })
////                .collect(Collectors.toList());
////        return attachmentResponseDtos;
////    }
////
////    @Override
////    public List<AttachmentResponseDto> findByWaterUserId(Long id) {
////        List<Attachments> findAllBySystem = repository.findAllByWaterUsersId(id);
////        List<AttachmentResponseDto> attachmentResponseDtos = findAllBySystem.stream()
////                .map(attachment -> {
////                    AttachmentResponseDto responseDto = mapToAttachmentResponseDto(attachment);
////                    String sanitizedFileName = responseDto.getName();
////                    sanitizedFileName = sanitizedFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
////                    responseDto.setDownloadUrl("http://localhost:8080/rest/attachment/download/" + responseDto.getAttachmentId());
////                    responseDto.setName(sanitizedFileName);
////                    return responseDto;
////                })
////                .collect(Collectors.toList());
////        return attachmentResponseDtos;
////    }
////
////    @Override
////    public List<AttachmentResponseDto> findByOrganizationId(Long id) {
////        List<Attachments> attachments = repository.findAllByOrganizationId(id);
////        List<AttachmentResponseDto> attachmentResponseDtos = attachments.stream()
////                .map(attachment -> {
////                    AttachmentResponseDto responseDto = mapToAttachmentResponseDto(attachment);
////                    String sanitizedFileName = responseDto.getName();
////                    sanitizedFileName = sanitizedFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
////                    responseDto.setDownloadUrl("http://localhost:8080/rest/attachment/download/" + responseDto.getAttachmentId());
////                    responseDto.setName(sanitizedFileName);
////                    return responseDto;
////                })
////                .collect(Collectors.toList());
////        return attachmentResponseDtos;
////    }
//
//
//    private Attachments convertDtoToEntity(AttachmentResponseDto responseDto) {
//        Attachments attachments = new Attachments();
//        attachments.setExtension(responseDto.getExtension());
//        attachments.setType(responseDto.getType());
//        attachments.setName(responseDto.getName());
//        attachments.setPath(responseDto.getFilePath());
//        attachments.setDescription(responseDto.getDescription());
//        return attachments;
//    }
//    private AttachmentResponseDto convertDtoToEntity(Attachments responseDto) {
//        AttachmentResponseDto attachments = new AttachmentResponseDto();
//        attachments.setExtension(responseDto.getExtension());
//        attachments.setType(responseDto.getType());
//        attachments.setName(responseDto.getName());
//        attachments.setFilePath(responseDto.getPath());
//        attachments.setAttachmentId(responseDto.getId());
//        attachments.setDescription(responseDto.getDescription());
////        if(responseDto.getIrrigationSystem() != null) {
////            attachments.setSystemId(responseDto.getIrrigationSystem().getId());
////        }
////        if(responseDto.getOrganization() != null) {
////            attachments.setOrganizationId(responseDto.getOrganization().getId());
////        }
////        if(responseDto.getSystemObject() != null) {
////            attachments.setObjectId(responseDto.getSystemObject().getId());
////        }
////        if(responseDto.getWaterUsers() != null) {
////            attachments.setWaterUserId(responseDto.getWaterUsers().getId());
////        }
//        return attachments;
//    }
//    @Override
//    public Resource convertFileFromPath(Attachments attachments) throws IOException {
//        Path filePath = Paths.get(attachments.getPath());
//        byte[] fileData = Files.readAllBytes(filePath);
//        return new ByteArrayResource(fileData);
//    }
//
//    @Override
//    public AttachmentResponseDto getFileById(Long id) throws IOException {
//        Attachments getById = repository.findById(id).get();
//        AttachmentResponseDto responseDto = convertDtoToEntity(getById);
//        responseDto.setFile(convertFileFromPath(getById));
//        return responseDto;
//    }
//
//    @Override
//    public Resource load(String fileName, Path root) {
//        try {
//            Path file = root.resolve(fileName);
//            Resource resource = new UrlResource(file.toUri());
//
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                throw new RuntimeException("Could not read the file!");
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Error: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public Stream<Path> loadAll() {
//        return null;
//    }
//}
