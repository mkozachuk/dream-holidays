package pl.devodds.mkozachuk.springdh.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Table(name = "transport")
public class Transport {
    public Transport(String type) {
        this.type = TransportType.valueOf(type);
    }
    public Transport(TransportType type) {
        this.type = type;
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long transport_id;
    @Column(name = "type")
    private TransportType type;
}
