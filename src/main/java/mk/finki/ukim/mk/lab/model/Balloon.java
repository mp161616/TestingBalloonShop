package mk.finki.ukim.mk.lab.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import mk.finki.ukim.mk.lab.model.enumerations.BalloonType;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import javax.persistence.*;

@Data
@Entity
public class Balloon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    String description;

    @ManyToOne
    Manufacturer manufacturer;

    //    @Enumerated(EnumType.STRING)
    //BalloonType type;


    public Balloon(String name, String description, Manufacturer manufacturer) {
        this.id = (long) (Math.random() * 1000);
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        //this.type = type;
    }


    public Balloon() {

    }
}
