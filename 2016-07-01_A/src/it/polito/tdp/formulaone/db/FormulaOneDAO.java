package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.formulaone.model.Arco;
import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.DriverIdMap;
import it.polito.tdp.formulaone.model.Season;


public class FormulaOneDAO {
	
	public List<Season> getAllSeasons() {
		
		String sql = "SELECT year, url FROM seasons ORDER BY year" ;
		
		try {
			Connection conn = ConnectDB.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Season> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Season(Year.of(rs.getInt("year")), rs.getString("url"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	/**
	 * Data una stagione, restituisce i piloti
	 * @param s
	 * @param map
	 * @return
	 */
	public List<Driver> getAllDrivers(Season s, DriverIdMap map) {
		String sql = "SELECT DISTINCT d.driverId as id, d.forename as nome, d.surname as cognome " + 
					 "FROM drivers AS d, results as r, races as g " + 
					 "WHERE d.driverId = r.driverId " + 
					 "		AND r.raceId = g.raceId " + 
					 "		AND g.year = ? " +
					 "		AND r.position is not null";

		try {
			Connection conn = ConnectDB.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, s.getYear().getValue());
			
			ResultSet rs = st.executeQuery();

			List<Driver> drivers = new ArrayList<>();
			
			while (rs.next())
				drivers.add(map.getDriver(new Driver(rs.getInt("id"), rs.getString("nome"), rs.getString("cognome"))));

			conn.close();
			return drivers;
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	/**
	 * Restituisce tutti gli archi pesati
	 * @param s
	 * @param map
	 * @return
	 */
	public List<Arco> getAllEdge(Season s, DriverIdMap map) {
		String sql = "SELECT r1.driverId as partenza, r2.driverId as arrivo, COUNT(*) as peso " + 
					 "FROM results as r1, results as r2, races as g " + 
					 "WHERE r1.driverId <> r2.driverId " + 
					 "		AND r1.raceId = r2.raceId " + 
					 "		AND r1.raceId = g.raceId " +
					 
					 // posizione maggiore significa che è r2 è "arrivato dopo" r1
					 "		AND r1.position < r2.position " + 
					 "		AND r1.position is not null " + 
					 "		AND r2.position is not null " + 
					 "		AND g.year = ? " + 
					 "GROUP BY partenza, arrivo";
	
		try {
			Connection conn = ConnectDB.getConnection();
	
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, s.getYear().getValue());
			
			ResultSet rs = st.executeQuery();
	
			List<Arco> archi = new ArrayList<>();
			
			while (rs.next()) {
			
				Driver partenza = map.getDriver(rs.getInt("partenza"));
				Driver arrivo = map.getDriver(rs.getInt("arrivo"));
				
				if (partenza != null && arrivo != null)
					archi.add(new Arco (partenza, arrivo, rs.getInt("peso")));
			}
			
			conn.close();
			return archi;
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
}
