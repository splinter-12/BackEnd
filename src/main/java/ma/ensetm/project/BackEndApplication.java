package ma.ensetm.project;

import ma.ensetm.project.entities.*;
import ma.ensetm.project.security.entities.AppRole;
import ma.ensetm.project.security.entities.AppUser;
import ma.ensetm.project.security.service.SecurityService;
import ma.ensetm.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.stream.Stream;

@SpringBootApplication
public class BackEndApplication {

    @Autowired
    private SpecialityService specialityService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ElementService elementService;
    @Autowired
    private SemesterService semesterService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SecurityService securityService;

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

    @Configuration
    public class AppConfiguration implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*").allowedHeaders("*");
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry
                    .addResourceHandler("/**")
                    .addResourceLocations("file:" + new File("").getAbsolutePath() + "/files/");
        }
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            // create roles
            if (securityService.getAllRoles().isEmpty()) {
                Stream.of("ADMIN", "PROF", "STUDENT").forEach(role -> {
                    securityService.addNewRole(new AppRole(null, role));
                });
            }
            if (securityService.getAllUsers().isEmpty()) {
                securityService.addNewUser(new AppUser(null, "admin", "admin@gmail.com", "admin"));
                securityService.addRoleToUser("admin", "ADMIN");
            }

            if (teacherService.getAll().isEmpty()) {
                teacherService.save(new Teacher("BV253654", "Youssfi", "Mohamed"));
//                securityService.addRoleToUser("BV253654", "PROF");
                teacherService.save(new Teacher("BR456789", "Daaif", "Aziz"));
                //              securityService.addNewUser(new AppUser(null, "BR456789", "daaif@gamil.com", "BR456789"));
                //            securityService.addRoleToUser("BR456789", "PROF");
            }

            if (specialityService.getAll().size() == 0) {
                Speciality speciality = new Speciality();
                speciality.setCode("TI2IBDC");
                speciality.setTitre("BDCC");
                specialityService.save(speciality);

                Semester semester1 = new Semester();
                semester1.setCode("TII11005");
                semester1.setTitre("Semestre 1");
               // semester1.setSpeciality(speciality);
                semesterService.save(semester1);

                Semester semester2 = new Semester();
                semester2.setCode("TII12005");
                semester2.setTitre("Semestre 2 ");
                semesterService.save(semester2);
                //semestre 3
                Semester semester3 = new Semester();
                semester3.setCode("TII13005");
                semester3.setTitre("Semestre 3 ");
                semesterService.save(semester3);
                //semestre 4
                Semester semester4 = new Semester();
                semester4.setCode("TII14005");
                semester4.setTitre("Semestre 4 ");
                semesterService.save(semester4);
                //semestre 5
                Semester semester5 = new Semester();
                semester5.setCode("TII15005");
                semester5.setTitre("Semestre 5 ");
                semesterService.save(semester5);
                //semestre 6
                Semester semester6 = new Semester();
                semester6.setCode("TII16005");
                semester6.setTitre("Semestre 6 ");
                semesterService.save(semester6);
               // semester2.setSpeciality(speciality);



                Module module1 = new Module();
                module1.setCode("TII11105");
                module1.setTitre("M1");
                module1.setSpeciality(speciality);
                module1.setSemester(semester1);
                moduleService.save(module1);

                Module module2 = new Module();
                module2.setCode("TII11205");
                module2.setTitre("M2");
                module2.setSpeciality(speciality);
                module2.setSemester(semester1);
                moduleService.save(module2);

                Module module3 = new Module();
                module3.setCode("TII11305");
                module3.setTitre("M3");
                module3.setSpeciality(speciality);
                module3.setSpeciality(speciality);
                module3.setSemester(semester1);
                moduleService.save(module3);

                Module module4 = new Module();
                module4.setCode("TII11405");
                module4.setTitre("M4");
                module4.setSpeciality(speciality);
                module4.setSpeciality(speciality);
                module4.setSemester(semester1);
                moduleService.save(module4);

                Module module5 = new Module();
                module5.setCode("TII11505");
                module5.setTitre("M5");
                module5.setSpeciality(speciality);
                module5.setSemester(semester1);
                moduleService.save(module5);

                Module module6 = new Module();
                module6.setCode("TII11605");
                module6.setTitre("M6");
                module6.setSpeciality(speciality);
                module6.setSemester(semester1);
                moduleService.save(module6);

                Module module7 = new Module();
                module7.setCode("TII11705");
                module7.setTitre("M7");
                module7.setSpeciality(speciality);
                module7.setSemester(semester1);
                moduleService.save(module7);

                Module module1s2 = new Module();
                module1s2.setCode("TII12105");
                module1s2.setTitre("Math appliquées 3");
                module1s2.setSpeciality(speciality);
                module1s2.setSemester(semester2);
                moduleService.save(module1s2);


                Module module2s2 = new Module();
                module2s2.setCode("TII12205");
                module2s2.setTitre("Stru de données et progra");
                module2s2.setSpeciality(speciality);
                module2s2.setSemester(semester2);
                moduleService.save(module2s2);


                Module module3s2 = new Module();
                module3s2.setCode("TII12305");
                module3s2.setTitre("Programmation orientée ob");
                module3s2.setSpeciality(speciality);
                module3s2.setSemester(semester2);
                moduleService.save(module3s2);


                Module module4s2 = new Module();
                module4s2.setCode("TII12405");
                module4s2.setTitre("Technologie Web");
                module4s2.setSpeciality(speciality);
                module4s2.setSemester(semester2);
                moduleService.save(module4s2);


                Module module5s2 = new Module();
                module5s2.setCode("TII12505");
                module5s2.setTitre("Systèmes d'exploitation");
                module5s2.setSpeciality(speciality);
                module5s2.setSemester(semester2);
                moduleService.save(module5s2);


                Module module6s2 = new Module();
                module6s2.setCode("TII12605");
                module6s2.setTitre("Projet personnel 1");
                module6s2.setSpeciality(speciality);
                module6s2.setSemester(semester2);
                moduleService.save(module6s2);


                Module module7s2 = new Module();
                module7s2.setCode("TII12705");
                module7s2.setTitre("Comptabilité et gestion");
                module7s2.setSpeciality(speciality);
                module7s2.setSemester(semester2);
                moduleService.save(module7s2);


                Module module8s2 = new Module();
                module8s2.setCode("TII12805");
                module8s2.setTitre("TEC 2");
                module8s2.setSpeciality(speciality);
                module8s2.setSemester(semester2);
                moduleService.save(module8s2);

                // teachers
                Teacher teacher1 = teacherService.findById("BV253654");
                Teacher teacher2 = teacherService.findById("BR456789");

                Element element1 = new Element(null,
                        "TII11115", "Logique et algèbre",
                        20f, 0.5f, module1, null);
                elementService.save(element1);


                Element element2 = new Element(null,
                        "TII11125", "Analyse Numérique 1",
                        20f, 0.5f, module1, null);
                elementService.save(element2);


                Element element3 = new Element(null,
                        "TII11215", "Probabilité",
                        20f, 0.5f, module2, null);
                elementService.save(element3);


                Element element4 = new Element(null,
                        "TII11225", "Recherche opérationnelle",
                        20f, 0.5f, module2, null);
                elementService.save(element4);


                Element element5 = new Element(null,
                        "TII11315", "Algorithmique",
                        20f, 0.5f, module3, teacher1);
                elementService.save(element5);

                Element element6 = new Element(null,
                        "TII11325", "Programmation en langage",
                        20f, 0.5f, module3, teacher1);
                elementService.save(element6);

                teacherService.assignTeacherToModule(teacher1.getCin(), module5.getId());
                teacherService.assignTeacherToModule(teacher1.getCin(), module6.getId());

                Element element7 = new Element(null,
                        "TII11415", "Introduction aux BDD",
                        20f, 0.5f, module4, null);
                elementService.save(element7);

                Element element8 = new Element(null,
                        "TII11425", "SQL et SGBD",
                        20f, 0.5f, module4, null);
                elementService.save(element8);


                Element element9 = new Element(null,
                        "TII11515", "Archit des ordi et assemb",
                        20f, 0.5f, module5, null);
                elementService.save(element9);


                Element element10 = new Element(null,
                        "TII11525", "Techniques base réseaux",
                        20f, 0.5f, module5, null);
                elementService.save(element10);


                Element element11 = new Element(null,
                        "TII11615", "Economie générale",
                        20f, 0.5f, module6, null);
                elementService.save(element11);


                Element element12 = new Element(null,
                        "TII11625", "Environnement socio-écono",
                        20f, 0.5f, module6, null);
                elementService.save(element12);


                Element element13 = new Element(null,
                        "TII11715", "TC langue française 1",
                        20f, 0.5f, module7, null);
                elementService.save(element13);


                Element element14 = new Element(null,
                        "TII11725", "Anglais 1",
                        20f, 0.5f, module7, null);
                elementService.save(element14);

////

                Element element1s2 = new Element(null,
                        "TII12115", "Analyse Numérique 2",
                        20f, 0.5f, module1s2, null);
                elementService.save(element1s2);


                Element element2s2 = new Element(null,
                        "TII12125", "Statistique",
                        20f, 0.5f, module1s2, null);
                elementService.save(element2s2);


                Element element3s2 = new Element(null,
                        "TII12215", "Structures de données",
                        20f, 0.5f, module2s2, null);
                elementService.save(element3s2);


                Element element4s2 = new Element(null,
                        "TII12225", "Programmation fonctionnel",
                        20f, 0.5f, module2s2, null);
                elementService.save(element4s2);


                Element element5s2 = new Element(null,
                        "TII12315", "Conception et progra C++",
                        20f, 0.5f, module3s2, null);
                elementService.save(element5s2);


                Element element6s2 = new Element(null,
                        "TII12325", "Projet progra ori obj C++",
                        20f, 0.5f, module3s2, null);
                elementService.save(element6s2);


                Element element7s2 = new Element(null,
                        "TII12415", "Développement web",
                        20f, 0.5f, module4s2, teacher2);
                elementService.save(element7s2);


                Element element8s2 = new Element(null,
                        "TII12425", "Projet Développement web",
                        20f, 0.5f, module4s2, teacher2);
                elementService.save(element8s2);


                Element element9s2 = new Element(null,
                        "TII12515", "Théorie des Systèmes d'ex",
                        20f, 0.5f, module5s2, null);
                elementService.save(element9s2);


                Element element10s2 = new Element(null,
                        "TII12525", "SE Windows/Unix/Linux",
                        20f, 0.5f, module5s2, null);
                elementService.save(element10s2);


                Element element11s2 = new Element(null,
                        "TII12615", "Projet personnel 1",
                        20f, 1f, module6s2, null);
                elementService.save(element11s2);


                Element element12s2 = new Element(null,
                        "TII12715", "Comptabilité générale",
                        20f, 0.5f, module7s2, null);
                elementService.save(element12s2);


                Element element13s2 = new Element(null,
                        "TII12725", "Gestion de l'entreprise",
                        20f, 0.5f, module7s2, null);
                elementService.save(element13s2);


                Element element14s2 = new Element(null,
                        "TII12815", "TC langue française 2",
                        20f, 0.5f, module8s2, null);
                elementService.save(element14s2);


                Element element15s2 = new Element(null,
                        "TII12825", "Anglais 2",
                        20f, 0.5f, module8s2, null);
                elementService.save(element15s2);
            }

        };
    }

}
