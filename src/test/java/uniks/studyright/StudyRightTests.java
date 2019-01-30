package uniks.studyright;

import org.fulib.FulibTools;
import org.fulib.tables.ObjectTable;
import org.fulib.tables.doubleTable;
import org.junit.Test;
import uniks.studyright.model.Assignment;
import uniks.studyright.model.Room;
import uniks.studyright.model.Student;
import uniks.studyright.model.University;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StudyRightTests
{
   @Test
   public void testModelQueries()
   {
      // start_code_fragment: FulibTables.objectModel
      // build object structure
      University studyRight = new University().setName("Study Right");
      String name = University[].class.getName();
      System.out.println(name);

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
      // end_code_fragment:

      FulibTools.objectDiagrams().dumpSVG("doc/images/studyRightObjects.svg", studyRight);
      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjects.png", studyRight);


      // some tables
      // start_code_fragment: FulibTables.createUniTable1
      ObjectTable uniTable = new UniversityTable(studyRight);
      RoomTable roomsTable = uniTable.expandRooms("Room");
      AssignmentTable assignmentsTable = roomsTable.expandAssignments("Assignment");
      // end_code_fragment:

      uniTable = new UniversityTable(studyRight);

      StringBuilder buf = new StringBuilder()
            .append("// start_code_fragment: StudyRightTables.uniTable1 \n")
            .append(uniTable)
            .append("// end_code_fragment: \n")
            ;
//
//      roomsTable = uniTable.expandRooms("Room");
//      buf.append("// start_code_fragment: StudyRightTables.uniTable2 \n")
//            .append(uniTable.toMarkDown())
//            .append("// end_code_fragment: \n")
//      ;
//
//      assignmentsTable = roomsTable.expandAssignments("Assignment");
//      buf.append("// start_code_fragment: StudyRightTables.uniTable3 \n")
//            .append(uniTable.toMarkDown())
//            .append("// end_code_fragment: \n")
//      ;
//
//
//
//      // start_code_fragment: FulibTables.loop_through_assignments
//      // loop through assignments
//      double sum = 0;
//      for (Assignment a : assignmentsTable.toSet())
//      {
//         sum += a.getPoints();
//      }
//      assertThat(sum, equalTo(89.0));
//      // end_code_fragment:
//
//
//      // start_code_fragment: FulibTables.pointsTable
//      doubleTable pointsTable = assignmentsTable.expandPoints("Points");
//      sum = pointsTable.sum();
//      assertThat(roomsTable.getTable().size(), equalTo(4));
//      assertThat(assignmentsTable.getTable().size(), equalTo(4));
//      assertThat(sum, equalTo(89.0));
//      // end_code_fragment:
//
//      buf.append("// start_code_fragment: StudyRightTables.pointsTableResult \n")
//            .append(uniTable.toMarkDown())
//            .append("// end_code_fragment: \n")
//      ;
//
//
//      // start_code_fragment: FulibTables.studentsTable
//      StudentTable students = roomsTable.expandStudents("Student");
//      assertThat(students.getTable().size(), equalTo(6));
//      // end_code_fragment:
//
//      buf.append("// start_code_fragment: FulibTables.studentsTableResult \n")
//            .append(uniTable.toMarkDown())
//            .append("// end_code_fragment: \n")
//      ;
//
//      // start_code_fragment: FulibTables.filterAssignmentsTable
//      assignmentsTable.filter( a -> a.getPoints() <= 30);
//      assertThat(students.getTable().size(), equalTo(4));
//      // end_code_fragment:
//
//      buf.append("// start_code_fragment: FulibTables.filterAssignmentsTableResult \n")
//            .append(uniTable.toMarkDown())
//            .append("// end_code_fragment: \n")
//      ;
//
//
//      // start_code_fragment: FulibTables.filterRowTable
//      // filter row
//      uniTable = new UniversityTable(studyRight);
//      roomsTable = uniTable.expandRooms();
//      students = roomsTable.expandStudents("Student");
//      assignmentsTable = roomsTable.expandAssignments("Assignment");
//
//      students.filterRow( row ->
//      {
//         Student studi = (Student) row.get("Student");
//         Assignment assignment = (Assignment) row.get("Assignment");
//         return studi.getDone().contains(assignment);
//      });
//
//      assertThat(students.getTable().size(), equalTo(1));
//      // end_code_fragment:
//
//      buf.append("// start_code_fragment: FulibTables.filterRowTableResult \n")
//            .append(uniTable.toMarkDown())
//            .append("// end_code_fragment: \n")
//      ;
//
//
//      // start_code_fragment: FulibTables.filterHasDone
//      // filter row
//      uniTable = new UniversityTable(studyRight);
//      roomsTable = uniTable.expandRooms();
//      students = roomsTable.expandStudents("Student");
//      assignmentsTable = roomsTable.expandAssignments("Assignment");
//      students.hasDone(assignmentsTable);
//
//      assertThat(students.getTable().size(), equalTo(1));
//      // end_code_fragment:
//
//      buf.append("// start_code_fragment: FulibTables.filterHasDoneResult \n")
//            .append(uniTable.toMarkDown())
//            .append("// end_code_fragment: \n")
//      ;
//
//
//      // start_code_fragment: FulibTables.doCurrentAssignments
//      uniTable = new UniversityTable(studyRight);
//      roomsTable = uniTable.expandRooms();
//      students = roomsTable.expandStudents("Student");
//      assignmentsTable = roomsTable.expandAssignments("Assignment");
//
//      // do current assignments
//      students.filterRow( row ->
//      {
//         Student studi = (Student) row.get("Student");
//         Assignment assignment = (Assignment) row.get("Assignment");
//         studi.withDone(assignment);
//         return true;
//      });
//
//      FulibTools.objectDiagrams().dumpPng("../fulib/doc/images/studyRightObjectsMoreDone4Tables.png", studyRight);
//
//      assertThat(alice.getDone().size(), equalTo(2));
//      assertThat(integrals.getStudents().contains(alice), is(true));
//
//      // show size of done
//      uniTable.addColumn("noOfDone",  row ->
//      {
//         Student studi = (Student) row.get("Student");
//         return studi.getDone().size();
//      });
//
//      // show done
//      students.expandDone("Done");
//      // end_code_fragment:
//
//      buf.append("// start_code_fragment: FulibTables.doCurrentAssignmentsResult \n")
//            .append(uniTable.toMarkDown())
//            .append("// end_code_fragment: \n")
//      ;
//
//
//      // start_code_fragment: FulibTables.dropColumnsAssignment
//      uniTable.dropColumns("Assignment");
//      // end_code_fragment:
//
//      buf.append("// start_code_fragment: FulibTables.dropColumnsAssignmentResult \n")
//            .append(uniTable.toMarkDown())
//            .append("// end_code_fragment: \n")
//      ;


//      // start_code_fragment: FulibTables.selectColumns
//      students.selectColumns("Student", "Done");
//      assertThat(students.getTable().size(), equalTo(6));
//      // end_code_fragment:
//
//      buf.append("// start_code_fragment: FulibTables.selectColumnsResult \n")
//            .append(uniTable.toMarkDown())
//            .append("// end_code_fragment: \n")
//      ;
//
//
//      // start_code_fragment: FulibTables.nestedTables
//      uniTable = new UniversityTable(studyRight);
//      students = uniTable.expandStudents("Students");
//      students.addColumn("Credits", row -> {
//         Student student = (Student) row.get("Students");
//         double pointSum = new StudentTable(student).expandDone().expandPoints().sum();
//         student.setCredits(pointSum);
//         return pointSum;
//      });
//      students.addColumn("Done", row -> {
//         Student student = (Student) row.get("Students");
//         String doneTopics = new StudentTable(student).expandDone().expandTopic().join(", ");
//         return doneTopics;
//      });
//      // end_code_fragment:
//
//      buf.append("// start_code_fragment: FulibTables.nestedTablesResult \n")
//            .append(uniTable.toMarkDown())
//            .append("// end_code_fragment: \n")
//      ;

      FulibTools.objectDiagrams().dumpPng("../fulib/doc/images/studyRightObjectsCreditsAssigned4Tables.png", studyRight);

      Files.write(Paths.get("src/test/resources/images/UniTable1.java"), buf.toString().getBytes());

      FulibTools.codeFragments().updateCodeFragments(".", "../fulib");


//      // some table stuff
//      ObjectTable universityTable = new ObjectTable("University", studyRight);
//      ObjectTable roomTable = universityTable.expandLink("Room", University.PROPERTY_rooms);
//      ObjectTable assignmentTable = roomTable.expandLink("Assignment", Room.PROPERTY_assignments);
//      doubleTable pointsTable = assignmentTable.expandDouble("Points", Assignment.PROPERTY_points);
//
//
//      System.out.println(universityTable);

   }
}
