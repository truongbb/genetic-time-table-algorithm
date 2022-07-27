package com.github.truongbb.timetableschedule;

import com.github.truongbb.timetableschedule.schedule.TimeTableScheduler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@AllArgsConstructor
@SpringBootApplication
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableScheduleApplication implements CommandLineRunner {

    TimeTableScheduler timeTableScheduler;

    public static void main(String[] args) {
        SpringApplication.run(TimeTableScheduleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        timeTableScheduler.generateTimeTable();
    }

}
