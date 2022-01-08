package blog;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	private String img;
	
	@Column(columnDefinition = "MEDIUMTEXT")
	private String content;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties(value = {"listPost", "listCmt"}, allowSetters = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;
	
	@ManyToOne(targetEntity = Category.class)
	@JoinColumn(name = "cate_id")
	@JsonIgnoreProperties(value = "listPost", allowSetters = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Category cate;
	
	@OneToMany(mappedBy = "post")
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
