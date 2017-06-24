package com.alexisvines.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexisvines.profesoresplatzi.dao.TeacherDao;
import com.alexisvines.profesoresplatzi.model.Teacher;

/**
 * 
 * @author Alexisvines
 *
 */
@Service("teacherService")
@Transactional
public class TeacherServiceImpl implements TeacherService {

	@Autowired
	private TeacherDao _teacherDao;

	@Override
	public void saveTeacher(Teacher teacher) {
		_teacherDao.saveTeacher(teacher);

	}

	@Override
	public void deleteTeacher(Long idTeacher) {
		_teacherDao.deleteTeacher(idTeacher);
	}

	@Override
	public void updateTeacher(Teacher teacher) {
		_teacherDao.updateTeacher(teacher);

	}

	@Override
	public Teacher findTeacherById(Long idTeacher) {
		return _teacherDao.findTeacherById(idTeacher);
	}

	@Override
	public Teacher findTeacherByName(String name) {
		return _teacherDao.findTeacherByName(name);
	}

	@Override
	public List<Teacher> findAllTeacher() {
		return _teacherDao.findAllTeacher();
	}

}
