package com.alexisvines.profesoresplatzi.dao;

import java.util.List;

import com.alexisvines.profesoresplatzi.model.Teacher;

/**
 * 
 * @author Alexisvines
 *
 */
public interface TeacherDao {

	public void saveTeacher(Teacher teacher);

	public void deleteTeacher(Long idTeacher);

	public void updateTeacher(Teacher teacher);

	public Teacher findTeacherById(Long idTeacher);

	public Teacher findTeacherByName(String name);

	public List<Teacher> findAllTeacher();

}
