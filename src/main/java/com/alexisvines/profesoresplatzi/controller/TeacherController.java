package com.alexisvines.profesoresplatzi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.alexisvines.profesoresplatzi.model.Teacher;
import com.alexisvines.profesoresplatzi.service.TeacherService;
import com.alexisvines.profesoresplatzi.util.CustomErrorType;

/**
 * 
 * @author Alexisvines
 *
 */
@Controller
@RequestMapping("/v1")
public class TeacherController {

	@Autowired
	TeacherService _teacherService;

	/**
	 * GET ALL
	 * 
	 * @return
	 */
	@RequestMapping(value = "/teachers", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Teacher>> getTeacher(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "id_teacher", required = false) Long idTeacher) {
		List<Teacher> teachers = new ArrayList<>();

		if (name != null) {
			Teacher teacher = _teacherService.findTeacherByName(name);
			if (teacher == null) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			teachers.add(teacher);
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);

		}

		if (idTeacher != null) {
			Teacher teacher = _teacherService.findTeacherById(idTeacher);
			if (teacher == null) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			teachers.add(teacher);
			return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);

		}

		if (name == null && idTeacher == null) {
			teachers = _teacherService.findAllTeacher();
			if (teachers.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		}

		return new ResponseEntity<List<Teacher>>(teachers, HttpStatus.OK);
	}

	/**
	 * GET BY ID
	 */
	@RequestMapping(value = "/teachers/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") Long idTeacher) {

		if (idTeacher == null || idTeacher <= 0) {

			return new ResponseEntity(new CustomErrorType("idTeacher es requerido"), HttpStatus.CONFLICT);
		}

		Teacher teacher = _teacherService.findTeacherById(idTeacher);

		if (teacher == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<Teacher>(teacher, HttpStatus.OK);
	}

	/**
	 * POST
	 *
	 */
	@RequestMapping(value = "/teachers", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createTeacher(@RequestBody Teacher teacher,
			UriComponentsBuilder uriComponentBuilder) {

		if (teacher.getName().equals(null) || teacher.getName().isEmpty()) {
			return new ResponseEntity(new CustomErrorType("name es requerido"), HttpStatus.CONFLICT);

		}

		if (_teacherService.findTeacherByName(teacher.getName()) != null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		_teacherService.saveTeacher(teacher);
		Teacher teacherInsertado = _teacherService.findTeacherByName(teacher.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponentBuilder.path("/v1/teachers/{id}").buildAndExpand(teacherInsertado.getId_teacher()).toUri());

		return new ResponseEntity<String>(headers, HttpStatus.CREATED);

	}

	/**
	 * UPDATE
	 * 
	 * @param idSocialMedia
	 * @param socialMedia
	 * @return
	 */
	@RequestMapping(value = "/teachers/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<Teacher> updateTeacher(@PathVariable("id") Long idTeacher, @RequestBody Teacher teacher) {

		Teacher currentTeacher = _teacherService.findTeacherById(idTeacher);

		if (idTeacher == null || idTeacher <= 0) {
			return new ResponseEntity(new CustomErrorType("idTeacher es requerido"), HttpStatus.CONFLICT);

		}

		if (currentTeacher == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		currentTeacher.setName(teacher.getName());
		currentTeacher.setAvatar(teacher.getAvatar());

		_teacherService.updateTeacher(currentTeacher);

		return new ResponseEntity<Teacher>(currentTeacher, HttpStatus.OK);

	}

	/**
	 * DELETE
	 */
	@RequestMapping(value = "/teachers/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteTeacher(@PathVariable("id") Long idTeacher) {

		Teacher currentTeacher = _teacherService.findTeacherById(idTeacher);

		if (idTeacher == null || idTeacher <= 0) {
			return new ResponseEntity(new CustomErrorType("idTeacher es requerido"), HttpStatus.CONFLICT);

		}

		Teacher teacher = _teacherService.findTeacherById(idTeacher);
		if (teacher == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		_teacherService.deleteTeacher(idTeacher);

		return new ResponseEntity<String>(HttpStatus.OK);

	}

}
