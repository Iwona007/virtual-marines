package pl.iwona.virtualmarines.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.iwona.virtualmarines.model.PointDatabase;


@Repository
public interface PointRepository extends JpaRepository<PointDatabase, Long> {
}
