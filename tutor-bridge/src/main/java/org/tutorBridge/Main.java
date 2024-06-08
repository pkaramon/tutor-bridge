package org.tutorBridge;

import org.tutorBridge.dao.TutorDao;
import org.tutorBridge.entities.Tutor;
import org.tutorBridge.service.StudentService;
import org.tutorBridge.service.TimeRange;
import org.tutorBridge.service.TutorService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        StudentService studentService = new StudentService();
        TutorService tutorService = new TutorService();
        TutorDao tutorDao = new TutorDao();

        Tutor tutor = tutorDao.findById(3L).orElseThrow();
        Map<DayOfWeek, List<TimeRange>> weekelyTimeRanges = new HashMap<>();

        weekelyTimeRanges.put(DayOfWeek.MONDAY, List.of(
                new TimeRange(LocalTime.of(9, 15), LocalTime.of(10, 15))
        ));


        tutorService.addWeeklyAvailability(tutor, weekelyTimeRanges, 4);



    }
}
