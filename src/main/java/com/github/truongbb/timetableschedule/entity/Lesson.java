package com.github.truongbb.timetableschedule.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TIME_TABLE_CONFIG")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lesson implements Serializable {

    @Id
    @Column(nullable = false)
    Integer id;

    @JoinColumn(name = "TEACHER_ID")
    @ManyToOne(targetEntity = Teacher.class)
    Teacher teacher;

    @JoinColumn(name = "SUBJECT_ID")
    @ManyToOne(targetEntity = Subject.class)
    Subject subject;

    @JoinColumn(name = "CLAZZ_ID")
    @ManyToOne(targetEntity = Clazz.class)
    Clazz clazz;

    @Column(name = "LESSON_QUANTITY", nullable = false)
    Integer lessonQuantity;

    @Transient
    boolean isDuplicated;

    public Lesson(Teacher teacher, Subject subject, Clazz clazz, Integer lessonQuantity) {
        this.teacher = teacher;
        this.subject = subject;
        this.clazz = clazz;
        this.lessonQuantity = lessonQuantity;
    }

}
