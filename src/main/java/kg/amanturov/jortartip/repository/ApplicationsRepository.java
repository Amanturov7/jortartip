package kg.amanturov.jortartip.repository;

import kg.amanturov.jortartip.model.Applications;
import kg.amanturov.jortartip.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationsRepository extends JpaRepository<Applications, Long> {

    List<Applications> findApplicationsByStatusIdAndUserId(Long status, Long id );
    List<Applications> findApplicationsByUserIdAndIsArchived( Long id, Boolean bool );

    List<Applications> findAllByUserId(Long id);
    List<Applications> findTop4ByOrderByCreatedDateDesc();

    List<Applications> findAllByNumberAutoIgnoreCase(String numberAuto);

}
