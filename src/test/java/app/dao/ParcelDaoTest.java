package app.dao;

import app.config.HibernateConfig;
import app.entities.DeliveryStatus;
import app.entities.Parcel;
import app.services.ParcelPopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ParcelDaoTest {
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final ParcelDao parcelDao = ParcelDao.getEntityManagerFactoryForTest(emf);
    private static Parcel p1;
    private static Parcel p2;

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Parcel").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE parcel_id_seq RESTART WITH 1");
            em.getTransaction().commit();
            Parcel[] parcels = ParcelPopulator.populate(parcelDao);
            p1 = parcels[0];
            p2 = parcels[1];
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Parcel").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Test
    void getInstance() {
        assertNotNull(parcelDao);
    }

    @Test
    void create() {
        Parcel newParcel = Parcel.builder()
                .trackingNumber("h126")
                .SenderName("peter")
                .receiverName("alex")
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();

        Parcel created = parcelDao.create(newParcel);
        assertNotNull(created.getId()); // nu bliver id sat af databasen
        assertEquals("h126", created.getTrackingNumber());
    }

    @Test
    void findByTrackingNumber() {
        Parcel newParcel = Parcel.builder()
                .trackingNumber("h128")
                .SenderName("ally")
                .receiverName("Dave")
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();

        parcelDao.create(newParcel);

        Optional<Parcel> found = parcelDao.findByTrackingNumber("h128");

        assertTrue(found.isPresent());
        assertEquals("ally", found.get().getSenderName());
    }

    @Test
    void findAll() {
        Parcel parcel1 = Parcel.builder()
                .trackingNumber("h130")
                .SenderName("jonas")
                .receiverName("Frank")
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();

        Parcel parcel2 = Parcel.builder()
                .trackingNumber("h131")
                .SenderName("micheal")
                .receiverName("Helen")
                .deliveryStatus(DeliveryStatus.DELIVERED)
                .build();

        parcelDao.create(parcel1);
        parcelDao.create(parcel2);

        List<Parcel> all = parcelDao.findAll();

        assertTrue(all.size() >= 2);
        assertTrue(all.stream().anyMatch(p -> p.getTrackingNumber().equals("h130")));
        assertTrue(all.stream().anyMatch(p -> p.getTrackingNumber().equals("h131")));

    }

    @Test
    void updateStatus() {
        Parcel parcel = Parcel.builder()
                .trackingNumber("h140")
                .SenderName("nick")
                .receiverName("Jack")
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();

        parcelDao.create(parcel);

        Parcel updated = parcelDao.updateStatus("h140", DeliveryStatus.DELIVERED);

        assertEquals(DeliveryStatus.DELIVERED, updated.getDeliveryStatus());

    }

    @Test
    void deleteByTrackingNumber() {
        Parcel parcel = Parcel.builder()
                .trackingNumber("h141")
                .SenderName("annie")
                .receiverName("Leo")
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();

        parcelDao.create(parcel);

        boolean deleted = parcelDao.deleteByTrackingNumber("h141");

        assertTrue(deleted);
        assertTrue(parcelDao.findByTrackingNumber("h141").isEmpty());
    }

    @Test
    void findByStatus() {
        Parcel parcel = Parcel.builder()
                .trackingNumber("h143")
                .SenderName("john")
                .receiverName("Noah")
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();

        parcelDao.create(parcel);

        List<Parcel> shippedParcels = parcelDao.findByStatus(DeliveryStatus.PENDING);

        assertFalse(shippedParcels.isEmpty());
        assertTrue(shippedParcels.stream().anyMatch(p -> p.getTrackingNumber().equals("h143")));
    }

}