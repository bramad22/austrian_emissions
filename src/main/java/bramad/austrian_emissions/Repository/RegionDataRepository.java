package bramad.austrian_emissions.Repository;

import bramad.austrian_emissions.pojo.RegionData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionDataRepository extends JpaRepository<RegionData, Long> {
}
