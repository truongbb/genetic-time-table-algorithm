package com.github.truongbb.timetableschedule.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "AVAILABLE_TEACHING_DAY")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AvailableTeachingDay implements Serializable {

    @Id
    @Column(nullable = false)
    Integer id;

    @JoinColumn(name = "TEACHER_ID")
    @ManyToOne(targetEntity = Teacher.class)
    Teacher teacher;

    @Column(name = "AVAILABLE_DAY", nullable = false)
    Integer availableDay;

    public AvailableTeachingDay(Teacher teacher, Integer availableDay) {
        this.teacher = teacher;
        this.availableDay = availableDay;
    }

}
