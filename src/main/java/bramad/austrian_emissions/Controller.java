package bramad.austrian_emissions;

import bramad.austrian_emissions.pojo.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class Controller {

    @GetMapping("/testServas")
    public ResponseEntity<String> getHello(){
        return ResponseEntity.ok("Servas!");
    }
}

