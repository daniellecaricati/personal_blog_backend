package com.daniellecaricati.personalblog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daniellecaricati.personalblog.model.Theme;

public interface ThemeRepository extends JpaRepository<Theme, Long>{
	
	public List<Theme> findAllByDescriptionContainingIgnoreCase(String description);
}
