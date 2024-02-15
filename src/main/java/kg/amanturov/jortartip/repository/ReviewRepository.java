package kg.amanturov.jortartip.repository;


import kg.amanturov.jortartip.model.Applications;
import kg.amanturov.jortartip.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findTop4ByOrderByCreatedDateDesc();

}
