package com.github.truongbb.timetableschedule.controller;

import com.github.truongbb.timetableschedule.entity.Clazz;
import com.github.truongbb.timetableschedule.service.clazz.ClazzService;
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
@RequestMapping("/clazzes")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClazzController {

    ClazzService clazzService;

    @GetMapping
    public ResponseEntity<List<Clazz>> getAll() {
        return new ResponseEntity<>(clazzService.getAll(), HttpStatus.OK);
    }

}
