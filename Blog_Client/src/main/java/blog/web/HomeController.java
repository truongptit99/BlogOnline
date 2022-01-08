package blog.web;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import blog.Category;
import blog.Post;
import blog.User;

@Controller
public class HomeController {
	private RestTemplate rest = new RestTemplate();

	@GetMapping("/")
	public String home(Principal principal, Model model) {
		if(principal != null) {
			Collection<SimpleGrantedAuthority> listGrant = (Collection<SimpleGrantedAuthority>) ((Authentication) principal).getAuthorities();
			for(GrantedAuthority grant : listGrant) {
				if(grant.getAuthority().equals("ROLE_ADMIN"))
					return "redirect:/admin";
			}
		}
		List<Post> listPost = Arrays.asList(rest.getForObject("http://localhost:8080/findAllPostOrderByUpdatedAt", Post[].class));
		model.addAttribute("listPost", listPost);
		List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
		model.addAttribute("listCate", listCate);
		return "index";
	}
	
	@GetMapping("/403")
	public String accessDenied(Principal principal) {
		if(principal != null)
			return "error403";
		return "redirect:/";
	}
	
	@GetMapping("/admin")
	public String showAdminPage() {
		return "admins/admin";
	}
	
	@GetMapping("/admin/statistic")
	public String statistic(Model model) {
		List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
		Collections.sort(listCate, new Comparator<Category>() {
			@Override
			public int compare(Category c1, Category c2) {
				return c2.getListPost().size() > c1.getListPost().size() ? 1 : -1;
			}
		});
		model.addAttribute("listCateSortByPost", listCate);
		
		List<User> listUserSortByPost = Arrays.asList(rest.getForObject("http://localhost:8080/findAllUserIsNotAdmin", User[].class));
		Collections.sort(listUserSortByPost, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				return u2.getListPost().size() > u1.getListPost().size() ? 1 : -1;
			}
		});
		model.addAttribute("listUserSortByPost", listUserSortByPost);
		
		List<User> listUserSortByCmt = Arrays.asList(rest.getForObject("http://localhost:8080/findAllUserIsNotAdmin", User[].class));
		Collections.sort(listUserSortByCmt, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				return u2.getListCmt().size() > u1.getListCmt().size() ? 1 : -1;
			}
		});
		model.addAttribute("listUserSortByCmt", listUserSortByCmt);
		
		List<Post> listPost = Arrays.asList(rest.getForObject("http://localhost:8080/findAllPost", Post[].class));
		Collections.sort(listPost, new Comparator<Post>() {
			@Override
			public int compare(Post p1, Post p2) {
				return p2.getListCmt().size() > p1.getListCmt().size() ? 1 : -1;
			}
		});
		model.addAttribute("listPostSortByCmt", listPost);
		
		return "admins/statistic";
	}
}
