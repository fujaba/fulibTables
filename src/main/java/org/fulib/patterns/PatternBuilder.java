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
      new AttributeConstraint().setPredicate((Predicate<? super Object>) predicate)
                               .setObject(object)
                               .setPattern(this.pattern);
      return this;
   }

   /**
    * @since 1.3
    */
   public <T> PatternBuilder buildAttributeConstraint(PatternObject object, Class<T> type, Predicate<? super T> predicate)
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
   public PatternBuilder buildInequalityConstraint(PatternObject... objects)
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
            return "notEqual(" + Arrays.stream(objects).map(Object::toString).collect(Collectors.joining(", ")) + ")";
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
