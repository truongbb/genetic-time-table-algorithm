package com.github.truongbb.timetableschedule.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SUBJECTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subject implements Serializable {

    @Id
    @Column(nullable = false)
    Integer id;

    @Column(nullable = false)
    String name;

    @Column(name = "LAB_ID", nullable = false)
    @ManyToOne(targetEntity = Laboratory.class)
    Laboratory laboratory;

    @Column(name = "BLOCK_NUMBER")
    Integer blockNumber;

    @Column(name = "AVOID_LAST_LESSON")
    Boolean avoidLastLesson;

    @Column(name = "REQUIRE_SPACING")
    Boolean requireSpacing;

    @Column(name = "GROUP_NAME")
    String groupName;

    @Column(name = "PRIORITY_NUMBER")
    Integer priorityNumber;

}
