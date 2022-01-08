package blog.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import blog.User;

public interface UserRepo extends CrudRepository<User, Long>{
	User findByEmail(String email);
	
	@Query(value = "select * from user where id != ?1 and email = ?2", nativeQuery = true)
	User checkEmailExistWhenUpdate(Long id, String email);

	@Query(value = "select * from user where email = ?1 and is_active = 1", nativeQuery = true)
	User findByEmailIfIsActive(String email);
	
	@Query(value = "select * from user where role_id != (select id from role where name = 'ROLE_ADMIN')", nativeQuery = true)
	List<User> findAllUserIsNotAdmin();
	
	@Modifying
	@Query(value = "update user set is_active = ?1, updated_at = ?2 where id = ?3", nativeQuery = true)
	int activeOrDeactiveUser(int is_active, Date date, Long id);
}
