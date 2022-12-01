package contact.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import contact.helper.FileUpload;
import contact.helper.Helper;
import contact.helper.Message;
import contact.model.User;
import contact.repository.ContactDao;
import contact.repository.UserDao;

@Controller
@RequestMapping("/user")
public class ProfileController {
	@Autowired
	FileUpload fileUpload;
	@Autowired
	ContactDao contactDao;
	@Autowired
	Helper helper;
	@Autowired
	UserDao userDao;
	
	// user profile page controller
		@GetMapping("/profile")
		public String getUserProfile(Model model, Principal principal) {
			String userName = principal.getName();
			User user = userDao.getUserByEmail(userName);
			model.addAttribute("user", user);
			model.addAttribute("totalContact", " " + user.getContact().size());
			return "/normal/profile-view";
		}
		
		@GetMapping("/profile-edit")
		public String getProfileEditPage(Principal pricipal, Model model) {
			User user = userDao.getUserByEmail(pricipal.getName());
			if (user == null) {
				return "redirect:logout";
			}
			model.addAttribute("user", user);
			return "/normal/profile-edit-view";
		}

		@PostMapping("/profile-editted")
		public String processProfileEditPage(@Valid @ModelAttribute("user") User user, BindingResult result,
				@RequestParam("file") MultipartFile file, Model model, RedirectAttributes redirectAttributes, Principal pricipal) {
			
			String extension = "";
			System.out.println("editted file " + file.getOriginalFilename());
			User oldUser = (User) userDao.getUserByEmail(pricipal.getName());
			if (oldUser == null) {
				redirectAttributes.addFlashAttribute("message", new Message("error-message", "you are not logged"));
				return "redirect:/logout";
			}	
			try {
				user.setUserId(oldUser.getUserId());
				System.out.println(result.getFieldErrorCount());
				if (result.hasErrors() && result.getErrorCount() > 2) {
					throw new Exception("validation error");
				}
				if (!oldUser.getEmail().matches(user.getEmail())) {
					User alreadyExit = userDao.getUserByEmail(user.getEmail());
					if (alreadyExit != null) {
						model.addAttribute("emailError", "email already exist choose different email");
						throw new Exception("email already exit");
					}
				}
				if (!oldUser.getContactNo().matches(user.getContactNo())) {
					User alreadyExit = userDao.getUserByEmail(user.getContactNo());
					if (alreadyExit != null) {
						model.addAttribute("contactError", "contact number is already exit");
						throw new Exception("contact number error.");
					}
				}
				String userProfile = file.getOriginalFilename();
				if (userProfile != "") {
					List<String> splitArr = helper.split(userProfile, '.');
					extension = splitArr.get(splitArr.size() - 1);
					if (!extension.matches(".jpeg") && !extension.matches(".jpg") && !extension.matches(".png")) {
						model.addAttribute("fileError",
								extension + " this file not allowed only .jpeg, .jpg, .png file are allowed");
						throw new Exception("file extension error");
					}
					if (file.getSize() > fileUpload.maxSize) {
						model.addAttribute("fileError", "file should be less then 10mb your file size "
								+ fileUpload.covertFileSize(file.getSize()));
						throw new Exception("file size error");
					}
					user.setProfileName(user.getFirstName().toLowerCase() + "_" + user.getLastName() + "_" + user.getContactNo() + extension);
					boolean deletedFile = fileUpload.deleteFile(oldUser.getProfileName(), "user_profile");
					if(deletedFile) {
						fileUpload.uploadFile(file, "user_profile", user.getProfileName());
					}
					
				} else {
					user.setProfileName(oldUser.getProfileName());
				}
				
				user.setUserRole("ROLE_USER");
				user.setPassword(oldUser.getPassword());
				userDao.save(user);
				redirectAttributes.addFlashAttribute("message", new Message("success-message", "user profile successfully updated!"));
				return "redirect:/user/profile";
			
			} catch(IOException e) {
				model.addAttribute("user", user);
				model.addAttribute("message", new Message("error-message", "file error : " + e.getMessage()));
				return "/user/profile-edit-view";
			} catch (Exception e) {
				model.addAttribute("user", user);
				model.addAttribute("message", new Message("error-message", "something went wrong : " + e.getMessage()));
				return "/normal/profile-edit-view";
			}
		}
}
