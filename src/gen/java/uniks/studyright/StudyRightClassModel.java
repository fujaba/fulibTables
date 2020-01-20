package uniks.studyright;

import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.builder.ClassBuilder;
import org.fulib.builder.ClassModelBuilder;
import org.fulib.classmodel.ClassModel;

import static org.fulib.builder.ClassModelBuilder.*;

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

      ClassBuilder university = mb.buildClass("University").buildAttribute("name", STRING);

      ClassBuilder student = mb.buildClass("Student")
                               .buildAttribute("name", STRING)
                               .buildAttribute("studentId", STRING)
                               .buildAttribute("credits", DOUBLE)
                               .buildAttribute("points", DOUBLE)
                               .buildAttribute("motivation", DOUBLE);

      ClassBuilder room = mb.buildClass("Room")
                            .buildAttribute("roomNo", STRING)
                            .buildAttribute("topic", STRING)
                            .buildAttribute("credits", DOUBLE);

      ClassBuilder assignment = mb.buildClass("Assignment")
                                  .buildAttribute("task", STRING)
                                  .buildAttribute("points", DOUBLE);

      university.buildAssociation(student, "students", MANY, "uni", ONE);
      university.buildAssociation(room, "rooms", MANY, "uni", ONE);
      room.buildAssociation(student, "students", MANY, "in", ONE);
      room.buildAssociation(assignment, "assignments", MANY, "room", ONE);
      student.buildAssociation(assignment, "done", MANY, "students", MANY);
      student.buildAssociation(student, "friends", MANY, "friends", MANY);

      ClassModel model = mb.getClassModel();

      FulibTools.classDiagrams().dumpSVG(model, "doc/images/MainClassDiagram.svg");
      FulibTools.classDiagrams().dumpPng(model, "doc/images/MainClassDiagram.png");

      Fulib.generator().generate(model);
      // end_code_fragment:

      FulibTools.codeFragments().updateCodeFragments(".");
   }
}
