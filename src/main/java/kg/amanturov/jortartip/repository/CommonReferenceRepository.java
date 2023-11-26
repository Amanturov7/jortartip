package kg.amanturov.jortartip.repository;

import kg.amanturov.jortartip.model.CommonReference;
import kg.amanturov.jortartip.model.CommonReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonReferenceRepository extends JpaRepository<CommonReference, Long> {

    List<CommonReference> findAllByType(CommonReferenceType code);

    CommonReference findByTitle(String title);

    List<CommonReference> findByTypeId(Long id);

    List<CommonReference> findAllByTypeId(Long id);

    CommonReference findByTypeIdAndCode(Long id, String code);
    List<CommonReference> findByParentId (Long id);

    List<CommonReference> findAllByParentIdAndType(Long parentId, CommonReferenceType type);
}
