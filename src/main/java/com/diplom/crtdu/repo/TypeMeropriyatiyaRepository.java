package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.TypeMeropriyatiya;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeMeropriyatiyaRepository extends JpaRepository<TypeMeropriyatiya,Long> {

    List<TypeMeropriyatiya> findAllByOrderByName();

}
