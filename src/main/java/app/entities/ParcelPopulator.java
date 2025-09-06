package app.entities;

import app.dao.ParcelDao;

import java.time.LocalDateTime;

public class ParcelPopulator {
    public static Parcel[] populate(ParcelDao parcelDao){
        Parcel p1 = Parcel.builder()
                .trackingNumber("h124")
                .SenderName("peter marcher")
                .receiverName("ida marcher")
                .deliveryStatus(DeliveryStatus.PENDING)
                .updated(LocalDateTime.now())
                .build();

        Parcel p2 = Parcel.builder()
                .trackingNumber("h125")
                .SenderName("lucas walker")
                .receiverName("ivy petersen")
                .deliveryStatus(DeliveryStatus.DELIVERED)
                .updated(LocalDateTime.now())
                .build();


        parcelDao.create(p1);
        parcelDao.create(p2);

        return new Parcel[]{p1, p2};
    }
}
