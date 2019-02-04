package org.fulib;

import org.fulib.patterns.PatternBuilder;
import org.fulib.patterns.PatternMatcher;
import org.fulib.patterns.model.Pattern;

public class FulibTables
{
   public static PatternBuilder patternBuilder(String packageName)
   {
      return new PatternBuilder(packageName);
   }

   public static PatternMatcher matcher(Pattern pattern)
   {
      return new PatternMatcher(pattern);
   }
}
