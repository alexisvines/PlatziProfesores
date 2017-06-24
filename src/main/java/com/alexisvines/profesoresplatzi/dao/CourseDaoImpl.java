package com.alexisvines.profesoresplatzi.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.alexisvines.profesoresplatzi.model.Course;

/**
 * 
 * @author Alexisvines
 *
 */
@Repository
@Transactional
public class CourseDaoImpl extends AbstractSession implements CourseDao {

	@Override
	public void saveCourse(Course course) {
		getSession().persist(course);

	}

	@Override
	public void deleteCourse(Long idCourse) {
		Course course = findCourseById(idCourse);
		if (course != null) {
			getSession().delete(course);
		}
	}

	@Override
	public void updateCourse(Course course) {
		getSession().update(course);

	}

	@Override
	public Course findCourseById(Long idCourse) {
		return getSession().get(Course.class, idCourse);
	}

	@Override
	public List<Course> findAllCourses() {
		return getSession().createQuery("from Course").list();
	}

	@Override
	public Course findCourseByName(String name) {
		return (Course) getSession().createQuery("from Course where  name = :name ").setParameter("name", name)
				.uniqueResult();
	}

	@Override
	public List<Course> findCoursesByIdTeacher(Long idTeacher) {
		return (List<Course>) getSession().createQuery("from Course c join c.teacher t where t.id_teacher = :idTeacher")
				.setParameter("idTeacher", idTeacher).list();
	}

}
