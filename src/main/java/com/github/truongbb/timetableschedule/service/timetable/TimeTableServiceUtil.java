package com.github.truongbb.timetableschedule.service.timetable;

import com.github.truongbb.timetableschedule.constant.TimeTableConstants;
import com.github.truongbb.timetableschedule.dto.LessonKey;
import com.github.truongbb.timetableschedule.dto.OffLessonConfig;
import com.github.truongbb.timetableschedule.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimeTableServiceUtil {

    public Teacher findTeacherById(List<Teacher> teachers, Integer teacherId) {
        return teachers.stream().filter(t -> t.getId().equals(teacherId)).findFirst().orElse(null);
    }

    public Subject findSubjectById(List<Subject> subjects, Integer subjectId) {
        return subjects.stream().filter(s -> s.getId().equals(subjectId)).findFirst().orElse(null);
    }

    public Clazz findClazzById(List<Clazz> clazzes, Integer clazzId) {
        return clazzes.stream().filter(c -> c.getId().equals(clazzId)).findFirst().orElse(null);
    }

    public boolean hasOffLesson(List<Lesson> waitingTimeTables, String grade) {
        return waitingTimeTables.stream().anyMatch(l -> l.getClazz().getGrade().equals(grade)
                && l.getSubject().getName().equalsIgnoreCase(TimeTableConstants.OFF_LESSON));
    }

    public boolean isCCOrSH(int day, int order) {
        return (day == TimeTableConstants.FIRST_DAY && order == TimeTableConstants.FIRST_ORDER) ||
                (day == TimeTableConstants.LAST_DAY && order == TimeTableConstants.LAST_ORDER);
    }

    public Lesson findLessonByKeyAndClass(Map<LessonKey, List<Lesson>> timeTables, int day, int order, String className) {
        return this.findByClassName(timeTables.get(new LessonKey(day, order)), className);
    }

    public Lesson findByClassName(List<Lesson> lessons, String className) {
        if (CollectionUtils.isEmpty(lessons) || StringUtils.isEmpty(className)) {
            return null;
        }
        return lessons.stream().filter(lesson -> lesson.getClazz().getName().equals(className)).findFirst().orElse(null);
    }

    public Teacher findHeadTeacher(List<Teacher> teachers, String name) {
        return teachers.stream().filter(t -> !ObjectUtils.isEmpty(t.getHeadClazz()) && t.getHeadClazz().getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Subject getStaticSubject(List<Subject> subjects, String name) {
        return subjects.stream().filter(sub -> sub.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public LessonKey findFirstReplacement(Map<String, List<OffLessonConfig>> offLessonConfig, Map<LessonKey, List<Lesson>> timeTables, int replaceDay, int replaceOrder, Lesson replacedLesson) {
        Clazz clazz = replacedLesson.getClazz();
        Teacher busyTeacher = replacedLesson.getTeacher();
        for (int day = TimeTableConstants.FIRST_DAY; day <= TimeTableConstants.LAST_DAY; day++) {
            for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) {
                if (this.isCCOrSH(day, order)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                    continue;
                }
                if (day == replaceDay && order == replaceOrder) {
                    continue;
                }
                Lesson lesson = this.findLessonByKeyAndClass(timeTables, day, order, clazz.getName());
                if (ObjectUtils.isEmpty(lesson)) {
                    return null;
                }

                if ((lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && !this.isFitOffLesson(offLessonConfig, replaceDay, replaceOrder, lesson))
                        || replacedLesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && !this.isFitOffLesson(offLessonConfig, day, order, replacedLesson)) {
                    continue;
                }

                // nếu có giáo viên và giáo viên đó có thể dạy (không vướng lịch bận của giáo viên, không trùng vào tiết sinh hoạt, chào cờ, ...)
                // và ngược lại giáo viên hôm nay đảo sang hôm đó cũng không bị trùng lịch
                if (!this.isTeacherBusy(timeTables, replaceDay, replaceOrder, clazz, lesson.getTeacher())
                        && !this.isTeacherBusy(timeTables, day, order, clazz, busyTeacher)) {
                    return new LessonKey(day, order);
                }
            }
        }
        return null;
    }

    public boolean isFitOffLesson(Map<String, List<OffLessonConfig>> offLessonConfig, int day, int order, Lesson lesson) {
        if (!lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON)) {
            return false;
        }
        if (ObjectUtils.isEmpty(offLessonConfig) || offLessonConfig.isEmpty()) {
            return lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && order == TimeTableConstants.LAST_ORDER;
        }

        List<OffLessonConfig> offLessonConfigs = offLessonConfig.get(lesson.getClazz().getGrade());
        if (CollectionUtils.isEmpty(offLessonConfigs)) {
            return false;
        }
        return offLessonConfigs.stream().anyMatch(c -> c.getOffDay() == day && c.getOffOrder() == order);
    }


    public boolean isTeacherBusy(Map<LessonKey, List<Lesson>> timeTables, int day, int order, Clazz clazz, Teacher teacher) {
        // check giáo viên bận (cùng ngày đó, tiết đó mà giáo viên này phải dạy ở 2 hoặc nhiều lớp khác nhau thì gọi là bận)
        return timeTables
                .get(new LessonKey(day, order))
                .stream()
                .anyMatch(lesson ->
                        !ObjectUtils.isEmpty(lesson.getClazz())
                                && !ObjectUtils.isEmpty(lesson.getTeacher())
                                && !lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON)
                                && !lesson.getClazz().getId().equals(clazz.getId())
                                && lesson.getTeacher().getId().equals(teacher.getId())
                );
    }

    public boolean checkBlockSubject(Map<LessonKey, List<Lesson>> timeTables, Lesson lesson, int day, int order, int index) {
        if (lesson.getSubject().getBlockNumber() != 2) {
            return false;
        }
        boolean checkSub1 = false;
        boolean checkSub2 = false;
        boolean checkSub3 = false;
        boolean checkSub4 = false;
        if (order < 3) {
            checkSub1 = checkAdjacentLessonBeforeReplace(timeTables, day, order + 1, lesson, index);
            checkSub2 = checkAdjacentLessonBeforeReplace(timeTables, day, order + 2, lesson, index);

        } else {
            checkSub1 = checkAdjacentLessonBeforeReplace(timeTables, day, order - 1, lesson, index);
            checkSub2 = checkAdjacentLessonBeforeReplace(timeTables, day, order - 2, lesson, index);
        }
        if (checkSub1 && !checkSub2) {
            return true;
        }
        if (order != 1 && order != 5) {
            checkSub3 = checkAdjacentLessonBeforeReplace(timeTables, day, order - 1, lesson, index);
            checkSub4 = checkAdjacentLessonBeforeReplace(timeTables, day, order + 1, lesson, index);
        }
        if ((checkSub3 && !checkSub4) || (!checkSub3 && checkSub4)) {
            if (checkSub1 && checkSub2) {
                return false;
            }
            if (!checkSub1 && checkSub2) {
                return true;
            }
        }
        return false;
    }

    public Map<LessonKey, List<Lesson>> saveBestResult(Map<LessonKey, List<Lesson>> data) {
        if (ObjectUtils.isEmpty(data) || data.isEmpty()) {
            return null;
        }
        return data.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public boolean checkTripleLessonInTheSameDay(Map<LessonKey, List<Lesson>> timeTables, Lesson lesson, int day, int order) {
        String subName = lesson.getSubject().getName();
        String mainSubjName = TimeTableConstants.MAIN_LESSONS.stream().filter(l -> l.equalsIgnoreCase(subName) || l.startsWith(subName)).findFirst().orElse(null);
        if (StringUtils.isEmpty(mainSubjName)) {
            return false;
        }
        int count = 0;
        for (int reporder = TimeTableConstants.FIRST_ORDER; reporder <= TimeTableConstants.LAST_ORDER; reporder++) {
            if (reporder == order) {
                continue;
            }
            Lesson tempLesson = this.findLessonByKeyAndClass(timeTables, day, reporder, lesson.getClazz().getName());
            if ((tempLesson.getSubject().getName().startsWith(mainSubjName) && lesson.getSubject().getName().startsWith(mainSubjName))
                    || (tempLesson.getSubject().getName().equalsIgnoreCase(mainSubjName) && lesson.getSubject().getName().equalsIgnoreCase(mainSubjName))) {
                count++;
            }
        }
        return count >= 2;
    }

    public boolean checkAdjacentLessonBeforeReplace(Map<LessonKey, List<Lesson>> timeTables, int day, int order, Lesson lesson, int k) {
        Lesson lesson1 = timeTables.get(new LessonKey(day, order)).get(k);
        return lesson.getSubject().getName().equals(lesson1.getSubject().getName());
    }

    public boolean checkAdjacentLesson(int day, int order, Lesson lesson, Map<LessonKey, List<Lesson>> timeTables) {
        int start, end;
        if (day == TimeTableConstants.FIRST_DAY) {
            start = day;
            end = day + 1;
        } else if (day == TimeTableConstants.LAST_DAY) {
            start = day - 1;
            end = day;
        } else {
            start = day - 1;
            end = day + 1;
        }
        for (int repDay = start; repDay <= end; repDay++) {
            for (int repOrder = TimeTableConstants.FIRST_ORDER; repOrder <= TimeTableConstants.LAST_ORDER; repOrder++) {
                if (repDay == day && repOrder == order) {
                    continue;
                }
                Lesson tempLesson = this.findLessonByKeyAndClass(timeTables, repDay, repOrder, lesson.getClazz().getName());
                if (ObjectUtils.isEmpty(tempLesson)) {
                    continue;
                }
                if (tempLesson.getSubject().getId().equals(lesson.getSubject().getId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<LessonKey> findAllReplacement(int replacedDay, int replacedOrder, Lesson replacedLesson,
                                              List<AvailableTeachingDay> availableTeachingDay,
                                              Map<LessonKey, List<Lesson>> timeTables,
                                              Map<String, List<OffLessonConfig>> offLessonConfig) {
        Clazz clazz = replacedLesson.getClazz();
        Teacher busyTeacher = replacedLesson.getTeacher();
        List<LessonKey> result = null;
        for (int day = TimeTableConstants.FIRST_DAY; day <= TimeTableConstants.LAST_DAY; day++) {
            for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) {
                Lesson tempLesson = this.findLessonByKeyAndClass(timeTables, day, order, replacedLesson.getClazz().getName());

                if (this.isIgnoreCases(day, order, replacedLesson, replacedDay, replacedOrder, tempLesson, availableTeachingDay, timeTables, offLessonConfig)) {
                    continue;
                }

                // nếu có giáo viên và giáo viên đó có thể dạy (không vướng lịch bận của giáo viên, không trùng vào tiết sinh hoạt, chào cờ, ...)
                // và ngược lại giáo viên hôm nay đảo sang hôm đó cũng không bị trùng lịch
                if (!this.isTeacherBusy(timeTables, replacedDay, replacedOrder, clazz, tempLesson.getTeacher())
                        && !this.isTeacherBusy(timeTables, day, order, clazz, busyTeacher)) {
                    if (CollectionUtils.isEmpty(result)) {
                        result = new ArrayList<>();
                    }
                    result.add(new LessonKey(day, order));
                }
            }
        }
        return result;
    }

    public boolean isIgnoreCases(int day, int order, Lesson replacedLesson, int replacedDay, int replacedOrder,
                                 Lesson tempLesson, List<AvailableTeachingDay> availableTeachingDay,
                                 Map<LessonKey, List<Lesson>> timeTables,
                                 Map<String, List<OffLessonConfig>> offLessonConfig) {
        if (this.isCCOrSH(day, order)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
            return true;
        }

        if (replacedDay == day && order == replacedOrder) {
            return true;
        }
        String subjectName = replacedLesson.getSubject().getName();
        if (ObjectUtils.isEmpty(tempLesson)) {
            return true;
        }

        // không đổi chỗ 2 môn giống nhau
        if (tempLesson.getSubject().getName().equals(subjectName)) {
            return true;
        }


        // tránh xếp lịch cho những gv thỉnh giảng dạy vào những ngày gv đó không đi dạy được
        if (this.checkVisitingTeacherSchedule(availableTeachingDay, replacedLesson, day) || this.checkVisitingTeacherSchedule(availableTeachingDay, tempLesson, replacedDay)) {
            return true;
        }

        // tránh gv có con nhỏ hoặc nhà xa dạy tiết 1
        if (this.hasChildrenOrFarFromHome(tempLesson, order, replacedLesson, replacedOrder)) {
            return true;
        }

        // môn sinh, địa, thể dục, ... không học 2 ngày liên tiếp
        if ((tempLesson.getSubject().getRequireSpacing() && this.checkAdjacentLesson(replacedDay, replacedOrder, tempLesson, timeTables))
                || (replacedLesson.getSubject().getRequireSpacing() && this.checkAdjacentLesson(day, order, replacedLesson, timeTables))) {
            return true;
        }

        // môn học tránh tiết cuối
        if (this.isAvoidLastLessonSubject(tempLesson, order, replacedLesson, replacedOrder)) {
            return true;
        }

        // tiết nghỉ theo cấu hình người dùng hoặc phải là tiết cuối của các buổi
        if ((tempLesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && !this.isFitOffLesson(offLessonConfig, replacedDay, replacedOrder, tempLesson))
                || replacedLesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && !this.isFitOffLesson(offLessonConfig, day, order, replacedLesson)) {
            return true;
        }

        return false;
    }

    public boolean hasChildrenOrFarFromHome(Lesson lesson, int order, Lesson replacedLesson, int replacedOrder) {
        return (replacedOrder == TimeTableConstants.FIRST_ORDER && (lesson.getTeacher().getHasChildren() || lesson.getTeacher().getHasFarfromHome()))
                || (order == TimeTableConstants.FIRST_ORDER && (replacedLesson.getTeacher().getHasChildren() || replacedLesson.getTeacher().getHasFarfromHome()));
    }

    public boolean isAvoidLastLessonSubject(Lesson lesson, int order, Lesson replacedLesson, int replacedOrder) {
        return (replacedLesson.getSubject().getAvoidLastLesson() && order == TimeTableConstants.LAST_ORDER)
                || (lesson.getSubject().getAvoidLastLesson() && replacedOrder == TimeTableConstants.LAST_ORDER);
    }

    public boolean checkVisitingTeacherSchedule(List<AvailableTeachingDay> availableTeachingDay, Lesson lesson, int day) {
        List<AvailableTeachingDay> availableTeachingDays = this.findAvailableTeachingDays(availableTeachingDay, lesson.getTeacher());
        boolean canTeachFullWeek = CollectionUtils.isEmpty(availableTeachingDays);
        boolean canTeachInDay = false;
        if (!canTeachFullWeek) {
            canTeachInDay = availableTeachingDays.stream().anyMatch(d -> d.getAvailableDay() == day);
        }
        return !canTeachFullWeek && !canTeachInDay;
    }

    public List<AvailableTeachingDay> findAvailableTeachingDays(List<AvailableTeachingDay> availableTeachingDay, Teacher teacher) {
        return availableTeachingDay
                .stream()
                .filter(t -> t.getTeacher().getId().equals(teacher.getId()))
                .collect(Collectors.toList());
    }

    public void showOutput(List<Clazz> clazzes, Map<LessonKey, List<Lesson>> mapData) {
        clazzes.forEach(c -> System.out.print("\t\t\t\t\t\t\t " + c.getName() + " "));
        System.out.println();
        mapData
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    LessonKey lessonKey = entry.getKey();
                    List<Lesson> lessons = entry.getValue();
                    lessons = lessons
                            .stream()
                            .sorted(Comparator.comparing(l -> l.getClazz().getName()))
                            .collect(Collectors.toList());
                    System.out.print("Thứ " + lessonKey.getDay() + ", tiết " + lessonKey.getOrder() + "\t\t");
                    lessons.forEach(l -> {
                        if (ObjectUtils.isEmpty(l.getTeacher())) {
                            System.out.printf("%-7s - %-12s\t\t\t\t|\t", "NULL", l.getSubject().getName());
                            return;
                        }
                        System.out.printf("%-7s - %-12s\t\t\t\t|\t", l.getTeacher().getName(), l.getSubject().getName());
                    });
                    System.out.println();
                });
    }

}
