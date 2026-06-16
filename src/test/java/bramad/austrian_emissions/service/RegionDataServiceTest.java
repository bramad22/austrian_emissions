package bramad.austrian_emissions.service;

import bramad.austrian_emissions.Repository.PopulationRepository;
import bramad.austrian_emissions.Repository.RegionDataRepository;
import bramad.austrian_emissions.pojo.Population;
import bramad.austrian_emissions.pojo.Region;
import bramad.austrian_emissions.pojo.RegionData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegionDataServiceTest {

    @Mock
    private RegionDataRepository regionDataRepository;

    @Mock
    private PopulationRepository populationRepository;

    @InjectMocks
    private RegionDataService regionDataService;

    @Test
    @DisplayName("Get detailed information - filter by region returns only matching entries")
    void getEmissionDetailed_filterByRegion() {
        Region vienna = new Region("vienna_id", "Vienna", List.of());
        Region graz   = new Region("graz_id",   "Graz",   List.of());

        RegionData d1 = new RegionData(1L, "src", 1000L, 2020, vienna, "Energy", "CO2", "t", "A");
        RegionData d2 = new RegionData(2L, "src", 2000L, 2020, graz,   "Energy", "CO2", "t", "A");

        when(regionDataRepository.findAll()).thenReturn(List.of(d1, d2));

        List<Map<String, Object>> result = regionDataService.getEmissionDetailed("Vienna", null, null, null, null);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Get detailed information - no filters returns all entries")
    void getEmissionDetailed_noFilter_returnsAll() {
        Region vienna = new Region("vienna_id", "Vienna", List.of());

        RegionData d1 = new RegionData(1L, "src", 1000L, 2020, vienna, "Energy",    "CO2", "t", "A");
        RegionData d2 = new RegionData(2L, "src", 2000L, 2021, vienna, "Transport", "CO2", "t", "A");

        when(regionDataRepository.findAll()).thenReturn(List.of(d1, d2));

        List<Map<String, Object>> result = regionDataService.getEmissionDetailed(null, null, null, null, null);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Total emissions per year per sector - values are summed correctly")
    void totalEmissionsPerYearPerSector_sumsCorrectly() {
        Region vienna = new Region("vienna_id", "Vienna", List.of());

        RegionData d1 = new RegionData(1L, "src", 1000L, 2020, vienna, "Energy", "CO2", "t", "A");
        RegionData d2 = new RegionData(2L, "src", 2000L, 2020, vienna, "Energy", "CO2", "t", "A");

        when(regionDataRepository.findAll()).thenReturn(List.of(d1, d2));

        Map<Integer, Map<String, Long>> result = regionDataService.totalEmissionsPerYearPerSector(null, null, null, null);

        assertEquals(3000L, result.get(2020).get("Energy"));
    }

    @Test
    @DisplayName("Total emissions per year per sector - years are sorted ascending")
    void totalEmissionsPerYearPerSector_yearsSortedAscending() {
        Region vienna = new Region("vienna_id", "Vienna", List.of());

        RegionData d1 = new RegionData(1L, "src", 1000L, 2022, vienna, "Energy", "CO2", "t", "A");
        RegionData d2 = new RegionData(2L, "src", 2000L, 2020, vienna, "Energy", "CO2", "t", "A");

        when(regionDataRepository.findAll()).thenReturn(List.of(d1, d2));

        Map<Integer, Map<String, Long>> result = regionDataService.totalEmissionsPerYearPerSector(null, null, null, null);

        assertIterableEquals(List.of(2020, 2022), result.keySet());
    }

    @Test
    @DisplayName("Emissions per capita - value is divided by population correctly")
    void emissionsPerCapita_calculatesCorrectly() {
        Region vienna = new Region("vienna_id", "Vienna", List.of());

        RegionData d1 = new RegionData(1L, "src", 1000L, 2020, vienna, "Energy", "CO2", "t", "A");

        when(regionDataRepository.findAll()).thenReturn(List.of(d1));
        when(populationRepository.findByRegionAndYear("Vienna", 2020))
                .thenReturn(Optional.of(new Population(1L, "Vienna", 2020, 500L)));

        List<Map<String, Object>> result = regionDataService.emissionsPerCapita(null, null, "2020", null, null);

        assertEquals(2.0, (Double) result.get(0).get("valuePerCapita"));
    }

    @Test
    @DisplayName("Percentage change - calculates correct change between two years")
    void percentageChange_calculatesCorrectly() {
        Region vienna = new Region("vienna_id", "Vienna", List.of());

        RegionData d1 = new RegionData(1L, "src", 1000L, 2020, vienna, "Energy", "CO2", "t", "A");
        RegionData d2 = new RegionData(2L, "src", 2000L, 2021, vienna, "Energy", "CO2", "t", "A");

        when(regionDataRepository.findAll()).thenReturn(List.of(d1, d2));

        List<Map<String, Object>> result = regionDataService.percentageChange("Vienna", "Energy", null, null, null);

        assertEquals(1, result.size());
        assertEquals(100.0, (Double) result.get(0).get("percentageChange"));
    }

    @Test
    @DisplayName("Percentage change - different regions are not compared")
    void percentageChange_differentRegions_notCompared() {
        Region vienna = new Region("vienna_id", "Vienna", List.of());
        Region graz   = new Region("graz_id",   "Graz",   List.of());

        RegionData d1 = new RegionData(1L, "src", 1000L, 2020, vienna, "Energy", "CO2", "t", "A");
        RegionData d2 = new RegionData(2L, "src", 2000L, 2021, graz,   "Energy", "CO2", "t", "A");

        when(regionDataRepository.findAll()).thenReturn(List.of(d1, d2));

        List<Map<String, Object>> result = regionDataService.percentageChange(null, null, null, null, null);

        assertTrue(result.isEmpty());
    }
}