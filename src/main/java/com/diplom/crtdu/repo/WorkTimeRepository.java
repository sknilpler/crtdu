package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.WorkTime;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WorkTimeRepository extends CrudRepository<WorkTime, Long> {
    List<WorkTime> findByDayOfWeekAndTeacherId(String day, Long id);
}
