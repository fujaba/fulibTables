package org.fulib.patterns;

import org.fulib.FulibTables;
import org.fulib.FulibTools;

import org.fulib.patterns.model.PatternObject;
import org.fulib.tables.ObjectTable;
import org.junit.Test;
import uniks.studyright.model.Assignment;
import uniks.studyright.model.Room;
import uniks.studyright.model.Student;
import uniks.studyright.model.University;

import java.util.LinkedHashMap;

public class TestStudyRightPatterns
{
   @Test
   @SuppressWarnings("unused")
   public void testPatterns()
   {
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
      Student bob = new Student().setStudentId("m2323").setName("Bobby").setUni(studyRight).setIn(artsRoom).withFriends(alice);
      Student carli = new Student().setStudentId("m2323").setName("Carli").setUni(studyRight).setIn(mathRoom);
      // end_code_fragment:

      PatternBuilder pb = FulibTables.patternBuilder();

      PatternObject uni = pb.buildPatternObject("uni");
      PatternObject room = pb.buildPatternObject("room");
      PatternObject student = pb.buildPatternObject("student");
      PatternObject assignment = pb.buildPatternObject("assignment");
      PatternObject points = pb.buildPatternObject("points");

      pb.buildPatternLink(uni, Room.PROPERTY_uni, University.PROPERTY_rooms, room);
      pb.buildPatternLink(uni, Student.PROPERTY_uni, University.PROPERTY_students, student);
      pb.buildPatternLink(student, Room.PROPERTY_students, Student.PROPERTY_in, room);
      pb.buildPatternLink(room, Assignment.PROPERTY_room, Room.PROPERTY_assignments, assignment);
      pb.buildPatternLink(assignment, Assignment.PROPERTY_points, points);

      pb.buildAttributeConstraint(points, (Double d) -> d > 20);
      pb.buildMatchConstraint(row -> {
         Room r = (Room) row.get("room");
         Assignment a = (Assignment) row.get("assignment");

         return r.getCredits() >= a.getPoints();
      }, room, assignment);

      PatternMatcher matcher = FulibTables.matcher(pb.getPattern());
      ObjectTable uniTable = matcher.match("uni", studyRight);

      // matcher.getObject2TableMap().get(room).expandDouble("credits", Room.PROPERTY_credits);

      System.out.println(uniTable);
   }
}
