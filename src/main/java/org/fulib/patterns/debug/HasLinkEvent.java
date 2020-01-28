package org.fulib.patterns.debug;

import org.fulib.patterns.model.RoleObject;

public class HasLinkEvent implements DebugEvent
{
   private final RoleObject role;

   public HasLinkEvent(RoleObject role)
   {
      this.role = role;
   }

   public RoleObject getRole()
   {
      return this.role;
   }
}
