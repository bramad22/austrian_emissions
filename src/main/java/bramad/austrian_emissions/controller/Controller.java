package bramad.austrian_emissions.controller;

import bramad.austrian_emissions.service.RegionDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class Controller {
    private final RegionDataService regionService;

    @GetMapping("/testServas")
    public ResponseEntity<String> getHello(){
        return ResponseEntity.ok("Servas!");
    }

    @GetMapping("/regionData")
    public List<Map<String, Object>> getRegionData() {
        return regionService.getRegions();
    }
}

