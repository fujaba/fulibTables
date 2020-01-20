package org.fulib.patterns;

import org.fulib.patterns.model.*;

import java.util.Map;
import java.util.function.Predicate;

public class PatternBuilder
{
   // =============== Fields ===============

   private String packageName;
   private final Pattern pattern;

   // =============== Constructors ===============

   public PatternBuilder(String packageName)
   {
      this.packageName = packageName;

      this.pattern = new Pattern();
   }

   // =============== Properties ===============

   public Pattern getPattern()
   {
      return this.pattern;
   }

   // =============== Methods ===============

   public PatternObject buildPatternObject(String name)
   {
      PatternObject patternObject = new PatternObject().setName(name);
      this.pattern.withObjects(patternObject);
      return patternObject;
   }

   public PatternBuilder buildPatternLink(PatternObject src, String srcRoleName, String tgtRoleName, PatternObject tgt)
   {
      RoleObject srcRole = new RoleObject().setName(srcRoleName).setObject(src).setPattern(this.pattern);
      new RoleObject().setName(tgtRoleName).setObject(tgt).setOther(srcRole).setPattern(this.pattern);
      return this;
   }

   public PatternBuilder buildAttibuteConstraint(Predicate<? super Object> predicate, PatternObject object)
   {
      new AttributeConstraint().setPredicate(predicate).setObject(object).setPattern(this.pattern);
      return this;
   }

   public PatternBuilder buildMatchConstraint(Predicate<? super Map<String, Object>> predicate,
      PatternObject... objects)
   {
      //noinspection RedundantCast
      new MatchConstraint().setPredicate(predicate).withObjects((Object[]) objects).setPattern(this.pattern);
      return this;
   }
}
