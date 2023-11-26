package kg.amanturov.jortartip.repository;

import kg.amanturov.jortartip.model.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ApplicationsRepository extends JpaRepository<Applications, Long> {
}
