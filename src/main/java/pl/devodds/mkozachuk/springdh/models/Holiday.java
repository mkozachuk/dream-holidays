package pl.devodds.mkozachuk.springdh.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "holidays")
public class Holiday {

    public Holiday(Place place) {
        this.place = place;
    }

    public Holiday() {

    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long holiday_id;

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn
    private User user;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "created_date")
    private Date createdAt;

    @ManyToOne()
    @JoinColumn(name = "place_id")
    private Place place;


    @Transient
    private Weather weather;

    //dreamed only

    @Column(name = "price")
    private BigDecimal price;

    private BigDecimal startCapital;
    private BigDecimal monthlySave;

    @Transient
    private List<String> imgs;

    @Transient
    private List<String> interestingPlacesUrls;

    @Transient
    private int timePercent;

    @Transient
    private BigDecimal currentCapital;

    @Transient
    private String enoughCapital;

    @Transient
    private int capitalPercent;


    @ManyToOne()
    @JoinColumn()
    private Transport transport;

    public void addDesign(Place place) {
        this.place = place;
    }
}

