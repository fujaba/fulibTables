package org.fulib.patterns.debug;

import org.fulib.tables.Table;

/**
 * @since 1.3
 */
public class TableDebugEvent implements DebugEvent
{
   private final Table<?> tableSnapshot;

   public TableDebugEvent(Table<?> table)
   {
      this.tableSnapshot = table.copy();
   }

   public Table<?> getTableSnapshot()
   {
      return this.tableSnapshot;
   }
}
