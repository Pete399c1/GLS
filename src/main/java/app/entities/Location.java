package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@EqualsAndHashCode
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double latitude;
    private double longitude;
    private String address;

    @OneToMany(mappedBy = "sourceLocation", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Shipment> shipmentsFrom = new HashSet<>();

    @OneToMany(mappedBy = "destinationLocation", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Shipment> shipmentsTo = new HashSet<>();

    public void addShipmentFrom(Shipment shipment) {
        shipmentsFrom.add(shipment);
        if (shipment != null) {
            shipment.setSourceLocation(this);
        }
    }

    public void addShipmentTo(Shipment shipment) {
        shipmentsTo.add(shipment);
        if (shipment != null) {
            shipment.setDestinationLocation(this);
        }
    }

    
}
