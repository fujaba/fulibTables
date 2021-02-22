package uniks.studyright.model;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.List;

@SuppressWarnings("unused")
public class GenModel implements ClassModelDecorator
{
   class University
   {
      String name;

      @Link("uni")
      List<Student> students;

      @Link("uni")
      List<Room> rooms;
   }

   class Student
   {
      String name;
      String studentId;
      double credits;
      double points;
      double motivation;

      @Link("students")
      University uni;

      @Link("students")
      Room in;

      @Link("students")
      List<Assignment> done;

      @Link("friends")
      List<Student> friends;
   }

   class Room
   {
      String roomNo;
      String topic;
      double credits;

      @Link("rooms")
      University uni;

      @Link("in")
      List<Student> students;

      @Link("room")
      List<Assignment> assignments;
   }

   class Assignment
   {
      String task;
      double points;

      @Link("assignments")
      Room room;

      @Link("done")
      List<Student> students;
   }

   @Override
   public void decorate(ClassModelManager classModelManager)
   {
      classModelManager.haveNestedClasses(GenModel.class);
   }
}
