package org.fulib.patterns;

import org.fulib.patterns.model.*;
import org.fulib.tables.ObjectTable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class PatternMatcher
{
   private Pattern pattern;
   private LinkedHashMap<PatternObject, ObjectTable> object2TableMap;

   public LinkedHashMap<PatternObject, ObjectTable> getObject2TableMap()
   {
      return object2TableMap;
   }

   public PatternMatcher(Pattern pattern)
   {
      this.pattern = pattern;
   }
   
   public ObjectTable match(String patternObjectName, Object... startObjects)
   {
      ArrayList<RoleObject> roles = (ArrayList<RoleObject>) pattern.getRoles().clone();
      ArrayList<AttributeConstraint> attributeConstraints = (ArrayList<AttributeConstraint>) pattern.getAttributeConstraints().clone();
      ArrayList<MatchConstraint> matchConstraints = (ArrayList<MatchConstraint>) pattern.getMatchConstraints().clone();

      ObjectTable result = new ObjectTable(patternObjectName, startObjects);
      PatternObject current = pattern.getObjects(patternObjectName);
      object2TableMap = new LinkedHashMap<>();
      object2TableMap.put(current, result);


      bigLoop: while (true)
      {
         if (roles.isEmpty()
               && attributeConstraints.isEmpty()
               && matchConstraints.isEmpty())
            break; //==============================

         if (checkAttributeConstraint(attributeConstraints, object2TableMap)) continue;

         if (checkMatchConstraint(matchConstraints, object2TableMap)) continue;

         if (checkHasLink(roles, object2TableMap)) continue;

         if (expandByRole(roles, object2TableMap)) continue;
      }

      return result;
   }



   private boolean checkAttributeConstraint(ArrayList<AttributeConstraint> attributeConstraints,
                                            LinkedHashMap<PatternObject, ObjectTable> object2TableMap)
   {
      // find roles from done to not done
      for (AttributeConstraint constraint : attributeConstraints)
      {
         PatternObject src = constraint.getObject();
         ObjectTable srcTable = object2TableMap.get(src);

         if (srcTable == null) continue; //=======================

         // use this
         ObjectTable nextTable = srcTable.filter(constraint.predicate);
         attributeConstraints.remove(constraint);

         return true;
      }

      return false;
   }


   private boolean checkMatchConstraint(ArrayList<MatchConstraint> matchConstraints,
                                            LinkedHashMap<PatternObject, ObjectTable> object2TableMap)
   {
      // find roles from done to not done
      constraintLoop: for (MatchConstraint constraint : matchConstraints)
      {
         for (PatternObject patternObject : constraint.getObjects())
         {
            ObjectTable srcTable = object2TableMap.get(patternObject);

            if ( srcTable == null) continue constraintLoop; //=====================
         }

         // use this
         object2TableMap.get(constraint.getObjects().get(0)).filterRow(constraint.predicate);
         matchConstraints.remove(constraint);

         return true;
      }

      return false;
   }



   private boolean checkHasLink(ArrayList<RoleObject> roles, LinkedHashMap<PatternObject, ObjectTable> object2TableMap)
   {
      // find roles from done to not done
      for (RoleObject role : roles)
      {
         PatternObject src = role.getObject();
         ObjectTable srcTable = object2TableMap.get(src);

         if (srcTable == null) continue; //=======================

         // use this
         RoleObject otherRole = role.getOther();
         PatternObject tgt = otherRole.getObject();
         ObjectTable tgtTable = object2TableMap.get(tgt);

         if (tgtTable == null) continue; //=================

         ObjectTable nextTable = srcTable.hasLink(otherRole.getName(), tgtTable);
         roles.remove(role);
         roles.remove(otherRole);

         return true;
      }
      return false;
   }



   private boolean expandByRole(ArrayList<RoleObject> roles, LinkedHashMap<PatternObject, ObjectTable> object2TableMap)
   {
      // find roles from done to not done
      for (RoleObject role : roles)
      {
         PatternObject src = role.getObject();
         ObjectTable srcTable = object2TableMap.get(src);

         if (srcTable == null) continue; //=======================

         // use this
         RoleObject otherRole = role.getOther();
         PatternObject tgt = otherRole.getObject();

         ObjectTable nextTable = srcTable.expandLink(tgt.getName(), otherRole.getName());
         object2TableMap.put(tgt, nextTable);
         roles.remove(role);
         roles.remove(otherRole);

         return true;
      }
      return false;
   }
}
