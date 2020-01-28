package org.fulib.patterns.debug;

import org.fulib.patterns.model.MatchConstraint;

public class MatchConstraintEvent implements DebugEvent
{
   private final MatchConstraint constraint;

   public MatchConstraintEvent(MatchConstraint constraint)
   {
      this.constraint = constraint;
   }

   public MatchConstraint getConstraint()
   {
      return this.constraint;
   }

   @Override
   public String toString()
   {
      return "filtering by " + this.constraint;
   }
}
