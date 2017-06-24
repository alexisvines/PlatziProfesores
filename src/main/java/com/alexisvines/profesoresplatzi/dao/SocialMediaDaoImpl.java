package com.alexisvines.profesoresplatzi.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.alexisvines.profesoresplatzi.model.SocialMedia;
import com.alexisvines.profesoresplatzi.model.TeacherSocialMedia;

/**
 * 
 * @author Alexisvines
 *
 */
@Repository
@Transactional
public class SocialMediaDaoImpl extends AbstractSession implements SocialMediaDao {

	@Override
	public void saveSocialMedia(SocialMedia socialmedia) {
		getSession().persist(socialmedia);

	}

	@Override
	public void deleteSocialMedia(Long idSocialMedia) {
		SocialMedia socialMedia = findSocialMediaById(idSocialMedia);
		if (socialMedia != null) {
			getSession().delete(socialMedia);
		}

	}

	@Override
	public void updateSocialMedia(SocialMedia socialMedia) {
		getSession().update(socialMedia);

	}

	@Override
	public SocialMedia findSocialMediaById(Long idSocialMedia) {
		return getSession().get(SocialMedia.class, idSocialMedia);
	}

	@Override
	public List<SocialMedia> findAllSocialMedias() {
		return getSession().createQuery("from SocialMedia").list();
	}

	@Override
	public SocialMedia findSocialMediaByName(String name) {
		return (SocialMedia) getSession().createQuery("from SocialMedia where name = :name").setParameter("name", name)
				.uniqueResult();
	}

	@Override
	public TeacherSocialMedia findSocialMediaByIdAndName(Long idSocialMedia, String nickname) {
		List<Object[]> objects = getSession()
				.createQuery(
						"from TeacherSocialMedia tsm join tsm.socialMedia sm where sm.idSocialMedia = :idSocialMedia and tsm.nickname = :nickname")
				.setParameter("idSocialMedia", idSocialMedia).setParameter("nickname", nickname).list();

		for (Object[] objects2 : objects) {
			for (Object object : objects2) {
				if (object instanceof TeacherSocialMedia) {
					return (TeacherSocialMedia) object;
				}
			}
		}
		return null;
	}

}
