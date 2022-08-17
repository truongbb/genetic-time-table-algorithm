package com.github.truongbb.timetableschedule.schedule;

import com.github.truongbb.timetableschedule.constant.StaticSubject;
import com.github.truongbb.timetableschedule.constant.TimeTableConstants;
import com.github.truongbb.timetableschedule.dto.LessonKey;
import com.github.truongbb.timetableschedule.dto.OffLessonConfig;
import com.github.truongbb.timetableschedule.entity.*;
import com.github.truongbb.timetableschedule.repository.availableTeachingDay.TeachingDayRepository;
import com.github.truongbb.timetableschedule.repository.clazz.ClazzRepository;
import com.github.truongbb.timetableschedule.repository.subject.SubjectRepository;
import com.github.truongbb.timetableschedule.repository.teacher.TeacherRepository;
import com.github.truongbb.timetableschedule.repository.timetableconfig.LessonRepository;
import com.github.truongbb.timetableschedule.service.timetable.TimeTableServiceUtil;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final TimeTableServiceUtil timeTableServiceUtil;

    private Map<LessonKey, List<Lesson>> bestResultsTimeTable;

    //    private final Map<String, List<OffLessonConfig>> OFF_LESSONS_CONFIG = new HashMap<>();
    private final Map<String, List<OffLessonConfig>> OFF_LESSONS_CONFIG = null;

    public TimeTableScheduler(SubjectRepository subjectRepository, TeacherRepository teacherRepository,
                              ClazzRepository clazzRepository, LessonRepository lessonRepository,
                              TeachingDayRepository teachingDayRepository, TimeTableServiceUtil timeTableServiceUtil) {
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.clazzRepository = clazzRepository;
        this.lessonRepository = lessonRepository;
        this.teachingDayRepository = teachingDayRepository;
        this.timeTableServiceUtil = timeTableServiceUtil;

//        OFF_LESSONS_CONFIG.put("10", Arrays.asList(new OffLessonConfig(3, 4), new OffLessonConfig(3, 5), new OffLessonConfig(5, 5)));
//        OFF_LESSONS_CONFIG.put("11", Arrays.asList(new OffLessonConfig(3, 5), new OffLessonConfig(5, 5), new OffLessonConfig(6, 5)));
//        OFF_LESSONS_CONFIG.put("12", Arrays.asList(new OffLessonConfig(2, 5), new OffLessonConfig(6, 5)));
//        OFF_LESSONS_CONFIG.put("9", Arrays.asList(new OffLessonConfig(2, 5), new OffLessonConfig(6, 5)));

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
        this.timeTableServiceUtil.showOutput(this.clazzes, this.timeTables);

        this.evolutionToCorrect();
        this.timeTableServiceUtil.showOutput(this.clazzes, this.timeTables);

        this.fineTuning(0, 15);
        this.timeTableServiceUtil.showOutput(this.clazzes, this.bestResultsTimeTable);
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
                if (this.timeTableServiceUtil.isCCOrSH(day, order)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
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

                        if (!ObjectUtils.isEmpty(OFF_LESSONS_CONFIG) && !OFF_LESSONS_CONFIG.isEmpty()) {
                            if (this.timeTableServiceUtil.hasOffLesson(this.waitingTimeTables, lesson.getClazz().getGrade())) {
                                List<OffLessonConfig> offLessonConfigs = OFF_LESSONS_CONFIG.get(lesson.getClazz().getGrade());
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
                                && this.timeTableServiceUtil.isTeacherBusy(this.timeTables, day, order, lesson.getClazz(), teacher)
                        ) {
                            // rơi vào tình huống trùng lịch thì tìm giáo viên thay thế
                            LessonKey replacementLessonKey = this.timeTableServiceUtil.findFirstReplacement(OFF_LESSONS_CONFIG, this.timeTables, day, order, lesson);
                            if (ObjectUtils.isEmpty(replacementLessonKey)) { // không tìm được giáo viên thay thế
                                hasIssue = true;
                                continue;
                            }
                            // đảo 2 tiết cho nhau ==> giáo viên hết bị trùng lịch
                            List<Lesson> replacementLessons = this.timeTables.get(replacementLessonKey);
                            Lesson replacementLesson = this.timeTableServiceUtil.findByClassName(replacementLessons, lesson.getClazz().getName());

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
     */
    private void fineTuning(int from, int to) {
        this.bestResultsTimeTable = this.timeTables;
        int max_score = Integer.MIN_VALUE;
        for (int i = from; i < to; i++) {
            for (int day = TimeTableConstants.FIRST_DAY; day <= TimeTableConstants.LAST_DAY; day++) {
                for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) {
                    if (this.timeTableServiceUtil.isCCOrSH(day, order)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                        continue;
                    }
                    LessonKey lessonKey = new LessonKey(day, order);
                    for (int k = 0; k < this.timeTables.get(lessonKey).size(); k++) {
                        Lesson lesson = this.timeTables.get(lessonKey).get(k);

                        // những môn đã có 2 tiết liền nhau không được đổi nữa: VănKT, Toán, tin...
                        // trường hợp môn được đổi
                        if (this.timeTableServiceUtil.checkBlockSubject(this.timeTables, lesson, day, order, k)) {
                            continue;
                        }

                        List<LessonKey> allReplacement = this.timeTableServiceUtil.findAllReplacement(day, order, lesson, this.availableTeachingDay, this.timeTables, OFF_LESSONS_CONFIG);
                        if (ObjectUtils.isEmpty(allReplacement)) {
                            continue;
                        }
                        for (int j = 0; j < allReplacement.size(); j++) {
                            LessonKey currentLessonKey = allReplacement.get(j);
                            List<Lesson> allReplacementLesson = this.timeTables.get(currentLessonKey);
                            Lesson replacementLesson = this.timeTableServiceUtil.findByClassName(allReplacementLesson, lesson.getClazz().getName());

                            // cùng 1 môn không cần đổi
                            if (lesson.getSubject().getName().equals(replacementLesson.getSubject().getName())) {
                                continue;
                            }

                            // nếu tiết tìm được hoặc tiết hiện tại mang đi đổi mà bị trùng tiết giáo viên ==> bỏ qua không đổi
                            if (this.timeTableServiceUtil.isTeacherBusy(this.timeTables, currentLessonKey.getDay(), currentLessonKey.getOrder(), lesson.getClazz(), lesson.getTeacher())
                                    || this.timeTableServiceUtil.isTeacherBusy(this.timeTables, day, order, replacementLesson.getClazz(), replacementLesson.getTeacher())) {
                                continue;
                            }

                            // Trường hợp ngược lại môn bị đổi: môn đã có hai tiết liền nhau
                            if (this.timeTableServiceUtil.checkBlockSubject(this.timeTables, replacementLesson, currentLessonKey.getDay(), currentLessonKey.getOrder(), k)) {
                                continue;
                            }

                            // tránh 1 ngày có cả Văn và 2 VănKT, Toán và 2 ToánKT (3 tiết văn hoặc toán), ... đối với các môn có từ 4 tiết 1 tuần trở lên
                            if (this.timeTableServiceUtil.checkTripleLessonInTheSameDay(this.timeTables, lesson, currentLessonKey.getDay(), currentLessonKey.getOrder())
                                    || this.timeTableServiceUtil.checkTripleLessonInTheSameDay(this.timeTables, replacementLesson, day, order)) {
                                continue;
                            }

                            // tiết nghỉ theo cấu hình người dùng hoặc phải là tiết cuối của các buổi
                            if ((lesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && !this.timeTableServiceUtil.isFitOffLesson(OFF_LESSONS_CONFIG, currentLessonKey.getDay(), currentLessonKey.getOrder(), lesson))
                                    || replacementLesson.getSubject().getName().equals(TimeTableConstants.OFF_LESSON) && !this.timeTableServiceUtil.isFitOffLesson(OFF_LESSONS_CONFIG, day, order, replacementLesson)) {
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
                                this.bestResultsTimeTable = this.timeTableServiceUtil.saveBestResult(this.timeTables);
                            }
                            System.out.println("Lần chạy thứ " + i + " kết quả " + score + ", tốt nhất " + max_score);
                            this.timeTables = this.timeTableServiceUtil.saveBestResult(this.bestResultsTimeTable);
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
                        Lesson previousLesson = this.timeTableServiceUtil.findByClassName(lessonList, lesson.getClazz().getName());
                        if (!ObjectUtils.isEmpty(previousLesson)) {
                            before = previousLesson.getSubject().getName().equals(lesson.getSubject().getName());
                        }

                        // kiểm tra tiết sau
                        boolean after = false;
                        List<Lesson> lessonListAfter = this.timeTables.get(new LessonKey(day, order + 1));
                        Lesson afterLesson = this.timeTableServiceUtil.findByClassName(lessonListAfter, lesson.getClazz().getName());
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
                    if (this.timeTableServiceUtil.isTeacherBusy(this.timeTables, day, order, lesson.getClazz(), lesson.getTeacher())) {
                        score -= 500;
                    }

                    // Phân bố đều các môn, ví dụ môn Địa,Sinh... 1 tuần có 2 tiết sẽ có tiết cách ngày
                    if (lesson.getSubject().getRequireSpacing()) {
                        boolean checkSpacing = this.timeTableServiceUtil.checkAdjacentLesson(day, order, lesson, this.timeTables);
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
                    if (this.timeTableServiceUtil.checkTripleLessonInTheSameDay(this.timeTables, lesson, day, order)) {
                        score -= 200;
                    }

                }
            }
        }
        return score;
    }

}
