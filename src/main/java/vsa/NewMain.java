package vsa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class NewMain {

    public static void main(String[] args) {
        novyTovar(null, null);
        //drahsiTovar();
        //znizCenu();
    }

    public static Long novyTovar(String nazov, Double cena) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();

        TypedQuery<Tovar> tovar = em.createQuery("SELECT t FROM Tovar t WHERE t.nazov = :nazov", Tovar.class);
        tovar.setParameter("nazov", nazov);
        List<Tovar> tovary = tovar.getResultList();

        if (!tovary.isEmpty()) {
            return null;
        }

        Tovar t = new Tovar();
        t.setNazov(nazov);
        t.setCena(cena);

        em.getTransaction().begin();
        try {
            em.persist(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return t.getId();
    }

    public static List<Tovar> drahsiTovar() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();
        TypedQuery<Tovar> tovar = em.createQuery("select t from Tovar t where t.cena > 10.0", Tovar.class);
        List<Tovar> tovary = tovar.getResultList();
        for (Tovar t : tovary) {
            System.out.println("ID: " + t.getId() + " Nazov: " + t.getNazov() + " Cena: " + t.getCena());
        }
        return tovary;
    }

    public static void znizCenu() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
        EntityManager em = emf.createEntityManager();

//        List<Tovar> tovary = drahsiTovar();
        TypedQuery<Tovar> tovar = em.createQuery("select t from Tovar t where t.cena > 10.0", Tovar.class);
        List<Tovar> tovary = tovar.getResultList();

        em.getTransaction().begin();
        for (Tovar t : tovary) {
            t.setCena(t.getCena() * 0.5);
        }
        em.getTransaction().commit();
    }

//    /* Vyhladanie knihy podla kluca - ilustruje: 
//        find
//    Skuste tiez 
//        vyhladat neexiatujucu knihu
//     */
//    public static void najdiKnihu() {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("vsaPU");
//        EntityManager em = emf.createEntityManager();
//
//        Kniha k = em.find(Kniha.class, "Janko Hrasko");
//        System.out.println("" + k);
//    }
}
