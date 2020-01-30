package org.fulib.patterns.debug;

import org.fulib.patterns.model.RoleObject;
import org.fulib.tables.Table;

public class ExpandRoleEvent extends TableDebugEvent
{
   private final RoleObject role;

   public ExpandRoleEvent(RoleObject role, Table<?> table)
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
      return "expanding " + this.role + '\n' + this.getTableSnapshot();
   }
}
