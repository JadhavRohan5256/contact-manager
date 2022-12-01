package contact.controller;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import contact.helper.FileUpload;
import contact.helper.Helper;
import contact.helper.Message;
import contact.model.Contact;
import contact.model.User;
import contact.repository.ContactDao;
import contact.repository.UserDao;

@Controller
@RequestMapping("/user")
public class ContactController {
	@Autowired
	FileUpload fileUpload;
	@Autowired
	ContactDao contactDao;
	@Autowired
	Helper helper;
	@Autowired
	UserDao userDao;
	
	// add contact page controller
		@RequestMapping("/")
		public String getAddContact(Model model) {
			model.addAttribute("contact", new Contact());
			model.addAttribute("message", null);
			return "/normal/add-contact-view";
		}

		// add contact page proeccessing controller
		@PostMapping("/process-contact")
		public String processContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
				@RequestParam("profileFile") MultipartFile file, Model model, Principal principal) {
			
			try {
				if (result.hasErrors()) {
					throw new ValidationException("validation errors");
				}
				if (contact.getBirthDate().isEmpty()) {
					model.addAttribute("birthDateError", "please select birth date");
					throw new Exception("birth date error");
				}
				if (file.getOriginalFilename().isEmpty()) {
					model.addAttribute("fileError", "please select profile picture");
					throw new FileNotFoundException("file error");
				}
				String fileName = file.getOriginalFilename();
				String extension = "";
				List<String> split = helper.split(fileName, '.');
				extension = split.get(split.size() - 1);
				if (!extension.matches(".jpeg") && !extension.matches(".png") && !extension.matches(".jpg")) {
					model.addAttribute("fileError",
							extension + " this file not allowed only .jpeg, .png, .jpg file allowed!");
					throw new Exception("file error");
				}
				if (file.getSize() > fileUpload.maxSize) {
					model.addAttribute("fileError",
							" maximum " + fileUpload.covertFileSize(fileUpload.maxSize) + " file size allowed");
					throw new Exception("file error");
				}
				User user = userDao.getUserByEmail(principal.getName());

				String modifiedFileName = contact.getFirstName() + "_" + contact.getLastName() + "_"
						+ contact.getContactNo() + extension;
				contact.setProfileName(modifiedFileName);
				contact.setUser(user);
				// uploading file
				fileUpload.uploadFile(file, "contact_profile", modifiedFileName);
				user.setContact(List.of(contact));
				contactDao.save(contact);
				model.addAttribute("message", new Message("success-message", "succesfully saved contact"));
				return "/normal/add-contact-view";
			} catch (DataIntegrityViolationException e) {
				model.addAttribute("message", new Message("error-message", "Contact already exit"));
				model.addAttribute("contact", contact);
				return "/normal/add-contact-view";
			} catch (Exception e) {
				System.out.println(e);
				model.addAttribute("message", new Message("error-message", "something went wrong : " + e.getMessage()));
				model.addAttribute("contact", contact);
				return "/normal/add-contact-view";
			}
		}

		
		// show contact page controller
		@GetMapping("/contacts/{pageId}")
		public String getContactList(@PathVariable("pageId") Integer page, Principal principal, Model model) {
			final int recordPerPage = 1;
			User user = userDao.getUserByEmail(principal.getName());
			Pageable pageable = PageRequest.of(page, recordPerPage);
			Page<Contact> list = contactDao.getContactsByContactId(user.getUserId(), pageable);
			model.addAttribute("contacts", list);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", list.getTotalPages());
			return "/normal/show-contacts";
		}

		// deleting contact after clicking delete btn
		@GetMapping("/delete-contact/{contactId}")
		public RedirectView deleteContact(@PathVariable("contactId") int contactId, RedirectAttributes redirectAttributes) {
			Contact contact = contactDao.getContactById(contactId);
			fileUpload.deleteFile(contact.getProfileName(), "contact_profile");
			contactDao.delete(contact);
			redirectAttributes.addFlashAttribute("message", new Message("success-message", contact.getContactNo() + " contact deleted successfully"));
			RedirectView redirectView = new RedirectView("/user/contacts/0");
			return redirectView;
		}

		// contact profile page controller
		@GetMapping("/contact/{contactId}")
		public String getContact(@PathVariable("contactId") int contactId, Principal principal, Model model) {
			User user = userDao.getUserByEmail(principal.getName());
			Contact contact = contactDao.getContactById(contactId);
			if (contact != null && user.getUserId() != contact.getUser().getUserId()) {
				model.addAttribute("message", new Message("error-message", "you can not access this contacts"));
				model.addAttribute("contact", null);
				return "/normal/contact-profile-view";
			}
			model.addAttribute("contact", contact);
			return "/normal/contact-profile-view";

		}

		// contact edit page controller
		@GetMapping("/contact-edit/{contactId}")
		public String getEditPage(@PathVariable("contactId") int contactId, Model model) {
			Contact contact = contactDao.getContactById(contactId);
			model.addAttribute("contact", contact);
			return "/normal/contact-edit-view";
		}

		// editted contact processing controller
		@PostMapping("/editted")
		public String editProcess(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
				@RequestParam("profileFile") MultipartFile file, Principal principal, Model model,RedirectAttributes redirectAttribute) {
			Contact oldContact = contactDao.getContactById(contact.getContactId());
			try {
				if (result.hasErrors()) {
					throw new ValidationException("validation errors");
				}
				if (contact.getBirthDate().isEmpty()) {
					redirectAttribute.addFlashAttribute("birthDateError", "please select birth date");
					throw new Exception("birth date error");
				}
				if (file.getOriginalFilename().isEmpty()) {
					contact.setProfileName(oldContact.getProfileName());
				} else {
					// deleting old file
					fileUpload.deleteFile(oldContact.getProfileName(), "contact_profile");
					String fileName = file.getOriginalFilename();
					String extension = "";
					List<String> split = helper.split(fileName, '.');
					extension = split.get(split.size() - 1);
					if (!extension.matches(".jpeg") && !extension.matches(".png") && !extension.matches(".jpg")) {
					redirectAttribute.addFlashAttribute("fileError", extension + " this file not allowed only .jpeg, .png, .jpg file allowed!");
					throw new Exception("file error");
					}
					if (file.getSize() > fileUpload.maxSize) {
						redirectAttribute.addFlashAttribute("fileError",
								" maximum " + fileUpload.covertFileSize(fileUpload.maxSize) + " file size allowed");
						throw new Exception("file error");
					}
					String modifiedFileName = contact.getFirstName() + "_" + contact.getLastName() + "_"
							+ contact.getContactNo() + extension;
					contact.setProfileName(modifiedFileName);
					fileUpload.uploadFile(file, "contact_profile", modifiedFileName);
				}
				User user = userDao.getUserByEmail(principal.getName());
				contact.setUser(user);
				// uploading file
				contactDao.save(contact);
				redirectAttribute.addFlashAttribute("message", new Message("success-message", "succesfully editted contact"));
				return "redirect:contact/"+contact.getContactId();
			} catch (DataIntegrityViolationException e) {
				model.addAttribute("message", new Message("error-message", "Contact already exit"));
				model.addAttribute("contact", contact);
				return "/normal/contact-edit-view";
			} catch (Exception e) {
				model.addAttribute("message", new Message("error-message", "something went wrong : " + e.getMessage()));
				model.addAttribute("contact", contact);
				return "/normal/contact-edit-view";
			}
		}

}
