package org.fulib.patterns;

import org.fulib.FulibTables;
import org.fulib.patterns.model.*;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class PatternBuilder
{
   // =============== Fields ===============

   private final Pattern pattern;

   // =============== Constructors ===============

   /**
    * @since 1.2
    */
   public PatternBuilder()
   {
      this.pattern = new Pattern();
   }

   /**
    * @param packageName
    *    unused
    *
    * @deprecated since 1.2, use {@link #PatternBuilder()} instead
    */
   @Deprecated
   public PatternBuilder(String packageName)
   {
      this();
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

   /**
    * @since 1.2
    */
   public PatternBuilder buildPatternLink(PatternObject src, String attrName, PatternObject tgt)
   {
      return this.buildPatternLink(src, null, attrName, tgt);
   }

   public PatternBuilder buildPatternLink(PatternObject src, String srcRoleName, String tgtRoleName, PatternObject tgt)
   {
      RoleObject srcRole = new RoleObject().setName(srcRoleName).setObject(src).setPattern(this.pattern);
      new RoleObject().setName(tgtRoleName).setObject(tgt).setOther(srcRole).setPattern(this.pattern);
      return this;
   }

   /**
    * @deprecated since 1.2; use {@link #buildAttributeConstraint(PatternObject, Predicate)} instead (typo "Att_ibute")
    */
   @Deprecated
   public PatternBuilder buildAttibuteConstraint(Predicate<? super Object> predicate, PatternObject object)
   {
      return this.buildAttributeConstraint(object, predicate);
   }

   /**
    * @since 1.2
    */
   public <T> PatternBuilder buildAttributeConstraint(PatternObject object, Predicate<? super T> predicate)
   {
      new AttributeConstraint().setPredicate(predicate).setObject(object).setPattern(this.pattern);
      return this;
   }

   /**
    * @since 1.2
    */
   public PatternBuilder buildEqualityConstraint(PatternObject object, Object value)
   {
      return this.buildAttributeConstraint(object, value == null ? Objects::isNull : value::equals);
   }

   public PatternBuilder buildMatchConstraint(Predicate<? super Map<String, Object>> predicate,
      PatternObject... objects)
   {
      //noinspection RedundantCast
      new MatchConstraint().setPredicate(predicate).withObjects((Object[]) objects).setPattern(this.pattern);
      return this;
   }

   public PatternBuilder buildInequalityConstraint(PatternObject a, PatternObject b)
   {
      return this.buildMatchConstraint(map -> {
         final Object aObj = map.get(a.getName());
         final Object bObj = map.get(b.getName());
         return !Objects.equals(aObj, bObj);
      }, a, b);
   }

   /**
    * @since 1.2
    */
   public PatternMatcher matcher()
   {
      return FulibTables.matcher(this.getPattern());
   }
}
