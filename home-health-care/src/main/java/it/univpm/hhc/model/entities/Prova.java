package it.univpm.hhc.model.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="prova")
public class Prova implements Serializable {

	private Long provaId;
	private String title;
	private String description;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROVA_ID")
	public Long getProvaId() {
		return this.provaId;
	}
	public void setProvaId(Long provaId){
		this.provaId= provaId;
	}

	@Column(name ="TITLE")
	public String getTitle() {
		return this.title;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
