package com.alexisvines.profesoresplatzi.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Alexisvines
 *
 */
@Entity
@Table(name = "social_media")
public class SocialMedia implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6270251774595773745L;

	@Id
	@Column(name = "id_social_media")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idSocialMedia;

	@Column(name = "name")
	private String name;
	@Column(name = "icon")
	private String icon;

	@OneToMany
	@JoinColumn(name = "id_social_media")
	@JsonIgnore // anotacion para que al consultar por social medias no me
				// traiga las relaciones
	private Set<TeacherSocialMedia> teacherSocialMedias;

	public SocialMedia() {
		super();
	}

	public SocialMedia(String name, String icon) {
		super();
		this.name = name;
		this.icon = icon;
	}

	public Long getIdSocialMedia() {
		return idSocialMedia;
	}

	public void setIdSocialMedia(Long idSocialMedia) {
		this.idSocialMedia = idSocialMedia;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Set<TeacherSocialMedia> getTeacherSocialMedias() {
		return teacherSocialMedias;
	}

	public void setTeacherSocialMedias(Set<TeacherSocialMedia> teacherSocialMedias) {
		this.teacherSocialMedias = teacherSocialMedias;
	}

}
