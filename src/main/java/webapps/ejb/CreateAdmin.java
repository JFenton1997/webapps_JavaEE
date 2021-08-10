package webapps.ejb;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import webapps.entity.SystemUser;
import webapps.entity.SystemUserGroup;



@Startup
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Singleton
public class CreateAdmin{
    @PersistenceContext
    EntityManager em;
    @Resource
    
    @PostConstruct
    public void init() {
        System.err.println("TESTING TO METHOD INIT");
        createAdmin();
    }

    @PreDestroy
    public void preDestroy() {
    }
    

    private void createAdmin(){
        if(checkUser("admin1")==null){
            System.err.println("TESTING TO METHOD CREATE ADMIN");
            registerUser("admin1", "admin1", "admin1", "admin1", 0,"P");
        }
        else{
            
        }
    }
    
    /**
     *  Check if admin exists in the database
     * @param username the username of the admin
     * @return
     */
    private SystemUser checkUser(String username) {
        TypedQuery<SystemUser> query = em.createNamedQuery("findByUsername", SystemUser.class);
        query.setParameter("name", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }                 
    }
    

       public void registerUser(String username, String userpassword, String name, String surname, float money, String currency) {
        try {
            SystemUser sys_user;
            SystemUserGroup sys_user_group;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = userpassword;
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String paswdToStoreInDB = bigInt.toString(16);

            // apart from the default constructor which is required by JPA
            // you need to also implement a constructor that will make the following code succeed
            sys_user = new SystemUser(username, paswdToStoreInDB, name, surname, money, currency);
            sys_user_group = new SystemUserGroup(username, "admins");

            em.persist(sys_user);
            em.persist(sys_user_group);
            em.flush();
            
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}