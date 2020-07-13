package org.fulib.patterns.debug;

import org.fulib.patterns.model.AttributeConstraint;
import org.fulib.tables.Table;

/**
 * @since 1.3
 */
public class AttributeConstraintEvent extends TableDebugEvent
{
   private final AttributeConstraint constraint;

   public AttributeConstraintEvent(AttributeConstraint constraint, Table<?> table)
   {
      super(table);
      this.constraint = constraint;
   }

   public AttributeConstraint getConstraint()
   {
      return this.constraint;
   }

   @Override
   public String toString()
   {
      return "filtering by " + this.constraint + '\n' + this.getTableSnapshot();
   }
}
