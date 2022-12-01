package contact.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import contact.helper.FileUpload;
import contact.helper.Helper;
import contact.helper.Message;
import contact.model.User;
import contact.repository.UserDao;
import contact.service.MessageService;
import net.bytebuddy.utility.RandomString;

@Controller
public class RegisterController {
	@Autowired
	FileUpload fileUpload;
	@Autowired
	Helper helper;
	@Autowired 
	UserDao userDao;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@Autowired
	MessageService messageService;
	
	// signup page controller
		@GetMapping("/signup")
		public String getRegisterPage(Model model) {
			model.addAttribute("user", new User());
			model.addAttribute("message", null);
			return "signup-view";
		}

		// signup processing controller after submitting signup form
		@PostMapping("/register")
		public String registerComponents(@Valid @ModelAttribute("user") User user, BindingResult result,
				@RequestParam("file") MultipartFile file, Model model, HttpServletRequest req) {
			String extension = "";
			try {
				if(result.hasErrors()) {
					throw new Exception("validation error");
				}
				if(!user.getPassword().matches(helper.passRegex)) {
					model.addAttribute("passwordError", "Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character");
					throw new Exception("password must be strong");
				}
				
				User alreadyExitContact = userDao.getUserByContactNo(user.getContactNo());
				if(alreadyExitContact != null) {
					model.addAttribute("contactError", "contact number is already exit please use different contact number!");
					throw new Exception("contact number is already exit");
				}
				
				User alreadyExitEmail = userDao.getUserByEmail(user.getEmail());
				if(alreadyExitEmail != null) {
					model.addAttribute("emailError", "email already exist choose different email");
					throw new Exception("email already exit");
				}
				String userProfile = file.getOriginalFilename();
				if (userProfile != "") {
					List<String> splitArr = helper.split(userProfile, '.');
					extension = splitArr.get(splitArr.size() - 1);
					if (!extension.matches(".jpeg") && !extension.matches(".jpg") && !extension.matches(".png")) {
						model.addAttribute("fileError",
								extension + " this file not allowed only .jpeg, .jpg, .png file are allowed");
						throw new Exception("invalid file");
					}
					if (file.getSize() > fileUpload.maxSize) {
						model.addAttribute("fileError", "file should be less then 10mb your file size "
								+ fileUpload.covertFileSize(file.getSize()));
						throw new Exception("file size error");
					}
				} else {
					model.addAttribute("fileError", extension + " file not selected please select your proifle");
					throw new Exception("file error");
				}
				
				user.setProfileName(user.getFirstName().toLowerCase() + "_" + user.getLastName() + "_" + user.getContactNo() + extension);
				user.setUserRole("ROLE_USER");
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setEnabled(false);
				String verifyCode = RandomString.make(64);
				user.setVerificationCode(verifyCode);
				
				fileUpload.uploadFile(file, "user_profile", user.getProfileName());
				userDao.save(user);
				
				
				//sending confirmation message
				String siteURL = req.getRequestURL().toString().replace(req.getServletPath(), "");
				siteURL += "/verification?code=" + user.getVerificationCode();
				String message = "Dear " + user.getFirstName() + " " + user.getLastName() + ",<br>"
			            + "Please click the link below to verify your registration:<br>"
			            + "<h3><a href='"+siteURL+"' target='_self' style='text-decoration:none; text-align:center;'>VERIFY</a></h3>"
			            + "Thank you,<br>"
			            + "Contact Manager.";
			     
				messageService.sendMessage("register confirmation", user.getEmail(), message);
				return "register-success-view";
			} catch (Exception e) {
				model.addAttribute("user", user);
				model.addAttribute("message", new Message("error-message", "error : " + e.getMessage()));
				return "signup-view";
			}
		}
		
		
		// email verification code processing
		@GetMapping("/verification")
		public String verificationComponents(@Param("code") String code, RedirectAttributes redirectAttributes) {
			User user = userDao.getUserByCode(code);
			if(user == null) {
				redirectAttributes.addFlashAttribute("message", new Message("error-message", "verification failed?"));
			}
			else {
				user.setVerificationCode("");
				user.setEnabled(true);
				userDao.save(user);
				redirectAttributes.addFlashAttribute("message", new Message("success-message", "your email is verified"));				
			}
			return "redirect:/signin";
			
		}
}
