package com.github.truongbb.timetableschedule.service.timetable;

import com.github.truongbb.timetableschedule.constant.StaticSubject;
import com.github.truongbb.timetableschedule.constant.TimeTableConstants;
import com.github.truongbb.timetableschedule.dto.LessonKey;
import com.github.truongbb.timetableschedule.entity.Clazz;
import com.github.truongbb.timetableschedule.entity.Lesson;
import com.github.truongbb.timetableschedule.entity.Subject;
import com.github.truongbb.timetableschedule.entity.Teacher;
import com.github.truongbb.timetableschedule.vm.LessonVm;
import com.github.truongbb.timetableschedule.vm.TimeTableVm;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeTableServiceImpl implements TimeTableService {

    List<Clazz> clazzes;
    List<Teacher> teachers;
    List<Subject> subjects;
    List<LessonVm> waitingTimeTableTmp;
    List<Lesson> waitingTimeTables;

    Map<LessonKey, List<Lesson>> timeTables; // các tiết học kết quả
    Map<LessonKey, List<Lesson>> bestResultsTimeTable;


    @Override
    public Map<LessonKey, List<Lesson>> generateTable(TimeTableVm timeTableVm) throws IllegalArgumentException {
        this.initData(timeTableVm);
        if (!this.validateInputData()) {
            throw new IllegalArgumentException();
        }

        this.prepareData();
        this.generateBase();
        this.showOutput(this.timeTables);

        this.evolutionToCorrect();
        this.showOutput(this.timeTables);

        this.fineTuning(0, 3);
        this.showOutput(this.bestResultsTimeTable);

        return this.bestResultsTimeTable;
    }

    private void initData(TimeTableVm timeTableVm) {
        this.teachers = timeTableVm.getTeachers();
        this.subjects = timeTableVm.getSubjects();
        this.clazzes = timeTableVm.getClazzes();
        this.waitingTimeTableTmp = timeTableVm.getConfigTimeTable();
    }

    private boolean validateInputData() throws IllegalArgumentException {
        if (CollectionUtils.isEmpty(this.subjects) || CollectionUtils.isEmpty(this.clazzes) || CollectionUtils.isEmpty(this.teachers) || CollectionUtils.isEmpty(this.waitingTimeTableTmp)) {
            return false;
        }

        for (int i = 0; i < this.waitingTimeTableTmp.size(); i++) {
            LessonVm lessonVm = this.waitingTimeTableTmp.get(i);

            Teacher teacher = this.findTeacherById(lessonVm.getTeacherId());
            if (!ObjectUtils.isEmpty(teacher)) {
                throw new IllegalArgumentException();
            }

            Subject subject = this.findSubjectById(lessonVm.getSubjectId());
            if (!ObjectUtils.isEmpty(subject)) {
                throw new IllegalArgumentException();
            }

            Clazz clazz = this.findClazzById(lessonVm.getClazzId());
            if (!ObjectUtils.isEmpty(clazz)) {
                throw new IllegalArgumentException();
            }
            this.waitingTimeTables.add(new Lesson(teacher, subject, clazz, lessonVm.getLessonQuantity()));
        }

        return true;
    }

    private Teacher findTeacherById(Integer teacherId) {
        return this.teachers.stream().filter(t -> t.getId().equals(teacherId)).findFirst().orElse(null);
    }

    private Subject findSubjectById(Integer subjectId) {
        return this.subjects.stream().filter(s -> s.getId().equals(subjectId)).findFirst().orElse(null);
    }

    private Clazz findClazzById(Integer clazzId) {
        return this.clazzes.stream().filter(c -> c.getId().equals(clazzId)).findFirst().orElse(null);
    }

    /**
     * 0. chuẩn bị dữ liệu
     */
    private void prepareData() {

        this.waitingTimeTables.forEach(lesson -> {
            if (ObjectUtils.isEmpty(lesson.getTeacher())) {
                lesson.setStatic(false);
            }
        });

        // khởi tạo timeTables là danh sách rỗng
        timeTables = new HashMap<>();
        for (int day = TimeTableConstants.FIRST_DAY; day <= TimeTableConstants.LAST_DAY; day++) { // từ thứ 2 tới thứ bảy
            for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) { // từ tiết 1 tới tiết 5
                LessonKey lessonKey = new LessonKey(day, order);
                List<Lesson> lessons = timeTables.get(lessonKey);
                if (!CollectionUtils.isEmpty(lessons)) {
                    continue;
                }
                timeTables.put(lessonKey, new ArrayList<>());
            }
        }
    }

    /**
     * 1. Tạo thời khóa biểu cơ sở (quần thể cơ sở): chỉ cần đủ các tiết, giáo viên, đủ giờ dạy, không quan tâm tới các yếu tố trùng tiết và yếu tố làm tốt
     */
    private void generateBase() {

        // đặt tiết chào cờ đầu tuần và tiết sinh hoạt cuối tuần
        this.setStaticLesson();

//        for (int i = 1; i <= 30; i++) {
        for (int day = TimeTableConstants.FIRST_DAY; day <= TimeTableConstants.LAST_DAY; day++) { // từ thứ 2 tới thứ bảy
            for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) { // từ tiết 1 tới tiết 5
                if ((day == TimeTableConstants.FIRST_DAY && order == TimeTableConstants.FIRST_ORDER) ||
                        (day == TimeTableConstants.LAST_DAY && order == TimeTableConstants.LAST_ORDER)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                    continue;
                }
                for (int j = 0; j < this.clazzes.size(); j++) {
                    Clazz currentClazz = this.clazzes.get(j);

                    for (int k = 0; k < this.waitingTimeTables.size(); k++) {
                        Lesson temp = new Lesson();
                        Lesson lesson = this.waitingTimeTables.get(k);

                        if (currentClazz.getName().startsWith("6")) {
                            if ((day == 3 && order == 5) || (day == 5 && order == 5) || (day == 6 && order == 5)) {
                                if (!lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON)) {
                                    continue;
                                }
                            }
                        }

                        if (currentClazz.getName().startsWith("7")) {
                            if ((day == 3 && order == 5) || (day == 5 && order == 5)) {
                                if (!lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON)) {
                                    continue;
                                }
                            }
                        }
                        if (currentClazz.getName().startsWith("9")) {
                            if ((day == 5 && order == 5)) {
                                if (!lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON)) {
                                    continue;
                                }
                            }
                        }

                        if (!lesson.getClazz().getName().equals(currentClazz.getName())) {// tiết đó không dành cho lớp này thì bỏ qua
                            continue;
                        }

                        List<Lesson> lessons = timeTables.get(new LessonKey(day, order));

                        // nếu trùng vào ngày đó, tiết đó, lớp đó có môn rồi --> duyệt tiếp, không chèn vào tiết học khác đã được xếp vào tiết đó hôm đó
                        if (lessons.stream().anyMatch(l -> !ObjectUtils.isEmpty(l)
                                && l.getClazz().getName().equals(lesson.getClazz().getName())
                                && !ObjectUtils.isEmpty(l.getSubject()))) {
                            continue;
                        }
                        // nếu tiết này có lịch rồi thì bỏ qua (vào hôm đó, tiết đó, môn đó, lớp đó -> nếu trùng tất cả thì sẽ bỏ qua)
                        if (lessons.stream().anyMatch(l -> !ObjectUtils.isEmpty(l)
                                && l.getClazz().getName().equals(lesson.getClazz().getName())
                                && l.getSubject().getName().equals(lesson.getSubject().getName()))) {
                            continue;
                        }

                        // ghi tiết đầu tiên đang chờ và thỏa mãn điều kiện
                        temp.setSubject(lesson.getSubject());
                        temp.setTeacher(lesson.getTeacher());
                        temp.setClazz(lesson.getClazz());
                        temp.setStatic(lesson.isStatic());

                        this.setLesson(day, order, temp, k);
                    }
                }
            }
        }
    }

    private void setLesson(int day, int order, Lesson temp, int lessonIndex) {
        LessonKey keyTmp = new LessonKey(day, order);
        List<Lesson> existedLesson = this.timeTables.get(keyTmp);
        if (CollectionUtils.isEmpty(existedLesson)) {
            existedLesson = new ArrayList<>();
        }
        existedLesson.add(temp);
        this.timeTables.put(keyTmp, existedLesson);
        Lesson lesson = this.waitingTimeTables.get(lessonIndex);
        if (lesson.getLessonQuantity() == 1) {
            this.waitingTimeTables.remove(lessonIndex);
        } else {
            lesson.setLessonQuantity(lesson.getLessonQuantity() - 1);
        }
    }

    private void setStaticLesson() {
        // tìm môn chào cờ
        Subject saluteFlagSubject = this.getStaticSubject(StaticSubject.SALUTE_FLAG.value);
        // tìm môn sinh hoạt lớp
        Subject classMeetingSubject = this.getStaticSubject(StaticSubject.CLASS_MEETING.value);

        LessonKey saluteFlagLessonKey = new LessonKey(2, 1);// thứ 2, tiết 1
        LessonKey classMeetingLessonKey = new LessonKey(7, 5);// thứ 7, tiết 5
        List<Lesson> saluteFlagLessons = new ArrayList<>();
        List<Lesson> classMeetingLessons = new ArrayList<>();
        for (int i = 0; i < this.clazzes.size(); i++) {
            Clazz clazz = this.clazzes.get(i);

            // chào cờ
            Lesson saluteFlagLesson = Lesson.builder()
                    .clazz(clazz)
                    .subject(saluteFlagSubject)
                    .teacher(null)
                    .isStatic(true)
                    .lessonQuantity(1)
                    .isTeacherBusy(false)
                    .build();
            saluteFlagLessons.add(saluteFlagLesson);

            // sinh hoạt
            Teacher headTeacher = this.findHeadTeacher(clazz.getName());
            Lesson classMeetingLesson = Lesson.builder()
                    .clazz(clazz)
                    .subject(classMeetingSubject)
                    .teacher(headTeacher)
                    .isStatic(true)
                    .lessonQuantity(1)
                    .isTeacherBusy(false)
                    .build();
            classMeetingLessons.add(classMeetingLesson);
        }
        timeTables.put(saluteFlagLessonKey, saluteFlagLessons);// thêm danh sách các tiết chào cờ vào thời khóa biểu
        timeTables.put(classMeetingLessonKey, classMeetingLessons);// thêm danh sách các tiết sinh hoạt lớp vào thời khóa biểu
    }

    private Teacher findHeadTeacher(String name) {
        return this.teachers.stream().filter(t -> t.getHeadClazz().getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private Subject getStaticSubject(String name) {
        return this.subjects.stream().filter(sub -> sub.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * 2. Tiến hóa đến mức đúng, để đảm bảo các tiêu chí sau:
     * - Không trùng tiết của giáo viên
     * - Không trùng phòng máy thực hành
     */
    private void evolutionToCorrect() {
        boolean hasIssue;
        int generation = 0;
        do {
            generation++;
            hasIssue = false;
            for (int day = TimeTableConstants.FIRST_DAY; day <= TimeTableConstants.LAST_DAY; day++) {
                for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) {
                    LessonKey lessonKey = new LessonKey(day, order);
                    List<Lesson> lessons = this.timeTables.get(lessonKey);
                    for (int k = 0; k < lessons.size(); k++) {
                        Lesson lesson = lessons.get(k);
                        // kiểm tra giáo viên trùng lịch
                        Teacher teacher = lesson.getTeacher();
                        if (!ObjectUtils.isEmpty(teacher) && !lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && this.isTeacherBusy(day, order, lesson.getClazz(), teacher)) {
                            lesson.setTeacherBusy(true);
                            // rơi vào tình huống trùng lịch thì tìm giáo viên thay thế
                            LessonKey replacementLessonKey = this.findFirstReplacement(day, order, lesson.getClazz(), teacher);
                            if (ObjectUtils.isEmpty(replacementLessonKey)) { // không tìm được giáo viên thay thế
                                hasIssue = true;
                                continue;
                            }
                            // sau khi đảo xong thì cả 2 giáo viên đã hết bị trùng tiết
                            List<Lesson> replacementLessons = this.timeTables.get(replacementLessonKey);
                            Lesson replacementLesson = findByClassName(replacementLessons, lesson.getClazz().getName());
                            replacementLesson.setTeacherBusy(false);
                            lesson.setTeacherBusy(false);

                            this.timeTables.get(lessonKey).set(k, replacementLesson);
                            this.timeTables.get(replacementLessonKey).set(k, lesson);
                        } else {
                            lesson.setTeacherBusy(false);
                        }
                    }
                }
            }
        } while (hasIssue);
        System.out.println("generation: " + generation);
    }

    private LessonKey findFirstReplacement(int replaceDay, int replaceOrder, Clazz clazz, Teacher busyTeacher) {
        for (int day = TimeTableConstants.FIRST_DAY; day <= TimeTableConstants.LAST_DAY; day++) {
            for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) {
                if ((day == TimeTableConstants.FIRST_DAY && order == TimeTableConstants.FIRST_ORDER) ||
                        (day == TimeTableConstants.LAST_DAY && order == TimeTableConstants.LAST_ORDER)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                    continue;
                }
                LessonKey lessonKey = new LessonKey(day, order);
                List<Lesson> lessons = this.timeTables.get(lessonKey);
                Lesson lesson = this.findByClassName(lessons, clazz.getName());
                if (ObjectUtils.isEmpty(lesson)) {
                    return null;
                }
                if (!lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON)
                        || (lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && replaceOrder == TimeTableConstants.LAST_ORDER)
                ) {
                    // nếu có giáo viên và giáo viên đó có thể dạy (không vướng lịch bận của giáo viên, không trùng vào tiết sinh hoạt, chào cờ, ...)
                    if (!this.isTeacherBusy(replaceDay, replaceOrder, clazz, lesson.getTeacher())) {
                        // và ngược lại giáo viên hôm nay đảo sang hôm đó cũng không bị trùng lịch`
                        if (!this.isTeacherBusy(day, order, clazz, busyTeacher)) {
                            return lessonKey;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isTeacherBusy(int day, int order, Clazz clazz, Teacher teacher) {
        // check giáo viên bận (cùng ngày đó, tiết đó mà giáo viên này phải dạy ở 2 hoặc nhiều lớp khác nhau thì gọi là bận)
        return this.timeTables
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

    private Lesson findByClassName(List<Lesson> lessons, String className) {
        if (CollectionUtils.isEmpty(lessons) || StringUtils.isEmpty(className)) {
            return null;
        }
        return lessons.stream().filter(lesson -> lesson.getClazz().getName().equals(className)).findFirst().orElse(null);
    }

    /**
     * Lai ghép - Đánh giá - Chọn lọc
     * <p>
     * - Các môn được cấu hình không học tiết cuối (như Thể dục) thì không cho học tiết cuối
     * - Trong tuần có một ngày có 2 tiết Văn liên tiếp, Toán liên tiếp để làm bài kiểm tra
     * - Phân bố đều các môn cần giãn cách, ví dụ môn Địa 1 tuần có 2 tiết sẽ có tiết cách ngày
     */
    private void fineTuning(int from, int to) {
        //this.bestResultsTimeTable = this.timeTables;
        int max_score = -99999999;
        for (int i = from; i < to; i++) {
            for (int day = 2; day <= TimeTableConstants.LAST_DAY; day++) {
                for (int order = 1; order <= TimeTableConstants.LAST_ORDER; order++) {
                    if ((day == TimeTableConstants.FIRST_DAY && order == TimeTableConstants.FIRST_ORDER) ||
                            (day == TimeTableConstants.LAST_DAY && order == TimeTableConstants.LAST_ORDER)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                        continue;
                    }
                    LessonKey lessonKey = new LessonKey(day, order);
                    for (int k = 0; k < this.timeTables.get(lessonKey).size(); k++) {
                        Lesson lesson = this.timeTables.get(lessonKey).get(k);
                        if (lesson.isStatic()) {
                            continue;
                        }

                        // những môn đã có 2 tiết liền nhau không được đổi nữa: VănKT, Toán, tin...
                        // trường hợp môn được đổi
                        if (lesson.getSubject().getBlockNumber() == 2) {
                            boolean checkSub1 = false;
                            boolean checkSub2 = false;

                            if (order < 3) {
                                checkSub1 = checkAdjacentLessonBeforeReplace(day, order + 1, lesson, k);
                                checkSub2 = checkAdjacentLessonBeforeReplace(day, order + 2, lesson, k);

                            } else {
                                checkSub1 = checkAdjacentLessonBeforeReplace(day, order - 1, lesson, k);
                                checkSub2 = checkAdjacentLessonBeforeReplace(day, order - 2, lesson, k);

                            }
                            if (checkSub1 && !checkSub2) {
                                continue;
                            }
                            if (order != 1 && order != 5) {
                                checkSub1 = checkAdjacentLessonBeforeReplace(day, order - 1, lesson, k);
                                checkSub2 = checkAdjacentLessonBeforeReplace(day, order + 1, lesson, k);
                            }
                            if ((checkSub1 && !checkSub2) || (!checkSub1 && checkSub2)) {
                                continue;
                            }
                        }
                        List<LessonKey> allReplacement = this.findAllReplacement(day, order, lesson);
                        if (ObjectUtils.isEmpty(allReplacement)) {
                            continue;
                        }
                        for (int j = 0; j < allReplacement.size(); j++) {
                            LessonKey currentLessonKey = allReplacement.get(j);
                            //có tìm được giáo viên thay thế thì đảo tiết giữa 2 GV
                            // Sau khi đảo xong thì cả 2 GV đã hết bị trùng lịch
                            // xử lý đảo
                            List<Lesson> allReplacementLesson = this.timeTables.get(currentLessonKey);
                            Lesson replacementLesson = this.findByClassName(allReplacementLesson, lesson.getClazz().getName());
                            replacementLesson.setTeacherBusy(false);
                            lesson.setTeacherBusy(false);

                            // đổi lịch môn nghỉ
                            if ((lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && currentLessonKey.getOrder() != TimeTableConstants.LAST_ORDER)
                                    || (replacementLesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && order != TimeTableConstants.LAST_ORDER)) {
                                continue;
                            }

                            // Trường hợp ngược lại môn bị đổi: môn đã có hai tiết liền nhau
                            if (replacementLesson.getSubject().getBlockNumber() == 2) {
                                boolean checkSub1 = false;
                                boolean checkSub2 = false;
                                if (currentLessonKey.getOrder() < 3) {
                                    checkSub1 = checkAdjacentLessonBeforeReplace(currentLessonKey.getDay(), currentLessonKey.getOrder() + 1, replacementLesson, k);
                                    checkSub2 = checkAdjacentLessonBeforeReplace(currentLessonKey.getDay(), currentLessonKey.getOrder() + 2, replacementLesson, k);

                                } else {
                                    checkSub1 = checkAdjacentLessonBeforeReplace(currentLessonKey.getDay(), currentLessonKey.getOrder() - 1, replacementLesson, k);
                                    checkSub2 = checkAdjacentLessonBeforeReplace(currentLessonKey.getDay(), currentLessonKey.getOrder() - 2, replacementLesson, k);
                                }
                                if (checkSub1 && !checkSub2) {
                                    continue;
                                }
                                if (currentLessonKey.getOrder() != 1 && currentLessonKey.getOrder() != 5) {
                                    checkSub1 = checkAdjacentLessonBeforeReplace(currentLessonKey.getDay(), currentLessonKey.getOrder() - 1, replacementLesson, k);
                                    checkSub2 = checkAdjacentLessonBeforeReplace(currentLessonKey.getDay(), currentLessonKey.getOrder() + 1, replacementLesson, k);
                                }
                                if ((checkSub1 && !checkSub2) || (!checkSub1 && checkSub2)) {
                                    continue;
                                }
                            }

                            // tránh việc khi đổi môn lại thành 1 ngày có 3 tiết như môn Văn khối lớp 9
                            if (lesson.getSubject().getBlockNumber() == 2) {
//                            if (lesson.getSubject().getName().equals("Văn")) {
                                int count = 0;
                                for (int reporder = 1; reporder <= TimeTableConstants.LAST_ORDER; reporder++) {
                                    count = countLesson(currentLessonKey.getDay(), reporder, replacementLesson, count);
                                }
                                if (count >= 2) {
                                    continue;
                                }
                            }
                            /// ngược lại
                            if (replacementLesson.getSubject().getBlockNumber() == 2) {
//                            if (replacementLesson.getSubject().getName().equals("Văn")) {
                                int count = 0;
                                for (int reporder = 1; reporder <= TimeTableConstants.LAST_ORDER; reporder++) {
                                    count = countLesson(day, reporder, lesson, count);
                                }
                                if (count >= 2) {
                                    continue;
                                }
                            }


                            this.timeTables.get(lessonKey).set(k, replacementLesson);
                            this.timeTables.get(currentLessonKey).set(k, lesson);

                            Lesson lessonTemp = lesson;
                            lesson = replacementLesson;
                            replacementLesson = lessonTemp;

                            int score = this.fitness();
                            // nếu kết quả tốt hơn thì lưu kết quả tốt nhất
                            if (max_score < score) {
                                max_score = score;
                                this.bestResultsTimeTable = this.timeTables;
                                // TODO - file_put_contents()
                            }
                            System.out.println("Lần chạy thứ " + i + " kết quả " + score + ", tốt nhất " + max_score);
                        }
                        this.timeTables = this.bestResultsTimeTable;
                    }
                }
            }
        }
    }

    private int countLesson(int day, int reporder, Lesson lesson, int count) {
        LessonKey lessonKey1 = new LessonKey(day, reporder);
        List<Lesson> checkExitLesson = this.timeTables.get(lessonKey1);
        Lesson replacementLesson1 = this.findByClassName(checkExitLesson, lesson.getClazz().getName());
        if (replacementLesson1.getSubject().getName().equals(lesson.getSubject().getName())) {
            count++;
        }
        return count;
    }

    private boolean checkAdjacentLessonBeforeReplace(int day, int order, Lesson lesson, int k) {
        Lesson lesson1 = this.timeTables.get(new LessonKey(day, order)).get(k);
        return lesson.getSubject().getName().equals(lesson1.getSubject().getName());
    }

    private boolean checkAdjacentLesson(int day, String className, String replacedSubjectName, boolean isBefore) {
        int start = isBefore ? day - 1 : day;
        int end = isBefore ? day : day + 1;
        for (int repDay = start; repDay <= end; repDay++) {
            for (int repOrder = 1; repOrder <= TimeTableConstants.LAST_ORDER; repOrder++) {
                LessonKey lessonKey1 = new LessonKey(repDay, repOrder);
                List<Lesson> checkExitLesson = this.timeTables.get(lessonKey1);
                Lesson replacementLesson1 = this.findByClassName(checkExitLesson, className);
                if (replacementLesson1.getSubject().getName().equals(replacedSubjectName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Đánh giá điểm của TKB
     */
    private int fitness() {
        int score = 10000;
        for (int day = 2; day <= TimeTableConstants.LAST_DAY; day++) {
            for (int order = 1; order <= TimeTableConstants.LAST_ORDER; order++) {
                if ((day == TimeTableConstants.FIRST_DAY && order == TimeTableConstants.FIRST_ORDER) ||
                        (day == TimeTableConstants.LAST_DAY && order == TimeTableConstants.LAST_ORDER)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                    continue;
                }
                LessonKey lessonKey = new LessonKey(day, order);
                List<Lesson> lessons = this.timeTables.get(lessonKey);
                for (int k = 0; k < lessons.size(); k++) {
                    Lesson lesson = lessons.get(k);
                    if (order == TimeTableConstants.LAST_ORDER) {
                        // Môn học tránh tiết cuối
                        if (lesson.getSubject().getAvoidLastLesson()) {
                            score -= 5;
                        } else {
                            score += 5;
                        }
                        // tiết nghỉ ở cuối ngày
                        if (lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON)) {
                            score += 100;
                        } else {
                            score -= 100;
                        }
                    }
                    // Môn học block nhưng nó lại không liền
                    if (lesson.getSubject().getBlockNumber() > 1) {

                        // kiểm tra tiết trước
                        boolean before = false;
                        List<Lesson> lessonList = this.timeTables.get(new LessonKey(day, order - 1));
                        Lesson previousLesson = this.findByClassName(lessonList, lesson.getClazz().getName());
                        if (!ObjectUtils.isEmpty(previousLesson)) {
                            before = previousLesson.getSubject().getName().equals(lesson.getSubject().getName());
                        }

                        // kiểm tra tiết sau
                        boolean after = false;
                        List<Lesson> lessonListAfter = this.timeTables.get(new LessonKey(day, order + 1));
                        Lesson afterLesson = this.findByClassName(lessonListAfter, lesson.getClazz().getName());
                        if (!ObjectUtils.isEmpty(afterLesson)) {
                            after = afterLesson.getSubject().getName().equals(lesson.getSubject().getName());
                        }
                        if (before || after) {
                            score += 100;
                        } else {
                            score -= 100;
                        }
                    }

                }
            }
        }
        return score;
    }

    private List<LessonKey> findAllReplacement(int replaceDay, int replaceOrder, Lesson lesson) {
        Clazz clazz = lesson.getClazz();
        Teacher busyTeacher = lesson.getTeacher();
        List<LessonKey> result = null;
        for (int day = replaceDay; day <= TimeTableConstants.LAST_DAY; day++) {
            for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) {
                if ((day == TimeTableConstants.FIRST_DAY && order == TimeTableConstants.FIRST_ORDER) ||
                        (day == TimeTableConstants.LAST_DAY && order == TimeTableConstants.LAST_ORDER)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                    continue;
                }
                if (replaceDay == day && order <= replaceOrder) {
                    continue;
                }
                String subjectName = lesson.getSubject().getName();
                LessonKey lessonKey = new LessonKey(day, order);
                List<Lesson> lessons = this.timeTables.get(lessonKey);
                Lesson tempLesson = this.findByClassName(lessons, clazz.getName());
                if (ObjectUtils.isEmpty(tempLesson)) {
                    continue;
                }
                if (tempLesson.getSubject().getName().equals(subjectName)) {
                    continue;
                }

                // tránh những môn tiết cuối
                if ((lesson.getSubject().getAvoidLastLesson() && order == TimeTableConstants.LAST_ORDER)
                        || (replaceOrder == TimeTableConstants.LAST_ORDER && tempLesson.getSubject().getAvoidLastLesson())) {
                    continue;
                }

                // tránh gv có con nhỏ hoặc nhà xa dạy tiết 1
                if (replaceOrder == TimeTableConstants.FIRST_ORDER && (tempLesson.getTeacher().getHasChildren() || tempLesson.getTeacher().getHasFarfromHome())) {
                    continue;
                }
                if ((lesson.getTeacher().getHasChildren() || lesson.getTeacher().getHasFarfromHome()) && order == TimeTableConstants.FIRST_ORDER) {
                    continue;
                }
                // môn sinh, địa, thể dục, ... không học 2 ngày liên tiếp
                if (tempLesson.getSubject().getRequireSpacing()) {
                    boolean afterDayLesson = false;
                    boolean beforeDayLesson = false;
                    if (replaceDay == TimeTableConstants.FIRST_DAY) {
                        afterDayLesson = checkAdjacentLesson(replaceDay, lesson.getClazz().getName(), tempLesson.getSubject().getName(), false);
                    } else if (replaceDay == TimeTableConstants.LAST_DAY) {
                        beforeDayLesson = checkAdjacentLesson(replaceDay, lesson.getClazz().getName(), tempLesson.getSubject().getName(), true);
                    } else {
                        boolean midDayLesson = false;
                        for (int repDay = replaceDay - 1; repDay <= replaceDay + 1; repDay++) {
                            for (int repOrder = 1; repOrder <= TimeTableConstants.LAST_ORDER; repOrder++) {
                                LessonKey lessonKey1 = new LessonKey(repDay, repOrder);
                                List<Lesson> checkExitLesson = this.timeTables.get(lessonKey1);
                                Lesson replacementLesson1 = this.findByClassName(checkExitLesson, lesson.getClazz().getName());
                                if (replacementLesson1.getSubject().getName().equals(tempLesson.getSubject().getName())) {
                                    midDayLesson = true;
                                    break;
                                }
                            }
                            if (midDayLesson)
                                break;
                        }
                        if (midDayLesson)
                            continue;
                    }
                    if (afterDayLesson || beforeDayLesson) {
                        continue;
                    }
                }
                if (lesson.getSubject().getRequireSpacing()) {
                    boolean beforeDayLesson = false;
                    boolean afterDayLesson = false;
                    if (day == TimeTableConstants.FIRST_DAY) {
                        afterDayLesson = checkAdjacentLesson(day, lesson.getClazz().getName(), lesson.getSubject().getName(), false);
                    } else if (day == TimeTableConstants.LAST_DAY) {
                        beforeDayLesson = checkAdjacentLesson(day, lesson.getClazz().getName(), lesson.getSubject().getName(), true);
                    } else {
                        boolean midDayLesson = false;
                        for (int repDay = day - 1; repDay <= day + 1; repDay++) {
                            for (int repOrder = 1; repOrder <= TimeTableConstants.LAST_ORDER; repOrder++) {
                                LessonKey lessonKey1 = new LessonKey(repDay, repOrder);
                                List<Lesson> checkExitLesson = this.timeTables.get(lessonKey1);
                                Lesson replacementLesson1 = this.findByClassName(checkExitLesson, lesson.getClazz().getName());
                                if (replacementLesson1.getSubject().getName().equals(lesson.getSubject().getName())) {
                                    midDayLesson = true;
                                    break;
                                }
                            }
                            if (midDayLesson)
                                break;
                        }
                        if (midDayLesson)
                            continue;
                    }
                    if (beforeDayLesson || afterDayLesson) {
                        continue;
                    }
                }

                if (this.checkOffLessonToSwitch(tempLesson, replaceOrder, lesson, order)) {
                    // nếu có giáo viên và giáo viên đó có thể dạy (không vướng lịch bận của giáo viên, không trùng vào tiết sinh hoạt, chào cờ, ...)
                    if (!this.isTeacherBusy(replaceDay, replaceOrder, clazz, tempLesson.getTeacher())) {
                        // và ngược lại giáo viên hôm nay đảo sang hôm đó cũng không bị trùng lịch
                        if (!this.isTeacherBusy(day, order, clazz, busyTeacher)) {
                            if (CollectionUtils.isEmpty(result)) {
                                result = new ArrayList<>();
                            }
                            result.add(new LessonKey(day, order));
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * kiểm tra các điều kiện của môn đổi và môn bị đổi:
     * - tiết nghỉ phải là tiết cuối ngày
     * - các môn học tránh tiết cuối không để ở cuối
     *
     * @param targetLesson   môn học được đổi
     * @param replacedOrder  ví trí bị đổi
     * @param replacedLesson môn học bị đổi
     * @param order          vị trí được đổi
     * @return
     */
    private boolean checkOffLessonToSwitch(Lesson targetLesson, int replacedOrder, Lesson replacedLesson, int order) {
        return (!targetLesson.getSubject().getAvoidLastLesson() || replacedOrder != TimeTableConstants.LAST_ORDER)
                && (!replacedLesson.getSubject().getAvoidLastLesson() || order != TimeTableConstants.LAST_ORDER);
    }

    private void showOutput(Map<LessonKey, List<Lesson>> mapData) {
        System.out.println("\t\t\t\t\t\t\t 6A \t\t\t\t\t\t\t\t 6B \t\t\t\t\t\t\t\t\t\t 6C \t\t\t\t\t\t\t\t 6D \t\t\t\t\t\t\t\t\t 7A \t\t\t\t\t\t\t\t\t 7B \t\t\t\t\t\t\t\t\t 7C \t\t\t\t\t\t\t\t\t 7D \t\t\t\t\t\t\t\t\t 8A \t\t\t\t\t\t\t\t\t 8B \t\t\t\t\t\t\t\t\t 8C \t\t\t\t\t\t\t\t\t 8D \t\t\t\t\t\t\t\t\t 9A \t\t\t\t\t\t\t\t\t 9B \t\t\t\t\t\t\t\t\t 9C \t\t\t\t\t\t\t\t\t 9D");
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
