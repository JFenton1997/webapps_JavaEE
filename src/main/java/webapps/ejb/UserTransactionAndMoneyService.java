package webapps.ejb;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import webapps.entity.SystemUser;
import webapps.entity.SystemUserGroup;
import webapps.entity.MoneyTransaction;

/**
 *
 * @author parisis
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserTransactionAndMoneyService {

    @PersistenceContext
    EntityManager em;

    public UserTransactionAndMoneyService() {
    }

    @RolesAllowed({"admins","users"})
    public synchronized List<SystemUser> getUserInfo(String name) {
        TypedQuery<SystemUser> query = em.createNamedQuery("findByUsername", SystemUser.class);
        query.setParameter("name", name);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }

    @RolesAllowed({"admins","users"})
    public synchronized List<MoneyTransaction> getCompleteTransactionsInvolvingUser(String name) {
        TypedQuery<MoneyTransaction> query = em.createNamedQuery("getFinalisedUserTransactions", MoneyTransaction.class);
        query.setParameter("name", name);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }

    @RolesAllowed({"users"})
    public synchronized List<MoneyTransaction> getPaymentRequestsAimedAtUser(String name) {
        TypedQuery<MoneyTransaction> query = em.createNamedQuery("getPaymentRequests", MoneyTransaction.class);
        query.setParameter("name", name);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }

    @RolesAllowed({"admins","users"})
    public synchronized SystemUser getSingleUser(String name) {
        TypedQuery<SystemUser> query = em.createNamedQuery("findByUsername", SystemUser.class);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @RolesAllowed({"users"})
    public synchronized Boolean doPayment(String sender, String reciever, float money) {
        try {
            System.out.println("webapps.ejb.UserTransactionAndMoneyService.doPayment()");
            SystemUser sendingUser = this.getSingleUser(sender);
            SystemUser recivingUser = this.getSingleUser(reciever);
            System.out.println("FOUND USERS");
            if (sendingUser.getMoney() >= money) { //NEED TO ADD MONEY CONVERSION!!!!!
                System.out.println("GOT MONEY");
                sendingUser.setMoney(sendingUser.getMoney() - money);
                recivingUser.setMoney(recivingUser.getMoney() + money);
                em.persist(sendingUser);
                em.persist(recivingUser);
                System.out.println("CREATING TRANSACTION RECORD");
                em.persist(this.newTransaction(sendingUser, recivingUser, money, Boolean.TRUE));
                em.flush();
                System.out.println("FINISHED");
                return true;
            }
        } catch (Exception e) {

            System.err.println(e);
        }
        return false;
    }

    @RolesAllowed({"users"})
    public synchronized Boolean acceptRequest(Long id) {
        try {
            MoneyTransaction t = em.find(MoneyTransaction.class, id);
            SystemUser sendingUser = this.getSingleUser(t.getSendingUser());
            SystemUser recivingUser = this.getSingleUser(t.getReceivingUser());
            float money = t.getMoney();
            System.out.println("FOUND USERS");
            if (sendingUser.getMoney() >= money) { //NEED TO ADD MONEY CONVERSION!!!!!
                System.out.println("GOT MONEY");
                sendingUser.setMoney(sendingUser.getMoney() - money);
                recivingUser.setMoney(recivingUser.getMoney() + money);
                em.persist(sendingUser);
                em.persist(recivingUser);
                System.out.println("UPDATING TRANSACTION RECORD");
                //fix?
                em.find(MoneyTransaction.class, t.getId()).setFinalised(Boolean.TRUE);
                em.flush();
                System.out.println("FINISHED");
                return true;
            }
        } catch (Exception e) {

            System.err.println(e);
        }
        return false;
    }

    @RolesAllowed({"users"})
    public synchronized Boolean refuseRequest(long id) {
        try {
            em.remove(em.find(MoneyTransaction.class, id));
            em.flush();
            System.out.println("Removed");
            return true;

        } catch (Exception e) {

            System.err.println(e);
        }
        return false;
    }

    @RolesAllowed({"users"})
    public synchronized Boolean requestPayment(String sender, String reciever, float money) {
        try {
            System.out.println("webapps.ejb.UserTransactionAndMoneyService.doPayment()");
            SystemUser sendingUser = this.getSingleUser(sender);
            SystemUser recivingUser = this.getSingleUser(reciever);
            System.out.println("FOUND USERS");
            System.out.println("CREATING TRANSACTION RECORD");
            em.persist(this.newTransaction(sendingUser, recivingUser, money, Boolean.FALSE));
            System.out.println("GOT");
            em.flush();
            System.out.println("FINISHED");
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    @RolesAllowed({"users"})
    public synchronized MoneyTransaction newTransaction(SystemUser sender, SystemUser reciver, float payment, Boolean finalised) {
        MoneyTransaction t = new MoneyTransaction(sender.getUsername(), reciver.getUsername(), payment, finalised);
        System.out.println("TRANSACTION CREATED");
        return t;
    }

}
