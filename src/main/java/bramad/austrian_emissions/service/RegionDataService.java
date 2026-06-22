package bramad.austrian_emissions.service;

import bramad.austrian_emissions.Repository.PopulationRepository;
import bramad.austrian_emissions.Repository.RegionDataRepository;
import bramad.austrian_emissions.mapper.RegionDataMapper;
import bramad.austrian_emissions.pojo.Population;
import bramad.austrian_emissions.pojo.RegionData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RegionDataService {

    private final RestTemplate restTemplate;

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
        return filterEmissionData(null, sector, year, startYear, endYear)
                .collect(Collectors.groupingBy(
                        RegionData::getYear,
                        TreeMap::new,
                        Collectors.groupingBy(
                                RegionData::getSector,
                                Collectors.summingLong(RegionData::getValue)
                        )
                ));
    }

    public List<Map<String, Object>> percentageChange(String region, String sector, String year, String startYear, String endYear) {

        List<RegionData> filteredData = filterEmissionData(region, sector, year, startYear, endYear)
                .sorted(Comparator.comparing((RegionData rd) -> rd.getRegion().getName())
                        .thenComparing(RegionData::getSector)
                        .thenComparing(RegionData::getYear))
                .toList();

        List<Map<String, Object>> result = new ArrayList<>();

        RegionData prev = null;

        for (RegionData curr : filteredData) {

            if (prev == null) {
                prev = curr;
                continue;
            }

            if (prev.getRegion().getName().equals(curr.getRegion().getName()) &&
                    prev.getSector().equals(curr.getSector())) {

                double prevValue = prev.getValue();
                double currValue = curr.getValue();
                double percentageChange = ((currValue - prevValue) / prevValue) * 100;

                Map<String, Object> trend = new HashMap<>();
                trend.put("region", curr.getRegion().getName());
                trend.put("sector", curr.getSector());
                trend.put("year", curr.getYear());
                trend.put("percentageChange", percentageChange);
                trend.put("unit", "t CO2eq");

                result.add(trend);
            }

            prev = curr;
        }

        return result;
    }

    public Map<String, Object> calculateHeatingShare(int year) {

        if (year > 2022 || year < 2004) {
            return Map.of(
                    "Error", "There are only values available between 2004 and 2022!"
            );
        }

        String heatingUrl = "http://10.143.165.155:8080/api/v1/heating/by_region/co2_ton_equ?year=" + year;

        Map<String, Object> heatingResponse = restTemplate.getForObject(heatingUrl, Map.class);

        Map<String, Double> heatingEmissions = (Map<String, Double>) heatingResponse.get("conversion");

        Map<String, Long> buildingEmissions = regionDataRepository.findAll().stream()
                .filter(rd -> rd.getSector().equals("Gebäude"))
                .filter(rd -> rd.getYear() == year)
                .collect(Collectors.toMap(
                        rd -> rd.getRegion().getName(),
                        RegionData::getValue
                ));

        Map<String, Object> heatingShare = new HashMap<>();

        if (heatingEmissions != null) {
            for (Map.Entry<String, Double> entry : heatingEmissions.entrySet()) {
                String region = entry.getKey();
                Double heatingValue = entry.getValue();
                Long buildingValue = buildingEmissions.getOrDefault(region, 0L);

                if (buildingValue != 0) {
                    double share = (heatingValue / buildingValue) * 100;
                    heatingShare.put(region, Math.floor(share * 1000) / 1000);
                } else {
                    heatingShare.put(region, 0.0);
                }
            }
        }

        return heatingShare;
    }
}