package blog.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import blog.Role;
import blog.User;

@Controller
public class UserController {
	private RestTemplate rest = new RestTemplate();
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "register";
	}
	
	@PostMapping("/processRegister")
	public String processRegister(@ModelAttribute("user") @Valid User user,
			BindingResult bindingResult,
			Model model, @RequestParam("createdAt") String createdAt) {
		if(bindingResult.getFieldErrorCount("fullname") > 0 || bindingResult.getFieldErrorCount("username") > 0
				|| bindingResult.getFieldErrorCount("password") > 0 || bindingResult.getFieldErrorCount("email") > 0) {
			if(user.getId() == null)
				return "register";
			return "admins/editUser";
		} else {
			if(user.getId() == null) {
				boolean checkEmailExistWhenRegister = rest.getForObject("http://localhost:8080/checkEmailExistWhenRegister/{email}", Boolean.class, user.getEmail());
				if(checkEmailExistWhenRegister) {
					model.addAttribute("email_exist", "Email already exists");
						return "register";
				}
				Role role = rest.getForObject("http://localhost:8080/role/findByName/{role_name}", Role.class, "ROLE_USER");
				user.setRole(role);
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setIsActive(1);
			} else {
				boolean checkEmailExistWhenUpdate = rest.postForObject("http://localhost:8080/checkEmailExistWhenUpdate", user, Boolean.class);
				if(checkEmailExistWhenUpdate) {
					model.addAttribute("email_exist", "Email already exists");
						return "admins/editUser";
				}
				Role role = rest.getForObject("http://localhost:8080/role/findByName/{role_name}", Role.class, "ROLE_USER");
				user.setRole(role);
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
				try {
					Date createdDate = sdf.parse(createdAt);
					user.setCreatedAt(createdDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}	
		}
		
		user = rest.postForObject("http://localhost:8080/register", user, User.class);
		if(user.getId() == null) {
			return "redirect:/login";
		} else return "redirect:/admin/users";
	}
	
	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}
	
	@GetMapping("/admin/users")
	public String showListUser(Model model) {
		List<User> listUser = Arrays.asList(rest.getForObject("http://localhost:8080/findAllUserIsNotAdmin", User[].class));
		model.addAttribute("listUser", listUser);
		return "admins/listUser";
	}
	
	@GetMapping("/admin/editUser")
	public String editUser(@RequestParam("id") Long id, Model model) {
		User user = rest.getForObject("http://localhost:8080/findUserById/{id}", User.class, id);
		model.addAttribute("user", user);
		return "admins/editUser";
	}
	
	@GetMapping("/admin/deleteUser")
	public String deleteUser(@RequestParam("id") Long id) {
		rest.delete("http://localhost:8080/deleteUser/{id}", id);
		return "redirect:/admin/users";
	}
}
