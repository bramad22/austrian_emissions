package bramad.austrian_emissions.controller;

import bramad.austrian_emissions.service.RegionDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/emission")
@RequiredArgsConstructor
public class Controller {
    private final RegionDataService regionService;

    @GetMapping("/test")
    public ResponseEntity<String> getHello(){
        return ResponseEntity.ok("Servas!");
    }

    @GetMapping("/emissionDetailed")
    public List<Map<String, Object>> getRegionData(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String startYear,
            @RequestParam(required = false) String endYear
    ) {
        return regionService.getEmissionDetailed(
                region,
                sector,
                year,
                startYear,
                endYear
        );
    }

    @GetMapping("/totalEmissions")
    public List<Map<String, Object>> totalEmissions(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String startYear,
            @RequestParam(required = false) String endYear
    ) {
        return regionService.getEmissionDetailed(
                region,
                "KSG",
                year,
                startYear,
                endYear
        );
    }

    @GetMapping("/emissionPerCapita")
    public List<Map<String, Object>> emissionPerCapita(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String startYear,
            @RequestParam(required = false) String endYear
    ) {
        return regionService.emissionsPerCapita(
                region,
                sector,
                year,
                startYear,
                endYear
        );
    }

    @GetMapping("/totalEmissionPerCapita")
    public List<Map<String, Object>> totalEmissionPerCapita(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String startYear,
            @RequestParam(required = false) String endYear
    ) {
        return regionService.emissionsPerCapita(
                region,
                "KSG",
                year,
                startYear,
                endYear
        );
    }

    @GetMapping("/totalEmissionPerSector")
    public Map<Integer, Map<String, Long>> totalEmissionPerSector(
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String startYear,
            @RequestParam(required = false) String endYear
    ) {
        return regionService.totalEmissionsPerYearPerSector(
                sector,
                year,
                startYear,
                endYear
        );
    }

    @GetMapping("/percentageChange")
    public List<Map<String, Object>> percentageChange(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String startYear,
            @RequestParam(required = false) String endYear
    ) {
        return regionService.percentageChange(
                region,
                sector,
                year,
                startYear,
                endYear
        );
    }
}

