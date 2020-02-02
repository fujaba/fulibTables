package org.fulib.patterns;

import org.fulib.patterns.model.PatternObject;

import java.util.List;

/**
 * @since 1.3
 */
public class AmbiguousMatchException extends RuntimeException
{
   private final PatternObject patternObject;
   private final List<Object> matches;

   public AmbiguousMatchException(PatternObject patternObject, List<Object> matches)
   {
      this.patternObject = patternObject;
      this.matches = matches;
   }

   public PatternObject getPatternObject()
   {
      return this.patternObject;
   }

   public List<Object> getMatches()
   {
      return this.matches;
   }

   @Override
   public String getMessage()
   {
      return "pattern " + this.patternObject + " matches multiple objects: " + this.matches;
   }
}
