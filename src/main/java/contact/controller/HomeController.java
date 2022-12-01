package contact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import contact.helper.FileUpload;
import contact.helper.Helper;
import contact.repository.UserDao;
import contact.service.MessageService;

@Controller
public class HomeController {
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

	// home page controller
	@GetMapping("/")
	public String getHomePage() {
		return "home-view";
	}
	
	
	// about use page controller
	@GetMapping("/about-us")
	public String getAboutPage() {
		return "about-view";
	}
	
	// login page controller 
	@GetMapping("/signin")
	public String getLoginPage() {
		return "login-view";
	}

}
