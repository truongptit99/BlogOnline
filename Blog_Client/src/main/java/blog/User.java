package blog;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class User {
	private Long id;
	
	@NotBlank(message = "Full name is required")
	private String fullname;
	
	@NotBlank(message = "Username is required")
	private String username;
	
	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	private String password;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Please enter a valid e-mail address")
	private String email;
	
	private String address;
	
	private int isActive;
	
	private Role role;
	
	private List<Post> listPost;
	
	private List<Comment> listCmt;
	
	private Date createdAt;
	
	private Date updatedAt;
}
