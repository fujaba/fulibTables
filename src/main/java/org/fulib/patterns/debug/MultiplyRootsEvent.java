package org.fulib.patterns.debug;

import org.fulib.patterns.model.PatternObject;
import org.fulib.tables.Table;

/**
 * @since 1.3
 */
public class MultiplyRootsEvent extends TableDebugEvent
{
   private final PatternObject root;

   public MultiplyRootsEvent(PatternObject root, Table<?> table)
   {
      super(table);
      this.root = root;
   }

   public PatternObject getRoot()
   {
      return this.root;
   }

   @Override
   public String toString()
   {
      return "multiplying by root " + this.root + '\n' + this.getTableSnapshot();
   }
}
