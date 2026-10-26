package com.daniellecaricati.personalblog.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity 
@Table(name = "tb_posts")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "The title attritbute is required.")
	@Size(min = 5, max = 100, message = "The title attribute must be between 5 and 100 characters.")
	@Column(length = 100)
	private String title;
	
	@NotBlank(message = "The text attribute is required.")
	@Size(min = 10, max = 1000, message = "The text attribute must be between 10 and 1000 characters.")
	@Column(length = 1000)
	private String text;
	
	@UpdateTimestamp
	private LocalDateTime date;
	
	@ManyToOne
	@JsonIgnoreProperties("post")
	private Theme theme;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	
	
	
	
}
