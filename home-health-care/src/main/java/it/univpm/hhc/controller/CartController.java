package it.univpm.hhc.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.univpm.hhc.model.entities.Cart;
import it.univpm.hhc.model.entities.Cart_item;
import it.univpm.hhc.services.CartItemService;
import it.univpm.hhc.services.CartService;

@RequestMapping("/carts")
@Controller
public class CartController {
	
	//Dovremo inserire di mostrare le info del carrello dell'utente loggato e la possibilità di visualizzare tutti gli item nel carrello, 
	//di eliminarne uno o più e modificare le quantità
	
	private final Logger logger = LoggerFactory.getLogger(CartController.class);
	
	
	
	private CartService cartService;
	private CartItemService cartItemService;
	
	//probabilmente mi servirà il getCarts di user per ottenere il suo carrello e da li ottengo il codice del carrello
	//anche se non so come passare l'id del carrello all'url
	@GetMapping(value = "/{cart_id}/list")//adesso fa vedere tutti i carrelli, ci servira poi un solo carrello da ritornare
	public String list(@PathVariable("cart_id") Long cart_id, Model uiModel) {
		logger.info("Listing carts");
		List<Cart_item> allItem = this.cartItemService.findAll();
		uiModel.addAttribute("items", allItem);  //quello che restituisco alla vista
		return "carts/list";
	}
	
	@GetMapping(value = "/{cart_Id}/delete")//occhio devo gestire la rimozione a cascata
	public String delete(@PathVariable("cartId") String cartId) {
		this.cartService.delete(new Long(cartId));
		
		return "redirect:/carts/list";
	}
	

	@GetMapping(value="/{cart_id}/edit")//occhio devo gestire la modifica a cascata
	public String edit(@PathVariable("cart_id") String cartId, 
			Model uiModel) {
		
		Cart c = this.cartService.findById(new Long(cartId));
		uiModel.addAttribute("cart", c);
		
		return "carts/form";
	}

	
	@PostMapping(value = "/save")
	public String save(@ModelAttribute("newCart") Cart newCart, BindingResult br) {
		this.cartService.update(newCart);
		
		return "redirect:/carts/list/";
		
		// return "redirect:singers/list"; // NB questo non funzionerebbe!
		
	}
	
	@Autowired
	public void setCartService(CartService cartService) {
		this.cartService = cartService;
	}
	
	@Autowired
	public void setCartItemService(CartItemService cartItemService) {
		this.cartItemService = cartItemService;
	}
}

