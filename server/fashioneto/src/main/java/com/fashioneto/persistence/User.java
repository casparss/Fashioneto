/**
 * 
 */
package com.fashioneto.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.FilterDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Felipe
 */
@Entity
@Table(name = "fashionetoer")
@FilterDef(name = User.PARENT_TYPE_FILTER)
public class User implements Serializable, UserDetails
{
	public static final String PARENT_TYPE_FILTER = "userParentTypeFilter";

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;

	@Column(name = "username")
	private String username;

	@Column(name = "email")
	private String email;

	@OneToMany(mappedBy = "user", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	//	@OrderBy("date desc")
	private Set<Comment> postedComments = new LinkedHashSet<Comment>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinTable(name = "comment_parent", joinColumns = @JoinColumn(name = "id_parent_user"), inverseJoinColumns = @JoinColumn(name = "id_comment"))
	//	@FilterJoinTable(name = User.PARENT_TYPE_FILTER, condition = "comment_parent.parent_type == USER")
	//	@OrderBy("date desc")
	private Set<Comment> receivedComments = new LinkedHashSet<Comment>();

	@Column(length = 64, nullable = false)
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> roles = new HashSet<String>();

	public User()
	{
		//No args constructor
	}

	public User(int id)
	{
		this.id = id;
	}

	public User(int id, String username, String email)
	{
		this.id = id;
		this.username = username;
		this.email = email;
	}

	public CommentSet getReceivedCommentsCommentSet()
	{
		return new CommentSet(receivedComments);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	@Override
	public String toString()
	{
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", postedComments=" + postedComments
				+ ", receivedComments=" + receivedComments + "]";
	}

	public Set<Comment> getPostedComments()
	{
		return postedComments;
	}

	public void setPostedComments(Set<Comment> postedComments)
	{
		this.postedComments = postedComments;
	}

	public Set<Comment> getReceivedComments()
	{
		return receivedComments;
	}

	public void setReceivedComments(Set<Comment> receivedComments)
	{
		this.receivedComments = receivedComments;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		Set<String> roles = this.getRoles();

		if (roles == null)
		{
			return Collections.emptyList();
		}

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for (String role : roles)
		{
			authorities.add(new SimpleGrantedAuthority(role));
		}

		return authorities;
	}

	@Override
	public String getPassword()
	{
		return this.password;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public boolean isEnabled()
	{
		return true;
	}

	public Set<String> getRoles()
	{
		return roles;
	}

	public void setRoles(Set<String> roles)
	{
		this.roles = roles;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

}
