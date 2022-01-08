package blog.web.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import blog.Post;
import blog.data.PostRepo;

@RestController
@CrossOrigin(origins = "*")
public class PostController {
	@Autowired
	private PostRepo postRepo;

	@PostMapping("/createPost")
	public Post createPost(@RequestBody Post post) {
		return postRepo.save(post);
	}
	
	@GetMapping("/findAllPost")
	public Iterable<Post> findAll(){
		return postRepo.findAll();
	}
	
	@GetMapping("/findAllPostOrderByUpdatedAt")
	public List<Post> findAllPostOrderByUpdatedAt(){
		return postRepo.findAllPost();
	}
	
	@GetMapping("/findPostById/{id}")
	public Post findPostById(@PathVariable("id") Long id) {
		Optional<Post> optPost = postRepo.findById(id);
		if(optPost.isPresent()) {
			return optPost.get();
		}
		return null;
	}
	
	@DeleteMapping("/deletePostById/{id}")
	public void deletePostById(@PathVariable("id") Long id) {
		postRepo.deleteById(id);
	}
	
//	@GetMapping("/findPostByCategory/{cate_id}")
//	public List<Post> findPostByCategory(@PathVariable("cate_id") Long cate_id){
//		return postRepo.findPostByCategory(cate_id);
//	}
	
	@GetMapping("/findPostByTitle/{title}")
	public List<Post> findPostByTitle(@PathVariable("title") String title){
		return postRepo.findByTitle(title);
	}
	
//	@GetMapping("/findPostByUser/{id}")
//	public List<Post> findPostByUser(@PathVariable("id") Long id){
//		return postRepo.findPostByUser(id);
//	}
}
