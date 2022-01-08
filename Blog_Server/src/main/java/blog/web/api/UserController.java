package blog.web.api;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog.User;
import blog.data.UserRepo;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
	@Autowired
	private UserRepo userRepo;
	
	@GetMapping("/findByEmail/{email}")
	public User findByEmail(@PathVariable("email") String email) {
		return userRepo.findByEmail(email);
	}

	@GetMapping("/findByEmailIfIsActive/{email}")
	public User findByEmailIfIsActive(@PathVariable("email") String email) {
		return userRepo.findByEmailIfIsActive(email);
	}
	
	@GetMapping("/findAllUser")
	public Iterable<User> findAllUser(){
		return userRepo.findAll();
	}
	
	@GetMapping("/findUserById/{id}")
	public User findUserById(@PathVariable("id") Long id){
		Optional<User> optUser = userRepo.findById(id);
		if(optUser.isPresent()) {
			return optUser.get();
		}
		return null;
	}
	
	@GetMapping("/findAllUserIsNotAdmin")
	public List<User> findAllUserIsNotAdmin(){
		return userRepo.findAllUserIsNotAdmin();
	}

	@GetMapping("/checkEmailExistWhenRegister/{email}")
	public boolean checkEmailExist(@PathVariable("email") String email) {
		return userRepo.findByEmail(email) != null;
	}
	
	@PostMapping("/checkEmailExistWhenUpdate")
	public boolean checkEmailExistWhenUpdate(@RequestBody User user) {
		return userRepo.checkEmailExistWhenUpdate(user.getId(), user.getEmail()) != null;
	}

	@PostMapping("/register")
	public User addUser(@RequestBody User user) {
		return userRepo.save(user);
	}
	
	@Transactional
	@PostMapping("/activeOrDeactiveUser")
	public int updateActiveUser(@RequestParam("user_id") Long user_id, @RequestParam("is_active") int is_active) {
		return userRepo.activeOrDeactiveUser(is_active, new Date(), user_id);
	}
	
	@DeleteMapping("/deleteUser/{id}")
	public void deleteUser(@PathVariable("id") Long id) {
		userRepo.deleteById(id);
	}
}
