package com.epfcore.epfcore.grades.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

// public class ClientDao {
	
	
	
	
	
// 	public long create(Client client) throws DaoException {
// 		try (Connection connection = ConnectionManager.getConnection();
// 			 PreparedStatement statement = connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS)) {

// 			statement.setString(1, client.getNom());
// 			statement.setString(2, client.getPrenom());
// 			statement.setString(3, client.getEmail());
// 			statement.setDate(4, Date.valueOf(client.getNaissance()));

// 			statement.executeUpdate();

// 			ResultSet generatedKeys = statement.getGeneratedKeys();
// 			if (generatedKeys.next()) {
// 				return generatedKeys.getLong(1);
// 			} else {
// 				throw new DaoException("Échec de la création du client, aucun ID généré.");
// 			}
// 		} catch (SQLException e) {
// 			throw new DaoException("Erreur lors de la création du client", e);
// 		}
// 	}
	
// 	public long delete(Client client) throws DaoException {
// 		try (Connection connection = ConnectionManager.getConnection();
// 			 PreparedStatement statement = connection.prepareStatement(DELETE_CLIENT_QUERY)) {

// 			statement.setLong(1, client.getId());
// 			return statement.executeUpdate();

// 		} catch (SQLException e) {
// 			throw new DaoException("Erreur lors de la suppression du client", e);
// 		}
// 	}

// 	public Client findById(long id) throws DaoException {
// 		try (Connection connection = ConnectionManager.getConnection();
// 			 PreparedStatement statement = connection.prepareStatement(FIND_CLIENT_QUERY)) {

// 			statement.setLong(1, id);
// 			ResultSet resultSet = statement.executeQuery();

// 			if (resultSet.next()) {
// 				Client client = new Client();
// 				client.setId(resultSet.getLong("id"));
// 				client.setNom(resultSet.getString("nom"));
// 				client.setPrenom(resultSet.getString("prenom"));
// 				client.setEmail(resultSet.getString("email"));

// 				Date naissanceDate = resultSet.getDate("naissance");
// 				if (naissanceDate != null) {
// 					client.setNaissance(naissanceDate.toLocalDate());
// 				}

// 				return client;
// 			}

// 			return null;

// 		} catch (SQLException e) {
// 			throw new DaoException("Erreur lors de la recherche du client par ID", e);
// 		}
// 	}

// 	public List<Client> findAll() throws DaoException {
// 		List<Client> clients = new ArrayList<>();

// 		try (Connection connection = ConnectionManager.getConnection();
// 			 PreparedStatement statement = connection.prepareStatement(FIND_CLIENTS_QUERY)) {

// 			ResultSet resultSet = statement.executeQuery();

// 			while (resultSet.next()) {
// 				Client client = new Client();
// 				client.setId(resultSet.getLong("id"));
// 				client.setNom(resultSet.getString("nom"));
// 				client.setPrenom(resultSet.getString("prenom"));
// 				client.setEmail(resultSet.getString("email"));

// 				Date naissanceDate = resultSet.getDate("naissance");
// 				if (naissanceDate != null) {
// 					client.setNaissance(naissanceDate.toLocalDate());
// 				}

// 				clients.add(client);
// 			}

// 			return clients;

// 		} catch (SQLException e) {
// 			throw new DaoException("Erreur lors de la récupération de tous les clients", e);
// 		}
// 	}

// }

