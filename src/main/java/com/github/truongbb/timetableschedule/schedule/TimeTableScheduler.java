package com.github.truongbb.timetableschedule.schedule;

import com.github.truongbb.timetableschedule.constant.StaticSubject;
import com.github.truongbb.timetableschedule.constant.TimeTableConstants;
import com.github.truongbb.timetableschedule.dto.LessonKey;
import com.github.truongbb.timetableschedule.entity.*;
import com.github.truongbb.timetableschedule.repository.availableTeachingDay.TeachingDayRepository;
import com.github.truongbb.timetableschedule.repository.clazz.ClazzRepository;
import com.github.truongbb.timetableschedule.repository.subject.SubjectRepository;
import com.github.truongbb.timetableschedule.repository.teacher.TeacherRepository;
import com.github.truongbb.timetableschedule.repository.timetableconfig.LessonRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
public class TimeTableScheduler {

    private List<Clazz> clazzes; // toàn bộ lớp học
    private List<Teacher> teachers; // toàn bộ giáo viên
    private List<Subject> subjects; // toàn bộ môn học
    private List<AvailableTeachingDay> availableTeachingDay;
    private Map<LessonKey, List<Lesson>> timeTables; // các tiết học kết quả
    private List<Lesson> waitingTimeTables; // các tiết học chờ xếp lịch (giáo viên được gán vào môn mình dạy và lớp mình dạy, cùng với tổng số tiết cần dạy trong tuần, nhưng chưa được xếp vào ngày nào, tiết nào)

    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ClazzRepository clazzRepository;
    private final LessonRepository lessonRepository;
    private final TeachingDayRepository teachingDayRepository;

    private Map<LessonKey, List<Lesson>> bestResultsTimeTable;

    public TimeTableScheduler(SubjectRepository subjectRepository, TeacherRepository teacherRepository, ClazzRepository clazzRepository, LessonRepository lessonRepository, TeachingDayRepository teachingDayRepository) {
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.clazzRepository = clazzRepository;
        this.lessonRepository = lessonRepository;
        this.teachingDayRepository = teachingDayRepository;
    }

    public void generateTimeTable() {

        /**
         *
         * 1. khởi tạo quần thể
         * 2. đánh giá
         * 3. chọn lọc
         * 4. lai ghép
         * 5. đột biến
         *
         */

        this.prepareData();
        this.generateBase();
        this.showOutput(this.timeTables);

        this.evolutionToCorrect();
        this.showOutput(this.timeTables);

        this.fineTuning(0, 15);
        this.showOutput(this.bestResultsTimeTable);
    }

