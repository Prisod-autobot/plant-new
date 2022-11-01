package com.example.demo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "typeplant")
public class TypePlant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "typeplant_id")
	private Integer typeplant_id;

	@Column(length = 200, nullable = false, unique = true)
	private String name;

	@OneToMany(targetEntity = Plant.class, mappedBy = "typeplant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Plant> plant;

	public Integer getTypeplant_id() {
		return typeplant_id;
	}

	public void setTypeplant_id(Integer typeplant_id) {
		this.typeplant_id = typeplant_id;
	}

	public List<Plant> getPlant() {
		return plant;
	};

	public void setPlant(List<Plant> plant) {
		this.plant = plant;
	}

	public TypePlant() {
		super();
	}

	public TypePlant(String name2) {
		this.name = name2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
