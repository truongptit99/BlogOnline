package blog.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import blog.Category;

@Controller
public class CategoryController {
	private RestTemplate rest = new RestTemplate();

	@GetMapping("/admin/categories")
	public String showListCategory(Model model) {
		List<Category> listCate = Arrays.asList(rest.getForObject("http://localhost:8080/findAllCategory", Category[].class));
		model.addAttribute("listCate", listCate);
		return "admins/listCategory";
	}
	
	@GetMapping("/admin/showAddCategoryForm")
	public String showAddCategoryForm(Model model) {
		Category cate = new Category();
		model.addAttribute("cate", cate);
		return "admins/addCategory";
	}
	
	@PostMapping("/admin/addCategory")
	public String addCategory(@ModelAttribute("cate") @Valid Category cate,
			BindingResult bindingResult, @RequestParam("createdAt") String createdAt, Model model) {
		if(bindingResult.getFieldErrorCount("name") > 0) {
			return "admins/addCategory";
		} else {
			if (cate.getId() == null) {
				String name = cate.getName().trim();
				boolean checkCateExistWhenCreate = rest.getForObject("http://localhost:8080/checkCateExistWhenCreate/{name}", Boolean.class, name);
				if(checkCateExistWhenCreate) {
					model.addAttribute("cate_exist", "Category name already exists");
					return "admins/addCategory";
				}
			} else {
				boolean checkCateExistWhenUpdate = rest.postForObject("http://localhost:8080/checkCateExistWhenUpdate", cate, Boolean.class);
				if(checkCateExistWhenUpdate) {
					model.addAttribute("cate_exist", "Category name already exists");
					return "admins/addCategory";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
				try {
					Date createdDate = sdf.parse(createdAt);
					cate.setCreatedAt(createdDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			rest.postForObject("http://localhost:8080/addCategory", cate, Category.class);
			return "redirect:/admin/categories";
		}
	}
	
	@GetMapping("/admin/editCategory")
	public String showEditCategoryForm(@RequestParam("id") Long id, Model model) {
		Category cate = rest.getForObject("http://localhost:8080/findCategoryById/{id}", Category.class, id);
		model.addAttribute("cate", cate);
		return "admins/addCategory";
	}
	
	@GetMapping("/admin/deleteCategory")
	public String deleteCategory(@RequestParam("id") Long id) {
		rest.delete("http://localhost:8080/deleteCategory/{id}", id);
		return "redirect:/admin/categories";
	}
}
