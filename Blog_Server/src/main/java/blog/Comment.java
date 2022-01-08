package blog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String content;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties(value = {"listPost", "listCmt"}, allowSetters = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
	@ManyToOne(targetEntity = Post.class)
	@JoinColumn(name = "post_id")
	@JsonIgnoreProperties(value = "listCmt", allowSetters = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Post post;
	
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
