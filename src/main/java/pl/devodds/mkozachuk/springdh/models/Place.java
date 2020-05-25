package pl.devodds.mkozachuk.springdh.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Place {

    @Id
    @GeneratedValue
    private Long place_id;

    @ManyToOne(targetEntity = Country.class)
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user; //created by

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "zip")
    private String zip;

    private boolean dreamed;
}
