package org.fulib.patterns;

import org.fulib.FulibTables;
import org.fulib.patterns.model.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides a DSL for constructing {@linkplain Pattern patterns}.
 */
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

   /**
    * @return the pattern created by this builder. Modifications are reflected both ways.
    */
   public Pattern getPattern()
   {
      return this.pattern;
   }

   // =============== Methods ===============

   /**
    * Creates a new pattern object with the given name.
    *
    * @param name
    *    the name of the pattern object
    *
    * @return the created pattern object
    */
   public PatternObject buildPatternObject(String name)
   {
      PatternObject patternObject = new PatternObject().setName(name);
      this.pattern.withObjects(patternObject);
      return patternObject;
   }

   /**
    * @since 1.3
    */
   public PatternObject buildPatternObject(String name, Class<?> type)
   {
      final PatternObject object = this.buildPatternObject(name);
      this.buildInstanceOfConstraint(object, type);
      return object;
   }

   /**
    * @since 1.3
    */
   public PatternBuilder buildPatternLink(PatternObject src, PatternObject tgt)
   {
      return this.buildPatternLink(src, "*", "*", tgt);
   }

   /**
    * Creates a new link constraint between the given pattern objects and with the expected attribute name.
    *
    * @param src
    *    the source pattern object
    * @param attrName
    *    the name of the attribute of the {@code src} object that should have the {@code tgt} value
    * @param tgt
    *    the target pattern object
    *
    * @return this instance, to allow method chaining
    *
    * @since 1.2
    */
   public PatternBuilder buildPatternLink(PatternObject src, String attrName, PatternObject tgt)
   {
      return this.buildPatternLink(src, null, attrName, tgt);
   }

   /**
    * Creates a new link constraint between the given pattern objects and with the expected attribute name.
    *
    * @param src
    *    the source pattern object
    * @param srcRoleName
    *    the name of the property of the {@code src} object that should have the {@code tgt} value
    * @param tgtRoleName
    *    the name of the property of the {@code tgt} object that should have the {@code src} value
    * @param tgt
    *    the target pattern object
    *
    * @return this instance, to allow method chaining
    */
   public PatternBuilder buildPatternLink(PatternObject src, String srcRoleName, String tgtRoleName, PatternObject tgt)
   {
      RoleObject srcRole = new RoleObject().setName(srcRoleName).setObject(src).setPattern(this.pattern);
      new RoleObject().setName(tgtRoleName).setObject(tgt).setOther(srcRole).setPattern(this.pattern);
      return this;
   }

   /**
    * Creates a new predicate/attribute constraint on the given pattern object.
    *
    * @param predicate
    *    the predicate on the value assigned to the pattern object
    * @param object
    *    the pattern object
    *
    * @return this instance, to allow method chaining
    *
    * @deprecated since 1.2; use {@link #buildAttributeConstraint(PatternObject, Predicate)} instead (typo "Att_ibute")
    */
   @Deprecated
   public PatternBuilder buildAttibuteConstraint(Predicate<? super Object> predicate, PatternObject object)
   {
      return this.buildAttributeConstraint(object, predicate);
   }

   /**
    * Creates a new predicate/attribute constraint on the given pattern object.
    *
    * @param <T>
    *    the type of objects expected to be passed to the predicate
    * @param object
    *    the pattern object
    * @param predicate
    *    the predicate on the value assigned to the pattern object
    *
    * @return this instance, to allow method chaining
    *
    * @since 1.2
    */
   public <T> PatternBuilder buildAttributeConstraint(PatternObject object, Predicate<? super T> predicate)
   {
      new AttributeConstraint()
         .setPredicate((Predicate<? super Object>) predicate)
         .setObject(object)
         .setPattern(this.pattern);
      return this;
   }

   /**
    * @since 1.3
    */
   public <T> PatternBuilder buildAttributeConstraint(PatternObject object, Class<T> type,
      Predicate<? super T> predicate)
   {
      new AttributeConstraint()
         .setPredicate(o -> type.isInstance(o) && predicate.test(type.cast(o)))
         .setObject(object)
         .setPattern(this.pattern);
      return this;
   }

   /**
    * @since 1.3
    */
   public PatternBuilder buildEqualityConstraint(PatternObject object, Object value)
   {
      return this.buildAttributeConstraint(object, new Predicate<Object>()
      {
         @Override
         public boolean test(Object o)
         {
            return Objects.equals(value, o);
         }

         @Override
         public String toString()
         {
            return "equals(" + value + ")";
         }
      });
   }

   public PatternBuilder buildCloseToConstraint(PatternObject object, Object value)
   {
      return this.buildAttributeConstraint(object, new Predicate<Object>()
      {
         @Override
         public boolean test(Object o)
         {
            return closeTo((String) value, (String) o);
         }

         @Override
         public String toString()
         {
            return "closeTo(" + value + ")";
         }
      });
   }

   private static boolean closeTo(String lhs, String rhs)
   {
      if (lhs == null)
      {
         return rhs == null;
      }

      lhs = lhs.toLowerCase();
      rhs = rhs.toLowerCase();

      int len0 = lhs.length() + 1;
      int len1 = rhs.length() + 1;

      // the array of distances
      int[] cost = new int[len0];
      int[] newcost = new int[len0];

      // initial cost of skipping prefix in String s0
      for (int i = 0; i < len0; i++)
      {
         cost[i] = i;
      }

      // dynamically computing the array of distances

      // transformation cost for each letter in s1
      for (int j = 1; j < len1; j++)
      {
         // initial cost of skipping prefix in String s1
         newcost[0] = j;

         // transformation cost for each letter in s0
         for (int i = 1; i < len0; i++)
         {
            // matching current letters in both strings
            int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

            // computing cost for each transformation
            int cost_replace = cost[i - 1] + match;
            int cost_insert = cost[i] + 1;
            int cost_delete = newcost[i - 1] + 1;

            // keep minimum cost
            newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
         }

         // swap cost/newcost arrays
         int[] swap = cost;
         cost = newcost;
         newcost = swap;
      }

      // the distance is the cost for transforming all letters in both strings
      double distance = cost[len0 - 1];
      boolean closeTo = distance / Math.max(len0, len1) <= 0.25;
      return closeTo;
   }

   /**
    * @since 1.3
    */
   public PatternBuilder buildInstanceOfConstraint(PatternObject object, Class<?> superClass)
   {
      return this.buildAttributeConstraint(object, new Predicate<Object>()
      {
         @Override
         public boolean test(Object o)
         {
            return superClass.isInstance(o);
         }

         @Override
         public String toString()
         {
            return "instanceof " + superClass.getCanonicalName();
         }
      });
   }

   /**
    * Creates a new match constraint on the given pattern object.
    *
    * @param predicate
    *    the predicate on the values assigned to the pattern objects
    * @param objects
    *    the pattern objects the constraint applies to
    *
    * @return this instance, to allow method chaining
    */
   public PatternBuilder buildMatchConstraint(Predicate<? super Map<String, Object>> predicate,
      PatternObject... objects)
   {
      //noinspection RedundantCast
      new MatchConstraint().setPredicate(predicate).withObjects((Object[]) objects).setPattern(this.pattern);
      return this;
   }

   /**
    * @since 1.3
    */
   public PatternBuilder buildDistinctConstraint(PatternObject... objects)
   {
      return this.buildMatchConstraint(new Predicate<Map<String, Object>>()
      {
         @Override
         public boolean test(Map<String, Object> map)
         {
            return Stream.of(objects).map(PatternObject::getName).map(map::get).distinct().count() == objects.length;
         }

         @Override
         public String toString()
         {
            return "distinct(" + Arrays.stream(objects).map(Object::toString).collect(Collectors.joining(", ")) + ")";
         }
      }, objects);
   }

   /**
    * @return a new matcher for the {@linkplain #getPattern() pattern} created with this builder.
    *
    * @since 1.2
    */
   public PatternMatcher matcher()
   {
      return FulibTables.matcher(this.getPattern());
   }
}
