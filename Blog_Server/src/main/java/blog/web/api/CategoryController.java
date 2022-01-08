package blog.web.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import blog.Category;
import blog.data.CategoryRepo;

@RestController
@CrossOrigin(origins = "*")
public class CategoryController {
	@Autowired
	private CategoryRepo cateRepo;
	
	@GetMapping("/findAllCategory")
	public Iterable<Category> findAllCategory(){
		return cateRepo.findAll();
	}
	
	@GetMapping("/checkCateExistWhenCreate/{name}")
	public boolean checkCateExistWhenCreate(@PathVariable("name") String name) {
		return cateRepo.checkCateExistWhenCreate(name) != null;
	}
	
	@PostMapping("/checkCateExistWhenUpdate")
	public boolean checkCateExistWhenUpdate(@RequestBody Category cate) {
		return cateRepo.checkCateExistWhenUpdate(cate.getId(), cate.getName().trim()) != null;
	}
	
	@PostMapping("/addCategory")
	public Category addCategory(@RequestBody Category cate) {
		return cateRepo.save(cate);
	}
	
	@GetMapping("/findCategoryById/{id}")
	public Category findCategoryById(@PathVariable("id") Long id) {
		Optional<Category> opt = cateRepo.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}
		return null;
	}
	
	@DeleteMapping("/deleteCategory/{id}")
	public void deleteCategory(@PathVariable("id") Long id) {
		cateRepo.deleteById(id);
	}
}
