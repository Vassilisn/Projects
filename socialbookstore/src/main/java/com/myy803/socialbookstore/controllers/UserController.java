package com.myy803.socialbookstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myy803.socialbookstore.formsdata.BookFormData;
import com.myy803.socialbookstore.formsdata.SearchFormData;
import com.myy803.socialbookstore.formsdata.UserProfileFormData;
import com.myy803.socialbookstore.services.UserProfileService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {
	
	@Autowired
	private UserProfileService userProfileService;
	
	private String currentUsername;
	
	@RequestMapping("/user/main")
	public String getUserMainMenu(Model model) {
		currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("profile", userProfileService.retrieveProfile(currentUsername));
		return "user/main";
	}
	
	@RequestMapping("/user/edit-profile")
	public String editUserProfile(Model model) {
		currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		
		UserProfileFormData formData = userProfileService.retrieveProfile(currentUsername);
		
		model.addAttribute("user", formData);
		return "user/edit";
	}
	
	@RequestMapping("/user/{username}")
	public String retrieveProfile(Model model, @PathVariable("username") String username) {
		model.addAttribute("user", userProfileService.retrieveProfile(username));
		
		return "user/profile";
	}
	
	@RequestMapping("/user/saveProfile")
	public String saveProfile(@ModelAttribute("user") UserProfileFormData userProfileFormData, Model model) {
		userProfileService.save(userProfileFormData);
		return "redirect:/user/main";
	}
	
	@RequestMapping("/user/book-offers")
	public String listBookOffers (Model model) {
		currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("books", userProfileService.retrieveBookOffers(currentUsername));
		
		return "book/offers";
	}
	
	@RequestMapping("/user/create-offer")
	public String showOfferForm(Model model) {
		model.addAttribute("offer", new BookFormData());
		return "book/create";
	}
	
	@RequestMapping("/user/save-offer")
	public String saveOffer (@ModelAttribute("offer") BookFormData bookFormData, Model model) {
		currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		userProfileService.addBookOffer(currentUsername, bookFormData);
		
		return "redirect:/user/book-offers";
	}
	
	@RequestMapping("/search/form")
	public String showSearchForm(Model model) {
		model.addAttribute("search", new SearchFormData());
		
		return "search/form";
	}

	@GetMapping("/search")
	public String search(@ModelAttribute("search") SearchFormData searchFormData, Model model) {
		model.addAttribute("books", userProfileService.searchBooks(searchFormData));
		
		return "search/results";
	}
			
	@RequestMapping("/user/request-book/{bookid}")
	public String requestBook(@PathVariable("bookid") int bookid, Model model, HttpServletRequest request) {
		currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		userProfileService.requestBook(bookid, currentUsername);
		System.out.println("Requests done");
		return "redirect:" + request.getHeader("Referer");
	}
	
	@RequestMapping("/user/requests")
	public String showUserBookRequests(Model model) {
		currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("requests", userProfileService.retrieveBookRequests(currentUsername));
		return "user/requests";
	}
	
	@RequestMapping("/book/{id}/requests")
	public String showRequestingUsersForBookOffer(@PathVariable("id") int bookId, Model model) {
		model.addAttribute("users", userProfileService.retrieveRequestingUsers(bookId));
		model.addAttribute("usernameAcc", userProfileService.findBookById(bookId).getAcceptedUsername());
		
		return "book/requests";
	}
	
	@RequestMapping("/book/{id}/requests/{username}/accept")
	public String acceptRequest(@PathVariable("username") String username, @PathVariable("id") int bookid, Model model) {
		userProfileService.acceptBookRequest(username, bookid);
		return "redirect:/book/" + bookid + "/requests";
	}
	
	@RequestMapping("/book/{id}/delete")
	public String deleteBookOffer(@PathVariable("id") int bookId, Model model) {
		userProfileService.deleteBookOffer(currentUsername, bookId);
		
		return "redirect:/user/book-offers";
	}	
}
