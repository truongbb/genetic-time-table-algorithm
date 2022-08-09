package com.github.truongbb.timetableschedule.controller;

import com.github.truongbb.timetableschedule.service.timetableconfig.TimeTableConfigService;
import com.github.truongbb.timetableschedule.vm.LessonVm;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/time-table-config")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TimeTableConfigController {

    TimeTableConfigService timeTableConfigService;

    @GetMapping
    public ResponseEntity<List<LessonVm>> getAllConfig() {
        return new ResponseEntity<>(timeTableConfigService.getAll(), HttpStatus.OK);
    }

}
