package com.github.truongbb.timetableschedule.schedule;

import com.github.truongbb.timetableschedule.constant.StaticSubject;
import com.github.truongbb.timetableschedule.constant.TimeTableConstants;
import com.github.truongbb.timetableschedule.dto.LessonKey;
import com.github.truongbb.timetableschedule.entity.Clazz;
import com.github.truongbb.timetableschedule.entity.Lesson;
import com.github.truongbb.timetableschedule.entity.Subject;
import com.github.truongbb.timetableschedule.entity.Teacher;
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
    private Map<LessonKey, List<Lesson>> timeTables; // các tiết học kết quả
    private List<Lesson> waitingTimeTables; // các tiết học chờ xếp lịch (giáo viên được gán vào môn mình dạy và lớp mình dạy, cùng với tổng số tiết cần dạy trong tuần, nhưng chưa được xếp vào ngày nào, tiết nào)

    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ClazzRepository clazzRepository;
    private final LessonRepository lessonRepository;

    private Map<LessonKey, List<Lesson>> bestResultsTimeTable;
    private Map<LessonKey, List<Lesson>> timeTableTemp;

    public TimeTableScheduler(SubjectRepository subjectRepository, TeacherRepository teacherRepository, ClazzRepository clazzRepository, LessonRepository lessonRepository) {
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.clazzRepository = clazzRepository;
        this.lessonRepository = lessonRepository;
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

        this.fineTuning(0, 3);
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

        // lấy tất cả data ở bảng time_table (đây là dữ liệu khởi tạo từ trước trong DB)
        this.waitingTimeTables = lessonRepository.getAll();

        this.waitingTimeTables.forEach(lesson -> {
            if (ObjectUtils.isEmpty(lesson.getTeacher())) {
                lesson.setStatic(true);
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

        /**
         * lặp tối đa 30 lần để rải hết các môn học, đáng lẽ không cần lặp tuy nhiên trong quá trình rải đôi khi 1 số môn bị chưa đủ thời lượng học
         * do môn đó chiếm 2 tiết, mà giả sử thứ tự rải hiện tại đang ở tiết 4, thì tạm thời để môn đó waiting rồi lần rải sau sẽ rải lại
         * 1 tuần có max là 30 tiết nên cứ cho lặp lại cho chắc, hết waiting thì break
         */
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
                        if (currentClazz.getName().charAt(0) == '6') {
                            if ((day == 3 && order == 5) || (day == 5 && order == 5) || (day == 6 && order == 5)) {
                                if (!lesson.getSubject().getName().equals("Nghỉ")) {
                                    continue;
                                }
                            }
                        }

                        if (currentClazz.getName().charAt(0) == '7') {
                            if ((day == 3 && order == 5) || (day == 5 && order == 5)) {
                                if (!lesson.getSubject().getName().equals("Nghỉ")) {
                                    continue;
                                }
                            }
                        }
                        if (currentClazz.getName().charAt(0) == '9') {
                            if ((day == 5 && order == 5)) {
                                if (!lesson.getSubject().getName().equals("Nghỉ")) {
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
        Subject saluteFlagSubject = subjectRepository.getStaticSubject(StaticSubject.SALUTE_FLAG.value);
        // tìm môn sinh hoạt lớp
        Subject classMeetingSubject = subjectRepository.getStaticSubject(StaticSubject.CLASS_MEETING.value);

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
            Teacher headTeacher = teacherRepository.findHeadTeacher(clazz.getName());
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

    /**
     * 2. Tiến hóa đến mức đúng, để đảm bảo các tiêu chí sau:
     * - Không trùng tiết của giáo viên
     * - Không trùng phòng máy thực hành
     * - Phân bố đều các môn còn lại, ví dụ không học 2 môn Địa cùng 1 ngày hoặc 2 ngày sát nhau
     */
    private void evolutionToCorrect() {
        boolean hasIssue = true;
        int generation = 0;
        do {
            generation++;
            hasIssue = false;
            for (int day = 2; day <= TimeTableConstants.LAST_DAY; day++) {
                for (int order = 1; order <= TimeTableConstants.LAST_ORDER; order++) {
                    LessonKey lessonKey = new LessonKey(day, order);
                    List<Lesson> lessons = this.timeTables.get(lessonKey);
                    for (int k = 0; k < lessons.size(); k++) {
                        Lesson lesson = lessons.get(k);
                        // kiểm tra giáo viên trùng lịch
                        Teacher teacher = lesson.getTeacher();
                        if (!ObjectUtils.isEmpty(teacher) && this.isTeacherBusy(day, order, lesson.getClazz(), teacher)) {
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
        timeTableTemp = timeTables.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
                if (!ObjectUtils.isEmpty(lesson.getTeacher())) {
                    // nếu có giáo viên và giáo viên đó có thể dạy (không vướng lịch bận của giáo viên, không trùng vào tiết sinh hoạt, chào cờ, ...)
                    if (!this.isTeacherBusy(replaceDay, replaceOrder, clazz, lesson.getTeacher())) {
                        // và ngược lại giáo viên hôm nay đảo sang hôm đó cũng không bị trùng lịch
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
                        !ObjectUtils.isEmpty(lesson.getClazz()) && !ObjectUtils.isEmpty(lesson.getTeacher()) && !lesson.getClazz().getId().equals(clazz.getId())
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
                        if (lesson.getSubject().getBlockNumber() == 2) {
                            boolean checkSub1 = false;
                            boolean checkSub2 = false;
                            if (order < 3) {
                                Lesson lesson1 = this.timeTables.get(new LessonKey(day, order + 1)).get(k);
                                checkSub1 = lesson.getSubject().getName().equals(lesson1.getSubject().getName());
                                Lesson lesson2 = this.timeTables.get(new LessonKey(day, order + 2)).get(k);
                                checkSub2 = lesson.getSubject().getName().equals(lesson2.getSubject().getName());

                            } else {
                                Lesson lesson1 = this.timeTables.get(new LessonKey(day, order - 1)).get(k);
                                checkSub1 = lesson.getSubject().getName().equals(lesson1.getSubject().getName());
                                Lesson lesson2 = this.timeTables.get(new LessonKey(day, order - 2)).get(k);
                                checkSub2 = lesson.getSubject().getName().equals(lesson2.getSubject().getName());

                            }
                            if (checkSub1 && !checkSub2) {
                                continue;
                            }
                            if (order != 1 && order != 5) {
                                Lesson lesson1 = this.timeTables.get(new LessonKey(day, order - 1)).get(k);
                                checkSub1 = lesson.getSubject().getName().equals(lesson1.getSubject().getName());
                                Lesson lesson2 = this.timeTables.get(new LessonKey(day, order + 1)).get(k);
                                checkSub2 = lesson.getSubject().getName().equals(lesson2.getSubject().getName());
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

                            // ưu tiên những môn xếp 2 tiết liền nhau

                            if (lesson.getSubject().getBlockNumber() == 2) {
                                boolean beforeConnectedLesson = false;
                                boolean afterConnectedLesson = false;
                                if (currentLessonKey.getOrder() > 1) {
                                    //timeTableTemp = timeTables1(currentLessonKey.getDay(), currentLessonKey.getOrder(), lesson, replacementLesson, true);
                                    LessonKey lessonKey1 = new LessonKey(currentLessonKey.getDay(), currentLessonKey.getOrder() - 1);
                                    List<Lesson> findSub = this.timeTables.get(lessonKey1);
                                    Lesson beforeSub = this.findByClassName(findSub, lesson.getClazz().getName());
                                    if (beforeSub.getSubject().getName().equals(lesson.getSubject().getName())) {
                                        beforeConnectedLesson = true;
                                    }
                                }
                                if (currentLessonKey.getOrder() < 5) {
                                    LessonKey lessonKey1 = new LessonKey(currentLessonKey.getDay(), currentLessonKey.getOrder() + 1);
                                    List<Lesson> findSub = this.timeTables.get(lessonKey1);
                                    Lesson afterSub = this.findByClassName(findSub, lesson.getClazz().getName());
                                    if (afterSub.getSubject().getName().equals(lesson.getSubject().getName())) {
                                        afterConnectedLesson = true;
                                    }
                                }
//                                if ((beforeConnectedLesson) && (afterConnectedLesson)) {
//                                    continue;
//                                }
                                if (beforeConnectedLesson || afterConnectedLesson) {
                                    this.timeTables.get(lessonKey).set(k, replacementLesson);
                                    this.timeTables.get(currentLessonKey).set(k, lesson);
                                    int score = this.fitness();
                                    // nếu kết quả tốt hơn thì lưu kết quả tốt nhất
                                    if (max_score < score) {
                                        max_score = score;
                                        this.bestResultsTimeTable = this.timeTables;
                                    }
                                    System.out.println("Lần chạy thứ " + j + " kết quả " + score + ", tốt nhất " + max_score);
                                    break;
                                }
                            }
                            if (replacementLesson.getSubject().getBlockNumber() == 2) {
                                boolean beforeConnectedLesson = false;
                                boolean afterConnectedLesson = false;
                                if (order > 1) {
                                    LessonKey lessonKey1 = new LessonKey(day, order - 1);
                                    List<Lesson> findSub = this.timeTables.get(lessonKey1);
                                    Lesson beforeSub = this.findByClassName(findSub, replacementLesson.getClazz().getName());
                                    if (beforeSub.getSubject().getName().equals(replacementLesson.getSubject().getName())) {
                                        beforeConnectedLesson = true;

                                    }
                                }
                                if (order < 5) {
                                    LessonKey lessonKey1 = new LessonKey(day, order + 1);
                                    List<Lesson> findSub = this.timeTables.get(lessonKey1);
                                    Lesson afterSub = this.findByClassName(findSub, replacementLesson.getClazz().getName());
                                    if (afterSub.getSubject().getName().equals(replacementLesson.getSubject().getName())) {
                                        afterConnectedLesson = true;
                                    }
                                }
//                                if ((beforeConnectedLesson) && (afterConnectedLesson)) {
//                                    continue;
//                                }
                                if (beforeConnectedLesson || afterConnectedLesson) {
                                    this.timeTables.get(lessonKey).set(k, replacementLesson);
                                    this.timeTables.get(currentLessonKey).set(k, lesson);
                                    int score = this.fitness();
                                    // nếu kết quả tốt hơn thì lưu kết quả tốt nhất
                                    if (max_score < score) {
                                        max_score = score;
                                        this.bestResultsTimeTable = this.timeTables;
                                    }
                                    System.out.println("Lần chạy thứ " + j + " kết quả " + score + ", tốt nhất " + max_score);
                                    break;
                                }
                            }

                            // tránh những môn tiết cuối
                            if (lesson.getSubject().getAvoidLastLesson() && currentLessonKey.getOrder() == 5) {
                                continue;
                            }
                            if (order == 5 && replacementLesson.getSubject().getAvoidLastLesson()) {
                                continue;
                            }

                            // môn sinh, địa không học 2 ngày liên tiếp
                            if (replacementLesson.getSubject().getRequireSpacing()) {
                                boolean afterDayLesson = false;
                                boolean beforeDayLesson = false;
                                if (day == TimeTableConstants.FIRST_DAY) {
                                    afterDayLesson = afterDayLesson(day, 1, lesson, replacementLesson);
                                } else if (day == TimeTableConstants.LAST_DAY) {
                                    beforeDayLesson = beforeDayLesson(day, 1, lesson, replacementLesson);
                                } else {
                                    boolean midDayLesson = false;
                                    for (int repDay = day - 1; repDay <= day + 1; repDay++) {
                                        for (int repOrder = 1; repOrder <= TimeTableConstants.LAST_ORDER; repOrder++) {
                                            LessonKey lessonKey1 = new LessonKey(repDay, repOrder);
                                            List<Lesson> checkExitLesson = this.timeTables.get(lessonKey1);
                                            Lesson replacementLesson1 = this.findByClassName(checkExitLesson, lesson.getClazz().getName());
                                            if (replacementLesson1.getSubject().getName().equals(replacementLesson.getSubject().getName())) {
                                                midDayLesson = true;
                                                break;
                                            }
                                        }
                                        if (midDayLesson)
                                            continue;
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
                                if (currentLessonKey.getDay() == TimeTableConstants.FIRST_DAY) {
                                    afterDayLesson = afterDayLesson(currentLessonKey.getDay(), 1, replacementLesson, lesson);
                                } else if (currentLessonKey.getDay() == TimeTableConstants.LAST_DAY) {
                                    beforeDayLesson = beforeDayLesson(currentLessonKey.getDay(), 1, replacementLesson, lesson);
                                } else {
                                    boolean midDayLesson = false;
                                    for (int repDay = currentLessonKey.getDay() - 1; repDay <= currentLessonKey.getDay() + 1; repDay++) {
                                        for (int repOrder = 1; repOrder <= TimeTableConstants.LAST_ORDER; repOrder++) {
                                            LessonKey lessonKey1 = new LessonKey(repDay, repOrder);
                                            List<Lesson> checkExitLesson = this.timeTables.get(lessonKey1);
                                            Lesson replacementLesson1 = this.findByClassName(checkExitLesson, replacementLesson.getClazz().getName());
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

                            // đổi lịch môn nghỉ

//                            if (lesson.getSubject().getName().equals("Nghỉ") && currentLessonKey.getOrder()!=TimeTableConstants.LAST_ORDER){
//                                continue;
//                            }
//                            if (replacementLesson.getSubject().getName().equals("Nghỉ") && order != TimeTableConstants.LAST_ORDER){
//                                continue;
//                            }

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
                                //this.timeTables = this.timeTableTemp;
                                // TODO - file_put_contents()
                            }
                            System.out.println("Lần chạy thứ " + i + " kết quả " + score + ", tốt nhất " + max_score);
                            //this.timeTables = this.bestResultsTimeTable;
                        }
                        this.timeTables = this.bestResultsTimeTable;
                    }
                }
            }
        }
    }

    private boolean afterDayLesson(int day, int i, Lesson lesson, Lesson replacementLesson) {
        for (int repDay = day; repDay <= day + 1; repDay++) {
            for (int repOrder = 1; repOrder <= TimeTableConstants.LAST_ORDER; repOrder++) {
                LessonKey lessonKey1 = new LessonKey(repDay, repOrder);
                List<Lesson> checkExitLesson = this.timeTables.get(lessonKey1);
                Lesson replacementLesson1 = this.findByClassName(checkExitLesson, lesson.getClazz().getName());
                if (replacementLesson1.getSubject().getName().equals(replacementLesson.getSubject().getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean beforeDayLesson(int day, int i, Lesson lesson, Lesson replacementLesson) {
        for (int repDay = day - 1; repDay <= day; repDay++) {
            for (int repOrder = 1; repOrder <= TimeTableConstants.LAST_ORDER; repOrder++) {
                LessonKey lessonKey1 = new LessonKey(repDay, repOrder);
                List<Lesson> checkExitLesson = this.timeTables.get(lessonKey1);
                Lesson replacementLesson1 = this.findByClassName(checkExitLesson, lesson.getClazz().getName());
                if (replacementLesson1.getSubject().getName().equals(replacementLesson.getSubject().getName())) {
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
                    // Môn học tránh tiết cuối
                    if (order == TimeTableConstants.LAST_ORDER) {
                        if (lesson.getSubject().getAvoidLastLesson()) {
                            score -= 5;
                        } else {
                            score += 5;
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
        List<LessonKey> result = new ArrayList<>();
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
                if (tempLesson.getSubject().getName().equals(subjectName)) {
                    continue;
                }
                if (lesson.getSubject().getAvoidLastLesson() && (order == 5)) {
                    continue;
                }
                if (ObjectUtils.isEmpty(tempLesson)) {
                    return null;
                }
                if (!ObjectUtils.isEmpty(tempLesson.getTeacher())) {
                    // nếu có giáo viên và giáo viên đó có thể dạy (không vướng lịch bận của giáo viên, không trùng vào tiết sinh hoạt, chào cờ, ...)
                    if (!this.isTeacherBusy(replaceDay, replaceOrder, clazz, tempLesson.getTeacher())) {
                        // và ngược lại giáo viên hôm nay đảo sang hôm đó cũng không bị trùng lịch
                        if (!this.isTeacherBusy(day, order, clazz, busyTeacher)) {
                            result.add(new LessonKey(day, order));
                        }
                    }
                }
            }
        }
        return result;
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

