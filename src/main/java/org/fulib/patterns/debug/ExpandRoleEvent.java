package org.fulib.patterns.debug;

import org.fulib.patterns.model.RoleObject;

public class ExpandRoleEvent implements DebugEvent
{
   private final RoleObject role;

   public ExpandRoleEvent(RoleObject role)
   {
      this.role = role;
   }

   public RoleObject getRole()
   {
      return this.role;
   }

   @Override
   public String toString()
   {
      return "expanding " + this.role;
   }
}
