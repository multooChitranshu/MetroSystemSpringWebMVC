package com.group9.model.persistence;

import java.util.List;

import com.group9.bean.MetroStation;

public interface MetroStationDAO {
	boolean isValidStation(int stationId);
	MetroStation getStation(int stationId);
	List<MetroStation> getAllStations();
	int getStationIdFromName(String stationName);
}
