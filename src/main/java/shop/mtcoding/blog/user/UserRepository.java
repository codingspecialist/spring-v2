package shop.mtcoding.blog.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRepository {
    @Autowired
    private EntityManager em;

    public User findByUsernameAndPassword(String username, String password) {
        Query query = em.createQuery("select u from User u where u.username=:username and u.password=:password", User.class);
        query.setParameter("username", username);
        query.setParameter("password", password);
        User user = (User) query.getSingleResult();
        return user;
    }

    @Transactional
    public void save(User user) {
        System.out.println("담기기전 : " + user.getId());
        em.persist(user);
        System.out.println("담긴후 : " + user.getId());
    }

}
