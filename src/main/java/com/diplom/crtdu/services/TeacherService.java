package com.diplom.crtdu.services;

import com.diplom.crtdu.models.Kid;
import com.diplom.crtdu.models.KidZanyatie;
import com.diplom.crtdu.models.Zanyatie;
import com.diplom.crtdu.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TeacherService {
    @Autowired
    private KidRepository kidRepository;
    @Autowired
    private ZanyatieRepository zanyatieRepository;
    @Autowired
    private KidZanyatieRepository kidZanyatieRepository;
    @Autowired
    private KrujokRepository krujokRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    public Boolean apply(Kid kid, Zanyatie zanyatie) {
        if (kid == null) {
            System.out.println("kid is null");
            return false;
        }
        if (zanyatie == null) {
            System.out.println("zanyatie is null");
            return false;
        }
        KidZanyatie kz = kidZanyatieRepository.findByKidIdAndZanyatieId(kid.getId(), zanyatie.getId());
        if (kz == null) {
            System.out.println("kz is null");
            return false;
        }
        return kz.isPoseshchenie();
    }

    public KidZanyatie getKZ(Long k_id, Long z_id){
        System.out.println(k_id+" "+z_id);
        return kidZanyatieRepository.findByKidIdAndZanyatieId(k_id, z_id);
    }

    public boolean isMoreThan24Hours(Date date1, Date date2) {
        long differenceInMillis = Math.abs(date1.getTime() - date2.getTime());
        long hours = differenceInMillis / (60 * 60 * 1000); // Количество часов между датами

        return hours > 24;
    }
}
