package org.fulib.patterns;

import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.builder.ClassBuilder;
import org.fulib.builder.ClassModelBuilder;

import java.util.function.Function;

public class PatternModelGen
{
   public static void main(String[] args)
   {
      ClassModelBuilder mb = Fulib.classModelBuilder("org.fulib.patterns.model");
      ClassBuilder pattern = mb.buildClass("Pattern");
      ClassBuilder patternObject = mb.buildClass("PatternObject")
            .buildAttribute("name", mb.STRING);
      ClassBuilder roleObject = mb.buildClass("RoleObject")
            .buildAttribute("name", mb.STRING);
      ClassBuilder attributeConstraint = mb.buildClass("AttributeConstraint");
            // .buildAttribute("function", "java.util.function.Function");
      ClassBuilder matchConstraint = mb.buildClass("MatchConstraint");
            // .buildAttribute("function", "java.util.function.Function");

      pattern.buildAssociation(patternObject, "objects", mb.MANY, "pattern", mb.ONE);
      pattern.buildAssociation(roleObject, "roles", mb.MANY, "pattern", mb.ONE);
      pattern.buildAssociation(attributeConstraint, "attributeConstraints", mb.MANY, "pattern", mb.ONE);
      pattern.buildAssociation(matchConstraint, "matchConstraints", mb.MANY, "pattern", mb.ONE);

      roleObject.buildAssociation(roleObject, "other", mb.ONE, "other", mb.ONE);
      roleObject.buildAssociation(patternObject, "object", mb.ONE, "roles", mb.MANY);

      attributeConstraint.buildAssociation(patternObject, "object", mb.ONE, "attributeConstraints", mb.MANY);
      matchConstraint.buildAssociation(patternObject, "objects", mb.MANY, "matchConstraints", mb.MANY);

      FulibTools.classDiagrams().dumpPng(mb.getClassModel(), "doc/patternClassModel.png");
      FulibTools.classDiagrams().dumpSVG(mb.getClassModel(), "doc/patternClassModel.svg");

      Fulib.generator().generate(mb.getClassModel());


   }
}
