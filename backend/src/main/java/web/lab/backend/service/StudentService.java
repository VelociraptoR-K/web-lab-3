package web.lab.backend.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import web.lab.backend.dto.*;
import web.lab.backend.dto.response.StudentResponse;
import web.lab.backend.dto.response.StudentsPage;
import web.lab.backend.repository.StudentRepository;

import java.util.Collections;
import java.util.List;


@ApplicationScoped
public class StudentService {

    @Inject
    StudentRepository studentRepository;

    private <T> List<T> listOrNull(T value) {
        return value == null ? null : List.of(value);
    }

    public QueryFilterDTO convertToQuery(GetFilterDTO getFilter) {
        return new QueryFilterDTO(
                getFilter.getName(),
                getFilter.getSurname(),
                getFilter.getPatronymic(),
                getFilter.getIsuId(),
                listOrNull(getFilter.getGroup()),
                listOrNull(getFilter.getDormitoryNumber()),
                listOrNull(getFilter.getRoom()),
                getFilter.getMoveInDateFrom(),
                getFilter.getMoveInDateTo(),
                getFilter.getForeigner(),
                getFilter.getSortBy(),
                getFilter.getOrder(),
                getFilter.getPage(),
                getFilter.getLimit()
        );
    }


    public StudentsPage getAll(QueryFilterDTO filterParams) {
        List<StudentInfoDTO> students = studentRepository.getAllStudents(filterParams);

        int studentsCount = studentRepository.countStudents(filterParams);
        Integer totalPages = null;

        if (filterParams.limit() != null) {
            totalPages = (int) Math.ceil(
                    (double) studentsCount / filterParams.limit()
            );
        }

        return new StudentsPage(
                "All students list",
                studentsCount,
                totalPages,
                filterParams.page(),
                filterParams.limit(),
                students
        );
    }


    public StudentResponse getById(Long id) {
        StudentInfoDTO studentInfo = studentRepository.getStudentById(id);
        return new StudentResponse(
                String.format("Student with id=%d", id),
                id,
                studentInfo
        );
    }

    public StudentResponse createNew(CreateStudentDTO createInfo) {
        StudentInfoDTO studentInfo = studentRepository.createStudent(createInfo);
        return new StudentResponse(
                "Student was added succesfully",
                studentInfo.id(),
                studentInfo
        );
    }


    public void deleteById(Long id) {
        studentRepository.deleteStudent(id);
    }


    public StudentResponse patchById(Long id, StudentPatchDTO patchInfo) {
        studentRepository.editStudent(id, patchInfo);
        StudentInfoDTO studentInfo = studentRepository.getStudentById(id);
        return new StudentResponse(
                String.format("Student with id=%d", id),
                id,
                studentInfo
        );
    }


}
