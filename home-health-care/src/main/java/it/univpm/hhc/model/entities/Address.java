
package it.univpm.hhc.model.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address implements Serializable {
	
	private Long address_id;
	private int cap;
	private String city;
 	private String street;//via
	private int civ_num;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ADDRESS_ID")
	public Long getAddress_id() {
		return address_id;
	}
	public void setAddress_id(Long address_id) {
		this.address_id = address_id;
	}
	

	@Column(name = "CAP", nullable = false) 
	public int getCap() {
		return cap;
	}
	
	public void setCap(int cap) {
		this.cap = cap;
	}
	
	@Column(name = "CITY", nullable = false) 
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city= city;
	}
	
	@Column(name = "STREET", nullable = false) 
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street= street;
	
	}
	
	
	@Column(name = "CIV_NUM", nullable = false) 
	public int getCiv_num() {
		return civ_num;
	}
	
	public void setCiv_num(int civ_num) {
		this.civ_num= civ_num;
	
	}
	

}