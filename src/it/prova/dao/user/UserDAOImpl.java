package it.prova.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import it.prova.dao.AbstractMySQLDAO;
import it.prova.model.User;
import it.prova.connection.MyConnection;
import it.prova.model.User;

public class UserDAOImpl extends AbstractMySQLDAO implements UserDAO {

	// la connection stavolta fa parte del this, quindi deve essere 'iniettata'
	// dall'esterno
	public UserDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<User> list() throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<User> result = new ArrayList<User>();

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from user")) {

			while (rs.next()) {
				User userTemp = new User();
				userTemp.setNome(rs.getString("NOME"));
				userTemp.setCognome(rs.getString("COGNOME"));
				userTemp.setLogin(rs.getString("LOGIN"));
				userTemp.setPassword(rs.getString("PASSWORD"));
				userTemp.setDateCreated(
						rs.getDate("DATECREATED") != null ? rs.getDate("DATECREATED").toLocalDate() : null);
				userTemp.setId(rs.getLong("ID"));
				result.add(userTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public User get(Long idInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		User result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from user where id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new User();
					result.setNome(rs.getString("NOME"));
					result.setCognome(rs.getString("COGNOME"));
					result.setLogin(rs.getString("LOGIN"));
					result.setPassword(rs.getString("PASSWORD"));
					result.setDateCreated(
							rs.getDate("DATECREATED") != null ? rs.getDate("DATECREATED").toLocalDate() : null);
					result.setId(rs.getLong("ID"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(User utenteInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (utenteInput == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO user (nome, cognome, login, password, dateCreated) VALUES (?, ?, ?, ?, ?);")) {
			ps.setString(1, utenteInput.getNome());
			ps.setString(2, utenteInput.getCognome());
			ps.setString(3, utenteInput.getLogin());
			ps.setString(4, utenteInput.getPassword());
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(5, java.sql.Date.valueOf(utenteInput.getDateCreated()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(User utenteInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (utenteInput == null || utenteInput.getId() == null || utenteInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE user SET nome=?, cognome=?, login=?, password=?, dateCreated=? where id=?;")) {
			ps.setString(1, utenteInput.getNome());
			ps.setString(2, utenteInput.getCognome());
			ps.setString(3, utenteInput.getLogin());
			ps.setString(4, utenteInput.getPassword());
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(5, java.sql.Date.valueOf(utenteInput.getDateCreated()));
			ps.setLong(6, utenteInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(User utenteInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (utenteInput == null || utenteInput.getId() == null || utenteInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM user WHERE ID=?")) {
			ps.setLong(1, utenteInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<User> findAllWhereDateCreatedGreaterThan(LocalDate dateCreatedInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (dateCreatedInput == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<User> result = new ArrayList<User>();

		try (PreparedStatement ps = connection.prepareStatement("select * from user where dateCreated > ? ;")) {
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(1, java.sql.Date.valueOf(dateCreatedInput));

			try (ResultSet rs = ps.executeQuery();) {
				while (rs.next()) {
					User userTemp = new User();
					userTemp.setNome(rs.getString("NOME"));
					userTemp.setCognome(rs.getString("COGNOME"));
					userTemp.setLogin(rs.getString("LOGIN"));
					userTemp.setPassword(rs.getString("PASSWORD"));
					userTemp.setDateCreated(
							rs.getDate("DATECREATED") != null ? rs.getDate("DATECREATED").toLocalDate() : null);
					userTemp.setId(rs.getLong("ID"));
					result.add(userTemp);
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	// DA FARE PER ESERCIZIO

	@Override
	public List<User> findAllByCognome(String cognomeInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
				if (isNotActive())
					throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
				
				
				if(cognomeInput==null) {
					throw new Exception("errore: non è stato inserito alcun cognome!");
				}
				List<User> elencoUtentiConCognome =new ArrayList<User>();
				
				try (PreparedStatement ps = connection.prepareStatement("select * from user u where u.cognome like ?");){

					ps.setString(1, cognomeInput);
					try(ResultSet rs = ps.executeQuery()){;

						while (rs.next()) {
							User temp = new User();
							temp.setId(rs.getLong("id"));
							temp.setNome(rs.getString("nome"));
							temp.setCognome(rs.getString("cognome"));
							temp.setLogin(rs.getString("login"));
							temp.setPassword(rs.getString("password"));
							temp.setDateCreated(
									rs.getDate("datecreated") != null ? rs.getDate("datecreated").toLocalDate() : null);
							elencoUtentiConCognome.add(temp);

						}
					}

				} catch (Exception e) {

					e.printStackTrace();

				}
				return elencoUtentiConCognome;
	}

	@Override
	public List<User> findAllByLoginIniziaCon(String caratteriInizialiInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		
		
		if(caratteriInizialiInput==null) {
			throw new Exception("errore: non è stato inserito alcun login!");
		}
		List<User> elencoUtentiConLogin =new ArrayList<User>();
		
		try (PreparedStatement ps = connection.prepareStatement("select * from user u where u.login like ?");){

			ps.setString(1, caratteriInizialiInput + '%');
			try(ResultSet rs = ps.executeQuery()){;

				while (rs.next()) {
					User temp = new User();
					temp.setId(rs.getLong("id"));
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setLogin(rs.getString("login"));
					temp.setPassword(rs.getString("password"));
					temp.setDateCreated(
							rs.getDate("datecreated") != null ? rs.getDate("datecreated").toLocalDate() : null);
					elencoUtentiConLogin.add(temp);

				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
		return elencoUtentiConLogin;
	}

	@Override
	public User findByLoginAndPassword(String loginInput, String passwordInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		
		
		if(loginInput==null) {
			throw new Exception("errore: non è stato inserito alcun login!");
		}
		if (passwordInput==null) {
			throw new Exception ("errore: non è stata inserita alcuna password!");
		}
		List<User> elencoUtentiConLoginEPassword =new ArrayList<User>();
		
		try (PreparedStatement ps = connection.prepareStatement("select * from user u where u.login like ? and u.password like ?");){

			ps.setString(1, loginInput);
			ps.setString(2, passwordInput);
			try(ResultSet rs = ps.executeQuery()){;

				while (rs.next()) {
					User temp = new User();
					temp.setId(rs.getLong("id"));
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setLogin(rs.getString("login"));
					temp.setPassword(rs.getString("password"));
					temp.setDateCreated(
							rs.getDate("datecreated") != null ? rs.getDate("datecreated").toLocalDate() : null);
					elencoUtentiConLoginEPassword.add(temp);

				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	@Override
	public List<User> findAllByPasswordIsNull() throws Exception {
		
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
		
		List<User> elencoUtentiConPasswordNull =new ArrayList<User>();
		
		try (PreparedStatement ps = connection.prepareStatement("select * from user u where u.password is null");){
			try(ResultSet rs = ps.executeQuery()){;

				while (rs.next()) {
					User temp = new User();
					temp.setId(rs.getLong("id"));
					temp.setNome(rs.getString("nome"));
					temp.setCognome(rs.getString("cognome"));
					temp.setLogin(rs.getString("login"));
					temp.setPassword(rs.getString("password"));
					temp.setDateCreated(
							rs.getDate("datecreated") != null ? rs.getDate("datecreated").toLocalDate() : null);
					elencoUtentiConPasswordNull.add(temp);

				}
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
		return elencoUtentiConPasswordNull;
	}

	@Override
	public List<User> findByExample(User input) throws Exception {
		if (isNotActive())
		throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

	if (input == null)
		throw new Exception("Valore di input non ammesso.");

	ArrayList<User> result = new ArrayList<User>();
	User userTemp = null;

	String query = "select * from user where 1=1 ";
	if (input.getCognome() != null && !input.getCognome().isEmpty()) {
		query += " and cognome like '" + input.getCognome() + "%' ";
	}
	if (input.getNome() != null && !input.getNome().isEmpty()) {
		query += " and nome like '" + input.getNome() + "%' ";
	}

	if (input.getLogin() != null && ! input.getLogin().isEmpty()) {
		query += " and login like '" + input.getLogin() + "%' ";
	}

	if (input.getPassword() != null && !input.getPassword().isEmpty()) {
		query += " and password like '" + input.getPassword() + "%' ";
	}

	if (input.getDateCreated() != null) {
		query += " and DATECREATED='" + java.sql.Date.valueOf(example.getDateCreated()) + "' ";
	}

	try (Statement ps = connection.createStatement()) {
		ResultSet rs = ps.executeQuery(query);

		while (rs.next()) {
			userTemp = new User();
			userTemp.setNome(rs.getString("nome"));
			userTemp.setCognome(rs.getString("cognome"));
			userTemp.setLogin(rs.getString("login"));
			userTemp.setPassword(rs.getString("password"));
			userTemp.setDateCreated(
					rs.getDate("datecreated") != null ? rs.getDate("datecreated").toLocalDate() : null);
			userTemp.setId(rs.getLong("id"));
			result.add(userTemp);
		}
	} catch (Exception e) {
		e.printStackTrace();
		throw e;
	}
	return result;
	}

	// ############################################################
	// ############ SOLUZIONE FINDBYEXAMPLE #######################
	// ############################################################
//	public List<User> findByExampleSolution(User example) throws Exception {
//		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
//		if (isNotActive())
//			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");
//
//		if (example == null)
//			throw new Exception("Valore di input non ammesso.");
//
//		ArrayList<User> result = new ArrayList<User>();
//		User userTemp = null;
//
//		String query = "select * from user where 1=1 ";
//		if (example.getCognome() != null && !example.getCognome().isEmpty()) {
//			query += " and cognome like '" + example.getCognome() + "%' ";
//		}
//		if (example.getNome() != null && !example.getNome().isEmpty()) {
//			query += " and nome like '" + example.getNome() + "%' ";
//		}
//
//		if (example.getLogin() != null && !example.getLogin().isEmpty()) {
//			query += " and login like '" + example.getLogin() + "%' ";
//		}
//
//		if (example.getPassword() != null && !example.getPassword().isEmpty()) {
//			query += " and password like '" + example.getPassword() + "%' ";
//		}
//
//		if (example.getDateCreated() != null) {
//			query += " and DATECREATED='" + java.sql.Date.valueOf(example.getDateCreated()) + "' ";
//		}
//
//		try (Statement ps = connection.createStatement()) {
//			ResultSet rs = ps.executeQuery(query);
//
//			while (rs.next()) {
//				userTemp = new User();
//				userTemp.setNome(rs.getString("NOME"));
//				userTemp.setCognome(rs.getString("COGNOME"));
//				userTemp.setLogin(rs.getString("LOGIN"));
//				userTemp.setPassword(rs.getString("PASSWORD"));
//				userTemp.setDateCreated(
//						rs.getDate("DATECREATED") != null ? rs.getDate("DATECREATED").toLocalDate() : null);
//				userTemp.setId(rs.getLong("ID"));
//				result.add(userTemp);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//		return result;
//	}

}
