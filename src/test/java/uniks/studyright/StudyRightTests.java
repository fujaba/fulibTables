package uniks.studyright;

import org.fulib.FulibTools;
import org.fulib.tables.ObjectTable;
import org.fulib.tables.StringTable;
import org.fulib.tables.doubleTable;
import org.fulib.tools.CodeFragments;
import org.hamcrest.MatcherAssert;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uniks.studyright.model.Assignment;
import uniks.studyright.model.Room;
import uniks.studyright.model.Student;
import uniks.studyright.model.University;

import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StudyRightTests
{
   private static CodeFragments fragments;

   private University studyRight;
   private Student alice;
   private Assignment integrals;

   @BeforeClass
   public static void setupClass()
   {
      fragments = FulibTools.codeFragments();
   }

   @Before
   @SuppressWarnings("unused")
   public void setup()
   {
      // @formatter:off
      // start_code_fragment: FulibTables.objectModel
      // build object structure
      University studyRight = new University().setName("Study Right");

      Room mathRoom = new Room().setRoomNo("wa1337").setTopic("Math").setCredits(42.0).setUni(studyRight);
      Room artsRoom = new Room().setRoomNo("wa1338").setTopic("Arts").setCredits(23.0).setUni(studyRight);
      Room sportsRoom = new Room().setRoomNo("wa1339").setTopic("Football").setUni(studyRight);

      Assignment integrals = new Assignment().setTask("integrals").setPoints(42).setRoom(mathRoom);
      Assignment matrix = new Assignment().setTask("matrices").setPoints(23).setRoom(mathRoom);
      Assignment drawings = new Assignment().setTask("drawings").setPoints(12).setRoom(artsRoom);
      Assignment sculptures = new Assignment().setTask("sculptures").setPoints(12).setRoom(artsRoom);

      Student alice = new Student().setStudentId("m4242").setName("Alice").setUni(studyRight).setIn(mathRoom).withDone(integrals);
      Student bob   = new Student().setStudentId("m2323").setName("Bobby").setUni(studyRight).setIn(artsRoom).withFriends(alice);
      Student carli = new Student().setStudentId("m2323").setName("Carli").setUni(studyRight).setIn(mathRoom);

      FulibTools.objectDiagrams().dumpSVG("doc/images/studyRightObjects.svg", studyRight);
      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjects.png", studyRight);
      // end_code_fragment:
      // @formatter:on

      this.studyRight = studyRight;
      this.alice = alice;
      this.integrals = integrals;
   }

   @SuppressWarnings( { "UnusedAssignment", "unused" })
   @Test
   public void testModelQueries()
   {
      final University studyRight = this.studyRight;

      // start_code_fragment: FulibTables.createUniTable1
      // some table stuff
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
      ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
      // end_code_fragment:

      universityTable = new ObjectTable<>("University", studyRight);

      fragments.addFragment("FulibTables.uniTable1", universityTable.toString());

      roomsTable = universityTable.expandAll("Room", University::getRooms);
      fragments.addFragment("FulibTables.uniTable2", universityTable.toString());

      assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
      fragments.addFragment("FulibTables.uniTable3", universityTable.toString());

      // start_code_fragment: FulibTables.loop_through_assignments
      double sum = 0;
      for (Assignment a : assignmentsTable)
      {
         sum += a.getPoints();
      }
      assertThat(sum, equalTo(89.0));
      // end_code_fragment:

      // start_code_fragment: FulibTables.pointsTable
      doubleTable pointsTable = assignmentsTable.expand("Points", Assignment::getPoints).as(doubleTable.class);
      sum = pointsTable.sum();
      assertThat(roomsTable.rowCount(), equalTo(4));
      assertThat(assignmentsTable.rowCount(), equalTo(4));
      assertThat(sum, equalTo(89.0));
      // end_code_fragment:

      fragments.addFragment("FulibTables.pointsTableResult", universityTable.toString());

      // start_code_fragment: FulibTables.studentsTable
      ObjectTable<Student> students = roomsTable.expandAll("Student", Room::getStudents);
      assertThat(students.rowCount(), equalTo(6));
      // end_code_fragment:

      fragments.addFragment("FulibTables.studentsTableResult", universityTable.toString());

      // start_code_fragment: FulibTables.filterAssignmentsTable
      assignmentsTable.filter(a -> a.getPoints() <= 30);
      assertThat(students.rowCount(), equalTo(4));
      // end_code_fragment:

      fragments.addFragment("FulibTables.filterAssignmentsTableResult", universityTable.toString());
   }

   @Test
   @SuppressWarnings( { "unused", "UnusedAssignment" })
   public void filterRows()
   {
      final University studyRight = this.studyRight;

      ObjectTable<University> universityTable;
      ObjectTable<Room> roomsTable;
      ObjectTable<Student> students;
      ObjectTable<Assignment> assignmentsTable;

      // start_code_fragment: FulibTables.filterRowTable
      // filter row
      universityTable = new ObjectTable<>("University", studyRight);
      roomsTable = universityTable.expandAll("Room", University::getRooms);
      students = roomsTable.expandAll("Student", Room::getStudents);
      assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);

      students.filterRows(row -> {
         Student studi = (Student) row.get("Student");
         Assignment assignment = (Assignment) row.get("Assignment");
         return studi.getDone().contains(assignment);
      });

      assertThat(students.rowCount(), equalTo(1));
      // end_code_fragment:

      fragments.addFragment("FulibTables.filterRowTableResult", universityTable.toString());
   }

   @Test
   public void hasLink()
   {
      final University studyRight = this.studyRight;

      ObjectTable<University> universityTable;
      ObjectTable<Room> roomsTable;
      ObjectTable<Student> students;
      ObjectTable<Assignment> assignmentsTable;

      // start_code_fragment: FulibTables.filterHasDone
      // filter row
      universityTable = new ObjectTable<>("University", studyRight);
      roomsTable = universityTable.expandAll("Room", University::getRooms);
      students = roomsTable.expandAll("Student", Room::getStudents);
      assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
      students.hasLink(Student.PROPERTY_done, assignmentsTable);

      assertThat(students.rowCount(), equalTo(1));
      // end_code_fragment:

      fragments.addFragment("FulibTables.filterHasDoneResult", universityTable.toString());
   }

   @Test
   @SuppressWarnings( { "unused", "UnusedAssignment" })
   public void doAssignmentsAndDropSelect()
   {
      final University studyRight = this.studyRight;
      final Student alice = this.alice;
      final Assignment integrals = this.integrals;

      ObjectTable<University> universityTable;
      ObjectTable<Room> roomsTable;
      ObjectTable<Student> students;
      ObjectTable<Assignment> assignmentsTable;

      // start_code_fragment: FulibTables.doCurrentAssignments
      universityTable = new ObjectTable<>("University", studyRight);
      roomsTable = universityTable.expandAll("Room", University::getRooms);
      students = roomsTable.expandAll("Student", Room::getStudents);
      assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);

      // do current assignments
      students.filterRows(row -> {
         Student studi = (Student) row.get("Student");
         Assignment assignment = (Assignment) row.get("Assignment");
         studi.withDone(assignment);
         return true;
      });

      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjectsMoreDone4Tables.png", studyRight);

      assertThat(alice.getDone().size(), equalTo(2));
      assertThat(integrals.getStudents().contains(alice), is(true));

      // show size of done
      universityTable.derive("noOfDone", row -> {
         Student studi = (Student) row.get("Student");
         return studi.getDone().size();
      });

      // show done
      students.expandAll("Done", Student::getDone);
      // end_code_fragment:

      fragments.addFragment("FulibTables.doCurrentAssignmentsResult", universityTable.toString());

      // start_code_fragment: FulibTables.dropColumnsAssignment
      universityTable.dropColumns("Assignment");
      // end_code_fragment:

      fragments.addFragment("FulibTables.dropColumnsAssignmentResult", universityTable.toString());

      // start_code_fragment: FulibTables.selectColumns
      students.selectColumns("Student", "Done");
      assertThat(students.rowCount(), equalTo(6));
      // end_code_fragment:

      fragments.addFragment("FulibTables.selectColumnsResult", universityTable.toString());
   }

   @Test
   public void nestedTables()
   {
      final University studyRight = this.studyRight;
      ObjectTable<University> universityTable;
      ObjectTable<Student> students;

      // do current assignments
      for (final Student student : this.studyRight.getStudents())
      {
         student.withDone(student.getIn().getAssignments());
      }

      // start_code_fragment: FulibTables.nestedTables
      universityTable = new ObjectTable<>("University", studyRight);
      students = universityTable.expandAll("Students", University::getStudents);
      students.derive("Credits", row -> {
         Student student = (Student) row.get("Students");
         double pointSum = new ObjectTable<>(student)
            .expandAll("Assignments", Student::getDone)
            .expand("Points", Assignment::getPoints)
            .as(doubleTable.class)
            .sum();
         student.setCredits(pointSum);
         return pointSum;
      });
      students.derive("Done", row -> {
         Student student = (Student) row.get("Students");
         return new ObjectTable<>("Students", student)
            .expandAll("Assignments", Student::getDone)
            .expand("Tasks", Assignment::getTask)
            .as(StringTable.class)
            .join(", ");
      });
      // end_code_fragment:

      fragments.addFragment("FulibTables.nestedTablesResult", universityTable.toString());

      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjectsCreditsAssigned4Tables.png", studyRight);
   }

   @Test
   public void testModelQueriesWithSource()
   {
      final University studyRight = this.studyRight;

      // start_code_fragment: FulibTables.createUniPathTable1
      // some table stuff
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      universityTable.expandAll("University", "Room", University::getRooms);
      universityTable.expandAll("Room", "Assignment", Room::getAssignments);
      // end_code_fragment:

      universityTable = new ObjectTable<>("University", studyRight);
      fragments.addFragment("FulibTables.uniPathTable1", universityTable.toString());

      universityTable.expandAll("University", "Room", University::getRooms);
      fragments.addFragment("FulibTables.uniPathTable2", universityTable.toString());

      universityTable.expandAll("Room", "Assignment", Room::getAssignments);
      fragments.addFragment("FulibTables.uniPathTable3", universityTable.toString());

      // start_code_fragment: FulibTables.loopThroughAssignmentsPath
      double sum = 0;
      for (Assignment a : universityTable.<Assignment>toSet("Assignment"))
      {
         sum += a.getPoints();
      }
      MatcherAssert.assertThat(sum, is(89.0));
      // end_code_fragment:

      // start_code_fragment: FulibTables.pointsPathTable
      universityTable.expandLink("Assignment", "Points", Assignment.PROPERTY_points);
      sum = universityTable.<Double>stream("Points").mapToDouble(Double::doubleValue).sum();
      assertThat(universityTable.rowCount(), equalTo(4));
      MatcherAssert.assertThat(sum, equalTo(89.0));
      // end_code_fragment:

      fragments.addFragment("FulibTables.pointsPathTableResult", universityTable.toString());

      // start_code_fragment: FulibTables.studentsPathTable
      universityTable.expandAll("Room", "Student", Room::getStudents);
      assertThat(universityTable.rowCount(), equalTo(6));
      // end_code_fragment:

      fragments.addFragment("FulibTables.studentsPathTableResult", universityTable.toString());

      // start_code_fragment: FulibTables.filterAssignmentsPathTable
      universityTable.filter("Assignment", (Assignment a) -> a.getPoints() <= 30);
      assertThat(universityTable.rowCount(), equalTo(4));
      // end_code_fragment:

      fragments.addFragment("FulibTables.filterAssignmentsPathTableResult", universityTable.toString());
   }

   @Test
   public void filterRowsWithSource()
   {
      final University studyRight = this.studyRight;

      ObjectTable<University> universityTable;

      // start_code_fragment: FulibTables.filterRowPathTable
      // filter row
      universityTable = new ObjectTable<>("University", studyRight);
      universityTable.expandLink("University", "Room", University.PROPERTY_rooms);
      universityTable.expandLink("Room", "Student", Room.PROPERTY_students);
      universityTable.expandLink("Room", "Assignment", Room.PROPERTY_assignments);

      universityTable.filterRows(row -> {
         Student studi = (Student) row.get("Student");
         Assignment assignment = (Assignment) row.get("Assignment");
         return studi.getDone().contains(assignment);
      });

      assertThat(universityTable.rowCount(), equalTo(1));
      // end_code_fragment:

      fragments.addFragment("FulibTables.filterRowPathTableResult", universityTable.toString());
   }

   @Test
   public void hasLinkWithColumn()
   {
      final University studyRight = this.studyRight;

      // start_code_fragment: FulibTables.filterPathTableHasDone
      // filter row
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      universityTable.expandLink("University", "Room", University.PROPERTY_rooms);
      universityTable.expandLink("Room", "Student", Room.PROPERTY_students);
      universityTable.expandLink("Room", "Assignment", Room.PROPERTY_assignments);
      universityTable.hasLink("Student", "Assignment", Student.PROPERTY_done);

      assertThat(universityTable.rowCount(), equalTo(1));
      // end_code_fragment:

      fragments.addFragment("FulibTables.filterPathTableHasDoneResult", universityTable.toString());
   }

   @Test
   public void doAssignmentsAndDropSelectWithColumn()
   {
      final University studyRight = this.studyRight;
      final Student alice = this.alice;
      final Assignment integrals = this.integrals;

      // start_code_fragment: FulibTables.pathTableDoCurrentAssignments
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      universityTable.expandLink("University", "Room", University.PROPERTY_rooms);
      universityTable.expandLink("Room", "Student", Room.PROPERTY_students);
      universityTable.expandLink("Room", "Assignment", Room.PROPERTY_assignments);

      // do current assignments
      universityTable.filterRows(row -> {
         Student studi = (Student) row.get("Student");
         Assignment assignment = (Assignment) row.get("Assignment");
         studi.withDone(assignment);
         return true;
      });

      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjectsMoreDone4PathTables.png", studyRight);

      MatcherAssert.assertThat(alice.getDone().size(), is(2));
      MatcherAssert.assertThat(integrals.getStudents().contains(alice), is(true));

      // show size of done
      universityTable.derive("noOfDone", row -> {
         Student studi = (Student) row.get("Student");
         return studi.getDone().size();
      });

      // show done
      universityTable.expandLink("Student", "Done", Student.PROPERTY_done);
      // end_code_fragment:

      fragments.addFragment("FulibTables.pathTableDoCurrentAssignmentsResult", universityTable.toString());
   }

   @Test
   public void nestedTablesWithColumn()
   {
      final University studyRight = this.studyRight;

      // do current assignments
      for (final Student student : this.studyRight.getStudents())
      {
         student.withDone(student.getIn().getAssignments());
      }

      // start_code_fragment: FulibTables.nestedPathTables
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      universityTable.expandLink("University", "Students", University.PROPERTY_students);
      universityTable.derive("Credits", row -> {
         Student student = (Student) row.get("Students");
         double pointSum = new ObjectTable<>("Student", student)
            .expandLink("Student", "Assignments", Student.PROPERTY_done)
            .expandLink("Assignments", "Points", Assignment.PROPERTY_points)
            .<Double>stream("Points")
            .mapToDouble(Double::doubleValue)
            .sum();
         student.setCredits(pointSum);
         return pointSum;
      });
      universityTable.derive("Done", row -> {
         Student student = (Student) row.get("Students");
         return new ObjectTable<>("Student", student)
            .expandLink("Student", "Assignments", Student.PROPERTY_done)
            .expandLink("Assignments", "Tasks", Assignment.PROPERTY_task)
            .<String>stream("Tasks")
            .collect(Collectors.joining(", "));
      });
      // end_code_fragment:

      fragments.addFragment("FulibTables.nestedPathTablesResult", universityTable.toString());

      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjectsCreditsAssigned4PathTables.png", studyRight);
   }

   @AfterClass
   public static void teardownClass()
   {
      fragments.update("README.md", "src/test/java/uniks/studyright/StudyRightTests.java",
                       "src/gen/java/uniks/studyright/StudyRightClassModel.java");
   }
}
