package org.fulib.patterns;

import org.fulib.patterns.model.*;
import org.fulib.tables.ObjectTable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows matching object structures against {@linkplain Pattern patterns} and retrieving values corresponding to
 * {@linkplain PatternObject pattern objects}.
 */
public class PatternMatcher
{
   // =============== Fields ===============

   private Pattern pattern;
   private Map<PatternObject, ObjectTable> object2TableMap;

   // =============== Constructors ===============

   /**
    * @param pattern
    *    the pattern
    */
   public PatternMatcher(Pattern pattern)
   {
      this.pattern = pattern;
   }

   // =============== Properties ===============

   /**
    * @return the map from pattern object to tables of matching values
    *
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public LinkedHashMap<PatternObject, ObjectTable> getObject2TableMap()
   {
      return new LinkedHashMap<>(this.object2TableMap);
   }

   /**
    * @param pattern
    *    the pattern object
    *
    * @return the table of matching values, or {@code null} if the pattern object was not reached
    *
    * @since 1.2
    */
   public ObjectTable getMatchTable(PatternObject pattern)
   {
      return this.object2TableMap.get(pattern);
   }

   // =============== Methods ===============

   /**
    * Matches the pattern against the structure of objects that can be discovered from the start objects,
    * and returns the table of matched values corresponding to the named pattern object.
    *
    * @param patternObjectName
    *    the name of the pattern object the results are requested for
    * @param startObjects
    *    the root objects for object structure discovery
    *
    * @return the table of matched values corresponding to the named pattern object
    *
    * @throws NoApplicableConstraintException
    *    if there were unmatched constraints, possibly due to disconnected pattern objects
    * @see #match(PatternObject, Object...)
    */
   public ObjectTable match(String patternObjectName, Object... startObjects)
   {
      return this.match(this.pattern.getObject(patternObjectName), startObjects);
   }

   /**
    * Matches the pattern against the structure of objects that can be discovered from the start objects,
    * and returns the table of matched values corresponding to the given pattern object.
    *
    * @param patternObject
    *    the pattern object the results are requested for
    * @param startObjects
    *    the root objects for object structure discovery
    *
    * @return the table of matched values corresponding to the given pattern object
    *
    * @throws NoApplicableConstraintException
    *    if there were unmatched constraints, possibly due to disconnected pattern objects
    * @since 1.2
    */
   public ObjectTable match(PatternObject patternObject, Object... startObjects)
   {
      List<RoleObject> roles = new ArrayList<>(this.pattern.getRoles());
      List<AttributeConstraint> attributeConstraints = new ArrayList<>(this.pattern.getAttributeConstraints());
      List<MatchConstraint> matchConstraints = new ArrayList<>(this.pattern.getMatchConstraints());

      ObjectTable result = new ObjectTable(patternObject.getName(), startObjects);
      this.object2TableMap = new LinkedHashMap<>();
      this.object2TableMap.put(patternObject, result);

      while (!roles.isEmpty() || !attributeConstraints.isEmpty() || !matchConstraints.isEmpty())
      {
         if (this.checkAttributeConstraint(attributeConstraints))
         {
            continue;
         }

         if (this.checkMatchConstraint(matchConstraints))
         {
            continue;
         }

         if (this.checkHasLink(roles))
         {
            continue;
         }

         if (this.expandByRole(roles))
         {
            continue;
         }

         throw new NoApplicableConstraintException();
      }

      return result;
   }

   private boolean checkAttributeConstraint(List<AttributeConstraint> attributeConstraints)
   {
      // find roles from done to not done
      for (AttributeConstraint constraint : attributeConstraints)
      {
         PatternObject src = constraint.getObject();
         ObjectTable srcTable = this.object2TableMap.get(src);

         if (srcTable == null)
         {
            continue; //=======================
         }

         srcTable.filter(constraint.getPredicate());
         attributeConstraints.remove(constraint);

         return true;
      }

      return false;
   }

   private boolean checkMatchConstraint(List<MatchConstraint> matchConstraints)
   {
      // find roles from done to not done
      constraintLoop:
      for (MatchConstraint constraint : matchConstraints)
      {
         for (PatternObject patternObject : constraint.getObjects())
         {
            ObjectTable srcTable = this.object2TableMap.get(patternObject);

            if (srcTable == null)
            {
               continue constraintLoop; //=====================
            }
         }

         // use this
         this.object2TableMap.get(constraint.getObjects().get(0)).filterRows(constraint.getPredicate());
         matchConstraints.remove(constraint);

         return true;
      }

      return false;
   }

   private boolean checkHasLink(List<RoleObject> roles)
   {
      // find roles from done to not done
      for (RoleObject role : roles)
      {
         PatternObject src = role.getObject();
         ObjectTable srcTable = this.object2TableMap.get(src);

         if (srcTable == null)
         {
            continue; //=======================
         }

         // use this
         RoleObject otherRole = role.getOther();
         PatternObject tgt = otherRole.getObject();
         ObjectTable tgtTable = this.object2TableMap.get(tgt);

         if (tgtTable == null)
         {
            continue; //=================
         }

         srcTable.hasLink(otherRole.getName(), tgtTable);
         roles.remove(role);
         roles.remove(otherRole);

         return true;
      }
      return false;
   }

   private boolean expandByRole(List<RoleObject> roles)
   {
      // find roles from done to not done
      for (RoleObject role : roles)
      {
         PatternObject src = role.getObject();
         ObjectTable srcTable = this.object2TableMap.get(src);

         if (srcTable == null)
         {
            continue; //=======================
         }

         // use this
         RoleObject otherRole = role.getOther();
         PatternObject tgt = otherRole.getObject();

         ObjectTable nextTable = srcTable.expandLink(tgt.getName(), otherRole.getName());
         this.object2TableMap.put(tgt, nextTable);
         roles.remove(role);
         roles.remove(otherRole);

         return true;
      }
      return false;
   }
}
