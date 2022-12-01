package contact.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "contact_table")
public class Contact {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="contact_id")
	private int contactId;
	
	@Column(name="contact_first_name")
	@NotBlank(message = "first name should not be empty")
	@Size(min = 3, max = 250 , message = "first name should be between 3 or 250 character")
	private String firstName;
	
	@Column(name="contact_last_name")
	@NotBlank(message="last name is should not be empty")
	@Size(min = 3, max = 250, message="last name should be between 3 or 250 characters")
	private String lastName;
	
	@Column(name="contact_number", unique = true)
	@NotBlank(message = "contact no should not be empty")
	@Pattern( regexp = "^\\d{10}$", message = "contact number must be containe 10 digit")
	private String contactNo;
	
	@Column(name="contact_email", unique = true)
	@NotBlank(message = "email should not empty")
	@Email(
			regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
			message="Invalid email")
	private String email;
	
	
	
	@Column(name="contact_birth_date")
	@NotNull(message = "birth date should not empty")
	private String birthDate;
	
	@Column(name="contact_profile_name")
	private String profileName;

	@ManyToOne
	@JsonBackReference
	private User user;


	public int getContactId() {
		return contactId;
	}


	public void setContactId(int contactId) {
		this.contactId = contactId;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getContactNo() {
		return contactNo;
	}


	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}


	public String getProfileName() {
		return profileName;
	}


	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Contact [contactId=" + contactId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", contactNo=" + contactNo + ", email=" + email + ", birthDate=" + birthDate + ", profileName="
				+ profileName;
	}
	
	
}
