// package com.epfcore.epfcore.grades.dao;

// public class ModuleDao {

//     private static ModuleDao instance = null;
// 	private ModuleDao() {}

// 	public static ModuleDao getInstance() {
// 		if(instance == null) {
// 			instance = new ModuleDao();
// 		}
// 		return instance;
// 	}

//     private static final String CREATE_MODULE_QUERY = "INSERT INTO Module(id, code, intitule, credits_ECTS, vol_horaire, vol_horaire_CM, vol_horaire_CMA, vol_horaire_TD, vol_horaire_TP, vol_horaire_Projet, vol_horaire_a_plannifier, vol_horaire_non_plannifier, coef) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
// 	private static final String DELETE_MODULE_QUERY = "DELETE FROM Module WHERE id=?;";
// 	private static final String FIND_MODULE_FROM_ID_QUERY = "SELECT id, code, intitule, credits_ECTS, vol_horaire, vol_horaire_CM, vol_horaire_CMA, vol_horaire_TD, vol_horaire_TP, vol_horaire_Projet, vol_horaire_a_plannifier, vol_horaire_non_plannifier, coef FROM Module WHERE id=?;";
// 	private static final String FIND_MODULE_QUERY = "SELECT id, code, intitule, credits_ECTS, vol_horaire, vol_horaire_CM, vol_horaire_CMA, vol_horaire_TD, vol_horaire_TP, vol_horaire_Projet, vol_horaire_a_plannifier, vol_horaire_non_plannifier, coef FROM Module;";
	

// }

// Module(intitule, credits_ECTS, vol_horaire, vol_horaire_CM, vol_horaire_CMA, vol_horaire_TD, vol_horaire_TP, vol_horaire_Projet, vol_horaire_a_plannifier, vol_horaire_non_plannifier, coef)