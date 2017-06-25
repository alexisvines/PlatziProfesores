package com.alexisvines.profesoresplatzi.service;

import java.util.List;

import com.alexisvines.profesoresplatzi.model.SocialMedia;
import com.alexisvines.profesoresplatzi.model.TeacherSocialMedia;

/**
 * 
 * @author Alexisvines
 *
 */
public interface SocialMediaService {
	public void saveSocialMedia(SocialMedia socialmedia);

	public void deleteSocialMedia(Long idSocialMedia);

	public void updateSocialMedia(SocialMedia socialMedia);

	public SocialMedia findSocialMediaById(Long idSocialMedia);

	public List<SocialMedia> findAllSocialMedias();
	
	public SocialMedia findSocialMediaByName(String name);
	
	public TeacherSocialMedia findSocialMediaByIdAndName(Long idSocialMedia, String nickname);
	
	public TeacherSocialMedia findSocialMediabyIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia);


}
