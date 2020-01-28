package org.fulib.patterns.debug;

import org.fulib.patterns.model.AttributeConstraint;

public class AttributeConstraintEvent implements DebugEvent
{
   private final AttributeConstraint constraint;

   public AttributeConstraintEvent(AttributeConstraint constraint)
   {
      this.constraint = constraint;
   }

   public AttributeConstraint getConstraint()
   {
      return this.constraint;
   }

   @Override
   public String toString()
   {
      return "filtering by " + this.constraint;
   }
}
