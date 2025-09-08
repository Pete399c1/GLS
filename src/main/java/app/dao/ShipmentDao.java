package app.dao;

import app.entities.DeliveryStatus;
import app.entities.Location;
import app.entities.Parcel;
import app.entities.Shipment;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ShipmentDao {
    private EntityManagerFactory emf;

    public Shipment findById(int id){
        try(EntityManager em = emf.createEntityManager()){
            return  em.find(Shipment.class, id);
        }
    }

    public List<Shipment> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Shipment> query = em.createQuery("select s from Shipment s", Shipment.class);
            return query.getResultList();
        }
    }

    public Shipment create(Shipment shipment){
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(shipment);
            em.getTransaction().commit();
        }
        return shipment;
    }


}
