package bramad.austrian_emissions.service;

import bramad.austrian_emissions.Repository.PopulationRepository;
import bramad.austrian_emissions.Repository.RegionDataRepository;
import bramad.austrian_emissions.mapper.RegionDataMapper;
import bramad.austrian_emissions.pojo.Population;
import bramad.austrian_emissions.pojo.RegionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RegionDataService {

    private final RegionDataRepository regionDataRepository;

    private final PopulationRepository populationRepository;

    private Stream<RegionData> filterEmissionData(String region, String sector, String year, String startYear, String endYear) {
        return regionDataRepository.findAll().stream()
                .filter(rd -> region == null || region.isEmpty() || rd.getRegion().getName().equals(region))
                .filter(rd -> sector == null || sector.isEmpty() || rd.getSector().equals(sector))
                .filter(rd -> year == null || year.isEmpty() || rd.getYear() == Integer.parseInt(year))
                .filter(rd -> startYear == null || startYear.isEmpty() || rd.getYear() >= Integer.parseInt(startYear))
                .filter(rd -> endYear == null || endYear.isEmpty() || rd.getYear() <= Integer.parseInt(endYear));
    }

    public List<Map<String, Object>> getEmissionDetailed(String region, String sector, String year, String startYear, String endYear) {
        return filterEmissionData(region, sector, year, startYear, endYear)
                .map(RegionDataMapper::toDto)
                .collect(Collectors.toList());
    }


    public List<Map<String, Object>> emissionsPerCapita(
            String region, String sector, String year, String startYear, String endYear) {

        return filterEmissionData(region, sector, year, startYear, endYear)
                .map(data -> {
                    String currentRegion = data.getRegion().getName();
                    int currentYear = data.getYear();
                    Long emissionValue = data.getValue();
                    String currentSector = data.getSector();

                    Long population = populationRepository.findByRegionAndYear(currentRegion, currentYear)
                            .map(Population::getPopulation)
                            .orElse(0L);

                    double valuePerCapita = emissionValue.doubleValue() / population;

                    Map<String, Object> result = new HashMap<>();
                    result.put("region", currentRegion);
                    result.put("year", currentYear);
                    result.put("sector", currentSector);
                    result.put("valuePerCapita", valuePerCapita);
                    return result;
                })
                .collect(Collectors.toList());
    }

    public Map<Integer, Map<String, Long>> totalEmissionsPerYearPerSector (String sector, String year, String startYear, String endYear) {
        return regionDataRepository.findAll().stream()
                .filter(rd -> sector == null || sector.isEmpty() || rd.getSector().equals(sector))
                .filter(rd -> year == null || year.isEmpty() || rd.getYear() == Integer.parseInt(year))
                .filter(rd -> startYear == null || startYear.isEmpty() || rd.getYear() >= Integer.parseInt(startYear))
                .filter(rd -> endYear == null || endYear.isEmpty() || rd.getYear() <= Integer.parseInt(endYear))
                .collect(Collectors.groupingBy(
                        RegionData::getYear,
                        TreeMap::new,
                        Collectors.groupingBy(
                                RegionData::getSector,
                                Collectors.summingLong(RegionData::getValue)
                        )
                ));
    }

    public List<Map<String, Object>> percentageChange (String region, String sector, String year, String startYear, String endYear) {
        List<RegionData> filteredData = filterEmissionData(region, sector, year, startYear, endYear)
                .sorted(Comparator.comparing(RegionData::getYear))
                .toList();

        Map<String, Map<String, List<RegionData>>> groupedByRegionAndSector = filteredData.stream()
                .collect(Collectors.groupingBy(
                        rd -> rd.getRegion().getName(),
                        Collectors.groupingBy(
                                RegionData::getSector,
                                Collectors.toList()
                        )
                ));

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map.Entry<String, Map<String, List<RegionData>>> regionEntry : groupedByRegionAndSector.entrySet()) {
            String currentRegion = regionEntry.getKey();

            for (Map.Entry<String, List<RegionData>> sectorEntry : regionEntry.getValue().entrySet()) {
                String currentSector = sectorEntry.getKey();
                List<RegionData> dataForSector = sectorEntry.getValue();

                for (int i = 1; i < dataForSector.size(); i++) {
                    RegionData prev = dataForSector.get(i - 1);
                    RegionData curr = dataForSector.get(i);

                    double prevValue = prev.getValue();
                    double currValue = curr.getValue();
                    double percentageChange = ((currValue - prevValue) / prevValue) * 100;

                    Map<String, Object> trend = new HashMap<>();
                    trend.put("region", currentRegion);
                    trend.put("sector", currentSector);
                    trend.put("year", curr.getYear());
                    trend.put("percentageChange", percentageChange);
                    trend.put("unit", "t CO2eq");

                    result.add(trend);
                }
            }
        }

        return result;
    }
}
