package com.github.truongbb.timetableschedule.controller;

import com.github.truongbb.timetableschedule.entity.Subject;
import com.github.truongbb.timetableschedule.service.subject.SubjectService;
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
@RequestMapping("/subjects")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectController {

    SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<Subject>> getAll() {
        return new ResponseEntity<>(subjectService.getAll(), HttpStatus.OK);
    }

}
