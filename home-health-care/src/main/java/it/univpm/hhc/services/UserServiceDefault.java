package it.univpm.hhc.services;


import java.util.List;

import org.mockito.internal.stubbing.answers.ThrowsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univpm.hhc.model.dao.UserDao;
import it.univpm.hhc.model.entities.User;
//con transactional abbiamo la gestione automatica delle transazioni, con service indichiamo che stiamo facendo un servizio
@Transactional
@Service("userService")
public class UserServiceDefault implements UserService, UserDetailsService {
    
	@Autowired
	UserDao userDao;
	
	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
		User user = userDao.findByEmail(email);
		UserBuilder builder = null;
		if(user != null ) {
			builder = org.springframework.security.core.userdetails.User.withUsername(email);
			builder.disabled(!user.isEnabled());
			builder.password(user.getPassword());
			
			String role = null;
			try {
					if(user.getRole()) {
						role = "admin";
			}
					else if(!user.getRole()) {
						role = "user";
					}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			builder.roles(role);
		} else {
			throw new UsernameNotFoundException("Utente non trovato.");
		}
		return builder.build();
	}
	
	
	
	@Override
	public List<User> findAll() {
		return this.userDao.findAll();
	}

	@Override
	public User findById(Long id) {
		return this.userDao.findById(id);
	}

	@Override
	public User create(String password,String email,String name,String surname) {
		return this.userDao.create( password, email,name,surname);
	}
	
	@Override
	public User update(User user) {
		return this.userDao.update(user);
	}

	@Override
	public void delete(Long userId) {
		User user= this.userDao.findById(userId);
		this.userDao.delete(user);

	}
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}

