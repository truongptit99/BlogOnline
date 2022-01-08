package blog;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Post {
	private Long id;
	
	@NotBlank(message = "Title is required")
	private String title;
	
	private String img;
	
	@NotBlank(message = "Content is required")
	private String content;
	
	private User user;

	private Category cate;
	
	private List<Comment> listCmt;
	
	private Date createdAt;
	
	private Date updatedAt;
}
