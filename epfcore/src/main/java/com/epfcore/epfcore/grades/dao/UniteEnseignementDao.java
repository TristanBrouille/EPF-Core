package com.epfcore.epfcore.grades.dao;

import com.epfcore.epfcore.grades.exception.DaoException;

public class UniteEnseignementDao {
	private static UniteEnseignementDao instance = null;
	private UniteEnseignementDao() {}

	public static UniteEnseignementDao getInstance() {
		if(instance == null) {
			instance = new UniteEnseignementDao();
		}
		return instance;
	}

	private static final String CREATE_MODULE_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_MODULE_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_MODULE_BY_ID_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_MODULE_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	
}
