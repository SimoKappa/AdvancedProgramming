package it.univpm.hhc.model.dao;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.univpm.hhc.model.entities.User;

public interface UserDetailsDao {
	Session getSession();
	public void setSession(Session session);

	List <User> findAll();
	
	User findById(Long id);

	User create(String password,String email,String name,String surname);
	
	User update(User user);
	
	void delete(User contact);

	User findByEmail(String email);
	
	public String encryptPassword(String password);
	
	void setPasswordEncoder(PasswordEncoder passwordEncoder);
	
	PasswordEncoder getpasswordEncoder();
}