package bramad.austrian_emissions.pojo;

import bramad.austrian_emissions.annotations.ToDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionData {

    @Id
    private Long id;

    private String source;

    @ToDto(key = "emissionValue")
    private Long value;

    @ToDto
    private int year;

    @ToDto
    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    @JsonManagedReference
    private Region region;

    @ToDto(key = "sektor_name")
    @JsonAlias("sektor")
    private String sector;

    private String pollutant;

    @ToDto
    private String unit;

    private String classification;
}
