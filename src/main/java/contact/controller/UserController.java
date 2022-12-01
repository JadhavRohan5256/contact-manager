package contact.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import contact.helper.Helper;
import contact.helper.Message;
import contact.model.User;
import contact.repository.UserDao;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired 
	UserDao userDao;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	Helper helper;
	
	// setting page controller
		@GetMapping("/setting")
		public String getSettingPage() {
			return "/normal/setting-view";
		}
		
		// setting page process controller
		@PostMapping("/update-password") 
		public String updatePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Model model, RedirectAttributes redirectAttributes, Principal principal) {
			User user = userDao.getUserByEmail(principal.getName());
			if(!passwordEncoder.matches(oldPassword, user.getPassword())) {
				model.addAttribute("message", new Message("error-message", "password does not match"));
				return "/normal/setting-view";
			}
			if(!newPassword.matches(helper.passRegex)) {
				model.addAttribute("message", new Message("error-message", "Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character"));
				return "/normal/setting-view";			
			}
			
			user.setPassword(passwordEncoder.encode(newPassword));
			userDao.save(user);
			redirectAttributes.addFlashAttribute("message", new Message("success-message", "password successfully changed! login using new password"));
			return "redirect:/signin";
		}
}
