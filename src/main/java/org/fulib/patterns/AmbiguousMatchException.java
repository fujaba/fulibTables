package org.fulib.patterns;

import org.fulib.patterns.model.PatternObject;

import java.util.Set;

/**
 * @since 1.3
 */
public class AmbiguousMatchException extends RuntimeException
{
   private final PatternObject patternObject;
   private final Set<Object> matches;

   public AmbiguousMatchException(PatternObject patternObject, Set<Object> matches)
   {
      this.patternObject = patternObject;
      this.matches = matches;
   }

   public PatternObject getPatternObject()
   {
      return this.patternObject;
   }

   public Set<Object> getMatches()
   {
      return this.matches;
   }

   @Override
   public String getMessage()
   {
      return "pattern " + this.patternObject + " matches multiple objects: " + this.matches;
   }
}
