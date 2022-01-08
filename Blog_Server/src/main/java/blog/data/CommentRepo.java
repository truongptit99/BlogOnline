package blog.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import blog.Comment;

public interface CommentRepo extends CrudRepository<Comment, Long>{
	@Query(value = "select * from comment where post_id = ?", nativeQuery = true)
	List<Comment> findCmtByPostId(Long id);
}
