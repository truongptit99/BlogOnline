package blog.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import blog.Post;

public interface PostRepo extends CrudRepository<Post, Long>{
	@Query(value = "select * from post order by updated_at desc", nativeQuery = true)
	List<Post> findAllPost();
	
//	@Query(value = "select * from post where cate_id = ?1 order by updated_at desc", nativeQuery = true)
//	List<Post> findPostByCategory(Long cate_id);
	
	@Query(value = "select * from post where title like %?1% order by updated_at desc", nativeQuery = true)
	List<Post> findByTitle(String title);	
	
//	@Query(value = "select * from post where user_id = ?1 order by updated_at desc", nativeQuery = true)
//	List<Post> findPostByUser(Long id);
}
