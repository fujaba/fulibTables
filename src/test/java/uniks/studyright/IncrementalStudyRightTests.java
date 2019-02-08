package uniks.studyright;

import org.fulib.FulibTools;
import org.fulib.tables.IncrementalObjectTable;
import org.junit.Test;
import uniks.studyright.model.Assignment;
import uniks.studyright.model.Room;
import uniks.studyright.model.Student;
import uniks.studyright.model.University;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class IncrementalStudyRightTests
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

      Assignment integrals = new Assignment().setTask("integrals").setPoints(42).setRoom(mathRoom);
      Assignment matrix = new Assignment().setTask("matrices").setPoints(23).setRoom(mathRoom);
      Assignment drawings = new Assignment().setTask("drawings").setPoints(12).setRoom(artsRoom);
      Assignment sculptures = new Assignment().setTask("sculptures").setPoints(12).setRoom(artsRoom);

      Student alice = new Student().setStudentId("m4242").setName("Alice").setUni(studyRight).setIn(mathRoom).withDone(integrals);
      Student bob = new Student().setStudentId("m2323").setName("Bobby").setUni(studyRight).setIn(artsRoom).withFriends(alice);
      Student carli = new Student().setStudentId("m4646").setName("Carli").setUni(studyRight).setIn(mathRoom);

      FulibTools.objectDiagrams().dumpSVG("doc/images/studyRightObjects.svg", studyRight);
      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjects.png", studyRight);
      // end_code_fragment:


      IncrementalObjectTable uni = new IncrementalObjectTable("Uni", studyRight);
      System.out.println(uni);
      assertThat(uni.getTable().size(), equalTo(1));

      IncrementalObjectTable room = uni.expandLink("Room", University.PROPERTY_rooms);
      System.out.println(uni);
      assertThat(uni.getTable().size(), equalTo(2));

      IncrementalObjectTable assignment = room.expandLink("Assignment", Room.PROPERTY_assignments);
      System.out.println(uni);
      assertThat(uni.getTable().size(), equalTo(4));

      IncrementalObjectTable allStudents = uni.expandLink("AllStudents", University.PROPERTY_students);
      System.out.println(uni);
      assertThat(uni.getTable().size(), equalTo(12));

      Student dora = new Student().setName("Dora").setStudentId("m8484").setUni(studyRight);
      System.out.println(uni);
      assertThat(uni.getTable().size(), equalTo(16));

      Assignment sitUps = new Assignment().setTask("SitUps").setPoints(12.0);
      Room sportsRoom = new Room().setRoomNo("wa1339").setTopic("Sports").withAssignments(sitUps)
            .withStudents(dora)
            .setUni(studyRight);
      System.out.println(uni);
      assertThat(uni.getTable().size(), equalTo(20));

      Assignment jumpingJacks = new Assignment().setTask("jumping jacks").setPoints(13).setRoom(sportsRoom);
      System.out.println(uni);
      assertThat(uni.getTable().size(), equalTo(24));
   }
}
