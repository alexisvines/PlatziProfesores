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

import com.alexisvines.profesoresplatzi.model.SocialMedia;
import com.alexisvines.profesoresplatzi.model.Teacher;
import com.alexisvines.profesoresplatzi.service.SocialMediaService;
import com.alexisvines.profesoresplatzi.util.CustomErrorType;

/**
 * 
 * @author Alexisvines
 *
 */
@Controller
@RequestMapping("/v1")
public class SocialMediaController {

	@Autowired
	SocialMediaService _socialMediaService;

	public static final String SOCIALMEDIA_UPLOAD_FOLDER = "images/socialMedias/";

	/**
	 * GET ALL
	 * 
	 * @return
	 */
	@RequestMapping(value = "/socialMedias", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<SocialMedia>> getSocialMedia(
			@RequestParam(value = "name", required = false) String name) {
		List<SocialMedia> socialMedias = new ArrayList<>();

		if (name == null) {
			socialMedias = _socialMediaService.findAllSocialMedias();

			if (socialMedias.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<List<SocialMedia>>(socialMedias, HttpStatus.OK);

		} else {
			SocialMedia socialMedia = _socialMediaService.findSocialMediaByName(name);
			if (socialMedia == null) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			socialMedias.add(socialMedia);
			return new ResponseEntity<List<SocialMedia>>(socialMedias, HttpStatus.OK);
		}

	}

	/**
	 * GET BY ID
	 * 
	 * @pathParam debe ser igual al parametro declarado en al URL
	 * @param idSocialMedia
	 * @return
	 */
	@RequestMapping(value = "/socialMedias/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<SocialMedia> getSocialMediaById(@PathVariable("id") Long idSocialMedia) {

		if (idSocialMedia == null || idSocialMedia <= 0) {

			return new ResponseEntity(new CustomErrorType("idSocialMedia es requerido"), HttpStatus.CONFLICT);
		}

		SocialMedia socialMedia = _socialMediaService.findSocialMediaById(idSocialMedia);

		if (socialMedia == null) {
			System.out.println("no hay socialMedia");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<SocialMedia>(socialMedia, HttpStatus.OK);
	}

	/**
	 * POST
	 * 
	 * @param socialMedia
	 * @param uriComponentBuilder
	 * @return
	 */
	@RequestMapping(value = "/socialMedias", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createSocialMedia(@RequestBody SocialMedia socialMedia,
			UriComponentsBuilder uriComponentBuilder) {

		if (socialMedia.getName().equals(null) || socialMedia.getName().isEmpty()) {
			return new ResponseEntity(new CustomErrorType("name es requerido"), HttpStatus.CONFLICT);

		}

		if (_socialMediaService.findSocialMediaByName(socialMedia.getName()) != null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		_socialMediaService.saveSocialMedia(socialMedia);
		SocialMedia socialMediaInsertado = _socialMediaService.findSocialMediaByName(socialMedia.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponentBuilder.path("/v1/socialMedias/{id}")
				.buildAndExpand(socialMediaInsertado.getIdSocialMedia()).toUri());

		return new ResponseEntity<String>(headers, HttpStatus.CREATED);

	}

	/**
	 * UPDATE
	 * 
	 * @param idSocialMedia
	 * @param socialMedia
	 * @return
	 */
	@RequestMapping(value = "/socialMedias/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> updateSocialMedia(@PathVariable("id") Long idSocialMedia,
			@RequestBody SocialMedia socialMedia) {
		SocialMedia currentSocialMedia = _socialMediaService.findSocialMediaById(idSocialMedia);

		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia es requerido"), HttpStatus.CONFLICT);

		}

		if (currentSocialMedia == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		currentSocialMedia.setName(socialMedia.getName());
		currentSocialMedia.setIcon(socialMedia.getIcon());

		_socialMediaService.updateSocialMedia(currentSocialMedia);

		return new ResponseEntity<SocialMedia>(currentSocialMedia, HttpStatus.OK);

	}

	/**
	 * UPDATE
	 * 
	 * @param idSocialMedia
	 * @param socialMedia
	 * @return
	 */
	@RequestMapping(value = "/socialMedias/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteSocialMedia(@PathVariable("id") Long idSocialMedia) {
		SocialMedia currentSocialMedia = _socialMediaService.findSocialMediaById(idSocialMedia);

		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia es requerido"), HttpStatus.CONFLICT);

		}

		SocialMedia socialMedia = _socialMediaService.findSocialMediaById(idSocialMedia);
		if (socialMedia == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		_socialMediaService.deleteSocialMedia(idSocialMedia);

		return new ResponseEntity<SocialMedia>(HttpStatus.OK);

	}

	/**
	 * UPLOAD IMAGE
	 * 
	 */
	@RequestMapping(value = "socialMedias/image", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
	public ResponseEntity<byte[]> uploadSocialMediaImage(@RequestParam("idSocialMedia") Long idSocialMedia,
			@RequestParam("file") MultipartFile multipartFile, UriComponentsBuilder componentsBuilder) {

		if (idSocialMedia == null) {
			return new ResponseEntity(new CustomErrorType("Por favor ingrese el idSocialMedia"), HttpStatus.NO_CONTENT);
		}

		if (multipartFile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Por favor seleccione una imagen para subir"),
					HttpStatus.NO_CONTENT);

		}

		SocialMedia socialMedia = _socialMediaService.findSocialMediaById(idSocialMedia);

		if (socialMedia == null) {
			return new ResponseEntity(
					new CustomErrorType("El socialMedia con id : " + idSocialMedia + " no se encuentra en BD"),
					HttpStatus.NO_CONTENT);
		}

		// Si es que ya existe el archivo se debe borrar de la carpeta para
		// ingresar el nuevo
		if (socialMedia.getIcon() != null || !socialMedia.getIcon().isEmpty()) {
			String filename = socialMedia.getIcon();
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
			String filename = String.valueOf(idSocialMedia) + "-pictureTeacher-" + dateName + "."
					+ multipartFile.getContentType().split("/")[1];
			socialMedia.setIcon(SOCIALMEDIA_UPLOAD_FOLDER + filename);

			// Se escribe el archivo en la carpeta de images
			byte[] bytes = multipartFile.getBytes();
			Path path = Paths.get(SOCIALMEDIA_UPLOAD_FOLDER + filename);
			Files.write(path, bytes);

			_socialMediaService.updateSocialMedia(socialMedia);

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
	 * Get image by id
	 */
	@RequestMapping(value = "/socialMedias/{idSocialMedia}/image", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getSocialMediaImage(@PathVariable("idSocialMedia") Long idSocialMedia) {
		if (idSocialMedia == null) {
			return new ResponseEntity(new CustomErrorType("Por favor ingrese el id de la socialMedia"),
					HttpStatus.NO_CONTENT);
		}

		SocialMedia socialMedia = _socialMediaService.findSocialMediaById(idSocialMedia);

		if (socialMedia == null) {
			return new ResponseEntity(
					new CustomErrorType("La social Media con id : " + idSocialMedia + " no se encuentra en BD"),
					HttpStatus.NO_CONTENT);
		}

		try {
			String filename = socialMedia.getIcon();
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
	@RequestMapping(value = "/socialMedias/{idSocialMedia}/image", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteSocialMediaImage(@PathVariable(value = "idSocialMedia") Long idSocialMedia) {

		if (idSocialMedia == null) {
			return new ResponseEntity(new CustomErrorType("Por favor ingrese el id de la socialMedia"),
					HttpStatus.NO_CONTENT);
		}

		SocialMedia socialMedia = _socialMediaService.findSocialMediaById(idSocialMedia);

		if (socialMedia == null) {
			return new ResponseEntity(
					new CustomErrorType("La social media con id : " + idSocialMedia + " no se encuentra en BD"),
					HttpStatus.NO_CONTENT);
		}

		if (socialMedia.getIcon() == null || socialMedia.getIcon().isEmpty()) {
			return new ResponseEntity(
					new CustomErrorType("El teacher con id : " + idSocialMedia + " no tiene una imagen asociada"),
					HttpStatus.NO_CONTENT);
		}

		String filename = socialMedia.getIcon();
		Path path = Paths.get(filename);
		File f = path.toFile();

		if (f.exists()) {
			f.delete();
		}

		socialMedia.setIcon("");
		_socialMediaService.updateSocialMedia(socialMedia);

		return new ResponseEntity<Teacher>(HttpStatus.OK);

	}

}
