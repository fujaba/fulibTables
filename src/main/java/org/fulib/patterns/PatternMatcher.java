package org.fulib.patterns;

import org.fulib.patterns.debug.*;
import org.fulib.patterns.model.*;
import org.fulib.tables.ObjectTable;

import java.util.*;

public class PatternMatcher
{
   // =============== Fields ===============

   private Pattern pattern;
   private Map<PatternObject, ObjectTable> object2TableMap;

   private List<PatternObject> rootPatternObjects = new ArrayList<>();
   private List<Object> rootObjects = new ArrayList<>();

   private List<DebugEvent> events;

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
      return new LinkedHashMap<>(this.object2TableMap);
   }

   /**
    * @since 1.2
    */
   public ObjectTable getMatchTable(PatternObject pattern)
   {
      return this.object2TableMap.get(pattern);
   }

   /**
    * @since 1.2
    */
   public boolean isDebugLogging()
   {
      return this.events != null;
   }

   /**
    * @since 1.2
    */
   public void setDebugLogging(boolean eventLogging)
   {
      if (!eventLogging)
      {
         this.events = null;
         return;
      }
      if (this.events == null)
      {
         this.events = new ArrayList<>();
      }
   }

   /**
    * @since 1.2
    */
   public List<DebugEvent> getDebugEvents()
   {
      return this.events == null ? Collections.emptyList() : Collections.unmodifiableList(this.events);
   }

   /**
    * @since 1.2
    */
   public List<PatternObject> getRootPatternObjects()
   {
      return this.rootPatternObjects;
   }

   /**
    * @since 1.2
    */
   public PatternMatcher withRootPatternObjects(PatternObject patternObject)
   {
      this.rootPatternObjects.add(patternObject);
      return this;
   }

   /**
    * @since 1.2
    */
   public List<Object> getRootObjects()
   {
      return this.rootObjects;
   }

   /**
    * @since 1.2
    */
   public PatternMatcher withRootObjects(Object... objects)
   {
      Collections.addAll(this.rootObjects, objects);
      return this;
   }

   // =============== Methods ===============

   public ObjectTable match(String patternObjectName, Object... startObjects)
   {
      return this.match(this.pattern.getObject(patternObjectName), startObjects);
   }

   /**
    * @since 1.2
    */
   public ObjectTable match(PatternObject patternObject, Object... startObjects)
   {
      this.withRootPatternObjects(patternObject);
      this.withRootObjects(startObjects);

      this.match();

      return this.getMatchTable(patternObject);
   }

   /**
    * @since 1.2
    */
   public void match()
   {
      this.object2TableMap = new LinkedHashMap<>();

      Deque<PatternObject> rootPatternObjects = new ArrayDeque<>(this.rootPatternObjects);
      List<RoleObject> roles = new ArrayList<>(this.pattern.getRoles());
      List<AttributeConstraint> attributeConstraints = new ArrayList<>(this.pattern.getAttributeConstraints());
      List<MatchConstraint> matchConstraints = new ArrayList<>(this.pattern.getMatchConstraints());

      final PatternObject firstPatternObject = rootPatternObjects.removeFirst();
      // TODO ObjectTable constructor that takes a Collection
      final ObjectTable firstTable = new ObjectTable(firstPatternObject.getName(), this.rootObjects.toArray());
      this.object2TableMap.put(firstPatternObject, firstTable);

      if (this.events != null)
      {
         this.events.add(new MultiplyRootsEvent(firstPatternObject, firstTable));
      }

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

         if (this.multiplyRoots(rootPatternObjects, firstTable))
         {
            continue;
         }

         throw new NoApplicableConstraintException();
      }
   }

   /**
    * @since 1.2
    */
   public <T> T findOne(PatternObject patternObject)
   {
      final ObjectTable table = this.getMatchTable(patternObject);
      final int count = table.rowCount();
      if (count == 1)
      {
         return (T) table.iterator().next();
      }
      if (count == 0)
      {
         throw new NoMatchException(patternObject);
      }
      throw new AmbiguousMatchException(patternObject, table.toList());
   }

   /**
    * @since 1.2
    */
   public <T> List<T> findAll(PatternObject patternObject)
   {
      final ObjectTable table = this.getMatchTable(patternObject);
      return (List<T>) table.toList();
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

         if (this.events != null)
         {
            this.events.add(new AttributeConstraintEvent(constraint, srcTable));
         }

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

         final ObjectTable table = this.object2TableMap.get(constraint.getObjects().get(0));
         table.filterRow(constraint.predicate);
         matchConstraints.remove(constraint);

         if (this.events != null)
         {
            this.events.add(new MatchConstraintEvent(constraint, table));
         }

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

         final String linkName = otherRole.getName();
         if ("*".equals(linkName))
         {
            srcTable.hasAnyLink(tgtTable);
         }
         else
         {
            srcTable.hasLink(linkName, tgtTable);
         }
         roles.remove(role);
         roles.remove(otherRole);

         if (this.events != null)
         {
            this.events.add(new HasLinkEvent(role, srcTable));
         }

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

         if (this.events != null)
         {
            this.events.add(new ExpandRoleEvent(role, srcTable));
         }

         return true;
      }
      return false;
   }

   private boolean multiplyRoots(Deque<PatternObject> rootPatternObjects, ObjectTable firstTable)
   {
      if (rootPatternObjects.isEmpty())
      {
         return false;
      }

      final PatternObject nextRoot = rootPatternObjects.removeFirst();
      final ObjectTable nextTable = firstTable.multiply(nextRoot.getName(), this.rootObjects);
      this.object2TableMap.put(nextRoot, nextTable);

      if (this.events != null)
      {
         this.events.add(new MultiplyRootsEvent(nextRoot, nextTable));
      }

      return true;
   }
}
