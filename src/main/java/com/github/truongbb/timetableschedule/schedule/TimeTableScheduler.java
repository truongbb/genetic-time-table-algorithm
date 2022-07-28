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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TimeTableScheduler {

    private List<Clazz> clazzes; // toàn bộ lớp học
    private List<Teacher> teachers; // toàn bộ giáo viên
    private List<Subject> subjects; // toàn bộ môn học
    private Map<LessonKey, List<Lesson>> timeTables; // các tiết học kết quả
    private List<Lesson> waitingTimeTables; // các tiết học chờ xếp lịch (chỉ có lớp và môn, chưa biết ai dạy và dạy vào buổi nào)

    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ClazzRepository clazzRepository;
    private final LessonRepository lessonRepository;


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

        System.out.println("\t\t\t\t\t\t 6A \t\t\t\t 6B \t\t\t\t 6C \t\t\t\t 6D \t\t\t\t 7A \t\t\t\t 7B \t\t\t\t 7C \t\t\t\t 7D \t\t\t\t 8A \t\t\t\t 8B \t\t\t\t 8C \t\t\t\t 8D \t\t\t\t 9A \t\t\t\t 9B \t\t\t\t 9C \t\t\t\t 9D");
        timeTables
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
                        if (ObjectUtils.isEmpty(l) || ObjectUtils.isEmpty(l.getTeacher()) || ObjectUtils.isEmpty(l.getSubject()) || ObjectUtils.isEmpty(l.getSubject())) {
                            return;
                        }
                        System.out.print("\t\t" + l.getTeacher().getName() + " | " + l.getSubject().getName() + "\t\t");
                    });
                    System.out.println();
                });

