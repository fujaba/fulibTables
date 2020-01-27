package org.fulib.patterns;

import org.fulib.patterns.model.*;
import org.fulib.tables.ObjectTable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PatternMatcher
{
   // =============== Fields ===============

   private Pattern pattern;
   private LinkedHashMap<PatternObject, ObjectTable> object2TableMap;

   // =============== Constructors ===============

   public PatternMatcher(Pattern pattern)
   {
      this.pattern = pattern;
   }

   // =============== Properties ===============

   /**
    * @deprecated since 1.2; for internal use only
    */
   @Deprecated
   public LinkedHashMap<PatternObject, ObjectTable> getObject2TableMap()
   {
      return this.object2TableMap;
   }

   // =============== Methods ===============

   public ObjectTable match(String patternObjectName, Object... startObjects)
   {
      List<RoleObject> roles = new ArrayList<>(this.pattern.getRoles());
      List<AttributeConstraint> attributeConstraints = new ArrayList<>(this.pattern.getAttributeConstraints());
      List<MatchConstraint> matchConstraints = new ArrayList<>(this.pattern.getMatchConstraints());

      ObjectTable result = new ObjectTable(patternObjectName, startObjects);
      PatternObject current = this.pattern.getObjects(patternObjectName);
      this.object2TableMap = new LinkedHashMap<>();
      this.object2TableMap.put(current, result);

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

         srcTable.filter(constraint.predicate);
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
         this.object2TableMap.get(constraint.getObjects().get(0)).filterRow(constraint.predicate);
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

         final String linkName = otherRole.getName();

         ObjectTable nextTable;
         if ("*".equals(linkName))
         {
            nextTable = srcTable.expandAll(tgt.getName());
         }
         else
         {
            nextTable = srcTable.expandLink(tgt.getName(), linkName);
         }

         this.object2TableMap.put(tgt, nextTable);
         roles.remove(role);
         roles.remove(otherRole);

         return true;
      }
      return false;
   }
}
