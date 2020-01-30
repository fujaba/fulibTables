package org.fulib.patterns.debug;

import org.fulib.patterns.model.PatternObject;

public class MultiplyRootsEvent implements DebugEvent
{
   private final PatternObject root;

   public MultiplyRootsEvent(PatternObject root)
   {
      this.root = root;
   }

   public PatternObject getRoot()
   {
      return this.root;
   }

   @Override
   public String toString()
   {
      return "multiplying by root " + this.root;
   }
}
