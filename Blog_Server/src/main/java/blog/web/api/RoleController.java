package blog.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import blog.Role;
import blog.data.RoleRepo;

@RestController
@CrossOrigin(origins = "*")
public class RoleController {
	@Autowired
	private RoleRepo roleRepo;
	
	@GetMapping("/role/findByName/{role_name}")
	public Role findByName(@PathVariable("role_name") String roleName) {
		return roleRepo.findByName(roleName);
	}
}
