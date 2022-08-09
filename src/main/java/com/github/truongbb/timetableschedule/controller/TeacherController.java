package com.github.truongbb.timetableschedule.controller;

import com.github.truongbb.timetableschedule.entity.Teacher;
import com.github.truongbb.timetableschedule.service.teacher.TeacherService;
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
@RequestMapping("/teachers")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeacherController {

    TeacherService teacherService;

    @GetMapping
    public ResponseEntity<List<Teacher>> getAll() {
        return new ResponseEntity<>(teacherService.getAll(), HttpStatus.OK);
    }

}
