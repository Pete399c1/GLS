package app.dao;

import app.config.HibernateConfig;
import app.entities.DeliveryStatus;
import app.entities.Parcel;
import app.exceptions.ApiException;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

public class ParcelDao {
    private static ParcelDao pD;
    private EntityManagerFactory emf;


    private ParcelDao(EntityManagerFactory emf){
        this.emf = emf;
    }

    public static synchronized ParcelDao getEntityManagerFactoryForTest(EntityManagerFactory emf) {
        if (pD == null) {
            pD = new ParcelDao(emf);
        }
        return pD;
    }

    public static synchronized ParcelDao getEntityManagerFactory() {
        if (pD == null) {
            pD = new ParcelDao(HibernateConfig.getEntityManagerFactory());
        }
        return pD;
    }

    public Parcel create(Parcel parcel){
        EntityManager em = emf.createEntityManager();
        try{
                em.getTransaction().begin();
                em.persist(parcel);
                em.getTransaction().commit();

                return parcel;
        }catch (RuntimeException e){
            if(em.getTransaction().isActive()){
               em.getTransaction().rollback();
            }
            throw new ApiException(400,"Failed while trying to create a new parcel: " + e);
        }finally {
            em.close();
        }
    }

    public Optional<Parcel> findByTrackingNumber(String trackingNumber){
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Parcel> query = em.createQuery("select p from Parcel p where p.trackingNumber = :trackingNumber", Parcel.class);
            query.setParameter("trackingNumber", trackingNumber);
            return Optional.ofNullable(query.getSingleResult());
        }catch (NoResultException e){
            return Optional.empty();
        }catch (RuntimeException e){
            throw new ApiException(500,"could not find a parcel by trackingNumber: " + e);
        }finally {
            em.close();
        }
    }

    public List<Parcel> findAll(){
        EntityManager em = emf.createEntityManager();
        try{
          TypedQuery<Parcel> query = em.createQuery("select p from Parcel p", Parcel.class);
                  return query.getResultList();
        }catch (RuntimeException e){
            throw new ApiException(570,"Failed trying to get the list of parcels: " + e);
        } finally {
            em.close();
        }
    }

    public Parcel updateStatus(String trackingNumber, DeliveryStatus newStatus) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Hent parcel
            Parcel parcel = em.createQuery("SELECT p FROM Parcel p WHERE p.trackingNumber = :trackingNumber", Parcel.class)
                    .setParameter("trackingNumber", trackingNumber)
                    .getSingleResult();

            // Opdater status
            parcel.setDeliveryStatus(newStatus);
            em.getTransaction().commit();

            return parcel;
        } catch (NoResultException e) {
            throw new ApiException(401,"Parcel with trackingNumber was not found: " + trackingNumber + e);
        } finally {
            em.close();
        }
    }

    public boolean deleteByTrackingNumber(String trackingNumber){
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            int rows = em.createQuery("delete from Parcel p where p.trackingNumber = :trackingNumber")
                            .setParameter("trackingNumber",trackingNumber)
                            .executeUpdate();
            em.getTransaction().commit();
            boolean deleted = rows > 0;
            return deleted;
        }catch (RuntimeException e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new ApiException(500,"Did not delete the parcel by trackingNumber: " + e);
        }finally {
            em.close();
        }
    }

    public List<Parcel> findByStatus(DeliveryStatus status){
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Parcel> query = em.createQuery("select p from Parcel p where p.deliveryStatus = :status", Parcel.class);
            query.setParameter("status", status);

            return query.getResultList();
        }catch (RuntimeException e){
            throw new ApiException(500,"Could not find a list of parcels with status" + e);
        }finally {
            em.close();
        }
    }

    public void close(){
        emf.close();
    }
}
