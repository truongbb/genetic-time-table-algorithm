package com.github.truongbb.timetableschedule.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "AVAILABLE_TEACHING_DAY")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@IdClass(AvailableTeachingDayPk.class)
public class AvailableTeachingDay implements Serializable {

    @Id
    @Column(name = "TEACHER_ID", nullable = false)
    Long teacherId;

    @Id
    @Column(name = "AVAILABLE_DAY", nullable = false)
    Integer availableDay;

}
