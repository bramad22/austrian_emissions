package bramad.austrian_emissions.database;

import bramad.austrian_emissions.Repository.PopulationRepository;
import bramad.austrian_emissions.Repository.RegionDataRepository;
import bramad.austrian_emissions.Repository.RegionRepository;
import bramad.austrian_emissions.pojo.DTO.ImportDataDto;
import bramad.austrian_emissions.pojo.Population;
import bramad.austrian_emissions.pojo.Region;
import bramad.austrian_emissions.pojo.RegionData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitDatabase {

    private final RegionDataRepository regionDataRepository;
    private final RegionRepository regionRepository;
    private final PopulationRepository populationRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    private void init() throws IOException {
        readEmissionsPerRegionFile();
        readPopulationDataFile();
    }

    private void readEmissionsPerRegionFile() throws IOException {

        InputStream inputStream = this.getClass()
                .getResourceAsStream("/emissionen_bundeslaender.json");

        List<ImportDataDto> importData = objectMapper.readerForListOf(ImportDataDto.class).readValue(inputStream);

        List<Region> allRegions = new ArrayList<>();

        for (ImportDataDto dto : importData) {
            if (allRegions.stream().noneMatch(r -> r.getId().equals(dto.getRegion_id()))) {
                Region region = new Region();
                region.setId(dto.getRegion_id());
                region.setName(dto.getRegion());
                allRegions.add(region);
            }
        }

        regionRepository.saveAll(allRegions);

        List<RegionData> regionDataList = importData.stream()
                .map(dto -> {
                    Region region = allRegions.stream().filter(r -> r.getId().equals(dto.getRegion_id())).findFirst().orElse(null);
                    RegionData regionData = new RegionData();
                    regionData.setId(dto.getId());
                    regionData.setSource(dto.getSource());
                    regionData.setValue(dto.getValue());
                    regionData.setYear(dto.getYear());
                    regionData.setRegion(region);
                    regionData.setSector(dto.getSector());
                    regionData.setPollutant(dto.getPollutant());
                    regionData.setUnit(dto.getUnit());
                    regionData.setClassification(dto.getClassification());
                    return regionData;
                })
                .collect(Collectors.toList());

        regionDataRepository.saveAll(regionDataList);
    }

    private void readPopulationDataFile() throws IOException {
        InputStream inputStream = this.getClass()
                .getResourceAsStream("/population_data.json");

        List<Population> populations = objectMapper.readerForListOf(Population.class).readValue(inputStream);

        populationRepository.saveAll(populations);
    }
}
