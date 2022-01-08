package blog;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Category {
	private Long id;
	
	@NotBlank(message = "Name is required")
	private String name;

	private String des;
	
	private List<Post> listPost;
	
	private Date createdAt;
	
	private Date updatedAt;
}
