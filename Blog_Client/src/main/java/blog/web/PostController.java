package blog.web;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import blog.Category;
import blog.Comment;
import blog.Post;
import blog.User;

@Controller
public class PostController {
	private RestTemplate rest = new RestTemplate();
	
	@GetMapping("/user/showCreatePostForm")
	public String showCreatePostForm(Model model) {
		List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
		model.addAttribute("listCate", listCate);
		Post post = new Post();
		model.addAttribute("post", post);
		return "createPost";
	}
	
	@Value("${upload.path}")
	private String uploadFolder;
	@PostMapping("/user/createPost")
	public String createPost(@ModelAttribute("post") @Valid Post post, BindingResult bindingResult,
			@RequestParam("cate_id") Long cate_id, @RequestParam("title") String title,
			@RequestParam("image") MultipartFile image, @RequestParam("content") String content,
			@RequestParam("createdAt") String createdAt, @RequestParam("email") String email,
			Principal principal, Model model) {
		boolean isAdmin = false;
		if(principal != null) {
			Collection<SimpleGrantedAuthority> listGrant = (Collection<SimpleGrantedAuthority>) ((Authentication) principal).getAuthorities();
			for(GrantedAuthority grant : listGrant) {
				if(grant.getAuthority().equals("ROLE_ADMIN"))
					isAdmin = true;
			}
		}

		if(bindingResult.getFieldErrorCount("title") > 0 || bindingResult.getFieldErrorCount("content") > 0) {
			List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
			model.addAttribute("listCate", listCate);
			if(!isAdmin)
				return "createPost";
			return "admins/editPost";
		}
		
		Category cate = rest.getForObject("http://localhost:8080/findCategoryById/{id}", Category.class, cate_id);
		User user;
		if(!isAdmin) {
			user = rest.getForObject("http://localhost:8080/findByEmail/{email}", User.class, principal.getName());
		} else {
			user = rest.getForObject("http://localhost:8080/findByEmail/{email}", User.class, email);
		}
		
		String imgName = image.getOriginalFilename();
		if(!imgName.equals("")) {
			String extension = imgName.substring(imgName.lastIndexOf(".") + 1);
			if(!extension.equals("jpg") && !extension.equals("png")) {
				model.addAttribute("invalid_imgext", "Image type must be jpg or png");
				List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
				model.addAttribute("listCate", listCate);
				if(!isAdmin)
					return "createPost";
				return "admins/editPost";
			}
			try {
				FileCopyUtils.copy(image.getBytes(), new File(uploadFolder + user.getId() + "_" + imgName));
			} catch (IOException e) {
				e.printStackTrace();
			}
			post.setImg(user.getId() + "_" + imgName);
		}
		if(post.getId() != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
			try {
				Date createdDate = sdf.parse(createdAt);
				post.setCreatedAt(createdDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		post.setCate(cate);
		post.setUser(user);
		rest.postForObject("http://localhost:8080/createPost", post, Post.class);
		
		if(!isAdmin)
			return "redirect:/";
		return "redirect:/admin/findPostByCategory?cate_id=" + cate.getId();
	}
	
	@GetMapping("/user/showEditPostForm")
	public String showEditPostForm(@RequestParam("id") Long id, Model model) {
		List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
		model.addAttribute("listCate", listCate);
		Post post = rest.getForObject("http://localhost:8080/findPostById/{id}", Post.class, id);
		model.addAttribute("post", post);
		return "createPost";
	}
	
	@GetMapping("/admin/showEditPostForm")
	public String adminShowEditPostForm(@RequestParam("id") Long id, Model model) {
		List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
		model.addAttribute("listCate", listCate);
		Post post = rest.getForObject("http://localhost:8080/findPostById/{id}", Post.class, id);
		model.addAttribute("post", post);
		return "admins/editPost";
	}
	
	@GetMapping("/user/deletePost")
	public String deletePost(@RequestParam("id") Long id) {
		rest.delete("http://localhost:8080/deletePostById/{id}", id);
		return "redirect:/";
	}
	
	@GetMapping("/admin/deletePost")
	public String adminDeletePost(@RequestParam("id") Long id, RedirectAttributes redirect) {
		Post post = rest.getForObject("http://localhost:8080/findPostById/{id}", Post.class, id);
		rest.delete("http://localhost:8080/deletePostById/{id}", id);
		redirect.addAttribute("cate_id", post.getCate().getId());
		return "redirect:/admin/findPostByCategory";
	}
	
	@GetMapping("/findPostByCategory")
	public String findPostByCategory(@RequestParam("cate_id") Long cate_id, Model model) {
		Category cate = rest.getForObject("http://localhost:8080//findCategoryById/{id}", Category.class, cate_id);
		model.addAttribute("listPost", cate.getListPost());
		List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
		model.addAttribute("listCate", listCate);
		model.addAttribute("listPostByCate", "List posts of category: " + cate.getName());
		return "index";
	}
	
	@GetMapping("/admin/findPostByCategory")
	public String adminFindPostByCategory(@RequestParam("cate_id") Long cate_id, Model model) {
		Category cate = rest.getForObject("http://localhost:8080//findCategoryById/{id}", Category.class, cate_id);
		model.addAttribute("listPost", cate.getListPost());
		model.addAttribute("listPostByCate", "List posts of category: " + cate.getName());
		return "admins/listPost";
	}
	
	@GetMapping("/findPostByTitle")
	public String findPostByName(@RequestParam("title") String title, Model model) {
		if(title == "") {
			title = "%";
		}
		List<Post> listPost = Arrays.asList(rest.getForObject("http://localhost:8080/findPostByTitle/{title}", Post[].class, title));
		model.addAttribute("listPost", listPost);
		List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
		model.addAttribute("listCate", listCate);
		return "index";
	}
	
	@GetMapping("/postDetail")
	public String getPostDetail(@RequestParam("id") Long id, Model model) {
		Post post = rest.getForObject("http://localhost:8080/findPostById/{id}", Post.class, id);
		model.addAttribute("post", post);
		List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
		model.addAttribute("listCate", listCate);
		List<Comment> listCmt = Arrays.asList(rest.getForObject("http://localhost:8080/findCmtByPostId/{id}", Comment[].class, id));
		model.addAttribute("listCmt", listCmt);
		return "blog-details";
	}
	
	@GetMapping("/findPostByUser")
	public String findPostByUser(@RequestParam("email") String email, Model model) {
		User user = rest.getForObject("http://localhost:8080/findByEmail/{email}", User.class, email);
		model.addAttribute("listPost", user.getListPost());
		List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
		model.addAttribute("listCate", listCate);
		model.addAttribute("listPostByUser", "List posts by: " + email);
		return "index";
	}
}
