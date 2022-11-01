package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Plant;

public interface PlantRepository extends CrudRepository<Plant, Integer> {
	List<Plant> findByName(String name);
}
