package org.fulib.patterns;

import org.fulib.patterns.model.PatternObject;

public class NoMatchException extends RuntimeException
{
   private final PatternObject patternObject;

   public NoMatchException(PatternObject patternObject)
   {
      this.patternObject = patternObject;
   }

   public PatternObject getPatternObject()
   {
      return this.patternObject;
   }

   @Override
   public String getMessage()
   {
      return "no matches for " + this.patternObject;
   }
}
