package kg.amanturov.jortartip.repository;

import kg.amanturov.jortartip.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
