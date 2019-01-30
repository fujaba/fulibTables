package uniks.studyright;

import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.builder.ClassBuilder;
import org.fulib.builder.ClassModelBuilder;
import org.fulib.classmodel.ClassModel;

public class StudyRightClassModel
{
   public static void main(String[] args)
   {
      new StudyRightClassModel().generateClassModelCode();
   }

   private void generateClassModelCode()
   {
      // start_code_fragment: FulibTables.classmodel
      ClassModelBuilder mb = Fulib.classModelBuilder("uniks.studyright.model", "src/test/java");

      ClassBuilder university = mb.buildClass("University")
            .buildAttribute("name", mb.STRING);

      ClassBuilder student = mb.buildClass("Student")
            .buildAttribute("name", mb.STRING)
            .buildAttribute("studentId", mb.STRING)
            .buildAttribute("credits", mb.DOUBLE)
            .buildAttribute("points", mb.DOUBLE)
            .buildAttribute("motivation", mb.DOUBLE);

      ClassBuilder room = mb.buildClass("Room")
            .buildAttribute("roomNo", mb.STRING)
            .buildAttribute("topic", mb.STRING)
            .buildAttribute("credits", mb.DOUBLE);

      ClassBuilder assignment = mb.buildClass("Assignment")
            .buildAttribute("task", mb.STRING)
            .buildAttribute("points", mb.DOUBLE);

      university.buildAssociation(student, "students", mb.MANY, "uni", mb.ONE);
      university.buildAssociation(room, "rooms", mb.MANY, "uni", mb.ONE);
      room.buildAssociation(student, "students", mb.MANY, "in", mb.ONE);
      room.buildAssociation(assignment, "assignments", mb.MANY, "room", mb.ONE);
      student.buildAssociation(assignment, "done", mb.MANY, "students", mb.MANY);
      student.buildAssociation(student, "friends", mb.MANY, "friends", mb.MANY);

      ClassModel model = mb.getClassModel();

      FulibTools.classDiagrams().dumpSVG(model, "doc/images/MainClassDiagram.svg");
      FulibTools.classDiagrams().dumpPng(model, "doc/images/MainClassDiagram.png");

      Fulib.generator().generate(model);
      // end_code_fragment:

      FulibTools.codeFragments().updateCodeFragments(".");
   }
}
