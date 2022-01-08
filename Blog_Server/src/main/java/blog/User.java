package blog;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fullname;
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String address;
	
	private int isActive;
	
	@ManyToOne(targetEntity = Role.class)
	private Role role;
	
	@OneToMany(mappedBy = "user")
	private List<Post> listPost;
	
	@OneToMany(mappedBy = "user")
	private List<Comment> listCmt;

	private Date createdAt;
	
	private Date updatedAt;
	
	@PrePersist
	void createdAt() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}
	
	@PreUpdate
	void updatedAt() {
		this.updatedAt = new Date();
	}
}
