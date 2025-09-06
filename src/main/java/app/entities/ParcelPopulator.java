package app.entities;

import app.dao.ParcelDao;

import java.time.LocalDateTime;

public class ParcelPopulator {
    public static Parcel[] populate(ParcelDao parcelDao){
        Parcel p1 = new Parcel(1,"h124","Peter Marcher","Ida Marcher", DeliveryStatus.PENDING, LocalDateTime.now());
        Parcel p2 = new Parcel(2,"h125","Lucas walker","Ivy petersen ", DeliveryStatus.DELIVERED, LocalDateTime.now());

        parcelDao.create(p1);
        parcelDao.create(p2);

        return new Parcel[]{p1, p2};
    }
}
