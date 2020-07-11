package org.fulib.patterns;

import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.builder.ClassBuilder;
import org.fulib.builder.ClassModelBuilder;

import static org.fulib.builder.Type.*;

public class PatternModelGen
{
   public static void main(String[] args)
   {
      ClassModelBuilder mb = Fulib.classModelBuilder("org.fulib.patterns.model");

      ClassBuilder pattern = mb.buildClass("Pattern");
      ClassBuilder patternObject = mb.buildClass("PatternObject").buildAttribute("name", STRING);
      ClassBuilder roleObject = mb.buildClass("RoleObject").buildAttribute("name", STRING);
      ClassBuilder attributeConstraint = mb.buildClass("AttributeConstraint");
      // .buildAttribute("function", "java.util.function.Function");
      ClassBuilder matchConstraint = mb.buildClass("MatchConstraint");
      // .buildAttribute("function", "java.util.function.Function");

      pattern.buildAssociation(patternObject, "objects", MANY, "pattern", ONE);
      pattern.buildAssociation(roleObject, "roles", MANY, "pattern", ONE);
      pattern.buildAssociation(attributeConstraint, "attributeConstraints", MANY, "pattern", ONE);
      pattern.buildAssociation(matchConstraint, "matchConstraints", MANY, "pattern", ONE);

      roleObject.buildAssociation(roleObject, "other", ONE, "other", ONE);
      roleObject.buildAssociation(patternObject, "object", ONE, "roles", MANY);

      attributeConstraint.buildAssociation(patternObject, "object", ONE, "attributeConstraints", MANY);
      matchConstraint.buildAssociation(patternObject, "objects", MANY, "matchConstraints", MANY);

      FulibTools.classDiagrams().dumpPng(mb.getClassModel(), "doc/patternClassModel.png");
      FulibTools.classDiagrams().dumpSVG(mb.getClassModel(), "doc/patternClassModel.svg");

      Fulib.generator().generate(mb.getClassModel());
   }
}
