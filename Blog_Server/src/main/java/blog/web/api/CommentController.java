package blog.web.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import blog.Comment;
import blog.Post;
import blog.User;
import blog.data.CommentRepo;
import blog.data.PostRepo;
import blog.data.UserRepo;

@RestController
@CrossOrigin(origins = "*")
public class CommentController {
	@Autowired
	private CommentRepo cmtRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PostRepo postRepo;
	
	@GetMapping("/findCmtByPostId/{id}")
	public List<Comment> findCmtByPostId(@PathVariable("id") Long id){
		return cmtRepo.findCmtByPostId(id);
	}
	
	@PostMapping("/createComment")
	public Comment createComment(@RequestParam("content") String content, @RequestParam("post_id") Long post_id,
			@RequestParam("auth_email") String auth_email) {
		User user = userRepo.findByEmail(auth_email);
		Optional<Post> optPost = postRepo.findById(post_id);
		Comment cmt = new Comment();
		cmt.setContent(content);
		cmt.setUser(user);
		if(optPost.isPresent()) cmt.setPost(optPost.get());
		return cmtRepo.save(cmt);
	}
	
	@PostMapping("/updateComment")
	public Comment updateComment(@RequestParam("cmt_id") Long cmt_id, @RequestParam("cmt_content") String cmt_content, @RequestParam("post_id") Long post_id,
			@RequestParam("cmt_email") String cmt_email, @RequestParam("cmt_createdAt") String cmt_createdAt) {
		User user = userRepo.findByEmail(cmt_email);
		Optional<Post> optPost = postRepo.findById(post_id);
		Comment cmt = new Comment();
		cmt.setId(cmt_id);
		cmt.setContent(cmt_content);
		cmt.setUser(user);
		if(optPost.isPresent()) cmt.setPost(optPost.get());
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		try {
			Date createdDate = sdf.parse(cmt_createdAt);
			cmt.setCreatedAt(createdDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cmtRepo.save(cmt);
	}
	
	@DeleteMapping("/deleteCmtById")
	public void deleteCmtById(@RequestParam("id") Long id) {
		cmtRepo.deleteById(id);
	}
	
	@GetMapping("/findCmtById/{id}")
	public Comment findCmtById(@PathVariable("id") Long id) {
		Optional<Comment> optCmt = cmtRepo.findById(id);
		if(optCmt.isPresent()) {
			return optCmt.get();
		}
		return null;
	}
}
