package com.github.truongbb.timetableschedule.service.timetable;

import com.github.truongbb.timetableschedule.constant.StaticSubject;
import com.github.truongbb.timetableschedule.constant.TimeTableConstants;
import com.github.truongbb.timetableschedule.dto.LessonKey;
import com.github.truongbb.timetableschedule.dto.OffLessonConfig;
import com.github.truongbb.timetableschedule.entity.*;
import com.github.truongbb.timetableschedule.vm.AvailableTeachingDayVm;
import com.github.truongbb.timetableschedule.vm.LessonVm;
import com.github.truongbb.timetableschedule.vm.TimeTableVm;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
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
    final List<AvailableTeachingDay> availableTeachingDay = new ArrayList<>();
    List<LessonVm> waitingTimeTableTmp;
    final List<Lesson> waitingTimeTables = new ArrayList<>();

    Map<LessonKey, List<Lesson>> timeTables; // các tiết học kết quả
    Map<LessonKey, List<Lesson>> bestResultsTimeTable;
    Map<String, List<OffLessonConfig>> offLessonConfig;
    List<String> mainLessons;

    @Autowired
    TimeTableServiceUtil timeTableServiceUtil;

    @Override
    public Map<LessonKey, List<Lesson>> generateTable(TimeTableVm timeTableVm) throws IllegalArgumentException {
        this.initData(timeTableVm);
        if (!this.validateInputData()) {
            throw new IllegalArgumentException();
        }

        this.prepareData();
        this.generateBase();
        timeTableServiceUtil.showOutput(this.clazzes, this.timeTables);

        this.evolutionToCorrect();
        timeTableServiceUtil.showOutput(this.clazzes, this.timeTables);

        this.fineTuning(0, ObjectUtils.isEmpty(timeTableVm.getGenerationNum()) ? 15 : timeTableVm.getGenerationNum());
        timeTableServiceUtil.showOutput(this.clazzes, this.bestResultsTimeTable);

        return this.bestResultsTimeTable;
    }

    private void initData(TimeTableVm timeTableVm) {
        this.teachers = timeTableVm.getTeachers();
        this.subjects = timeTableVm.getSubjects();
        this.clazzes = timeTableVm.getClazzes();
        this.waitingTimeTableTmp = timeTableVm.getConfigTimeTable();
        List<AvailableTeachingDayVm> availableTeachingDayTmp = timeTableVm.getAvailableTeachingDay();
        if (!CollectionUtils.isEmpty(availableTeachingDayTmp)) {
            for (AvailableTeachingDayVm vm : availableTeachingDayTmp) {
                Teacher teacher = timeTableServiceUtil.findTeacherById(this.teachers, vm.getTeacherId());
                if (ObjectUtils.isEmpty(teacher)) {
                    throw new IllegalArgumentException();
                }
                this.availableTeachingDay.add(new AvailableTeachingDay(teacher, vm.getAvailableDay()));
            }
        }
        this.offLessonConfig = timeTableVm.getOffLessonConfig();

        String mainLessonStr = timeTableVm.getMainLessons();
        this.mainLessons = new ArrayList<>();
        if (!StringUtils.isEmpty(mainLessonStr)) {
            StringTokenizer stringTokenizer = new StringTokenizer(mainLessonStr, ",");
            while (stringTokenizer.hasMoreTokens()) {
                this.mainLessons.add(stringTokenizer.nextToken());
            }
        }

    }

    private boolean validateInputData() throws IllegalArgumentException {
        if (CollectionUtils.isEmpty(this.subjects) || CollectionUtils.isEmpty(this.clazzes) || CollectionUtils.isEmpty(this.teachers) || CollectionUtils.isEmpty(this.waitingTimeTableTmp)) {
            return false;
        }

        for (LessonVm lessonVm : this.waitingTimeTableTmp) {
            Teacher teacher = timeTableServiceUtil.findTeacherById(this.teachers, lessonVm.getTeacherId());
            if (ObjectUtils.isEmpty(teacher)) {
                throw new IllegalArgumentException();
            }

            Subject subject = timeTableServiceUtil.findSubjectById(this.subjects, lessonVm.getSubjectId());
            if (ObjectUtils.isEmpty(subject)) {
                throw new IllegalArgumentException();
            }

            Clazz clazz = timeTableServiceUtil.findClazzById(this.clazzes, lessonVm.getClazzId());
            if (ObjectUtils.isEmpty(clazz)) {
                throw new IllegalArgumentException();
            }
            this.waitingTimeTables.add(new Lesson(teacher, subject, clazz, lessonVm.getLessonQuantity()));
        }

        return true;
    }

    /**
     * 0. chuẩn bị dữ liệu
     */
    private void prepareData() {
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
                if (timeTableServiceUtil.isCCOrSH(day, order)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
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

                        if (!ObjectUtils.isEmpty(this.offLessonConfig) && !this.offLessonConfig.isEmpty()) {
                            if (timeTableServiceUtil.hasOffLesson(this.waitingTimeTables, lesson.getClazz().getGrade())) {
                                List<OffLessonConfig> offLessonConfigs = this.offLessonConfig.get(lesson.getClazz().getGrade());
                                int finalDay = day;
                                int finalOrder = order;
                                if (!CollectionUtils.isEmpty(offLessonConfigs) && offLessonConfigs.stream().anyMatch(c -> c.getOffDay() == finalDay && c.getOffOrder() == finalOrder)) {
                                    if (!lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON)) {
                                        continue;
                                    }
                                }
                            }
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
        Subject saluteFlagSubject = timeTableServiceUtil.getStaticSubject(this.subjects, StaticSubject.SALUTE_FLAG.value);
        // tìm môn sinh hoạt lớp
        Subject classMeetingSubject = timeTableServiceUtil.getStaticSubject(this.subjects, StaticSubject.CLASS_MEETING.value);

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
                    .lessonQuantity(1)
                    .build();
            saluteFlagLessons.add(saluteFlagLesson);

            // sinh hoạt
            Teacher headTeacher = timeTableServiceUtil.findHeadTeacher(this.teachers, clazz.getName());
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
                                && timeTableServiceUtil.isTeacherBusy(this.timeTables, day, order, lesson.getClazz(), teacher)
                        ) {
                            // rơi vào tình huống trùng lịch thì tìm giáo viên thay thế
                            LessonKey replacementLessonKey = timeTableServiceUtil.findFirstReplacement(offLessonConfig, timeTables, day, order, lesson);
                            if (ObjectUtils.isEmpty(replacementLessonKey)) { // không tìm được giáo viên thay thế
                                hasIssue = true;
                                continue;
                            }
                            // đảo 2 tiết cho nhau ==> giáo viên hết bị trùng lịch
                            List<Lesson> replacementLessons = this.timeTables.get(replacementLessonKey);
                            Lesson replacementLesson = timeTableServiceUtil.findByClassName(replacementLessons, lesson.getClazz().getName());

                            this.timeTables.get(lessonKey).set(k, replacementLesson);
                            this.timeTables.get(replacementLessonKey).set(k, lesson);
                        }
                    }
                }
            }
        } while (hasIssue);
        System.out.println("generation: " + generation);
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
                    if (timeTableServiceUtil.isCCOrSH(day, order)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                        continue;
                    }
                    LessonKey lessonKey = new LessonKey(day, order);
                    for (int k = 0; k < this.timeTables.get(lessonKey).size(); k++) {
                        Lesson lesson = this.timeTables.get(lessonKey).get(k);

                        // những môn đã có 2 tiết liền nhau không được đổi nữa: VănKT, Toán, tin...
                        // trường hợp môn được đổi
                        if (timeTableServiceUtil.checkBlockSubject(this.timeTables, lesson, day, order, k)) {
                            continue;
                        }

                        List<LessonKey> allReplacement = timeTableServiceUtil.findAllReplacement(day, order, lesson,
                                this.availableTeachingDay, this.timeTables, this.offLessonConfig);
                        if (ObjectUtils.isEmpty(allReplacement)) {
                            continue;
                        }
                        for (int j = 0; j < allReplacement.size(); j++) {
                            LessonKey currentLessonKey = allReplacement.get(j);
                            List<Lesson> allReplacementLesson = this.timeTables.get(currentLessonKey);
                            Lesson replacementLesson = timeTableServiceUtil.findByClassName(allReplacementLesson, lesson.getClazz().getName());

                            // cùng 1 môn không cần đổi
                            if (lesson.getSubject().getName().equals(replacementLesson.getSubject().getName())) {
                                continue;
                            }

                            // nếu tiết tìm được hoặc tiết hiện tại mang đi đổi mà bị trùng tiết giáo viên ==> bỏ qua không đổi
                            if (timeTableServiceUtil.isTeacherBusy(this.timeTables, currentLessonKey.getDay(), currentLessonKey.getOrder(), lesson.getClazz(), lesson.getTeacher())
                                    || timeTableServiceUtil.isTeacherBusy(this.timeTables, day, order, replacementLesson.getClazz(), replacementLesson.getTeacher())) {
                                continue;
                            }

                            // Trường hợp ngược lại môn bị đổi: môn đã có hai tiết liền nhau
                            if (timeTableServiceUtil.checkBlockSubject(this.timeTables, replacementLesson, currentLessonKey.getDay(), currentLessonKey.getOrder(), k)) {
                                continue;
                            }

                            // tránh 1 ngày có cả Văn và 2 VănKT, Toán và 2 ToánKT (3 tiết văn hoặc toán), ... đối với các môn có từ 4 tiết 1 tuần trở lên
                            if (!CollectionUtils.isEmpty(this.mainLessons)) {
                                if (timeTableServiceUtil.checkTripleLessonInTheSameDay(this.timeTables, this.mainLessons, lesson, currentLessonKey.getDay(), currentLessonKey.getOrder())
                                        || timeTableServiceUtil.checkTripleLessonInTheSameDay(this.timeTables, this.mainLessons, replacementLesson, day, order)) {
                                    continue;
                                }
                            }

                            // tiết nghỉ theo cấu hình người dùng hoặc phải là tiết cuối của các buổi
                            if ((lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && !timeTableServiceUtil.isFitOffLesson(this.offLessonConfig, currentLessonKey.getDay(), currentLessonKey.getOrder(), lesson))
                                    || replacementLesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && !timeTableServiceUtil.isFitOffLesson(this.offLessonConfig, day, order, replacementLesson)) {
                                continue;
                            }

                            // môn học tránh tiết cuối
                            if ((lesson.getSubject().getAvoidLastLesson() && currentLessonKey.getOrder() == TimeTableConstants.LAST_ORDER)
                                    || (replacementLesson.getSubject().getAvoidLastLesson() && order == TimeTableConstants.LAST_ORDER)) {
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
                                this.bestResultsTimeTable = timeTableServiceUtil.saveBestResult(this.timeTables);
                            }
                            System.out.println("Lần chạy thứ " + i + " kết quả " + score + ", tốt nhất " + max_score);
                            this.timeTables = timeTableServiceUtil.saveBestResult(this.bestResultsTimeTable);
                        }
                    }
                }
            }
        }
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
                    // Môn học tránh tiết cuối
                    if (lesson.getSubject().getAvoidLastLesson() && order == TimeTableConstants.LAST_ORDER) {
                        score -= 200;
                    } else {
                        score += 200;
                    }
                    // tiết nghỉ ở cuối ngày
                    if (lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && order == TimeTableConstants.LAST_ORDER) {
                        score += 100;
                    } else {
                        score -= 100;
                    }
                    // Môn học block nhưng nó lại không liền
                    if (lesson.getSubject().getBlockNumber() == 2) {

                        // kiểm tra tiết trước
                        boolean before = false;
                        List<Lesson> lessonList = this.timeTables.get(new LessonKey(day, order - 1));
                        Lesson previousLesson = timeTableServiceUtil.findByClassName(lessonList, lesson.getClazz().getName());
                        if (!ObjectUtils.isEmpty(previousLesson)) {
                            before = previousLesson.getSubject().getName().equals(lesson.getSubject().getName());
                        }

                        // kiểm tra tiết sau
                        boolean after = false;
                        List<Lesson> lessonListAfter = this.timeTables.get(new LessonKey(day, order + 1));
                        Lesson afterLesson = timeTableServiceUtil.findByClassName(lessonListAfter, lesson.getClazz().getName());
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
                    if (timeTableServiceUtil.isTeacherBusy(this.timeTables, day, order, lesson.getClazz(), lesson.getTeacher())) {
                        score -= 500;
                    }

                    // Phân bố đều các môn, ví dụ môn Địa,Sinh... 1 tuần có 2 tiết sẽ có tiết cách ngày
                    if (lesson.getSubject().getRequireSpacing()) {
                        boolean checkSpacing = timeTableServiceUtil.checkAdjacentLesson(day, order, lesson, this.timeTables);
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

                    // một ngày có 3 tiết học giống nhau: Toán, Văn, T.Anh, KHTN
                    if (!CollectionUtils.isEmpty(this.mainLessons)) {
                        if (timeTableServiceUtil.checkTripleLessonInTheSameDay(this.timeTables, this.mainLessons, lesson, day, order)) {
                            score -= 200;
                        }
                    }

                }
            }
        }
        return score;
    }

}
