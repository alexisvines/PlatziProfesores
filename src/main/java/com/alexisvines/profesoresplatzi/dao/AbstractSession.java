package com.alexisvines.profesoresplatzi.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Alexisvines
 * abstracta paar que sea obligatorio sobreescribir los metodos en la herencia
 *
 */

public abstract class AbstractSession {
	@Autowired
	private SessionFactory sessionFactory;
	
	protected Session getSession(){
		return sessionFactory.getCurrentSession();
	}

}
