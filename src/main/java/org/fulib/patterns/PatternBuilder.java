package org.fulib.patterns;

import org.fulib.patterns.model.*;

import java.util.function.Function;
import java.util.function.Predicate;

public class PatternBuilder
{
   private String packageName;
   private final Pattern pattern;

   public PatternBuilder(String packageName)
   {
      this.packageName = packageName;

      pattern = new Pattern();
   }

   public Pattern getPattern()
   {
      return pattern;
   }

   public PatternObject buildPatternObject(String name)
   {
      PatternObject patternObject = new PatternObject().setName(name);
      pattern.withObjects(patternObject);
      return patternObject;
   }

   public PatternBuilder buildPatternLink(PatternObject src, String srcRoleName, String tgtRoleName, PatternObject tgt)
   {
      RoleObject srcRole = new RoleObject().setName(srcRoleName).setObject(src).setPattern(pattern);
      new RoleObject().setName(tgtRoleName).setObject(tgt).setOther(srcRole).setPattern(pattern);
      return this;
   }

   public PatternBuilder buildAttibuteConstraint(Predicate predicate, PatternObject object)
   {
      AttributeConstraint constraint = new AttributeConstraint().setPredicate(predicate).setObject(object).setPattern(pattern);
      return this;
   }

   public PatternBuilder buildMatchConstraint(Predicate predicate, PatternObject... objects)
   {
      MatchConstraint constraint = new MatchConstraint().setPredicate(predicate).withObjects(objects).setPattern(pattern);
      return this;
   }

}
