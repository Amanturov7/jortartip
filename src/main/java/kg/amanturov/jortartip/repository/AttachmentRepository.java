package kg.amanturov.jortartip.repository;

import kg.amanturov.jortartip.model.Attachments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachments, Long> {

    Attachments findByApplicationsId(Long id);
    Attachments findByReviewsId(Long id);
    Optional<Attachments> findById(Long id);
}
