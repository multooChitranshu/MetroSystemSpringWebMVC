package com.group9.model.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.group9.bean.Transaction;
import com.group9.model.persistence.helper.TransactionRowMapper;

@Repository
public class TransactionDAOImpl implements TransactionDAO {

	@Autowired
	MetroStationDAO metroStationDAOImpl;
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public long getLastTransactionId() {
		Transaction transaction=null;
		String query="SELECT * FROM TRANSACTION ORDER BY transactionId DESC LIMIT 1";
		try {
			transaction=jdbcTemplate.queryForObject(query, new TransactionRowMapper());
		}
		catch(EmptyResultDataAccessException ex) {
			return 0;
		}
		return transaction.getTransactionId();
	}
	
	@Override
	public boolean swipeIn(long transactionId,long cardId, int sourceStationId) {
		String query="INSERT INTO TRANSACTION(transactionId,cardId,sourceStationId,dateAndTimeOfBoarding) VALUES(?,?,?,?)";
		int rows=jdbcTemplate.update(query, transactionId, cardId, sourceStationId,java.sql.Timestamp.valueOf(LocalDateTime.now()));
		if(rows==1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean swipeOut(long cardId, int destinationStationId, double fare) {
		String query="UPDATE TRANSACTION "
				+ "SET destinationStationId=?,dateAndTimeOfExit=?,fare=? "
				+ "WHERE cardId=? AND dateAndTimeofExit is null";
		int rows=jdbcTemplate.update(query, destinationStationId, java.sql.Timestamp.valueOf(LocalDateTime.now()), fare, cardId);
		if(rows>=1) {
			return true;
		}
		return false;
	}

	@Override
	public List<Transaction> transactionHistory(long cardId) {
		List<Transaction> transactionList=new ArrayList<>();
		String query="SELECT * FROM TRANSACTION WHERE CARDID=? ORDER BY dateAndTimeOfBoarding DESC";
		transactionList=jdbcTemplate.query(query, new TransactionRowMapper(), cardId);
		return transactionList;
	}

	@Override
	public Transaction lastTransaction(long cardId) {
		
		Transaction lastTransac=null;
		String query="SELECT * FROM TRANSACTION WHERE CARDID=? ORDER BY dateAndTimeOfBoarding DESC LIMIT 1";
		try {
			lastTransac=jdbcTemplate.queryForObject(query, new TransactionRowMapper(), cardId);
		}
		catch(EmptyResultDataAccessException ex) {
			return lastTransac;
		}
		return lastTransac;
	}

}
