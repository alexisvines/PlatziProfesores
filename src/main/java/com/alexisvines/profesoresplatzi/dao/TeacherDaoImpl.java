package com.alexisvines.profesoresplatzi.dao;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.alexisvines.profesoresplatzi.model.Teacher;
import com.alexisvines.profesoresplatzi.model.TeacherSocialMedia;

/**
 * 
 * @author Alexisvines
 *
 *         Se extiende de platzisession para poder reutilizar la session que
 *         inicia las transacciones y se implementa el dao correspondiente
 *
 */
@Repository
@Transactional
public class TeacherDaoImpl extends AbstractSession implements TeacherDao {

	public void saveTeacher(Teacher teacher) {
		getSession().persist(teacher);
	}

	public void deleteTeacher(Long idTeacher) {
		Teacher teacher = findTeacherById(idTeacher);

		if (teacher != null) {

			Iterator<TeacherSocialMedia> i = teacher.getTeacherSocialMedias().iterator();
			while (i.hasNext()) {
				TeacherSocialMedia teacherSocialMedia = i.next();
				i.remove();
				getSession().delete(teacherSocialMedia);
			}
			teacher.getTeacherSocialMedias().clear();

			getSession().delete(teacher);
		}

	}

	public void updateTeacher(Teacher teacher) {
		getSession().update(teacher);

	}

	public Teacher findTeacherById(Long idTeacher) {
		return (Teacher) getSession().get(Teacher.class, idTeacher);
	}

	public Teacher findTeacherByName(String name) {
		return (Teacher) getSession().createQuery("from Teacher where name = :name").setParameter("name", name)
				.uniqueResult();
	}

	/**
	 * al usar createQuery no se pone la tabla sino que se pone la entidad de
	 * objeto
	 */
	public List<Teacher> findAllTeacher() {

		return getSession().createQuery("from Teacher").list();
	}

}
