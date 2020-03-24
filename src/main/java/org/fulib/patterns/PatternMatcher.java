package org.fulib.patterns;

import org.fulib.patterns.debug.*;
import org.fulib.patterns.model.*;
import org.fulib.tables.ObjectTable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Allows matching object structures against {@linkplain Pattern patterns} and retrieving values corresponding to
 * {@linkplain PatternObject pattern objects}.
 */
public class PatternMatcher
{
   // =============== Fields ===============

   private Pattern pattern;
   private Map<PatternObject, ObjectTable> object2TableMap;

   private List<PatternObject> rootPatternObjects = new ArrayList<>();
   private List<Object> rootObjects = new ArrayList<>();

   private List<DebugEvent> events;

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

   // --------------- Deprecated ---------------

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
    * @deprecated since 1.3; for internal use only; use {@link #findAll(PatternObject)} instead
    */
   @Deprecated
   public ObjectTable getMatchTable(PatternObject pattern)
   {
      return this.object2TableMap.get(pattern);
   }

   // --------------- Debugging ---------------

   /**
    * @return {@code true} if debug logging is enabled
    *
    * @since 1.3
    */
   public boolean isDebugLogging()
   {
      return this.events != null;
   }

   /**
    * @param eventLogging
    *    determines whether debug logging is on ({@code true}) or off ({@code false})
    *
    * @since 1.3
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
    * @return the list of debug events that happened during the match process.
    * Empty if debug logging is turned off, or when {@link #match()} or its variants have not been called yet.
    *
    * @since 1.3
    */
   public List<DebugEvent> getDebugEvents()
   {
      return this.events == null ? Collections.emptyList() : Collections.unmodifiableList(this.events);
   }

   // --------------- Roots ---------------

   /**
    * @return the root pattern objects
    *
    * @since 1.3
    */
   public List<PatternObject> getRootPatternObjects()
   {
      return this.rootPatternObjects;
   }

   /**
    * @param patternObject
    *    the root pattern object to add
    *
    * @return this instance, to allow method chaining
    *
    * @since 1.3
    */
   public PatternMatcher withRootPatternObjects(PatternObject patternObject)
   {
      this.rootPatternObjects.add(patternObject);
      return this;
   }

   /**
    * @param patternObjects
    *    the root pattern objects to add
    *
    * @return this instance, to allow method chaining
    *
    * @since 1.3
    */
   public PatternMatcher withRootPatternObjects(PatternObject... patternObjects)
   {
      Collections.addAll(this.rootPatternObjects, patternObjects);
      return this;
   }

   /**
    * @param patternObjects
    *    the root pattern objects to add
    *
    * @return this instance, to allow method chaining
    *
    * @since 1.3
    */
   public PatternMatcher withRootPatternObjects(Collection<? extends PatternObject> patternObjects)
   {
      this.rootPatternObjects.addAll(patternObjects);
      return this;
   }

   /**
    * @return the root objects
    *
    * @since 1.3
    */
   public List<Object> getRootObjects()
   {
      return this.rootObjects;
   }

   /**
    * @param object
    *    the root object to add
    *
    * @return this instance, to allow method chaining
    *
    * @since 1.3
    */
   public PatternMatcher withRootObjects(Object object)
   {
      this.rootObjects.add(object);
      return this;
   }

   /**
    * @param objects
    *    the root objects to add
    *
    * @return this instance, to allow method chaining
    *
    * @since 1.3
    */
   public PatternMatcher withRootObjects(Object... objects)
   {
      Collections.addAll(this.rootObjects, objects);
      return this;
   }

   /**
    * @param objects
    *    the root objects to add
    *
    * @return this instance, to allow method chaining
    *
    * @since 1.3
    */
   public PatternMatcher withRootObjects(Collection<?> objects)
   {
      this.rootObjects.addAll(objects);
      return this;
   }

   // =============== Methods ===============

   // --------------- Matching Start ---------------

   /**
    * Matches the pattern against the structure of objects that can be discovered from the root objects,
    * and returns the table of matched values corresponding to the named pattern object.
    * <p>
    * Equivalent to:
    * <pre>{@code
    *    this.match(pattern.getObject(rootPatternObjectName), rootObjects);
    * }</pre>
    *
    * @param rootPatternObjectName
    *    the name of the root pattern object, and for which the results are requested for
    * @param rootObjects
    *    the root objects for object structure discovery
    *
    * @return the table of matched values corresponding to the named pattern object
    *
    * @throws NoApplicableConstraintException
    *    if there were unmatched constraints, possibly due to disconnected pattern objects
    * @see #match(PatternObject, Object...)
    */
   public ObjectTable match(String rootPatternObjectName, Object... rootObjects)
   {
      return this.match(this.pattern.getObject(rootPatternObjectName), rootObjects);
   }

   /**
    * Matches the pattern against the structure of objects that can be discovered from the root objects,
    * and returns the table of matched values corresponding to the given pattern object.
    * <p>
    * Equivalent to:
    * <pre>{@code
    *    this.withRootPatternObjects(rootPatternObject);
    *    this.withRootObjects(rootObjects);
    *    this.match();
    *    return this.getMatchTable(rootPatternObject);
    * }</pre>
    *
    * @param rootPatternObject
    *    the root pattern object, and for which the results are requested for
    * @param rootObjects
    *    the root objects for object structure discovery
    *
    * @return the table of matched values corresponding to the given pattern object
    *
    * @throws NoApplicableConstraintException
    *    if there were unmatched constraints, possibly due to disconnected pattern objects
    * @since 1.2
    */
   public ObjectTable match(PatternObject rootPatternObject, Object... rootObjects)
   {
      this.withRootPatternObjects(rootPatternObject);
      this.withRootObjects(rootObjects);

      this.match();

      return this.getMatchTable(rootPatternObject);
   }

   /**
    * Matches the pattern against the structure of objects that can be discovered from the root objects
    *
    * @throws NoApplicableConstraintException
    *    if there were unmatched constraints, possibly due to disconnected pattern objects
    * @since 1.3
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

         throw new NoApplicableConstraintException("the following constraints could not be applied:\n" + Stream
            .of(attributeConstraints, roles, matchConstraints)
            .flatMap(List::stream)
            .map(Object::toString)
            .collect(Collectors.joining("\n")));
      }
   }

   // --------------- Result Extraction ---------------

   /**
    * Attempts to retrieve exactly one unique match for the given pattern object.
    * Must be called after {@link #match()} or its variants.
    *
    * @param <T>
    *    the expected type of the result
    * @param patternObject
    *    the pattern object results are requested for
    *
    * @return the match for the given pattern object
    *
    * @throws NoMatchException
    *    if no objects match
    * @throws AmbiguousMatchException
    *    if more than one distinct objects match
    * @since 1.3
    */
   public <T> T findOne(PatternObject patternObject)
   {
      final ObjectTable table = this.getMatchTable(patternObject);
      final int count = table.rowCount();
      if (count == 0)
      {
         throw new NoMatchException(patternObject);
      }
      if (count == 1)
      {
         return (T) table.iterator().next();
      }
      // there may be the same object multiple times in the column
      final Set<Object> set = table.toSet();
      if (set.size() == 1)
      {
         return (T) set.iterator().next();
      }
      throw new AmbiguousMatchException(patternObject, set);
   }

   /**
    * Retrieves the matches for the given pattern object.
    * Must be called after {@link #match()} or its variants.
    *
    * @param <T>
    *    the expected type of the results
    * @param patternObject
    *    the pattern object results are requested for
    *
    * @return the matches for the given pattern object. May be empty.
    *
    * @since 1.3
    */
   public <T> Set<T> findAll(PatternObject patternObject)
   {
      final ObjectTable table = this.getMatchTable(patternObject);
      return (Set<T>) table.toSet();
   }

   // --------------- Implementation ---------------

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
      for (MatchConstraint constraint : matchConstraints)
      {
         if (!this.areAllPatternObjectsAvailable(constraint))
         {
            continue;
         }

         final ObjectTable table = this.object2TableMap.get(constraint.getObjects().get(0));
         table.filterRows(constraint.getPredicate());
         matchConstraints.remove(constraint);

         if (this.events != null)
         {
            this.events.add(new MatchConstraintEvent(constraint, table));
         }

         return true;
      }

      return false;
   }

   private boolean areAllPatternObjectsAvailable(MatchConstraint constraint)
   {
      return constraint.getObjects().stream().allMatch(this.object2TableMap::containsKey);
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

         RoleObject otherRole = role.getOther();
         final String linkName = otherRole.getName();
         if (linkName == null)
         {
            continue;
         }

         // use this
         PatternObject tgt = otherRole.getObject();

         final ObjectTable nextTable;
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
      final ObjectTable nextTable = firstTable.expandAll(nextRoot.getName(), x -> this.rootObjects);
      this.object2TableMap.put(nextRoot, nextTable);

      if (this.events != null)
      {
         this.events.add(new MultiplyRootsEvent(nextRoot, nextTable));
      }

      return true;
   }
}
