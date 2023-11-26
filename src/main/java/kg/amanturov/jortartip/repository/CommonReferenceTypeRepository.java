package kg.amanturov.jortartip.repository;


import kg.amanturov.jortartip.model.CommonReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonReferenceTypeRepository extends JpaRepository<CommonReferenceType, Long> {

    CommonReferenceType findByCode(String code);

    CommonReferenceType findByTitle(String code);

}
