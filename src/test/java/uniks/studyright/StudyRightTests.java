package uniks.studyright;

import org.fulib.FulibTools;
import org.fulib.tables.ObjectTable;
import org.fulib.tables.doubleTable;
import org.junit.Test;
import uniks.studyright.model.Assignment;
import uniks.studyright.model.Room;
import uniks.studyright.model.Student;
import uniks.studyright.model.University;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StudyRightTests
{
   @Test
   public void testModelQueries() throws IOException
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

      FulibTools.objectDiagrams().dumpSVG("doc/images/studyRightObjects.svg", studyRight);
      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjects.png", studyRight);
      // end_code_fragment:


      // start_code_fragment: FulibTables.createUniTable1
      // some table stuff
      ObjectTable universityTable = new ObjectTable("University", studyRight);
      ObjectTable roomsTable = universityTable.expandLink("Room", University.PROPERTY_rooms);
      ObjectTable assignmentsTable = roomsTable.expandLink("Assignment", Room.PROPERTY_assignments);
      // end_code_fragment:

      universityTable = new ObjectTable("University", studyRight);

      StringBuilder buf = new StringBuilder()
            .append("// start_code_fragment: FulibTables.uniTable1 \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
            ;

      roomsTable = universityTable.expandLink("Room", University.PROPERTY_rooms);
      buf.append("// start_code_fragment: FulibTables.uniTable2 \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
      ;

      assignmentsTable = roomsTable.expandLink("Assignment", Room.PROPERTY_assignments);
      buf.append("// start_code_fragment: FulibTables.uniTable3 \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
      ;



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
      doubleTable pointsTable = assignmentsTable.expandDouble("Points", Assignment.PROPERTY_points);
      sum = pointsTable.sum();
      assertThat(roomsTable.getTable().size(), equalTo(4));
      assertThat(assignmentsTable.getTable().size(), equalTo(4));
      assertThat(sum, equalTo(89.0));
      // end_code_fragment:

      buf.append("// start_code_fragment: StudyRightTables.pointsTableResult \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
      ;


      // start_code_fragment: FulibTables.studentsTable
      ObjectTable students = roomsTable.expandLink("Student", "students");
      assertThat(students.getTable().size(), equalTo(6));
      // end_code_fragment:

      buf.append("// start_code_fragment: FulibTables.studentsTableResult \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
      ;

      // start_code_fragment: FulibTables.filterAssignmentsTable
      assignmentsTable.filter( a -> ((Assignment) a).getPoints() <= 30);
      assertThat(students.getTable().size(), equalTo(4));
      // end_code_fragment:

      buf.append("// start_code_fragment: FulibTables.filterAssignmentsTableResult \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
      ;


      // start_code_fragment: FulibTables.filterRowTable
      // filter row
      universityTable = new ObjectTable("University", studyRight);
      roomsTable = universityTable.expandLink("Room", University.PROPERTY_rooms);
      students = roomsTable.expandLink("Student", "students");
      assignmentsTable = roomsTable.expandLink("Assignment", Room.PROPERTY_assignments);

      students.filterRow( row ->
      {
         Student studi = (Student) row.get("Student");
         Assignment assignment = (Assignment) row.get("Assignment");
         return studi.getDone().contains(assignment);
      });

      assertThat(students.getTable().size(), equalTo(1));
      // end_code_fragment:

      buf.append("// start_code_fragment: FulibTables.filterRowTableResult \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
      ;


      // start_code_fragment: FulibTables.filterHasDone
      // filter row
      universityTable = new ObjectTable("University", studyRight);
      roomsTable = universityTable.expandLink("Room", University.PROPERTY_rooms);
      students = roomsTable.expandLink("Student", "students");
      assignmentsTable = roomsTable.expandLink("Assignment", Room.PROPERTY_assignments);
      students.hasLink(Student.PROPERTY_done, assignmentsTable);

      assertThat(students.getTable().size(), equalTo(1));
      // end_code_fragment:

      buf.append("// start_code_fragment: FulibTables.filterHasDoneResult \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
      ;


      // start_code_fragment: FulibTables.doCurrentAssignments
      universityTable = new ObjectTable("University", studyRight);
      roomsTable = universityTable.expandLink("Room", University.PROPERTY_rooms);
      students = roomsTable.expandLink("Student", "students");
      assignmentsTable = roomsTable.expandLink("Assignment", Room.PROPERTY_assignments);

      // do current assignments
      students.filterRow( row ->
      {
         Student studi = (Student) row.get("Student");
         Assignment assignment = (Assignment) row.get("Assignment");
         studi.withDone(assignment);
         return true;
      });

      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjectsMoreDone4Tables.png", studyRight);

      assertThat(alice.getDone().size(), equalTo(2));
      assertThat(integrals.getStudents().contains(alice), is(true));

      // show size of done
      universityTable.addColumn("noOfDone",  row ->
      {
         Student studi = (Student) row.get("Student");
         return studi.getDone().size();
      });

      // show done
      students.expandLink("Done", Student.PROPERTY_done);
      // end_code_fragment:

      buf.append("// start_code_fragment: FulibTables.doCurrentAssignmentsResult \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
      ;


      // start_code_fragment: FulibTables.dropColumnsAssignment
      universityTable.dropColumns("Assignment");
      // end_code_fragment:

      buf.append("// start_code_fragment: FulibTables.dropColumnsAssignmentResult \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
      ;


      // start_code_fragment: FulibTables.selectColumns
      students.selectColumns("Student", "Done");
      assertThat(students.getTable().size(), equalTo(6));
      // end_code_fragment:

      buf.append("// start_code_fragment: FulibTables.selectColumnsResult \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
      ;


      // start_code_fragment: FulibTables.nestedTables
      universityTable = new ObjectTable("University", studyRight);
      students = universityTable.expandLink("Students", "students");
      students.addColumn("Credits", row -> {
         Student student = (Student) row.get("Students");
         double pointSum = new ObjectTable(student)
               .expandLink("Assignments", Student.PROPERTY_done)
               .expandDouble("Points", Assignment.PROPERTY_points).sum();
         student.setCredits(pointSum);
         return pointSum;
      });
      students.addColumn("Done", row -> {
         Student student = (Student) row.get("Students");
         String doneTopics = new ObjectTable("Students", student)
               .expandLink("Assignments", Student.PROPERTY_done)
               .expandString("Tasks", Assignment.PROPERTY_task).join(", ");
         return doneTopics;
      });
      // end_code_fragment:

      buf.append("// start_code_fragment: FulibTables.nestedTablesResult \n")
            .append(universityTable)
            .append("// end_code_fragment: \n")
      ;

      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjectsCreditsAssigned4Tables.png", studyRight);

      new File("src/test/resources/images").mkdirs();
      Path path = Paths.get("src/test/resources/images/UniTable1.java");
      Files.write(path, buf.toString().getBytes());

      FulibTools.codeFragments().updateCodeFragments(".");


   }
}