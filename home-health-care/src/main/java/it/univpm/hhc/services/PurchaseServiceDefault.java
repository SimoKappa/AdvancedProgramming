package it.univpm.hhc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.univpm.hhc.model.dao.PurchaseDao;
import it.univpm.hhc.model.entities.Purchase;
import it.univpm.hhc.model.entities.User;

@Transactional
@Service("purchaseService")
public class PurchaseServiceDefault implements PurchaseService {
	
	PurchaseDao purchaseDao;

	@Override
	public List<Purchase> findAll() {
		return this.purchaseDao.findAll();
	}

	@Override
	public Purchase findById(Long id) {
		return this.purchaseDao.findById(id);
	}

	@Override
	public Purchase create(String pay_method, String date, double total, User user) {
		return this.purchaseDao.create(pay_method, date, total, user);
	}

	@Override
	public Purchase update(Purchase purchase) {
		return this.purchaseDao.update(purchase);
	}

	@Override
	public void delete(Long purchaseId) {
		Purchase purchase = this.purchaseDao.findById(purchaseId);
		this.purchaseDao.delete(purchase);
	}
	
	@Autowired
	public void setPurchaseDao(PurchaseDao purchaseDao) {
		this.purchaseDao = purchaseDao;
	}

}