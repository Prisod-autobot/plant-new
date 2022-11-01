package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
public class Pict {
	@Id
	@Column(name = "id")
	private Integer id;

	private String linkpict;

	@OneToOne
	@MapsId
	@JoinColumn(name = "id")
	private Plant plant;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Plant getPlant() {
		return plant;
	}

	public void setPlant(Plant plant) {
		this.plant = plant;
	}

	public String getLinkpict() {
		return linkpict;
	}

	public void setLinkpict(String linkpict) {
		this.linkpict = linkpict;
	}

}
