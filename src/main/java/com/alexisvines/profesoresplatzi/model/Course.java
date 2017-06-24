package com.alexisvines.profesoresplatzi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Alexisvines
 * @entity sirve para poder determinar que es una entidad DAO
 * @ Table es el nombre de la tabla con el cual se mapeara el objeto
 * @Id indica que ese campo sera el identificador de la tabla
 * @column: indica que campo se mapeara con que campo de la BD
 * @GeneratedValue : identity, identifica que sera unico el campos
 */
@Entity
@Table(name="course")
public class Course implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1695543883995166464L;
	
	
	@Id
	@Column(name="id_course")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id_course;
	@Column(name="name")
	private String name;
	@Column(name="themes")
	private String themes;
	@Column(name="project")
	private String project;
	
	//Eager dice ejecuta una query que me traiga los datos del teacher
	@ManyToOne(optional=true, fetch= FetchType.EAGER)
	@JoinColumn(name="id_teacher")
	@JsonIgnore
	private Teacher teacher;

	public Course() {
		super();
	}

	public Course(String name, String themes, String project) {
		super();
		this.name = name;
		this.themes = themes;
		this.project = project;
	}

	public Long getId_course() {
		return id_course;
	}

	public void setId_course(Long id_course) {
		this.id_course = id_course;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThemes() {
		return themes;
	}

	public void setThemes(String themes) {
		this.themes = themes;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

}
