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
    @ToDto
    private Long id;

    @ToDto(key = "dataSource")
    private String source;

    @ToDto(key = "emissionValue")
    private Double value;

    @ToDto
    private int year;

    @ManyToOne
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    @JsonManagedReference
    private Region region;

    @ToDto(key = "sektorName")
    @JsonAlias("sektor")
    private String sector;

    @ToDto
    private String pollutant;

    @ToDto
    private String unit;

    @ToDto
    private String classification;
}
