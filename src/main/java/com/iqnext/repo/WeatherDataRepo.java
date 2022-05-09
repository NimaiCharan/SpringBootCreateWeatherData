package com.iqnext.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iqnext.model.WeatherData;

@Repository
public interface WeatherDataRepo extends JpaRepository<WeatherData,Long> {

}
