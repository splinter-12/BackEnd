package ma.ensetm.project.services;

import ma.ensetm.project.entities.Teacher;

import java.util.List;

public interface TeacherService {
    Teacher findById(String id);
    Teacher save(Teacher teacher);
    Teacher update(Teacher teacher);
    void delete(String id);
    List<Teacher> getAll();
    void assignTeacherToModule(String teacherId, Long moduleId);
    void unassignTeacherFromModule(String teacherId, Long moduleId);
    void saveAll(List<Teacher> teachers);
}
