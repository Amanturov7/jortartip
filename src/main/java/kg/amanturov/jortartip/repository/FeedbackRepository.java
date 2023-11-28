package kg.amanturov.jortartip.repository;

import kg.amanturov.jortartip.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}