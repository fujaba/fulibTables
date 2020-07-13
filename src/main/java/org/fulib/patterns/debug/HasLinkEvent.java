package org.fulib.patterns.debug;

import org.fulib.patterns.model.RoleObject;
import org.fulib.tables.Table;

/**
 * @since 1.3
 */
public class HasLinkEvent extends TableDebugEvent
{
   private final RoleObject role;

   public HasLinkEvent(RoleObject role, Table<?> table)
   {
      super(table);
      this.role = role;
   }

   public RoleObject getRole()
   {
      return this.role;
   }

   @Override
   public String toString()
   {
      return "filtering by " + this.role + '\n' + this.getTableSnapshot();
   }
}
