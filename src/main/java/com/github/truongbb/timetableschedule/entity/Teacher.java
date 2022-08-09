package com.github.truongbb.timetableschedule.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "TEACHERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Teacher implements Serializable {

    @Id
    @Column(nullable = false)
    Integer id;

    @Column(nullable = false)
    String name;

    @Column(name = "HAS_CHILDREN")
    Boolean hasChildren;

    @Column(name = "SKIP_DAYS")
    String skipDays;

    @JoinColumn(name = "HEAD_CLAZZ_ID")
    @ManyToOne(targetEntity = Clazz.class)
    Clazz headClazz;

    @Column(name = "HAS_FARFROM_HOME")
    Boolean hasFarfromHome;

//    @OneToMany(targetEntity = AvailableTeachingDay.class, fetch = FetchType.EAGER)
//    List<AvailableTeachingDay> teachingDays;

}
