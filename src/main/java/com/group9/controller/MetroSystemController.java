package com.group9.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.group9.bean.DisplayTransaction;
import com.group9.bean.MetroCard;
import com.group9.bean.MetroStation;
import com.group9.bean.Transaction;
import com.group9.model.service.MetroSystemService;

@Controller
public class MetroSystemController {
	
	@Autowired
	MetroSystemService metroServiceImpl;
	
	@ModelAttribute("allStations")
	public List<String> getAllStations(){
		return metroServiceImpl.getAllStations().stream()
		.map(MetroStation::getStationName).collect(Collectors.toList());
	}
	
	@RequestMapping("/swipeIn")
	public ModelAndView swipeInController() {
		return new ModelAndView("SwipeInInput");
	}
	
	@RequestMapping("/swipeIn/Input")
	public ModelAndView swipeInInputController(@RequestParam("stationName") String sourceStation, HttpSession session) {
		ModelAndView modelAndView=new ModelAndView();
		int sourceStationId=metroServiceImpl.getStationIdFromName(sourceStation);
		MetroCard card=(MetroCard)session.getAttribute("card");
		if(metroServiceImpl.swipeIn(card.getCardID(), sourceStationId)) {
			modelAndView.addObject("message","Swiped In successfully!");
			modelAndView.setViewName("SwipedInIndex");
		}
		else {
			modelAndView.addObject("message","Swipe In failed! Please try again...");
			modelAndView.setViewName("Index");
		}
		return modelAndView;
	}
	
	@RequestMapping("/swipeOut")
	public ModelAndView swipeOutController() {
		return new ModelAndView("SwipeOutInput");
	}
	
	@RequestMapping("/swipeOut/Input")
	public ModelAndView swipeOutInputController(@RequestParam("stationName") String destinationStation, HttpSession session) {
		ModelAndView modelAndView=new ModelAndView();
		int destinationStationId=metroServiceImpl.getStationIdFromName(destinationStation);
		MetroCard card=(MetroCard)session.getAttribute("card");
		String message=metroServiceImpl.swipeOut(card.getCardID(), destinationStationId);
		return new ModelAndView("Display","message", message);
	}	
	
	@RequestMapping("/checkBalance")
	public ModelAndView checkBalanceController(HttpSession session) {
		MetroCard card=(MetroCard)session.getAttribute("card");
		double bal=metroServiceImpl.cardBalance(card.getCardID());
		String message="Available balance : "+bal;
		return new ModelAndView("Display","message",message);
	}
	
	@RequestMapping("/rechargeCard")
	public ModelAndView rechargeCardController(HttpSession session) {
		MetroCard card=(MetroCard)session.getAttribute("card");
		double bal=metroServiceImpl.cardBalance(card.getCardID());
		String message="Your current balance is Rs."+bal;
		return new ModelAndView("DisplayBalanceRechargeCard","displayBalance", message);
	}
	@RequestMapping("/rechargeCard/update")
	public ModelAndView validateAmountAndRechargeCardController(@RequestParam double money,HttpSession session) {
		if(money>0) {
			MetroCard card=(MetroCard)session.getAttribute("card");
			metroServiceImpl.rechargeCard(card.getCardID(),money);
			String message="Rs."+money+" added successfully! Your current balance is Rs."+metroServiceImpl.cardBalance(card.getCardID());;
			return new ModelAndView("Display","message", message);
		}
		return new ModelAndView("DisplayBalanceRechargeCard","rechargeCard", "Recharge Failed! Please enter a positive amount...");
	}
	
	@RequestMapping("/showAllPreviousTransactions")
	public ModelAndView slowAllTransactionsController(HttpSession session) {
		MetroCard card=(MetroCard)session.getAttribute("card");
		List<Transaction> history=metroServiceImpl.transactionHistory(card.getCardID());
		if(history==null || history.isEmpty()) {
			return new ModelAndView("Display","message", "No previous transactions to show");
		}
		List<DisplayTransaction> displayableHistory=new ArrayList<>();
		int sno=0;
		for(Transaction tr:history){
			sno++;
			String destStationName=null;
			MetroStation destStation=metroServiceImpl.getStation(tr.getDestinationStationId());
			if(destStation!=null)
				destStationName=destStation.getStationName();
			displayableHistory.add(new DisplayTransaction(sno,
					metroServiceImpl.getStation(tr.getSourceStationId()).getStationName(),
					tr.getDateAndTimeOfBoarding(),
					destStationName,
					tr.getDateAndTimeOfExit(),tr.getFare()));
		}
		return new ModelAndView("ShowAllTransactions","allTransactions", displayableHistory);
	}
	
}
