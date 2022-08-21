package com.group9.model.persistence.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.group9.bean.MetroStation;

public class MetroStationRowMapper implements RowMapper<MetroStation>{

	@Override
	public MetroStation mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		int stationID=resultSet.getInt(1);
	    String stationName=resultSet.getString(2);
	    int previousStationId=resultSet.getInt(3);
	    int nextStationId=resultSet.getInt(4);
		return new MetroStation(stationID,stationName,previousStationId,nextStationId);
	}

}
