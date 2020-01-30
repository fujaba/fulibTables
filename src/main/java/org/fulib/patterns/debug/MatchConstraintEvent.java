package org.fulib.patterns.debug;

import org.fulib.patterns.model.MatchConstraint;
import org.fulib.tables.Table;

public class MatchConstraintEvent extends TableDebugEvent
{
   private final MatchConstraint constraint;

   public MatchConstraintEvent(MatchConstraint constraint, Table<?> table)
   {
      super(table);
      this.constraint = constraint;
   }

   public MatchConstraint getConstraint()
   {
      return this.constraint;
   }

   @Override
   public String toString()
   {
      return "filtering by " + this.constraint + '\n' + this.getTableSnapshot();
   }
}
