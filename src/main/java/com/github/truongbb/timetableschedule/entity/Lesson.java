package com.github.truongbb.timetableschedule.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TIME_TABLE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lesson implements Serializable {

    @Id
    @Column(nullable = false)
    Integer id;

    @Column(nullable = false)
    @ManyToOne(targetEntity = Teacher.class)
    Teacher teacher;

    @Column(nullable = false)
    @ManyToOne(targetEntity = Subject.class)
    Subject subject;

    @Column(nullable = false)
    @ManyToOne(targetEntity = Clazz.class)
    Clazz clazz;

    @Column(name = "LESSON_QUANTITY", nullable = false)
    Integer lessonQuantity;

    @Transient
    boolean isTeacherBusy;

    @Transient
    boolean isStatic;

}
