package com.group9.model.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.group9.bean.MetroCard;
import com.group9.model.persistence.helper.MetroCardRowMapper;

@Repository
public class MetroCardDAOImpl implements MetroCardDAO {
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public boolean addCard(MetroCard metroCard) {
		String query="INSERT INTO CARD values(?,?,?)";
		int rows = jdbcTemplate.update(query, metroCard.getCardID(), metroCard.getAadharID(), metroCard.getBalance());
		if(rows==0)
			return true;
		return false;
	}
	
	@Override
	public boolean isValidCard(long cardId) {
		if(searchCard(cardId)!=null)
			return true;
		return false;
	}


	@Override
	public MetroCard searchCard(long cardId) {
		MetroCard card=null;
		String query="SELECT * FROM CARD where CARDID=?";
		try {
			card=jdbcTemplate.queryForObject(query, new MetroCardRowMapper(), cardId);
		}
		catch(EmptyResultDataAccessException ex) {
			return card;
		}
		return card;
	}
	
	@Override
	public double getCardBalance(long cardId) {
		MetroCard newCard=searchCard(cardId);
		return newCard.getBalance();
	}

	@Override
	public boolean rechargeCard(long cardId, double money) {
		String query="UPDATE CARD SET BALANCE=BALANCE+? where CARDID=?";
		int rows = jdbcTemplate.update(query, money, cardId);
		if(rows==0)
			return true;
		return false;
	}
	
}

