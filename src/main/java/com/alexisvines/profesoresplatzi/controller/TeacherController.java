package com.alexisvines.profesoresplatzi.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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

	public static final String TEACHER_UPLOAD_FOLDER = "images/teachers/";

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

	/**
	 * UPLOAD IMAGE
	 * 
	 */
	@RequestMapping(value = "teachers/image", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	public ResponseEntity<byte[]> uploadTeacherImage(@RequestParam("id_teacher") Long idTeacher,
			@RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder componentsBuilder) {

		if (idTeacher == null) {
			return new ResponseEntity(new CustomErrorType("Por favor ingrese el idTeacher"), HttpStatus.NO_CONTENT);
		}

		if (multipartFile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Por favor seleccione una imagen para subir"),
					HttpStatus.NO_CONTENT);

		}

		Teacher teacher = _teacherService.findTeacherById(idTeacher);

		if (teacher == null) {
			return new ResponseEntity(
					new CustomErrorType("El teacher con id : " + idTeacher + " no se encuentra en BD"),
					HttpStatus.NO_CONTENT);
		}

		// Si es que ya existe el archivo se debe borrar de la carpeta para
		// ingresar el nuevo
		if (teacher.getAvatar() != null || !teacher.getAvatar().isEmpty()) {
			String filename = teacher.getAvatar();
			Path path = Paths.get(filename);
			File f = path.toFile();
			if (f.exists()) {
				f.delete();
			}
		}

		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
			String dateName = dateFormat.format(date);

			// Se seta el nombre del teacher en el campo de imagen en BD
			String filename = String.valueOf(idTeacher) + "-pictureTeacher-" + dateName + "."
					+ multipartFile.getContentType().split("/")[1];
			teacher.setAvatar(TEACHER_UPLOAD_FOLDER + filename);

			// Se escribe el archivo en la carpeta de images
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(TEACHER_UPLOAD_FOLDER + filename);
			Files.write(path, bytes);

			_teacherService.updateTeacher(teacher);

			// Se retorna la imagen
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(
					new CustomErrorType("Error durante la subida: " + multipartFile.getOriginalFilename()),
					HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Get image by id teacher
	 */
	@RequestMapping(value = "/teachers/{id_teacher}/images",method = RequestMethod.GET)
	public ResponseEntity<byte[]> getTeachaerImage(@PathVariable("id_teacher") Long idTeacher) {
		if (idTeacher == null) {
			return new ResponseEntity(new CustomErrorType("Por favor ingrese el id del teacher"),
					HttpStatus.NO_CONTENT);
		}

		Teacher teacher = _teacherService.findTeacherById(idTeacher);

		if (teacher == null) {
			return new ResponseEntity(
					new CustomErrorType("El teacher con id : " + idTeacher + " no se encuentra en BD"),
					HttpStatus.NO_CONTENT);
		}

		try {
			String filename = teacher.getAvatar();
			Path path = Paths.get(filename);
			File f = path.toFile();
			if (!f.exists()) {
				return new ResponseEntity(new CustomErrorType("Imagen no encontrada"), HttpStatus.NO_CONTENT);
			}
			byte[] image = Files.readAllBytes(path);

			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error al mostrar la imagen"), HttpStatus.NO_CONTENT);
		}

	}

	/**
	 * Delete Image
	 */
	@RequestMapping(value = "/teachers/{id_teacher}/images", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteTeacherImage(@PathVariable(value = "id_teacher") Long idTeacher) {

		if (idTeacher == null) {
			return new ResponseEntity(new CustomErrorType("Por favor ingrese el id del teacher"),
					HttpStatus.NO_CONTENT);
		}

		Teacher teacher = _teacherService.findTeacherById(idTeacher);

		if (teacher == null) {
			return new ResponseEntity(
					new CustomErrorType("El teacher con id : " + idTeacher + " no se encuentra en BD"),
					HttpStatus.NO_CONTENT);
		}

		if (teacher.getAvatar() == null || teacher.getAvatar().isEmpty()) {
			return new ResponseEntity(
					new CustomErrorType("El teacher con id : " + idTeacher + " no tiene una imagen asociada"),
					HttpStatus.NO_CONTENT);
		}

		String filename = teacher.getAvatar();
		Path path = Paths.get(filename);
		File f = path.toFile();

		if (f.exists()) {
			f.delete();
		}

		teacher.setAvatar("");
		_teacherService.updateTeacher(teacher);

		return new ResponseEntity<Teacher>(HttpStatus.NO_CONTENT);

	}

}
