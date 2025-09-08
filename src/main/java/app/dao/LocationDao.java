package app.dao;

import app.entities.Location;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class LocationDao {

    private EntityManagerFactory emf;

    public LocationDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

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

    public Location createLocation(Location location){
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(location);
            em.getTransaction().commit();
        }
        return location;
    }


    public Location update(Location location) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Location merged = em.merge(location);
            em.getTransaction().commit();
            return merged;
        }
    }

    public boolean delete(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            Location l = em.find(Location.class, id);
            if (l == null) return false;
            em.getTransaction().begin();
            em.remove(l);
            em.getTransaction().commit();
            return true;
        }
    }

}
