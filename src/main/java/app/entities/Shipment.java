package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@EqualsAndHashCode
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Parcel parcel;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Location sourceLocation;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Location destinationLocation;

    private LocalDateTime shipmentDateTime;
    

}
