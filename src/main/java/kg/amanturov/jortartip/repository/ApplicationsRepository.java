package kg.amanturov.jortartip.repository;

import kg.amanturov.jortartip.model.Applications;
import kg.amanturov.jortartip.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationsRepository extends JpaRepository<Applications, Long> {

    List<Applications> findApplicationsByStatusAndUserId(String status, Long id );

    List<Applications> findAllByUserId(Long id);

}
