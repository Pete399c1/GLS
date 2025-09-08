package app.dao;

import app.entities.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class LocationDao {

    private EntityManagerFactory emf;

    public Location findById(int id){
        try(EntityManager em = emf.createEntityManager()){
            return em.find(Location.class, id);
        }
    }

    public List<Location> findAll(){
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Location> query = em.createQuery("select l from Location  l", Location.class);
            return query.getResultList();
        }
    }

    public Location create(Location location){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(location);
            em.getTransaction().commit();
        }
        return location;
    }

}
