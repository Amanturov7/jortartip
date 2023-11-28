package kg.amanturov.jortartip.service;

import kg.amanturov.jortartip.dto.ViolationsDto;
import kg.amanturov.jortartip.model.CommonReference;
import kg.amanturov.jortartip.model.Violations;
import kg.amanturov.jortartip.repository.CommonReferenceRepository;
import kg.amanturov.jortartip.repository.ViolationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViolationsServiceImpl implements ViolationsService {

    private final ViolationsRepository repository;
    private final CommonReferenceRepository commonReferenceRepository;

    public ViolationsServiceImpl(ViolationsRepository repository, CommonReferenceRepository commonReferenceRepository) {
        this.repository = repository;
        this.commonReferenceRepository = commonReferenceRepository;
    }

    @Override
    public List<Violations> findAll() {
        return repository.findAll();
    }

    @Override
    public Violations findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Violations save(Violations violations) {
        return repository.save(violations);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public ViolationsDto convertEntityToDto(Violations violations) {
        ViolationsDto violationsDto = new ViolationsDto();
        violationsDto.setDescription(violations.getDescription());
        violationsDto.setId(violations.getId());
        violationsDto.setTitle(violations.getTitle());
        violationsDto.setCostFiz(violations.getCostFiz());
        violationsDto.setCostUr(violations.getCostUr());
        violationsDto.setCreatedDate(violations.getCreatedDate());
        if(violations.getOrgan() != null){
            violationsDto.setOrganId(violations.getOrgan().getId());
        }
        violationsDto.setStatya(violations.getStatya());
        violationsDto.setPart(violations.getPart());
        violationsDto.setUpdateDate(violations.getUpdateDate());

        return  violationsDto;
    }

    @Override
    public Violations convertDtoToEntity(ViolationsDto violationsDto) {
        Violations violations = new Violations();
        violations.setDescription(violationsDto.getDescription());
        violations.setId(violationsDto.getId());
        violations.setTitle(violationsDto.getTitle());
        violations.setCostFiz(violationsDto.getCostFiz());
        violations.setCostUr(violationsDto.getCostUr());
        violations.setCreatedDate(violationsDto.getCreatedDate());

        if (violationsDto.getOrganId() != null) {
            CommonReference organ = commonReferenceRepository.findById(violationsDto.getOrganId()).orElse(null);
            violations.setOrgan(organ);
        }

        violations.setStatya(violationsDto.getStatya());
        violations.setPart(violationsDto.getPart());
        violations.setUpdateDate(violationsDto.getUpdateDate());

        return violations;
    }

}
