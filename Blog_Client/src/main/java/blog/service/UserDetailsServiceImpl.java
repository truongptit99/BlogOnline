package blog.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import blog.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	private RestTemplate rest = new RestTemplate();
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = rest.getForObject("http://localhost:8080/findByEmailIfIsActive/{email}", User.class, email);
		if(user == null) {
			throw new UsernameNotFoundException("User " + email + " was not found in database");
		}
		
		String roleName = user.getRole().getName();
		
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if(roleName != null) {
			GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
			grantList.add(authority);
		}
		
		UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(email, user.getPassword(), grantList);
		return userDetails;
	}

}
