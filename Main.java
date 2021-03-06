import assignment.*;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Department ee = new Department("EE", "1", 1000000);
        Department cs = new Department("CS", "1", 1000000);
        Department maths = new Department("Math", "2", 1000000);

        Instructor steve = new Instructor(1l, "Steve", ee, 120000);
        Instructor dmitri = new Instructor(2l, "Dmitri", ee, 110000);
        Instructor cheng = new Instructor(3l, "Cheng", cs, 120000);
        Instructor joe = new Instructor(4l, "Joe", maths, 120000);


        Student john = new Student(1l, "John", ee);
        Student joseph = new Student(2l, "Joseph", cs);
        Student jane = new Student(3l, "Jane", ee);
        Student andrew = new Student(4l, "Andrew", maths);

        Course circuits = new Course(1l, "Circuits", ee, (short) 3);
        Course discreteMaths = new Course(2l, "Discrete Maths", cs, (short) 3);
        Course linearAlgebra = new Course(3l, "Linear Algebra", maths, (short) 2);
        Course calculus = new Course(4l, "Calculus", maths, (short) 2);
        Course numberTheory = new Course(5l, "Number Theory", maths, (short) 3);

        Teaches steveCircuits = new Teaches(steve, circuits, (short) 1, (short) 2019);
        Teaches joeLinal = new Teaches(joe, linearAlgebra, (short) 1, (short) 2019);
        Teaches chengDM = new Teaches(cheng, discreteMaths, (short) 1, (short) 2019);
        Teaches joeNumberTheory = new Teaches(joe, numberTheory, (short) 1, (short) 2019);
        Teaches joeCalculus = new Teaches(joe, calculus, (short) 1, (short) 2019);

        TimeSlot timeSlot1 = new TimeSlot(1l, Day.MONDAY, null, null);
        TimeSlot timeSlot2 = new TimeSlot(2l, Day.TUESDAY, null, null);
        TimeSlot timeSlot3 = new TimeSlot(3l, Day.WEDNESDAY, null, null);

        Section sec1 = new Section(1l, circuits, (short) 1, (short) 2019, "1", (short) 1, timeSlot1);
        Section sec2 = new Section(2l, linearAlgebra, (short) 1, (short) 2019, "1", (short) 1, timeSlot2);
        Section sec3 = new Section(3l, discreteMaths, (short) 1, (short) 2019, "2", (short) 1, timeSlot3);

        Takes johnCircuits = new Takes(john, circuits, sec1, (short) 1, (short) 2019, "B+");
        Takes janeCircuits = new Takes(jane, circuits, sec1, (short) 1, (short) 2019, "A+");
        Takes andrewLinal = new Takes(andrew, linearAlgebra, sec2, (short) 1, (short) 2019, "B-");
        Takes andrewCalculus = new Takes(andrew, calculus, sec2, (short) 1, (short) 2019, "B");
        Takes andrewNumberTheory = new Takes(andrew, numberTheory, sec3, (short) 1, (short) 2019, "B");
        Takes josephDM = new Takes(joseph, discreteMaths, sec3, (short) 1, (short) 2019, "A+");

        Collection<Instructor> instructors = new ArrayList<>(Arrays.asList(steve, dmitri, cheng, joe));
        Collection<Course> courses = Arrays.asList(circuits, discreteMaths, linearAlgebra, calculus, numberTheory);

        Collection<Teaches> teaches = Arrays.asList(steveCircuits, joeLinal, chengDM, joeNumberTheory, joeCalculus);
        Collection<Takes> takes = Arrays.asList(johnCircuits, janeCircuits, andrewLinal, andrewCalculus, andrewNumberTheory, josephDM);

        // 1.
        System.out.println("1. Courses in EE or CS with 3 credits:");
        System.out.println(
        courses.stream()
                .filter(course -> course.getDepartment().getName().equals("EE") || course.getDepartment().getName().equalsIgnoreCase("CS"))
                .filter(course -> course.getCredits() == 3)
                .map(Course::getTitle)
                .collect(Collectors.toList()));
        System.out.println();

        // 2.
        System.out.println("2. Ids of all students taught by Prof. Steve");
        Map<Course, Instructor> course2Instructor = new HashMap<>();
        Set<String> names = new HashSet<>();
        for (Teaches t : teaches) {
            course2Instructor.put(t.getCourse(), t.getInstructor());
        }
        for (Takes t : takes) {
            Instructor instructor = course2Instructor.get(t.getCourse());
            if (instructor != null && instructor.getName().equalsIgnoreCase("steve")) {
                names.add(t.getStudent().getName());
            }
        }
        System.out.println(names);
        System.out.println();


        // 3.
        System.out.println("3. Highest salary:");
        System.out.println(instructors.stream().map(Instructor::getSalary).max(Double::compareTo).get());
        System.out.println();

        // 4.
        System.out.println("4. Highest Salary Instructors:");
        System.out.println(instructors.stream().filter(x -> x.getSalary() == instructors.stream().map(Instructor::getSalary).max(Double::compareTo).get()).map(Instructor::getName).collect(Collectors.toList()));
        System.out.println();


        Map<Section, Set<Takes>>  section2Enrollments = new HashMap<>();
        for (Takes t : takes) {
            if (t.getSemester() == 1) {
                section2Enrollments.computeIfAbsent(t.getSection(), __ -> new HashSet<>());
                section2Enrollments.get(t.getSection()).add(t);
            }
        }

        // 5.
        System.out.println("5. Section-wise enrollments in semester 1:");
        System.out.println(section2Enrollments.entrySet().stream()
                .map(x -> String.format("%s:%s", x.getKey().getId(), x.getValue().stream().map(y -> y.getStudent().getName()).collect(Collectors.toList())))
        .collect(Collectors.toList()));
        System.out.println();

        // 6.
        System.out.println("6. Max enrollment section:");
        System.out.println(section2Enrollments.entrySet().stream().map(x -> new AbstractMap.SimpleImmutableEntry<>(x.getKey(), x.getValue())).max(Comparator.comparingInt(o -> o.getValue().size())).get().getValue().size());
        System.out.println();

        // 7.
        System.out.println("7. Max enrollment sections for semester 1:");
        Map<Section, Integer> counts = new HashMap<>();
        for (Takes t : takes) {
            if (t.getSemester() == 1) {
                counts.put(t.getSection(), counts.getOrDefault(t.getSection(), 0) + 1);
            }
        }
        System.out.println(counts.entrySet().stream().filter(x -> x.getValue() == counts.values().stream().max(Integer::compareTo).get()).map(x -> x.getKey().getId()).collect(Collectors.toList()));
        System.out.println();

        // 8.
        System.out.println("8. Now salaries");
        for (Instructor instructor : instructors) {
            instructor.setSalary(instructor.getSalary() * 1.1);
        }
        System.out.println(instructors.stream().map(Instructor::getSalary).collect(Collectors.toList()));
        System.out.println();

        // 9.
        System.out.println("9. Offered Courses:");
        courses.retainAll(teaches.stream().map(Teaches::getCourse).collect(Collectors.toSet()));
        System.out.println();

        // 10.
        System.out.println("10. All instructors:");
        Map<Student, Integer> student2TotalCreds = new HashMap<>();
        for (Takes t : takes) {
            student2TotalCreds.put(t.getStudent(), student2TotalCreds.getOrDefault(t.getStudent(), 0) + t.getCourse().getCredits());
        }
        for (Map.Entry<Student, Integer> entry : student2TotalCreds.entrySet()) {
            if (entry.getValue() > 4) {
                Student student = entry.getKey();
                Instructor instructor = new Instructor(instructors.size() + 1, student.getName(), student.getDepartment(), 90000);
                instructors.add(instructor);
            }
        }
        System.out.println(instructors.stream().map(Instructor::getName).collect(Collectors.toList()));
    }
}
