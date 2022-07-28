package com.github.truongbb.timetableschedule.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class LessonKey implements Comparable<LessonKey> {

    int day;
    int order;

    @Override
    public int compareTo(LessonKey o) {
        return this.day - o.day;
    }
}
