package org.fulib;

import org.fulib.patterns.PatternBuilder;
import org.fulib.patterns.PatternMatcher;
import org.fulib.patterns.model.Pattern;

public class FulibTables
{
   /**
    * @since 1.2
    */
   public static PatternBuilder patternBuilder()
   {
      return new PatternBuilder();
   }

   /**
    * @param packageName
    *    unused
    *
    * @deprecated since 1.2; use {@link #patternBuilder()} instead
    */
   @Deprecated
   public static PatternBuilder patternBuilder(String packageName)
   {
      return new PatternBuilder(packageName);
   }

   public static PatternMatcher matcher(Pattern pattern)
   {
      return new PatternMatcher(pattern);
   }
}
