package contact.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import contact.helper.Helper;
import contact.helper.Message;
import contact.model.User;
import contact.repository.UserDao;
import contact.service.MessageService;

@Controller
public class ForgatePasswordController {
	@Autowired 
	UserDao userDao;
	@Autowired
	MessageService messageService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	Helper helper;

	
	// forgate page controller
	@GetMapping("/forgate")
	public String getForgatePage() {
		return "forgate-view";
	}

	// after submitting forgate form processing controller
	@PostMapping("/verify")
	public String getVerificationPage(@RequestParam("username") String username, Model model, HttpServletRequest req) {
		if (username.isEmpty() || !username.matches(helper.emailRegex)) {
			model.addAttribute("message", new Message("error-message", "invalid email"));
			return "forgate-view";
		}
		User user = userDao.getUserByEmail(username);
		if (user == null) {
			model.addAttribute("message", new Message("error-message", "you are not register!"));
			return "forgate-view";
		}
		int min = 111111;
		int max = 999999;
		HttpSession session = req.getSession();

		int otp = (int) (Math.random() * (max - min + 1) + min);
		String subject = "Verification";
		String message = "" + "<hr>" + "<p> Hi " + user.getFirstName() + " " + user.getLastName() + ",</p>"
				+ "<p> As you have requested for reset password instructuions, here they are, please enter otp </p>"
				+ "<h1 style='color:#006400; font-weight: 500; text-align: center; border: 2px dotted #006400; padding: 0.5rem;'>"
				+ otp + "</h1>";
		
		boolean result = messageService.sendMessage(subject, username, message);
		if (!result) {
			model.addAttribute("message", new Message("error-message", "something went wrong : otp is not sended"));
			return "forgate-view";
		}

		User sessionUser = (User) session.getAttribute("user");
		if (sessionUser != null && sessionUser != user) {
			session.removeAttribute("user");
		} else {
			session.setAttribute("user", user);
		}
		session.setAttribute("otp", otp);
		model.addAttribute("message", new Message("success-message", "Otp has been sended check you mail"));

		return "verification-view";
	}

	
	// otp porcessing controller
	@PostMapping("/verify-otp")
	public String checkOtp(@RequestParam("otp") int otp, Model model, HttpServletRequest req) {
		HttpSession session = req.getSession();
		int sessionOtp = (Integer) session.getAttribute("otp");
		if (otp != sessionOtp) {
			model.addAttribute("message", new Message("error-message", "otp does not match"));
			return "verification-view";
		}

		session.setAttribute("changePassword", true);
		session.removeAttribute("otp");
		model.addAttribute("message", new Message("success-message", "otp are matched"));
		return "new-password-view";
	}

	
	// processing controller
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newPassword") String newPassword,
			@RequestParam("repeatPassword") String repeatPassword, Model model, HttpServletRequest req) {

		if (!newPassword.matches(helper.passRegex)) {
			model.addAttribute("message", new Message("error-message",
					"Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character"));
			return "new-password-view";
		}

		if (!newPassword.matches(repeatPassword)) {
			System.out.println(newPassword + " " + repeatPassword);
			model.addAttribute("message", new Message("error-message", "your password does not match."));
			return "new-password-view";
		}
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		User result = null;
		if (user != null) {
			user.setPassword(passwordEncoder.encode(newPassword));
			result = userDao.save(user);
		}
		if (result == null) {
			model.addAttribute("message",
					new Message("error-message", "something went wrong your password not changed!"));
			return "new-password-view";
		}
		model.addAttribute("message",
				new Message("success-message", "password successfully changed! now you can login"));
		return "login-view";
	}
	
	
}
