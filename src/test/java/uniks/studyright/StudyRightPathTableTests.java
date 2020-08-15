package uniks.studyright;

import org.fulib.FulibTools;
import org.fulib.tables.ObjectTable;
import org.fulib.tables.PathTable;
import org.fulib.tables.StringTable;
import org.fulib.tables.doubleTable;
import org.fulib.tools.CodeFragments;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uniks.studyright.model.Assignment;
import uniks.studyright.model.Room;
import uniks.studyright.model.Student;
import uniks.studyright.model.University;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StudyRightPathTableTests
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

      // start_code_fragment: FulibTables.createUniPathTable1
      // some table stuff
      PathTable universityTable = new PathTable("University", studyRight);
      universityTable.expandAll("University", u -> ((University) u).getRooms() , "Room");
      universityTable.expandAll("Room", r -> ((Room) r).getAssignments(), "Assignment");
      // end_code_fragment:

      universityTable = new PathTable("University", studyRight);
      fragments.addFragment("FulibTables.uniPathTable1", universityTable.toString());

      universityTable.expandAll("University", u -> ((University) u).getRooms(), "Room");
      fragments.addFragment("FulibTables.uniPathTable2", universityTable.toString());

      universityTable.expandAll("Room", r -> ((Room) r).getAssignments(), "Assignment");
      fragments.addFragment("FulibTables.uniPathTable3", universityTable.toString());

      // start_code_fragment: FulibTables.loop_through_assignments
      double sum = 0;
      for (Assignment a : (Set<Assignment>) universityTable.toSet("Assignment"))
      {
         sum += a.getPoints();
      }
      assertThat(sum, is(89.0));
      // end_code_fragment:

      // start_code_fragment: FulibTables.pointsPathTable
      universityTable.expand("Assignment", Assignment.PROPERTY_points, "Points");
      sum = universityTable.sum("Points");
      assertThat(universityTable.rowCount(), equalTo(4));
      assertThat(sum, equalTo(89.0));
      // end_code_fragment:

      fragments.addFragment("FulibTables.pointsPathTableResult", universityTable.toString());

      // start_code_fragment: FulibTables.studentsTable
      universityTable.expandAll("Room", r -> ((Room) r).getStudents(), "Student");
      assertThat(universityTable.rowCount(), equalTo(6));
      // end_code_fragment:

      fragments.addFragment("FulibTables.studentsPathTableResult", universityTable.toString());

      // start_code_fragment: FulibTables.filterAssignmentsTable
      universityTable.filter("Assignment", a -> ((Assignment) a).getPoints() <= 30);
      assertThat(universityTable.rowCount(), equalTo(4));
      // end_code_fragment:

      fragments.addFragment("FulibTables.filterAssignmentsPathTableResult", universityTable.toString());
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

   @AfterClass
   public static void teardownClass()
   {
      fragments.update("README.md", "src/test/java/uniks/studyright/StudyRightTests.java",
                       "src/gen/java/uniks/studyright/StudyRightClassModel.java");
   }
}
