package ma.ensetm.project.controllers;

import ma.ensetm.project.ExeptionClass.ResourceNotFoundException;
import ma.ensetm.project.entities.Semester;
import ma.ensetm.project.repositories.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/semesters")

public class SemesterRestController {
        @Autowired
        private SemesterRepository semesterRepository;

        // Get all semesters
        @GetMapping
        public List<Semester> getAllSemesters() {
            return semesterRepository.findAll();
        }

        // Create a new semester
        @PostMapping
        public Semester createSemester(@RequestBody Semester semester) {
            return semesterRepository.save(semester);
        }

        // Get a single semester by id
        @GetMapping("/{id}")
        public Semester getSemesterById(@PathVariable Long id) {
            return semesterRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Semester not found with id " + id));
        }

        // Update a semester
        @PutMapping("/{id}")
        public Semester updateSemester(@PathVariable Long id, @RequestBody Semester semesterDetails) {
            Semester semester = semesterRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Semester not found with id " + id));

            semester.setCode(semesterDetails.getCode());
            semester.setTitre(semesterDetails.getTitre());

            return semesterRepository.save(semester);
        }

        // Delete a semester
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteSemester(@PathVariable Long id) {
            Semester semester = semesterRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Semester not found with id " + id));

            semesterRepository.delete(semester);

            return ResponseEntity.ok().build();
        }

    }

