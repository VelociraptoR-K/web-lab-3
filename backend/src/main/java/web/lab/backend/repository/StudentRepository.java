package web.lab.backend.repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

import static jooq.generated.tables.Students.STUDENTS;

import jooq.generated.tables.records.StudentsRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Records;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import web.lab.backend.dto.CreateStudentDTO;
import web.lab.backend.dto.StudentInfoDTO;
import web.lab.backend.dto.StudentPatchDTO;


import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class StudentRepository {

    @Resource(lookup = "java:jboss/datasources/Lab3DataSource")
    private DataSource dataSource;

    private DSLContext dsl;

    @PostConstruct
    private void init() {
        dsl = DSL.using(dataSource, SQLDialect.POSTGRES);
    }


    public List<StudentInfoDTO> getAllStudents() {
        return dsl.select(
                        STUDENTS.ID,
                        STUDENTS.NAME,
                        STUDENTS.SURNAME,
                        STUDENTS.PATRONYMIC,
                        STUDENTS.STUDENT_GROUP,
                        STUDENTS.ISU_ID,
                        STUDENTS.DORMITORY_NUMBER,
                        STUDENTS.ROOM,
                        STUDENTS.MOVE_IN_DATE,
                        STUDENTS.FOREIGNER,
                        STUDENTS.NOTES
                )
                .from(STUDENTS)
                .fetch(Records.mapping(StudentInfoDTO::new));
    }


    public StudentInfoDTO getStudentById(Long id) {
        return dsl.select(
                        STUDENTS.ID,
                        STUDENTS.NAME,
                        STUDENTS.SURNAME,
                        STUDENTS.PATRONYMIC,
                        STUDENTS.STUDENT_GROUP,
                        STUDENTS.ISU_ID,
                        STUDENTS.DORMITORY_NUMBER,
                        STUDENTS.ROOM,
                        STUDENTS.MOVE_IN_DATE,
                        STUDENTS.FOREIGNER,
                        STUDENTS.NOTES
                )
                .from(STUDENTS)
                .where(STUDENTS.ID.eq(id))
                .fetchOne(Records.mapping(StudentInfoDTO::new));
    }


    public StudentInfoDTO createStudent(CreateStudentDTO studentInfo) {
        StudentsRecord created = dsl.insertInto(STUDENTS,
                        STUDENTS.NAME,
                        STUDENTS.SURNAME,
                        STUDENTS.PATRONYMIC,
                        STUDENTS.STUDENT_GROUP,
                        STUDENTS.ISU_ID,
                        STUDENTS.DORMITORY_NUMBER,
                        STUDENTS.ROOM,
                        STUDENTS.MOVE_IN_DATE,
                        STUDENTS.FOREIGNER,
                        STUDENTS.NOTES)
                .values(
                        studentInfo.name(),
                        studentInfo.surname(),
                        studentInfo.patronymic(),
                        studentInfo.group(),
                        studentInfo.isuId(),
                        studentInfo.dormitoryNumber(),
                        studentInfo.room(),
                        studentInfo.moveInDate(),
                        studentInfo.foreigner(),
                        studentInfo.notes()
                )
                .returning(STUDENTS.ID)
                .fetchOne();

        return new StudentInfoDTO(
                created.getId(),
                studentInfo.name(),
                studentInfo.surname(),
                studentInfo.patronymic(),
                studentInfo.group(),
                studentInfo.isuId(),
                studentInfo.dormitoryNumber(),
                studentInfo.room(),
                studentInfo.moveInDate(),
                studentInfo.foreigner(),
                studentInfo.notes()
        );
    }


    public int deleteStudent(Long id) {
        return dsl.deleteFrom(STUDENTS)
                .where(STUDENTS.ID.eq(id))
                .execute();
    }


    public int editStudent(Long id, StudentPatchDTO patchInfo) {
        Map<Field<?>, Object> values = new LinkedHashMap<>();

        if (patchInfo.getName() != null) { values.put(STUDENTS.NAME, patchInfo.getName()); }
        if (patchInfo.getSurname() != null) { values.put(STUDENTS.SURNAME, patchInfo.getSurname()); }
        if (patchInfo.isPatronymicProvided()) { values.put(STUDENTS.PATRONYMIC, patchInfo.getPatronymic()); }
        if (patchInfo.getGroup() != null) { values.put(STUDENTS.STUDENT_GROUP, patchInfo.getGroup()); }
        if (patchInfo.getIsuId() != null) { values.put(STUDENTS.ISU_ID, patchInfo.getIsuId()); }
        if (patchInfo.isDormitoryNumberProvided()) {values.put(STUDENTS.DORMITORY_NUMBER, patchInfo.getDormitoryNumber()); }
        if (patchInfo.isRoomProvided()) {values.put(STUDENTS.ROOM, patchInfo.getRoom()); }
        if (patchInfo.isMoveInDateProvided()) {values.put(STUDENTS.MOVE_IN_DATE, patchInfo.getMoveInDate()); }
        if (patchInfo.getForeigner() != null) { values.put(STUDENTS.FOREIGNER, patchInfo.getForeigner()); }
        if (patchInfo.isNotesProvided()) {values.put(STUDENTS.NOTES, patchInfo.getNotes()); }

        if (values.isEmpty()) {
            return 0;
        }

        return dsl.update(STUDENTS)
                .set(values)
                .where(STUDENTS.ID.eq(id))
                .execute();
    }
}



    /*public boolean checkConnection() {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement state = connection.prepareStatement("SELECT 1");
            state.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }*/

