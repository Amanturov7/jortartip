package kg.amanturov.jortartip.repository;

import kg.amanturov.jortartip.model.User;
import kg.amanturov.jortartip.model.Violations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViolationsRepository extends JpaRepository<Violations, Long> {
    Optional<Violations> findByTitle(String title);
}
