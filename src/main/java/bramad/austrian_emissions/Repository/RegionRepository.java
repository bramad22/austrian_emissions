package bramad.austrian_emissions.Repository;

import bramad.austrian_emissions.pojo.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, String> {
}
