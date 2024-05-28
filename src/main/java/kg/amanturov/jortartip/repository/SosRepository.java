package kg.amanturov.jortartip.repository;

import kg.amanturov.jortartip.model.Sos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SosRepository extends JpaRepository<Sos, Long> {
    List<Sos> findAllByUserId (Long userId);
}
