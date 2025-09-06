package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
@Table(name = "parcels")
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parcel_seq")
    @SequenceGenerator(name = "parcel_seq", sequenceName = "parcel_id_seq", allocationSize = 1)
    private int id;

    /*
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     */

    @Column(name = "tracking_number", nullable = false, unique = true)
    private String trackingNumber;

    @Column(name = "sender_name", nullable = false)
    private String SenderName;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    // enum makes it to a string and more readable than a number
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;


    @Column(name = "updated")
    private LocalDateTime updated;

    //Hibernate will be working with this on auto
    // every time I will be using update og create in my Dao class.
    // we will be getting the time and date of when it happened so the now()
    @PrePersist
    private void prePersist(){
        updated = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate(){
        updated = LocalDateTime.now();
    }


}
