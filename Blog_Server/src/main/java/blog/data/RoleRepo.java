package blog.data;

import org.springframework.data.repository.CrudRepository;

import blog.Role;

public interface RoleRepo extends CrudRepository<Role, Long>{
	Role findByName(String name);
}
