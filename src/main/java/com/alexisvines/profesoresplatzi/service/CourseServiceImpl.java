package com.alexisvines.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexisvines.profesoresplatzi.dao.CourseDao;
import com.alexisvines.profesoresplatzi.model.Course;

/**
 * 
 * @author Alexisvines
 *
 */
@Service("courseService")
@Transactional
public class CourseServiceImpl implements CourseService {

	@Autowired
	private CourseDao _courseDao;

	@Override
	public void saveCourse(Course course) {
		_courseDao.saveCourse(course);

	}

	@Override
	public void deleteCourse(Long idCourse) {
		_courseDao.deleteCourse(idCourse);

	}

	@Override
	public void updateCourse(Course course) {
		_courseDao.updateCourse(course);

	}

	@Override
	public Course findCourseById(Long idCourse) {
		return _courseDao.findCourseById(idCourse);
	}

	@Override
	public List<Course> findAllCourses() {
		return _courseDao.findAllCourses();
	}

	@Override
	public Course findCourseByName(String name) {
		return _courseDao.findCourseByName(name);
	}

	@Override
	public List<Course> findCoursesByIdTeacher(Long idTeacher) {
		return _courseDao.findCoursesByIdTeacher(idTeacher);
	}

}
