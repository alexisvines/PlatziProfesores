package com.alexisvines.profesoresplatzi.dao;

import java.util.List;

import com.alexisvines.profesoresplatzi.model.Course;

public interface CourseDao {
	
	public void saveCourse(Course course);

	public void deleteCourse(Long idCourse);

	public void updateCourse(Course course);

	public Course findCourseById(Long idCourse);

	public List<Course> findAllCourses();
	
	public Course findCourseByName(String name);

	public List<Course> findCoursesByIdTeacher(Long idTeacher);

}
