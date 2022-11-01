package com.example.demo.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Plant {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private String name;

	private String detail;

	@ManyToOne(optional = false)
	@JoinColumn(name = "typeplant_id")
	private TypePlant typeplant;

	public TypePlant getTypeplant() {
		return typeplant;
	}

	public void setTypeplant(TypePlant typeplant) {
		this.typeplant = typeplant;
	}

	@OneToOne(mappedBy = "plant", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Pict pict;


	public Pict getPict() {
		return pict;
	}

	public void setPict(Pict pict) {
		this.pict = pict;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
