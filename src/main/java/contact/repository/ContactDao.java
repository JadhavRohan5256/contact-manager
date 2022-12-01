package contact.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import contact.model.Contact;

@Repository
public interface ContactDao extends JpaRepository<Contact, Integer>{
	@Query("from Contact as c where c.user.userId =:userId")
	public List<Contact> getUserContacts(@Param("userId") int userId);
	
	@Query("from Contact as c where c.contactId =:id")
	public Contact getContactById(@Param("id") int contactId);
	
	@Query("from Contact as c where c.user.userId =:id")
	public Page<Contact> getContactsByContactId(@Param("id") int contactId, Pageable pageable);
}
