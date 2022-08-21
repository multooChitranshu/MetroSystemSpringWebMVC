package com.group9.model.persistence;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.group9.bean.MetroStation;
import com.group9.model.persistence.helper.MetroStationRowMapper;

@Repository
public class MetroStationDAOImpl implements MetroStationDAO {
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public boolean isValidStation(int stationId) {
		if(getStation(stationId)!=null) {
			return true;
		}
		return false;
	}

	@Override
	public MetroStation getStation(int stationId) {
		MetroStation metroStation=null;
		String query="SELECT * FROM STATION WHERE stationId=?";
		try {
		metroStation=jdbcTemplate.queryForObject(query, new MetroStationRowMapper(), stationId);
		}
		catch(EmptyResultDataAccessException ex) {
			return metroStation;
		}
		return metroStation;
	}

	@Override
	public List<MetroStation> getAllStations() {
		String query="SELECT * FROM station";
		List<MetroStation> allStations=new ArrayList<>();
		allStations=jdbcTemplate.query(query, new MetroStationRowMapper());
		return allStations;
	}

	@Override
	public int getStationIdFromName(String stationName) {
		String query="SELECT * FROM station WHERE stationName=?";
		MetroStation metroStation=null;
		try {
			metroStation=jdbcTemplate.queryForObject(query, new MetroStationRowMapper(), stationName);
		}
		catch(EmptyResultDataAccessException ex) {
			return -1;
		}
		return metroStation.getStationId();
	}
	
}
