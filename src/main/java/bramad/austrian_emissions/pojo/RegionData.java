package bramad.austrian_emissions.pojo;

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

    private Long value;

    private int year;

    @ManyToOne
    @JoinColumn(name = "id")
    @JsonManagedReference
    private Region region;

    @JsonAlias("sektor")
    private String sector;

    private String pollutant;

    private String unit;

    private String classification;
}
