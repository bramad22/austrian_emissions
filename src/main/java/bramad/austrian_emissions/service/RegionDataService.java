package bramad.austrian_emissions.service;

import bramad.austrian_emissions.Repository.RegionDataRepository;
import bramad.austrian_emissions.mapper.RegionDataMapper;
import bramad.austrian_emissions.pojo.RegionData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegionDataService {
    private final RegionDataRepository regionDataRepository;

    public List<Map<String, Object>> getRegions() {
        List<Map<String, Object>> result = new ArrayList<>();

        for(RegionData rd : regionDataRepository.findAll()) {
            result.add(RegionDataMapper.toDto(rd));
        }

        return result;
    }
}
