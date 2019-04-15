/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pac;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author eyvind
 */
public class NewMain {
    
        private static final String PERSISTENCE_UNIT_NAME = "JPA_familiesPU";
    private EntityManagerFactory factory;
    
    private void startup()
    {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();

        // Begin a new local transaction so that we can persist a new entity
        em.getTransaction().begin();

        // read the existing entries
        Query q = em.createQuery("select m from Person m");
        // Persons should be empty

        // do we have entries?
//^^        boolean createNewEntries = (q.getResultList().size() == 0);

        // No, so lets create new entries
       //^^ if (createNewEntries) 
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
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      
         NewMain main=new NewMain();
         main.startup();
        
    }
    
}
