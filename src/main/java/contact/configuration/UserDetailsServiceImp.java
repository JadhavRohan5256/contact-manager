package contact.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import contact.model.User;
import contact.repository.UserDao;
public class UserDetailsServiceImp implements UserDetailsService {
	@Autowired
	UserDao userDao;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.getUserByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Bad Credentials");
		}
		UserDetailsImp userDetailsImp = new UserDetailsImp(user);
		return userDetailsImp;
	}

}