    /**
     * 0. chuẩn bị dữ liệu
     */
    private void prepareData() {
        // load all data from DB
        this.clazzes = clazzRepository.getAll();
        this.subjects = subjectRepository.getAll();
        this.teachers = teacherRepository.getAll();
        this.availableTeachingDay = teachingDayRepository.getAll();

        // lấy tất cả data ở bảng time_table (đây là dữ liệu khởi tạo từ trước trong DB)
        this.waitingTimeTables = lessonRepository.getAll();

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

        for (int day = TimeTableConstants.FIRST_DAY; day <= TimeTableConstants.LAST_DAY; day++) { // từ thứ 2 tới thứ bảy
            for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) { // từ tiết 1 tới tiết 5
                if (this.isCCOrSH(day, order)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                    continue;
                }
                for (int j = 0; j < this.clazzes.size(); j++) {
                    Clazz currentClazz = this.clazzes.get(j);

                    for (int k = 0; k < this.waitingTimeTables.size(); k++) {
                        Lesson temp = new Lesson();
                        Lesson lesson = this.waitingTimeTables.get(k);

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

                        // ghi tiết đầu tiên đang chờ và thỏa mãn điều kiện
                        temp.setSubject(lesson.getSubject());
                        temp.setTeacher(lesson.getTeacher());
                        temp.setClazz(lesson.getClazz());

                        this.setLesson(day, order, temp, k);
                    }
                }
            }
        }
    }

    private boolean isCCOrSH(int day, int order) {
        return (day == TimeTableConstants.FIRST_DAY && order == TimeTableConstants.FIRST_ORDER) ||
                (day == TimeTableConstants.LAST_DAY && order == TimeTableConstants.LAST_ORDER);
    }

    private boolean isOffLesson(Lesson lesson) {
        return !ObjectUtils.isEmpty(lesson) && lesson.getSubject().getName().equalsIgnoreCase(TimeTableConstants.OFF_LESSON);
    }

    private Lesson findLessonByKeyAndClass(int day, int order, String className) {
        return this.findByClassName(this.timeTables.get(new LessonKey(day, order)), className);
    }

    private Lesson findByClassName(List<Lesson> lessons, String className) {
        if (CollectionUtils.isEmpty(lessons) || StringUtils.isEmpty(className)) {
            return null;
        }
        return lessons.stream().filter(lesson -> lesson.getClazz().getName().equals(className)).findFirst().orElse(null);
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
        Subject saluteFlagSubject = subjectRepository.getStaticSubject(StaticSubject.SALUTE_FLAG.value);
        // tìm môn sinh hoạt lớp
        Subject classMeetingSubject = subjectRepository.getStaticSubject(StaticSubject.CLASS_MEETING.value);

        LessonKey saluteFlagLessonKey = new LessonKey(TimeTableConstants.FIRST_DAY, TimeTableConstants.FIRST_ORDER);// thứ 2, tiết 1
        LessonKey classMeetingLessonKey = new LessonKey(TimeTableConstants.LAST_DAY, TimeTableConstants.LAST_ORDER);// thứ 7, tiết 5
        List<Lesson> saluteFlagLessons = new ArrayList<>();
        List<Lesson> classMeetingLessons = new ArrayList<>();
        for (Clazz clazz : this.clazzes) {
            // chào cờ
            Lesson saluteFlagLesson = Lesson.builder()
                    .clazz(clazz)
                    .subject(saluteFlagSubject)
                    .teacher(null)
                    .lessonQuantity(1)
                    .build();
            saluteFlagLessons.add(saluteFlagLesson);

            // sinh hoạt
            Teacher headTeacher = teacherRepository.findHeadTeacher(clazz.getName());
            Lesson classMeetingLesson = Lesson.builder()
                    .clazz(clazz)
                    .subject(classMeetingSubject)
                    .teacher(headTeacher)
                    .lessonQuantity(1)
                    .build();
            classMeetingLessons.add(classMeetingLesson);
        }
        timeTables.put(saluteFlagLessonKey, saluteFlagLessons);// thêm danh sách các tiết chào cờ vào thời khóa biểu
        timeTables.put(classMeetingLessonKey, classMeetingLessons);// thêm danh sách các tiết sinh hoạt lớp vào thời khóa biểu
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
                        if (!ObjectUtils.isEmpty(teacher)
                                && !lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON)
                                && this.isTeacherBusy(day, order, lesson.getClazz(), teacher)
                        ) {
                            // rơi vào tình huống trùng lịch thì tìm giáo viên thay thế
                            LessonKey replacementLessonKey = this.findFirstReplacement(day, order, lesson.getClazz(), teacher);
                            if (ObjectUtils.isEmpty(replacementLessonKey)) { // không tìm được giáo viên thay thế
                                hasIssue = true;
                                continue;
                            }
                            // đảo 2 tiết cho nhau ==> giáo viên hết bị trùng lịch
                            List<Lesson> replacementLessons = this.timeTables.get(replacementLessonKey);
                            Lesson replacementLesson = this.findByClassName(replacementLessons, lesson.getClazz().getName());

                            this.timeTables.get(lessonKey).set(k, replacementLesson);
                            this.timeTables.get(replacementLessonKey).set(k, lesson);
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
                if (this.isCCOrSH(day, order)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                    continue;
                }
                Lesson lesson = this.findLessonByKeyAndClass(day, order, clazz.getName());
                if (ObjectUtils.isEmpty(lesson)) {
                    return null;
                }
                if (!lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON)
                        || (lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && replaceOrder == TimeTableConstants.LAST_ORDER)
                ) {
                    // nếu có giáo viên và giáo viên đó có thể dạy (không vướng lịch bận của giáo viên, không trùng vào tiết sinh hoạt, chào cờ, ...)
                    // và ngược lại giáo viên hôm nay đảo sang hôm đó cũng không bị trùng lịch
                    if (!this.isTeacherBusy(replaceDay, replaceOrder, clazz, lesson.getTeacher())
                            && !this.isTeacherBusy(day, order, clazz, busyTeacher)) {
                        return new LessonKey(day, order);
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

    /**
     * Lai ghép - Đánh giá - Chọn lọc
     * <p>
     * - Các môn được cấu hình không học tiết cuối (như Thể dục) thì không cho học tiết cuối
     * - Trong tuần có một ngày có 2 tiết Văn liên tiếp, Toán liên tiếp để làm bài kiểm tra
     * - Phân bố đều các môn cần giãn cách, ví dụ môn Địa 1 tuần có 2 tiết sẽ có tiết cách ngày
     */
    private void fineTuning(int from, int to) {
        this.bestResultsTimeTable = this.timeTables;
        int max_score = Integer.MIN_VALUE;
        for (int i = from; i < to; i++) {
            for (int day = TimeTableConstants.FIRST_DAY; day <= TimeTableConstants.LAST_DAY; day++) {
                for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) {
                    if (this.isCCOrSH(day, order)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                        continue;
                    }
                    LessonKey lessonKey = new LessonKey(day, order);
                    for (int k = 0; k < this.timeTables.get(lessonKey).size(); k++) {
                        Lesson lesson = this.timeTables.get(lessonKey).get(k);

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
                            List<Lesson> allReplacementLesson = this.timeTables.get(currentLessonKey);
                            Lesson replacementLesson = this.findByClassName(allReplacementLesson, lesson.getClazz().getName());

                            // nếu tiết tìm được hoặc tiết hiện tại mang đi đổi mà bị trùng tiết giáo viên ==> bỏ qua không đổi
                            if (this.isTeacherBusy(currentLessonKey.getDay(), currentLessonKey.getOrder(), lesson.getClazz(), lesson.getTeacher())
                                    || this.isTeacherBusy(day, order, replacementLesson.getClazz(), replacementLesson.getTeacher())) {
                                continue;
                            }

                            // tiết nghỉ phải là tiết 5
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

                            // tránh 1 ngày có cả Văn và 2 VănKT, Toán và 2 ToánKT (3 tiết văn hoặc toán)
                            if (this.checkTripleLessonInTheSameDay(lesson, currentLessonKey.getDay(), currentLessonKey.getOrder(), TimeTableConstants.LITERATURE_LESSON)
                                    || this.checkTripleLessonInTheSameDay(replacementLesson, day, order, TimeTableConstants.LITERATURE_LESSON)
                                    || this.checkTripleLessonInTheSameDay(lesson, currentLessonKey.getDay(), currentLessonKey.getOrder(), TimeTableConstants.MATH_LESSON)
                                    || this.checkTripleLessonInTheSameDay(replacementLesson, day, order, TimeTableConstants.MATH_LESSON)) {
                                continue;
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
                            this.timeTables = this.bestResultsTimeTable;
                        }
                    }
                }
            }
        }
    }

    private boolean checkTripleLessonInTheSameDay(Lesson lesson, int day, int order, String subjectName) {
        if (!lesson.getSubject().getName().startsWith(subjectName)) {
            return false;
        }
        int count = 0;
        for (int reporder = TimeTableConstants.FIRST_ORDER; reporder <= TimeTableConstants.LAST_ORDER; reporder++) {
            Lesson tempLesson = this.findLessonByKeyAndClass(day, reporder, lesson.getClazz().getName());
            if (tempLesson.getSubject().getName().startsWith(subjectName) && lesson.getSubject().getName().startsWith(subjectName)) {
                count++;
            }
        }
        return count >= 2;
    }

    private boolean checkAdjacentLessonBeforeReplace(int day, int order, Lesson lesson, int k) {
        Lesson lesson1 = this.timeTables.get(new LessonKey(day, order)).get(k);
        return lesson.getSubject().getName().equals(lesson1.getSubject().getName());
    }

    private boolean checkAdjacentLesson(int day, int order, Lesson lesson) {
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
                Lesson tempLesson = this.findLessonByKeyAndClass(repDay, repOrder, lesson.getClazz().getName());
                if (tempLesson.getSubject().getName().equals(lesson.getSubject().getName())) {
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
                            score -= 200;
                        } else {
                            score += 200;
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

                    // kiểm tra gv xem có bị trùng sau khi đáp ứng được các điều kiện khác không
                    if (this.isTeacherBusy(day, order, lesson.getClazz(), lesson.getTeacher())) {
                        score -= 500;
                    }

                    // Phân bố đều các môn, ví dụ môn Địa,Sinh... 1 tuần có 2 tiết sẽ có tiết cách ngày
                    if (lesson.getSubject().getRequireSpacing()) {
                        boolean checkSpacing = false;
                        for (int afterDay = day; afterDay <= day + 1; afterDay++) {
                            for (int afterOrder = TimeTableConstants.FIRST_ORDER; afterOrder < TimeTableConstants.LAST_ORDER; afterOrder++) {
                                if (afterDay == day && afterOrder == order) {
                                    continue;
                                }
                                List<Lesson> lessonList = this.timeTables.get(new LessonKey(afterDay, afterOrder));
                                Lesson nextLesson = this.findByClassName(lessonList, lesson.getClazz().getName());
                                if (nextLesson.getSubject().getName().equals(lesson.getSubject().getName())) {
                                    checkSpacing = true;
                                    //break;
                                }
                            }
                            break;
                        }
                        if (checkSpacing) {
                            score -= 200;
                        } else
                            score += 200;
                    }

                    // Giảng viên thuê ngoài chỉ dạy được một vài ngày cố định trong tuần
                    List<AvailableTeachingDay> availableTeachingDayList = this.availableTeachingDay
                            .stream()
                            .filter(t -> t.getTeacher().getId().equals(lesson.getTeacher().getId()))
                            .collect(Collectors.toList());
                    boolean checkAvailableTeaching = false;
                    if (CollectionUtils.isEmpty(availableTeachingDayList)) {
                        continue;
                    }
                    for (int i = 0; i < availableTeachingDayList.size(); i++) {
                        if (day == availableTeachingDayList.get(i).getAvailableDay()) {
                            checkAvailableTeaching = true;
                        }
                    }
                    if (checkAvailableTeaching) {
                        score += 100;
                    } else {
                        score -= 100;
                    }


                    // Ưu tiên giáo viên nhà xa, có con nhỏ không dạy tiết 1
                    if (lesson.getTeacher().getHasChildren() || lesson.getTeacher().getHasFarfromHome()) {
                        if (order == TimeTableConstants.FIRST_ORDER) {
                            score -= 20;
                        } else {
                            score += 20;
                        }
                    }

                }
            }
        }
        return score;
    }

    private List<LessonKey> findAllReplacement(int replacedDay, int replacedOrder, Lesson replacedLesson) {
        Clazz clazz = replacedLesson.getClazz();
        Teacher busyTeacher = replacedLesson.getTeacher();
        List<LessonKey> result = null;
        for (int day = replacedDay; day <= TimeTableConstants.LAST_DAY; day++) {
            for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) {
                Lesson tempLesson = this.findLessonByKeyAndClass(day, order, replacedLesson.getClazz().getName());
                if (this.isIgnoreCases(day, order, replacedLesson, replacedDay, replacedOrder, tempLesson)) {
                    continue;
                }

                // nếu có giáo viên và giáo viên đó có thể dạy (không vướng lịch bận của giáo viên, không trùng vào tiết sinh hoạt, chào cờ, ...)
                if (!this.isTeacherBusy(replacedDay, replacedOrder, clazz, tempLesson.getTeacher())
                        && !this.isTeacherBusy(day, order, clazz, busyTeacher)) {
                    // và ngược lại giáo viên hôm nay đảo sang hôm đó cũng không bị trùng lịch
                    if (CollectionUtils.isEmpty(result)) {
                        result = new ArrayList<>();
                    }
                    result.add(new LessonKey(day, order));
                }
            }
        }
        return result;
    }

    private boolean isIgnoreCases(int day, int order, Lesson replacedLesson, int replacedDay, int replacedOrder, Lesson tempLesson) {
        if (this.isCCOrSH(day, order)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
            return true;
        }
        if (replacedDay == day && order <= replacedOrder) { // không đổi môn đã sắp xếp trước đó của lớp đó
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
        if (this.checkVisitingTeacherSchedule(replacedLesson, day) || this.checkVisitingTeacherSchedule(tempLesson, replacedDay)) {
            return true;
        }

        // tránh gv có con nhỏ hoặc nhà xa dạy tiết 1
        if (this.hasChildrenOrFarFromHome(tempLesson, order, replacedLesson, replacedOrder)) {
            return true;
        }

        // môn sinh, địa, thể dục, ... không học 2 ngày liên tiếp
        if ((tempLesson.getSubject().getRequireSpacing() && this.checkAdjacentLesson(replacedDay, replacedOrder, tempLesson))
                || (replacedLesson.getSubject().getRequireSpacing() && this.checkAdjacentLesson(day, order, replacedLesson))) {
            return true;
        }

        // môn học tránh tiết cuối
        if (this.isAvoidLastLessonSubject(tempLesson, order, replacedLesson, replacedOrder)) {
            return true;
        }
        return false;
    }

    private boolean hasChildrenOrFarFromHome(Lesson lesson, int order, Lesson replacedLesson, int replacedOrder) {
        return (replacedOrder == TimeTableConstants.FIRST_ORDER && (lesson.getTeacher().getHasChildren() || lesson.getTeacher().getHasFarfromHome()))
                || (order == TimeTableConstants.FIRST_ORDER && (replacedLesson.getTeacher().getHasChildren() || replacedLesson.getTeacher().getHasFarfromHome()));
    }

    private boolean isAvoidLastLessonSubject(Lesson lesson, int order, Lesson replacedLesson, int replacedOrder) {
        return (replacedLesson.getSubject().getAvoidLastLesson() && order == TimeTableConstants.LAST_ORDER)
                || (lesson.getSubject().getAvoidLastLesson() && replacedOrder == TimeTableConstants.LAST_ORDER);
    }

    private boolean checkVisitingTeacherSchedule(Lesson lesson, int day) {
        List<AvailableTeachingDay> availableTeachingDays = this.findAvailableTeachingDays(lesson.getTeacher());
        boolean canTeachFullWeek = CollectionUtils.isEmpty(availableTeachingDays);
        boolean canTeachInDay = false;
        if (!canTeachFullWeek) {
            canTeachInDay = availableTeachingDays.stream().anyMatch(d -> d.getAvailableDay() == day);
        }
        return !canTeachFullWeek && !canTeachInDay;
    }

    private List<AvailableTeachingDay> findAvailableTeachingDays(Teacher teacher) {
        return this.availableTeachingDay
                .stream()
                .filter(t -> t.getTeacher().getId().equals(teacher.getId()))
                .collect(Collectors.toList());
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
