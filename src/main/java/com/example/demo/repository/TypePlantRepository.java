package com.example.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TypePlant;

public interface TypePlantRepository extends JpaRepository<TypePlant, Integer> {

}
