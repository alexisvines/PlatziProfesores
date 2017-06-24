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
import org.springframework.web.util.UriComponentsBuilder;

import com.alexisvines.profesoresplatzi.model.Course;
import com.alexisvines.profesoresplatzi.model.SocialMedia;
import com.alexisvines.profesoresplatzi.model.Teacher;
import com.alexisvines.profesoresplatzi.service.CourseService;
import com.alexisvines.profesoresplatzi.service.SocialMediaService;
import com.alexisvines.profesoresplatzi.service.TeacherService;
import com.alexisvines.profesoresplatzi.util.CustomErrorType;

/**
 * 
 * @author Alexisvines
 *
 */
@Controller
@RequestMapping("/v1")
public class CourseController {

	@Autowired
	CourseService _courseService;

	/**
	 * GET ALL
	 * 
	 * @return
	 */
	@RequestMapping(value = "/courses", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<Course>> getCourses() {
		List<Course> courses = new ArrayList<>();
		courses = _courseService.findAllCourses();

		if (courses.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
	}

	/**
	 * GET BY ID
	 */
	@RequestMapping(value = "/courses/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<Course> getCourseById(@PathVariable("id") Long idCourse) {

		if (idCourse == null || idCourse <= 0) {

			return new ResponseEntity(new CustomErrorType("idCourses es requerido"), HttpStatus.CONFLICT);
		}

		Course course = _courseService.findCourseById(idCourse);

		if (course == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<Course>(course, HttpStatus.OK);
	}

	/**
	 * POST
	 *
	 */
	@RequestMapping(value = "/courses", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createCourse(@RequestBody Course course, UriComponentsBuilder uriComponentBuilder) {

		if (course.getName().equals(null) || course.getName().isEmpty()) {
			return new ResponseEntity(new CustomErrorType("name es requerido"), HttpStatus.CONFLICT);

		}

		if (_courseService.findCourseByName(course.getName()) != null) {
			return new ResponseEntity(HttpStatus.CONFLICT);
		}
		_courseService.saveCourse(course);
		Course courseInsertado = _courseService.findCourseByName(course.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				uriComponentBuilder.path("/v1/courses/{id}").buildAndExpand(courseInsertado.getId_course()).toUri());

		return new ResponseEntity<String>(headers, HttpStatus.CREATED);

	}

	/**
	 * UPDATE
	 */
	@RequestMapping(value = "/courses/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<Course> updateCourse(@PathVariable("id") Long idCourse, @RequestBody Course course) {

		if (idCourse == null || idCourse <= 0) {
			return new ResponseEntity(new CustomErrorType("idTeacher es requerido"), HttpStatus.CONFLICT);

		}

		Course currentCourse = _courseService.findCourseById(idCourse);

		if (currentCourse == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		currentCourse.setName(course.getName());
		currentCourse.setProject(course.getProject());
		currentCourse.setThemes(course.getThemes());
		

		_courseService.updateCourse(currentCourse);

		return new ResponseEntity<Course>(currentCourse, HttpStatus.OK);

	}

	/**
	 * DELETE
	 */
	@RequestMapping(value = "/courses/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteCourse(@PathVariable("id") Long idCourse) {

		Course currentCourse= _courseService.findCourseById(idCourse);

		if (idCourse == null || idCourse <= 0) {
			return new ResponseEntity(new CustomErrorType("idTeacher es requerido"), HttpStatus.CONFLICT);

		}

		Course course = _courseService.findCourseById(idCourse);
		if (course == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		_courseService.deleteCourse(idCourse);

		return new ResponseEntity<String>(HttpStatus.OK);

	}

}
