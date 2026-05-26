package bramad.austrian_emissions.Repository;

import bramad.austrian_emissions.pojo.Population;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PopulationRepository extends JpaRepository<Population, Long> {
    Optional<Population> findByRegionAndYear(String region, int year);
}
