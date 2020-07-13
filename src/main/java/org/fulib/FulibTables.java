package org.fulib;

import org.fulib.patterns.PatternBuilder;
import org.fulib.patterns.PatternMatcher;
import org.fulib.patterns.model.Pattern;

/**
 * Provides factory methods for table- and pattern-related classes.
 */
public class FulibTables
{
   /**
    * @return a new {@link PatternBuilder} instance
    *
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
    * @return a new {@link PatternBuilder} instance
    *
    * @deprecated since 1.2; use {@link #patternBuilder()} instead
    */
   @Deprecated
   public static PatternBuilder patternBuilder(String packageName)
   {
      return new PatternBuilder(packageName);
   }

   /**
    * @param pattern
    *    the pattern
    *
    * @return a new pattern matcher for the given pattern
    */
   public static PatternMatcher matcher(Pattern pattern)
   {
      return new PatternMatcher(pattern);
   }
}
