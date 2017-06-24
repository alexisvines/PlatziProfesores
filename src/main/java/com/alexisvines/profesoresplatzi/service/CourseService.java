package com.alexisvines.profesoresplatzi.service;

import java.util.List;

import com.alexisvines.profesoresplatzi.model.Course;

/**
 * 
 * @author Alexisvines
 *
 */
public interface CourseService {
	
	public void saveCourse(Course course);

	public void deleteCourse(Long idCourse);

	public void updateCourse(Course course);

	public Course findCourseById(Long idCourse);

	public List<Course> findAllCourses();
	
	public Course findCourseByName(String name);

	public List<Course> findCoursesByIdTeacher(Long idTeacher);


}
