package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Uchastnik;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UchastnikRepository extends CrudRepository<Uchastnik,Long> {

    List<Uchastnik> findByMeropriyatieId(Long id);
    List<Uchastnik> findByKidId(Long id);
    List<Uchastnik> findByKrujokId(Long id);

}