//        this.evolutionToCorrect();
        // fineTuning
    }

    private void prepareData() {
        // load all data from DB
        this.clazzes = clazzRepository.getAll();
        this.subjects = subjectRepository.getAll();
        this.teachers = teacherRepository.getAll();

        // lấy tất cả data ở bảng time_table (đây là dữ liệu khởi tạo từ trước)
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
     * 1. Tạo thời khóa biểu cơ sở (quần thể cơ sở): chỉ cần đủ các tiết, giáo viên, đủ giờ dạy, không quan tâm tới các yếu tố trùng tiết và yếu tốt làm tốt
     */
    private void generateBase() {

        // đặt tiết chào cờ đầu tuần và tiết sinh hoạt cuối tuần
        this.setStaticLesson();

        /**
         * lặp tối đa 30 lần để rải hết các môn học, đáng lẽ không cần lặp tuy nhiên trong quá trình rải đôi khi 1 số môn bị chưa đủ thời lượng học
         * do môn đó chiếm 2 tiết, mà giả sử thứ tự rải hiện tại đang ở tiết 4, thì tạm thời để môn đó waiting rồi lần rải sau sẽ rải lại
         * 1 tuần có max là 30 tiết nên cứ cho lặp lại cho chắc, hết waiting thì break
         */

        for (int i = 1; i <= 30; i++) {
            for (int day = TimeTableConstants.FIRST_DAY; day <= TimeTableConstants.LAST_DAY; day++) { // từ thứ 2 tới thứ bảy
                for (int order = TimeTableConstants.FIRST_ORDER; order <= TimeTableConstants.LAST_ORDER; order++) { // từ tiết 1 tới tiết 5
                    if ((day == TimeTableConstants.FIRST_DAY && order == TimeTableConstants.FIRST_ORDER) ||
                            (day == TimeTableConstants.LAST_DAY && order == TimeTableConstants.LAST_ORDER)) { // bỏ qua tiết chào cờ và sinh hoạt lớp
                        continue;
                    }
                    for (int k = 0; k < this.waitingTimeTables.size(); k++) {
                        Lesson lesson = waitingTimeTables.get(k);
                        List<Lesson> lessons = timeTables.get(new LessonKey(day, order));

                        // nếu trùng vào ngày đó, tiết đó, lớp đó có môn rồi --> duyệt tiếp, không chèn vào tiết học khác đã tồn tại
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

                        // ghi tiết nghỉ nếu tiết này là tiết nghỉ
                        if (this.isOffLesson(day, order, lesson.getClazz().getName().substring(0, 1))) {
                            // TODO - thêm vào timetables
                        }

                        // ghi tiết đầu tiên đang chờ
                        Lesson temp = new Lesson();
                        temp.setSubject(lesson.getSubject());
                        temp.setTeacher(lesson.getTeacher());
                        temp.setClazz(lesson.getClazz());

                        this.setLesson(day, order, temp, k);
                    }
                }
            }
            if (this.waitingTimeTables.isEmpty()) {
                break;
            }
        }
    }

    private boolean isOffLesson(int day, int order, String substring) {
        // TODO - check tiết nghỉ
        return false;
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
     * 2. Tiến hóa đến mức đúng
     */
    private void evolutionToCorrect() {
        boolean hasIssue = true;
        int generation = 0;
        do {
            generation++;
            hasIssue = false;
            for (int day = 2; day < TimeTableConstants.LAST_DAY; day++) {
                for (int order = 1; order < TimeTableConstants.LAST_ORDER; order++) {
                    LessonKey lessonKey = new LessonKey(day, order);
                    List<Lesson> lessons = this.timeTables.get(lessonKey);
                    for (int k = 0; k < lessons.size(); k++) {
                        Lesson lesson = lessons.get(k);
                        Teacher teacher = lesson.getTeacher();
                        if (!ObjectUtils.isEmpty(teacher) && this.isTeacherBusy(day, order, lesson.getClazz(), teacher)) {
                            lesson.setTeacherBusy(true);
                            // rơi vào tình huống trùng lịch thì tìm giáo viên thay thế
                            LessonKey replacementLessonKey = this.findFirstReplacement(day, order, lesson.getClazz());
                            if (ObjectUtils.isEmpty(replacementLessonKey)) { // không tìm được giáo viên thay thế
                                hasIssue = true;
                                continue;
                            }
                            // sau khi đảo xong thì cả 2 giáo viên đã hết bị trùng tiết
                            List<Lesson> replacementLessons = this.timeTables.get(replacementLessonKey);
                            Lesson replacementLesson = findByClassName(replacementLessons, lesson.getClazz().getName());// xem lại
                            replacementLesson.setTeacherBusy(false);
                            lesson.setTeacherBusy(false);

                            this.timeTables.get(lessonKey).set(k, replacementLesson);
                            this.timeTables.get(replacementLessonKey).set(k, lesson);
                        } else {
                            lesson.setTeacherBusy(true);
                        }
                    }
                }
            }
        } while (hasIssue);
        System.out.println(generation);
    }

    private LessonKey findFirstReplacement(int replaceDay, int replaceOrder, Clazz clazz) {
        for (int day = 2; day < TimeTableConstants.LAST_DAY; day++) {
            for (int order = 1; order < TimeTableConstants.LAST_ORDER; order++) {
                LessonKey lessonKey = new LessonKey(day, order);
                List<Lesson> lessons = this.timeTables.get(lessonKey);
                Lesson lesson = this.findByClassName(lessons, clazz.getName());
                if (ObjectUtils.isEmpty(lesson)) {
                    return null;
                }
                if (!ObjectUtils.isEmpty(lesson.getTeacher()) && !lesson.isStatic()) {
                    // nếu có giáo viên và giáo viên đó có thể dạy (không vướng lịch bận của giáo viên, không trùng vào tiết sinh hoạt, chào cờ, ...)
                    if (!this.isTeacherBusy(replaceDay, replaceOrder, clazz, lesson.getTeacher())) {
                        // và ngược lại giáo viên hôm nay đảo sang hôm đó cũng không bị trùng lịch
                        Lesson replaceLesson = this.findByClassName(lessons, clazz.getName());
                        if (ObjectUtils.isEmpty(replaceLesson)) {
                            return null;
                        }
                        if (!this.isTeacherBusy(day, order, clazz, replaceLesson.getTeacher())) {
                            return lessonKey;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isTeacherBusy(int day, int order, Clazz clazz, Teacher teacher) {
        // TODO - check giáo viên bận
        return false;
    }

    private Lesson findByClassName(List<Lesson> lessons, String className) {
        if (CollectionUtils.isEmpty(lessons) || StringUtils.isEmpty(className)) {
            return null;
        }
        return lessons.stream().filter(lesson -> lesson.getClazz().getName().equals(className)).findFirst().orElse(null);
    }


}
