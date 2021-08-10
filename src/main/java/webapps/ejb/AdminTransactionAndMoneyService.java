package webapps.ejb;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
public class AdminTransactionAndMoneyService {

    @PersistenceContext
    EntityManager em;

    public AdminTransactionAndMoneyService() {
    }

    
        @RolesAllowed({"admins"})
        public synchronized List<MoneyTransaction> getAllUserTransactions() {
        TypedQuery<MoneyTransaction> query = em.createNamedQuery("getAllTransactions", MoneyTransaction.class);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }
        
        @RolesAllowed({"admins"})
        public synchronized List<SystemUser> getAllUsers() {
        TypedQuery<SystemUser> query_users = em.createNamedQuery("getAllUserData", SystemUser.class);
        try {
            return query_users.getResultList();
        } catch (NoResultException e) {
            return null;
        }

    }


}
