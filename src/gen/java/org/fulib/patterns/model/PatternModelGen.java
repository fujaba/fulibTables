package org.fulib.patterns.model;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.List;

@SuppressWarnings("unused")
public class PatternModelGen implements ClassModelDecorator
{
   class Pattern
   {
      @Link("pattern")
      List<PatternObject> objects;

      @Link("pattern")
      List<RoleObject> roles;

      @Link("pattern")
      List<AttributeConstraint> attributeConstraints;

      @Link("pattern")
      List<MatchConstraint> matchConstraints;
   }

   class PatternObject
   {
      String name;

      @Link("objects")
      Pattern pattern;

      @Link("object")
      List<RoleObject> roles;

      @Link("object")
      List<AttributeConstraint> attributeConstraints;

      @Link("objects")
      List<MatchConstraint> matchConstraints;
   }

   class RoleObject
   {
      String name;

      @Link("roles")
      Pattern pattern;

      @Link("other")
      RoleObject other;

      @Link("roles")
      PatternObject object;
   }

   class AttributeConstraint
   {
      @Link("attributeConstraints")
      Pattern pattern;

      @Link("attributeConstraints")
      PatternObject object;
   }

   class MatchConstraint
   {
      @Link("matchConstraints")
      Pattern pattern;

      @Link("matchConstraints")
      List<PatternObject> objects;
   }

   @Override
   public void decorate(ClassModelManager mm)
   {
      mm.haveNestedClasses(PatternModelGen.class);
   }
}
