package uniks.studyright;

import org.fulib.FulibTools;
import org.fulib.tables.IncrementalObjectTable;
import org.junit.Test;
import uniks.studyright.model.Assignment;
import uniks.studyright.model.Room;
import uniks.studyright.model.Student;
import uniks.studyright.model.University;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class IncrementalStudyRightTests
{
   @Test
   @SuppressWarnings("unused")
   public void testModelQueries()
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

      Student alice = new Student().setStudentId("m4242").setName("Alice").setUni(studyRight).withDone(integrals);
      Student bob = new Student().setStudentId("m2323").setName("Bobby").setUni(studyRight).setIn(artsRoom).withFriends(alice);
      Student carli = new Student().setStudentId("m4646").setName("Carli").setUni(studyRight).setIn(mathRoom);

      FulibTools.objectDiagrams().dumpSVG("doc/images/studyRightObjects.svg", studyRight);
      FulibTools.objectDiagrams().dumpPng("doc/images/studyRightObjects.png", studyRight);
      // end_code_fragment:


      IncrementalObjectTable u42 = new IncrementalObjectTable("Uni", studyRight);
      IncrementalObjectTable r42 = u42.expandLink("Room", University.PROPERTY_rooms);
      r42.expandDouble("Credits", Room.PROPERTY_credits);
      System.out.println(u42);

      artsRoom.setCredits(32);
      System.out.println(u42);

      Room sports = new Room().setRoomNo("wa1339").setTopic("Sports").setCredits(2);
      studyRight.withRooms(sports);
      System.out.println(u42);

      studyRight.withoutRooms(sports);
      System.out.println(u42);

      IncrementalObjectTable u = new IncrementalObjectTable("Uni", studyRight);
      IncrementalObjectTable r = u.expandLink("Room", University.PROPERTY_rooms);
      IncrementalObjectTable s = u.expandLink("Studi", University.PROPERTY_students);
      System.out.println(u);

      s.hasLink(Student.PROPERTY_in, r);
      System.out.println(u);

      alice.setIn(mathRoom);
      System.out.println(u);

      bob.setIn(mathRoom);
      System.out.println(u);

      IncrementalObjectTable u1 = new IncrementalObjectTable("Uni", studyRight);
      IncrementalObjectTable r1 = u1.expandLink("Room", University.PROPERTY_rooms);
      IncrementalObjectTable s1 = u1.expandLink("Studi", University.PROPERTY_students);
      System.out.println(u1);

      studyRight.withRooms(sports);
      System.out.println(u1);

      studyRight.withoutRooms(artsRoom);
      System.out.println(u1);

      studyRight.withRooms(artsRoom);
      studyRight.withoutRooms(sports);
      System.out.println(u1);

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
