package it.polito.tdp.formulaone.model;

import java.util.HashMap;
import java.util.Map;

public class DriverIdMap {

	private Map <Integer, Driver> map;
	
	public DriverIdMap () {
		
		this.map = new HashMap<>();
		
	}
	
	public Driver getDriver (Driver driver) {
		Driver old = this.map.get(driver.getDriverId());
		if (old == null)
			map.put(driver.getDriverId(), driver);
	
		return map.get(driver.getDriverId());
	}
	
	public Driver getDriver (int driverId) {
		return map.get(driverId);
	}
	
	public void put (int driverId, Driver driver) {
		this.map.put(driverId, driver);
	}
}
