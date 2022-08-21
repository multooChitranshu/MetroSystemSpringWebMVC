package com.group9.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.group9.bean.MetroCard;
import com.group9.bean.Transaction;
import com.group9.model.service.MetroSystemService;

@Controller
public class LoginController {

	@Autowired
	MetroSystemService metroSystemServiceImpl;
	
	@RequestMapping("/")
	public ModelAndView loginCardController() {
		return new ModelAndView("Home");
	}
	
	@RequestMapping("/newCard")
	public ModelAndView newUserRegistrationController() {
		return new ModelAndView("NewCard");
	}
	
	@RequestMapping("/saveCard")
	public ModelAndView saveNewCardController(@RequestParam("aadharID") long aadharId, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		if(metroSystemServiceImpl.checkCard(aadharId)) {
			String message="Card ID "+aadharId+" already exists!";
			modelAndView.addObject("message", message);
			modelAndView.setViewName("NewCard");
			return modelAndView;
		}
		MetroCard card=new MetroCard(aadharId, aadharId, 100);
		metroSystemServiceImpl.addCard(card);
		session.setAttribute("card", card);
		modelAndView.setViewName("Index");
		return modelAndView;
	}
	
	@RequestMapping("/existingCard")
	public ModelAndView existingCardController() {
		return new ModelAndView("ExistingCard");
	}
	
	@RequestMapping("/login")
	public ModelAndView loginExistingCardController(@RequestParam("cardID") long cardId, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView();
		if(metroSystemServiceImpl.checkCard(cardId)) {
			Transaction lastTransaction=metroSystemServiceImpl.lastTransaction(cardId);
			if(lastTransaction==null || lastTransaction.getDateAndTimeOfExit()!=null) {
				modelAndView.setViewName("Index");
			}
			else if(lastTransaction.getDateAndTimeOfExit()==null) {
				modelAndView.setViewName("SwipedInIndex");
			}
			else {
				modelAndView.setViewName("Index");
			}
			session.setAttribute("card", metroSystemServiceImpl.getCardByID(cardId));
			return modelAndView;
		}
		String message="Card ID "+cardId+" does not exist! Please try again... ";
		modelAndView.addObject("message", message);
		modelAndView.setViewName("ExistingCard");
		return modelAndView;
	}
	
	@RequestMapping("/menuDecider")
	public ModelAndView menuDeciderController(HttpSession session) {
		MetroCard card=(MetroCard)session.getAttribute("card");
		long cardId=card.getCardID();
		ModelAndView modelAndView = new ModelAndView();
		Transaction lastTransaction=metroSystemServiceImpl.lastTransaction(cardId);
		if(lastTransaction==null || lastTransaction.getDateAndTimeOfExit()!=null) {
			modelAndView.setViewName("Index");
		}
		else if(lastTransaction.getDateAndTimeOfExit()==null) {
			modelAndView.setViewName("SwipedInIndex");
		}
		else {
			modelAndView.setViewName("Index");
		}
		return modelAndView;
		
	}
	
	@RequestMapping("/logout")
	public ModelAndView logoutController(HttpSession session) {
		session.invalidate();
		return new ModelAndView("Home");
	}
	
}
