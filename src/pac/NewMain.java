/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pac;

import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author eyvind
 */
public class NewMain {

    private EntityManagerFactory factory;

    private void startup() {
        factory = Persistence.createEntityManagerFactory("JPA_familiesPU");
        EntityManager em = factory.createEntityManager();
// Begin a new local transaction so that we can persist a new entity
        em.getTransaction().begin();
// read the existing entries
        Query q = em.createQuery("select m from Person m");
        int size=q.getResultList().size();
        // Persons should be empty

        // do we have entries?
//^^        boolean createNewEntries = (q.getResultList().size() == 0);
        // No, so lets create new entries
       if (size<1) 
        {
            //^ assertTrue(q.getResultList().size() == 0);
            Family family = new Family();
            family.setDescription("Family for the Knopfs");
            em.persist(family);
            for (int i = 0; i < 40; i++) {
                Person person = new Person();
                person.setFirstName("Jim_" + i);
                person.setLastName("Knopf_" + i);
                em.persist(person);
                // now persists the family person relationship
                family.getMembers().add(person);
                em.persist(person);
                em.persist(family);
            }
        }

        // Commit the transaction, which will cause the entity to
        // be stored in the database
        em.getTransaction().commit();
        // It is always good practice to close the EntityManager so that
        // resources are conserved.
        em.close();
        checkAvailablePeople();
        deletePerson();

    }

    public void checkAvailablePeople() {

        // now lets check the database and see if the created entries are there
        // create a fresh, new EntityManager
        EntityManager em = factory.createEntityManager();

        // Perform a simple query for all the Message entities
        Query q = em.createQuery("select m from Person m");

        List list = q.getResultList();
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        em.close();
    }

    public void deletePerson() {
        EntityManager em = factory.createEntityManager();
        // Begin a new local transaction so that we can persist a new entity
        try {
            em.getTransaction().begin();
            Query q = em
                    .createQuery("SELECT p FROM Person p WHERE p.firstName = :firstName AND p.lastName = :lastName");
            q.setParameter("firstName", "Jim_5");
            q.setParameter("lastName", "Knopf_5");
            Person user = (Person) q.getSingleResult();
            System.out.println("Person before delete: " + user);
            em.remove(user);
            em.getTransaction().commit();
            user = (Person) q.getSingleResult();
            System.out.println("Person after delete: " + user);
            em.close();
        } catch (NoResultException | NonUniqueResultException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        NewMain main = new NewMain();
        main.startup();

    }

}
