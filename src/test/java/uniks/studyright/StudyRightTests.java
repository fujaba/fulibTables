package uniks.studyright;

import org.fulib.FulibTools;
import org.fulib.tables.ObjectTable;
import org.fulib.tables.StringTable;
import org.fulib.tables.doubleTable;
import org.fulib.tools.CodeFragments;
import org.junit.Test;
import uniks.studyright.model.Assignment;
import uniks.studyright.model.Room;
import uniks.studyright.model.Student;
import uniks.studyright.model.University;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StudyRightTests
{
   @SuppressWarnings( { "UnusedAssignment", "unused" })
   @Test
   public void testModelQueries()
   {
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
      Student bob   = new Student().setStudentId("m2323").setName("Bobby"  ).setUni(studyRight).setIn(artsRoom).withFriends(alice);
      Student carli = new Student().setStudentId("m2323").setName("Carli").setUni(studyRight).setIn(mathRoom);

      FulibTools.objectDiagrams().dumpSVG("doc/images/studyRightObjects.svg", studyRight);
      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjects.png", studyRight);
      // end_code_fragment:


      // start_code_fragment: FulibTables.createUniTable1
      // some table stuff
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
      ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
      // end_code_fragment:

      universityTable = new ObjectTable<>("University", studyRight);

      final CodeFragments fragments = FulibTools.codeFragments();
      addFragment(fragments, "FulibTables.uniTable1", universityTable.toString());

      roomsTable = universityTable.expandAll("Room", University::getRooms);
      addFragment(fragments, "FulibTables.uniTable2", universityTable.toString());

      assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
      addFragment(fragments, "FulibTables.uniTable3", universityTable.toString());

      // start_code_fragment: FulibTables.loop_through_assignments
      // loop through assignments
      double sum = 0;
      for (Object a : assignmentsTable.toSet())
      {
         sum += ((Assignment)a).getPoints();
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

      addFragment(fragments, "FulibTables.pointsTableResult", universityTable.toString());

      // start_code_fragment: FulibTables.studentsTable
      ObjectTable<Student> students = roomsTable.expandAll("Student", Room::getStudents);
      assertThat(students.rowCount(), equalTo(6));
      // end_code_fragment:

      addFragment(fragments, "FulibTables.studentsTableResult", universityTable.toString());

      // start_code_fragment: FulibTables.filterAssignmentsTable
      assignmentsTable.filter(a -> a.getPoints() <= 30);
      assertThat(students.rowCount(), equalTo(4));
      // end_code_fragment:

      addFragment(fragments, "FulibTables.filterAssignmentsTableResult", universityTable.toString());

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

      addFragment(fragments, "FulibTables.filterRowTableResult", universityTable.toString());

      // start_code_fragment: FulibTables.filterHasDone
      // filter row
      universityTable = new ObjectTable<>("University", studyRight);
      roomsTable = universityTable.expandAll("Room", University::getRooms);
      students = roomsTable.expandAll("Student", Room::getStudents);
      assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
      students.hasLink(Student.PROPERTY_done, assignmentsTable);

      assertThat(students.rowCount(), equalTo(1));
      // end_code_fragment:

      addFragment(fragments, "FulibTables.filterHasDoneResult", universityTable.toString());

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
      universityTable.deriveColumn("noOfDone",  row ->
      {
         Student studi = (Student) row.get("Student");
         return studi.getDone().size();
      });

      // show done
      students.expandAll("Done", Student::getDone);
      // end_code_fragment:

      addFragment(fragments, "FulibTables.doCurrentAssignmentsResult", universityTable.toString());

      // start_code_fragment: FulibTables.dropColumnsAssignment
      universityTable.dropColumns("Assignment");
      // end_code_fragment:

      addFragment(fragments, "FulibTables.dropColumnsAssignmentResult", universityTable.toString());

      // start_code_fragment: FulibTables.selectColumns
      students.selectColumns("Student", "Done");
      assertThat(students.rowCount(), equalTo(6));
      // end_code_fragment:

      addFragment(fragments, "FulibTables.selectColumnsResult", universityTable.toString());

      // start_code_fragment: FulibTables.nestedTables
      universityTable = new ObjectTable<>("University", studyRight);
      students = universityTable.expandAll("Students", University::getStudents);
      students.deriveColumn("Credits", row -> {
         Student student = (Student) row.get("Students");
         double pointSum = new ObjectTable<>(student)
            .expandAll("Assignments", Student::getDone)
            .expand("Points", Assignment::getPoints)
            .as(doubleTable.class)
            .sum();
         student.setCredits(pointSum);
         return pointSum;
      });
      students.deriveColumn("Done", row -> {
         Student student = (Student) row.get("Students");
         String doneTopics = new ObjectTable<>("Students", student)
            .expandAll("Assignments", Student::getDone)
            .expand("Tasks", Assignment::getTask)
            .as(StringTable.class).join(", ");
         return doneTopics;
      });
      // end_code_fragment:

      addFragment(fragments, "FulibTables.nestedTablesResult", universityTable.toString());

      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjectsCreditsAssigned4Tables.png", studyRight);

      fragments.updateCodeFragments(".");
   }

   private static void addFragment(CodeFragments fragments, String name, String content)
   {
      fragments.getFragmentMap().put(name, content);
   }
}
