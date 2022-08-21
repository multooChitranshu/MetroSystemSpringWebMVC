package com.group9.model.persistence.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.group9.bean.MetroCard;

public class MetroCardRowMapper implements RowMapper<MetroCard> {

	@Override
	public MetroCard mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		long id = resultSet.getLong("cardId");
		long aadhar = resultSet.getLong("aadharId");
		double balance = resultSet.getDouble("balance");

		return new MetroCard(id, aadhar,balance);
	}

}
