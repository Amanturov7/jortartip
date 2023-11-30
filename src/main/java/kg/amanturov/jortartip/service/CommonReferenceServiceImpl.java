package kg.amanturov.jortartip.service;


import jakarta.persistence.EntityNotFoundException;
import kg.amanturov.jortartip.model.CommonReference;
import kg.amanturov.jortartip.model.CommonReferenceType;
import kg.amanturov.jortartip.repository.CommonReferenceRepository;
import kg.amanturov.jortartip.repository.CommonReferenceTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CommonReferenceServiceImpl implements CommonReferenceService {

    private final CommonReferenceRepository repository;
    private final CommonReferenceTypeRepository typeRepository;

    public CommonReferenceServiceImpl(CommonReferenceRepository repository, CommonReferenceTypeRepository typeRepository) {
        this.repository = repository;
        this.typeRepository = typeRepository;
    }

    @Override
    public List<CommonReference> findAll() {
        return repository.findAll();
    }

    @Override
    public List<CommonReference> findAllByCode(String code) {
        return repository.findAllByType(typeRepository.findByTitle(code));
    }

    @Override
    public CommonReference findByTitle(String search) {
        return repository.findByTitle(search);
    }

    @Override
    public List<CommonReference> findAllByType(String code) {
        CommonReferenceType id = typeRepository.findByTitle(code);
        return repository.findByTypeId(id.getId());
    }

    @Override
    public List<CommonReference> findAllById(Long id) {
        return repository.findAllByTypeId(id);
    }

    @Override
    public CommonReference findById(Long id) {
        Optional<CommonReference> optionalCommonReference = repository.findById(id);

        if (optionalCommonReference.isPresent()) {
            return optionalCommonReference.get();
        } else {
            throw new EntityNotFoundException("CommonReference with ID " + id + " not found");
        }
    }


    @Override
    public List<CommonReferenceType> findAllTyeps() {
        return typeRepository.findAll();
    }

    @Override
    public CommonReferenceType findTypeByCode(String code) {
        return typeRepository.findByCode(code);
    }

    @Override
    public List<CommonReference> findParentIdById(Long id) {
        return repository.findByParentId(id);
    }

    @Override
    public CommonReference findByTypeIdAndCode(String code, Long typeId) {
        return repository.findByTypeIdAndCode(typeId,code);
    }

    @Override
    public List<CommonReference> findByParentIdAndType(Long parentId, Long typeId) {
        CommonReferenceType type = typeRepository.findById(typeId).orElseThrow();
        return repository.findAllByParentIdAndType(parentId, type);
    }


}
