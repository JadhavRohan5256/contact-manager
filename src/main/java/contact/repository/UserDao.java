package contact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import contact.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
	@Query("Select u from User u where u.email =:emailId")
	public User getUserByEmail(@Param("emailId") String email);
	
	@Query("Select u from User u where u.userId =:id")
	public User getUserById(@Param("id") int userId);
	
	@Query("Select u from User u where u.contactNo =:contact")
	public User getUserByContactNo(@Param("contact") String contactNo);
	
	@Query("Select u from User u where u.password =:pass")
	public User getUserByPassword(@Param("pass") String password);
}
