package it.univpm.hhc.model.dao;

import org.hibernate.Session;

import java.util.List;
import it.univpm.hhc.model.entities.Cart;
import it.univpm.hhc.model.entities.Item;
import it.univpm.hhc.model.entities.Purchase;
import it.univpm.hhc.model.entities.Cart_item;

public interface CartItemDao {

	Session getSession();
	
	public void setSession(Session session);
	
	Cart_item findById(Long id);
	
	List<Cart_item> findByCart (Cart cart);
	
	List<Cart_item> findAll();
	
	List<Cart_item> findByPurchase(Purchase purchase);
	
	Cart_item create(Cart cart, Item item, int quantity);
	
	Cart_item update(Cart_item cart_item);
	
	void delete(Cart_item cart_item);
	
	List<Cart_item> findByCart_item(Cart cart, Item item);
}

