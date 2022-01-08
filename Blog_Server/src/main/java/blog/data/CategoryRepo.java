package blog.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import blog.Category;

public interface CategoryRepo extends CrudRepository<Category, Long>{
	@Query(value = "select * from category where name = ?1", nativeQuery = true)
	Category checkCateExistWhenCreate(String name);
	
	@Query(value = "select * from category where id != ?1 and name = ?2", nativeQuery = true)
	Category checkCateExistWhenUpdate(Long id, String name);
}
