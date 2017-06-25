package com.alexisvines.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexisvines.profesoresplatzi.dao.SocialMediaDao;
import com.alexisvines.profesoresplatzi.model.SocialMedia;
import com.alexisvines.profesoresplatzi.model.TeacherSocialMedia;

/**
 * 
 * @author Alexisvines
 *
 */
@Service("socialMediaService")
@Transactional
public class SocialMediaServiceImpl implements SocialMediaService {

	@Autowired
	private SocialMediaDao _socialMediaDao;

	@Override
	public void saveSocialMedia(SocialMedia socialmedia) {
		_socialMediaDao.saveSocialMedia(socialmedia);

	}

	@Override
	public void deleteSocialMedia(Long idSocialMedia) {
		_socialMediaDao.deleteSocialMedia(idSocialMedia);

	}

	@Override
	public void updateSocialMedia(SocialMedia socialMedia) {
		_socialMediaDao.updateSocialMedia(socialMedia);

	}

	@Override
	public SocialMedia findSocialMediaById(Long idSocialMedia) {
		return _socialMediaDao.findSocialMediaById(idSocialMedia);
	}

	@Override
	public List<SocialMedia> findAllSocialMedias() {
		return _socialMediaDao.findAllSocialMedias();
	}

	@Override
	public SocialMedia findSocialMediaByName(String name) {
		return _socialMediaDao.findSocialMediaByName(name);
	}

	@Override
	public TeacherSocialMedia findSocialMediaByIdAndName(Long idSocialMedia, String nickname) {
		return _socialMediaDao.findSocialMediaByIdAndName(idSocialMedia, nickname);
	}

	@Override
	public TeacherSocialMedia findSocialMediabyIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia) {
		return _socialMediaDao.findSocialMediabyIdTeacherAndIdSocialMedia(idTeacher,idSocialMedia);
	}

}
