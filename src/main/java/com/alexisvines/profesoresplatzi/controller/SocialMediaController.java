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

import com.alexisvines.profesoresplatzi.model.SocialMedia;
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

	/**
	 * GET ALL
	 * @return
	 */
	@RequestMapping(value = "/socialMedias", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<List<SocialMedia>> getSocialMedia() {
		List<SocialMedia> socialMedias = new ArrayList<>();
		socialMedias = _socialMediaService.findAllSocialMedias();

		if (socialMedias.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<SocialMedia>>(socialMedias, HttpStatus.OK);
	}

	/**
	 * GET BY ID
	 * @pathParam debe ser igual al parametro declarado en al URL
	 * @param idSocialMedia
	 * @return
	 */
	@RequestMapping(value = "/socialMedias/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<SocialMedia> getSocialMediaById(@PathVariable("id") Long idSocialMedia) {

		if (idSocialMedia == null || idSocialMedia <= 0) {
			
			return new ResponseEntity(new CustomErrorType("idSocialMedia es requerido"),HttpStatus.CONFLICT);
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
			return new ResponseEntity(new CustomErrorType("name es requerido"),HttpStatus.CONFLICT);

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
	 * @param idSocialMedia
	 * @param socialMedia
	 * @return
	 */
	@RequestMapping(value = "/socialMedias/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> updateSocialMedia(@PathVariable("id") Long idSocialMedia, @RequestBody SocialMedia socialMedia){
		SocialMedia currentSocialMedia = _socialMediaService.findSocialMediaById(idSocialMedia);
		
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia es requerido"),HttpStatus.CONFLICT);

		}
		
		if(currentSocialMedia == null){
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		currentSocialMedia.setName(socialMedia.getName());
		currentSocialMedia.setIcon(socialMedia.getIcon());
		
		_socialMediaService.updateSocialMedia(currentSocialMedia);
		
		return new ResponseEntity<SocialMedia>(currentSocialMedia,HttpStatus.OK);
		

	}
	
	/**
	 * UPDATE
	 * @param idSocialMedia
	 * @param socialMedia
	 * @return
	 */
	@RequestMapping(value = "/socialMedias/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteSocialMedia(@PathVariable("id") Long idSocialMedia){
		SocialMedia currentSocialMedia = _socialMediaService.findSocialMediaById(idSocialMedia);
		
		if (idSocialMedia == null || idSocialMedia <= 0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia es requerido"),HttpStatus.CONFLICT);

		}
		
		SocialMedia socialMedia = _socialMediaService.findSocialMediaById(idSocialMedia);
		if(socialMedia == null){
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		_socialMediaService.deleteSocialMedia(idSocialMedia);
		
		return new ResponseEntity<SocialMedia>(HttpStatus.OK);

	}

}
