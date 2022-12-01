package contact.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="user_table")
public class User {
	@Id
	@Column(name="user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId = 0;
	
	@Column(name="user_first_name")
	@NotBlank(message = "first name should not be empty")
	@Size(min = 3 , max = 250, message = "first name should be between 3 and 250 characters")
	private String firstName;
	
	
	
	@Column(name="user_last_name")
	@NotBlank(message = "last name should not be empty")
	@Size(min = 3 , max = 250, message = "last name should be between 3 and 250 characters")
	private String lastName;
	
	@Column(name="user_contact_no", unique = true)
	@NotBlank(message = "contact number should not be empty")
	@Pattern( regexp = "^\\d{10}$", message = "contact number should be containe 10 digit number")
	private String contactNo;
	
	@Column(name="user_email", unique = true)
	@Email(
			regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
			message="Invalid email")
	private String email;
	
	@Column(name="user_password")
	@NotBlank(message = "password should not empty")
	private String password;
	
	@Column(name="user_profile_name")
	private String profileName;

	@Column(name="user_role")
	private String userRole;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Contact> contact;

	private boolean enabled = false;
	
	@Column(name="verification_code", length = 256)
	private String verificationCode;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public List<Contact> getContact() {
		return contact;
	}

	public void setContact(List<Contact> contact) {
		this.contact = contact;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + ", contactNo="
				+ contactNo + ", email=" + email + ", password=" + password + ", profileName=" + profileName
				+ ", userRole=" + userRole + ", contact=" + contact + ", enabled=" + enabled + ", verificationCode="
				+ verificationCode + "]";
	}

	
	
}
