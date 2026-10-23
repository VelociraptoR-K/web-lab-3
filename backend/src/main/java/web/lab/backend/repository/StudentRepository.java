package web.lab.backend.repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

import static jooq.generated.tables.Students.STUDENTS;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jooq.generated.tables.records.StudentsRecord;
import org.jooq.*;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import web.lab.backend.dto.students.*;


import javax.sql.DataSource;
import java.util.ArrayList;
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



    private Condition buildConditions(QueryFilterDTO filterParams) {
        List<Condition> conditions = new ArrayList<>();

        if (filterParams.name() != null) {
            conditions.add(STUDENTS.NAME.containsIgnoreCase(filterParams.name()));
        }
        if (filterParams.surname() != null) {
            conditions.add(STUDENTS.SURNAME.containsIgnoreCase(filterParams.surname()));
        }
        if (filterParams.patronymic() != null) {
            conditions.add(STUDENTS.PATRONYMIC.containsIgnoreCase(filterParams.patronymic()));
        }

        if (filterParams.group() != null && !filterParams.group().isEmpty()) {
            conditions.add(STUDENTS.STUDENT_GROUP.in(filterParams.group()));
        }
        if (filterParams.dormitoryNumber() != null && !filterParams.dormitoryNumber().isEmpty()) {
            conditions.add(STUDENTS.DORMITORY_NUMBER.in(filterParams.dormitoryNumber()));
        }
        if (filterParams.room() != null && !filterParams.room().isEmpty()) {
            conditions.add(STUDENTS.ROOM.in(filterParams.room()));
        }

        if (filterParams.moveInDateFrom() != null) {
            conditions.add(
                    STUDENTS.MOVE_IN_DATE.between(
                            filterParams.moveInDateFrom(),
                            filterParams.moveInDateTo()
                    )
            );
        }

        if (filterParams.foreigner() != null) {
            conditions.add(STUDENTS.FOREIGNER.eq(filterParams.foreigner()));
        }

        return DSL.and(conditions);
    }

    private SortField<?> buildSortField(QueryFilterDTO filterParams) {
        Field<?> field = switch (filterParams.sortBy()) {
            case id -> STUDENTS.ID;
            case name -> STUDENTS.NAME;
            case surname -> STUDENTS.SURNAME;
            case patronymic -> STUDENTS.PATRONYMIC;
            case group -> STUDENTS.STUDENT_GROUP;
            case isuId -> STUDENTS.ISU_ID;
            case dormitoryNumber -> STUDENTS.DORMITORY_NUMBER;
            case room -> STUDENTS.ROOM;
            case moveInDate -> STUDENTS.MOVE_IN_DATE;
            case foreigner -> STUDENTS.FOREIGNER;
        };

        return filterParams.order() == SortByDirections.up
                ? field.asc()
                : field.desc();
    }

    private int calculateOffset(QueryFilterDTO filterParams) {
        return (filterParams.page() - 1) * filterParams.limit();
    }




    public int countStudents(QueryFilterDTO filterParams) {
        Condition filterCondition = buildConditions(filterParams);

        return dsl.fetchCount(
                dsl.selectFrom(STUDENTS)
                        .where(filterCondition)
        );
    }


    public List<StudentInfoDTO> getAllStudents(QueryFilterDTO filterParams) {
        Condition filterCondition = buildConditions(filterParams);
        SortField<?> sortField = buildSortField(filterParams);

        if (filterParams.limit() == null) {
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
                    .where(filterCondition)
                    .orderBy(sortField)
                    .fetch(Records.mapping(StudentInfoDTO::new));
        }

        int offset = calculateOffset(filterParams);

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
                .where(filterCondition)
                .orderBy(sortField)
                .limit(filterParams.limit())
                .offset(offset)
                .fetch(Records.mapping(StudentInfoDTO::new));
    }


    public StudentInfoDTO getStudentById(Long id) {
        StudentInfoDTO student =  dsl.select(
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

        if (student == null){
            throw new NotFoundException("Student with id=" + id + " was not found");
        }

        return student;
    }


    public StudentInfoDTO createStudent(CreateStudentDTO studentInfo) {
        try {
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
        } catch (DataAccessException exception) {
            if ("23505".equals(exception.sqlState())) {
                throw new ClientErrorException(
                        "Student with this isuId already exists",
                        Response.Status.CONFLICT
                );
            }
            throw exception;
        }
    }


    public void deleteStudent(Long id) {
        int result = dsl.deleteFrom(STUDENTS)
                .where(STUDENTS.ID.eq(id))
                .execute();
        if (result < 1) {
            throw new NotFoundException("Student with id=" + id + " was not found");
        }
    }


    public void editStudent(Long id, StudentPatchDTO patchInfo) {
        try {
            Map<Field<?>, Object> values = new LinkedHashMap<>();

            if (patchInfo.getName() != null) {
                values.put(STUDENTS.NAME, patchInfo.getName());
            }
            if (patchInfo.getSurname() != null) {
                values.put(STUDENTS.SURNAME, patchInfo.getSurname());
            }
            if (patchInfo.isPatronymicProvided()) {
                values.put(STUDENTS.PATRONYMIC, patchInfo.getPatronymic());
            }
            if (patchInfo.getGroup() != null) {
                values.put(STUDENTS.STUDENT_GROUP, patchInfo.getGroup());
            }
            if (patchInfo.getIsuId() != null) {
                values.put(STUDENTS.ISU_ID, patchInfo.getIsuId());
            }
            if (patchInfo.isDormitoryNumberProvided()) {
                values.put(STUDENTS.DORMITORY_NUMBER, patchInfo.getDormitoryNumber());
            }
            if (patchInfo.isRoomProvided()) {
                values.put(STUDENTS.ROOM, patchInfo.getRoom());
            }
            if (patchInfo.isMoveInDateProvided()) {
                values.put(STUDENTS.MOVE_IN_DATE, patchInfo.getMoveInDate());
            }
            if (patchInfo.getForeigner() != null) {
                values.put(STUDENTS.FOREIGNER, patchInfo.getForeigner());
            }
            if (patchInfo.isNotesProvided()) {
                values.put(STUDENTS.NOTES, patchInfo.getNotes());
            }

            if (values.isEmpty()) {
                throw new BadRequestException("For editing json body must contain edit parameters");
            }

            int result = dsl.update(STUDENTS)
                    .set(values)
                    .where(STUDENTS.ID.eq(id))
                    .execute();

            if (result < 1) {
                throw new NotFoundException("Student with id=" + id + " was not found");
            }
        } catch(DataAccessException exception) {
            if ("23505".equals(exception.sqlState())) {
                throw new ClientErrorException(
                        "Student with this isuId already exists",
                        Response.Status.CONFLICT
                );
            }
            throw exception;
        }
    }
}


