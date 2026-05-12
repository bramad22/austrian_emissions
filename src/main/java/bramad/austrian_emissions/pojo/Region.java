package bramad.austrian_emissions.pojo;

import bramad.austrian_emissions.annotations.ToDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Region {
    @Id
    private String id;

    @ToDto(key = "regionName")
    private String name;

    @JsonBackReference
    @OneToMany(mappedBy = "region")
    private List<RegionData> data;
}
