package bramad.austrian_emissions.pojo.DTO;

import bramad.austrian_emissions.pojo.Region;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImportDataDto {

    private Long id;

    private String source;

    private Long value;

    private int year;

    private Long region_id;

    private String region;

    @JsonAlias("sektor")
    private String sector;

    private String pollutant;

    private String unit;

    private String classification;
}
