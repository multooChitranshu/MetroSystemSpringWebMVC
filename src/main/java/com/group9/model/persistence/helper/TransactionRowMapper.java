package com.group9.model.persistence.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.group9.bean.Transaction;

public class TransactionRowMapper implements RowMapper<Transaction> {

	@Override
	public Transaction mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		long transactionId=resultSet.getLong(1);
		long cardID=resultSet.getLong(2);
		int sourceStationId=resultSet.getInt(3);
		LocalDateTime dateAndTimeOfBoarding=resultSet.getTimestamp(4).toLocalDateTime();
		int destinationStationId=resultSet.getInt(5);
		LocalDateTime dateAndTimeOfExit=null;
		if(resultSet.getTimestamp(6)!=null)
			dateAndTimeOfExit=resultSet.getTimestamp(6).toLocalDateTime();
	    double fare=resultSet.getDouble(7);
	    
	    Transaction transaction=new Transaction(transactionId,cardID,sourceStationId,dateAndTimeOfBoarding,
	    		destinationStationId,dateAndTimeOfExit,fare);
	    return transaction;
	}

}
