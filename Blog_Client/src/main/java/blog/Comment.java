package blog;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comment {
	private Long id;
	
	private String content;
	
	private User user;

	private Post post;
	
	private Date createdAt;
	
	private Date updatedAt;
}
