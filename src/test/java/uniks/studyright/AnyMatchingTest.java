package uniks.studyright;

import org.fulib.FulibTables;
import org.fulib.patterns.AmbiguousMatchException;
import org.fulib.patterns.PatternBuilder;
import org.fulib.patterns.PatternMatcher;
import org.fulib.patterns.model.PatternObject;
import org.fulib.tables.ObjectTable;
import org.fulib.yaml.Reflector;
import org.fulib.yaml.ReflectorMap;
import org.junit.Before;
import org.junit.Test;
import uniks.studyright.model.Room;
import uniks.studyright.model.Student;
import uniks.studyright.model.University;

import java.util.*;

import static org.junit.Assert.*;

public class AnyMatchingTest
{
   private Object[] roots;
   private Object[] all;
   private University studyRight;
   private Student alice;

   @Before
   public void scenario()
   {
      // --- end of code generated by solution scenario ---
      University studyRight = new University();
      studyRight.setName("StudyRight");

      Student alice = new Student();
      alice.setName("Alice");
      alice.setCredits(20);

      Student bob = new Student();
      bob.setName("Bob");
      bob.setCredits(10);

      studyRight.withStudents(alice, bob);

      for (int i = 0; i < 5; i++)
      {
         studyRight.withRooms(new Room().setTopic("T" + i).setRoomNo("R" + i).setCredits(i * 10));
      }
      // --- end of code generated by solution scenario ---

      this.studyRight = studyRight;
      this.alice = alice;
      // captured by fulibScenarios
      this.roots = new Object[] { studyRight, alice, bob };
      this.all = findAll(this.roots);
   }

   @Test
   public void knownObjectKnownAttribute()
   {
      // Normal case, already generated by fulibScenarios

      // We expect that studyRight has name "StudyRight".
      assertEquals("StudyRight", this.studyRight.getName());
   }

   @Test
   public void knownObjectUnknownAttribute()
   {
      // We expect that studyRight has some attribute with value "StudyRight".

      final PatternBuilder builder = FulibTables.patternBuilder();

      final PatternObject studyRight = builder.buildPatternObject("studyRight");
      final PatternObject studyRightAttr1 = builder.buildPatternObject("studyRightAttr1");

      builder.buildEqualityConstraint(studyRightAttr1, "StudyRight");
      builder.buildPatternLink(studyRight, "*", studyRightAttr1);

      final PatternMatcher matcher = FulibTables.matcher(builder.getPattern());
      matcher.withRootPatternObjects(studyRight);
      matcher.withRootObjects(this.studyRight);
      matcher.match();

      final University studyRight2 = matcher.findOne(studyRight);
      assertSame(this.studyRight, studyRight2);

      // assertSame is not a sanity check but part of the actual generated code in this case
   }

   @Test
   public void unknownObjectKnownAttribute()
   {
      // We expect that there is some object c40 with 40 credits.

      final PatternBuilder builder = FulibTables.patternBuilder();

      final PatternObject c40PO = builder.buildPatternObject("c40");
      final PatternObject c40Credits = builder.buildPatternObject("c40Credits");

      builder.buildEqualityConstraint(c40Credits, 40.0);
      builder.buildPatternLink(c40PO, "credits", c40Credits);

      final PatternMatcher matcher = FulibTables.matcher(builder.getPattern());
      matcher.withRootPatternObjects(c40PO);
      matcher.withRootObjects(this.all);
      matcher.match();

      final Object c40 = matcher.findOne(c40PO);

      // sanity check, not part of generated code:
      assertSame(this.studyRight.getRooms().get(4), c40);
   }

   @Test
   public void nonRootObjectKnownAttribute()
   {
      // We expect that there is some object r3 with roomNo R3.

      final PatternBuilder builder = FulibTables.patternBuilder();

      final PatternObject r3PO = builder.buildPatternObject("r3");
      final PatternObject r3RoomNo = builder.buildPatternObject("r3RoomNo");

      builder.buildEqualityConstraint(r3RoomNo, "R3");
      builder.buildPatternLink(r3PO, "roomNo", r3RoomNo);

      final PatternMatcher matcher = FulibTables.matcher(builder.getPattern());
      matcher.withRootPatternObjects(r3PO);
      matcher.withRootObjects(this.all);
      matcher.match();

      final Object r3 = matcher.findOne(r3PO);
   }

   @Test
   public void unknownObjectsUnknownAttributes()
   {
      final PatternBuilder builder = FulibTables.patternBuilder();

      // We expect that there is some object studyRight
      final PatternObject studyRightPO = builder.buildPatternObject("studyRight");

      // that has some attribute with value "StudyRight".
      final PatternObject studyRightAttr1 = builder.buildPatternObject("studyRightAttr1");
      builder.buildEqualityConstraint(studyRightAttr1, "StudyRight");
      builder.buildPatternLink(studyRightPO, "*", studyRightAttr1);

      // We expect that there is some object alice
      final PatternObject alicePO = builder.buildPatternObject("alice");

      // that has some attribute with value "Alice"
      final PatternObject aliceAttr1 = builder.buildPatternObject("aliceAttr1");
      builder.buildEqualityConstraint(aliceAttr1, "Alice");
      builder.buildPatternLink(alicePO, "*", aliceAttr1);

      // and that has some attribute with value 10.
      final PatternObject aliceAttr2 = builder.buildPatternObject("aliceAttr2");
      builder.buildEqualityConstraint(aliceAttr2, 20.0);
      builder.buildPatternLink(alicePO, "*", aliceAttr2);

      // We expect that there is some object bob
      final PatternObject bobPO = builder.buildPatternObject("bob");

      // that has some attribute with value "Bob"
      final PatternObject bobName = builder.buildPatternObject("bobName");
      builder.buildEqualityConstraint(bobName, "Bob");
      builder.buildPatternLink(bobPO, "*", bobName);

      // and that has some attribute with value 20.
      final PatternObject bobCredits = builder.buildPatternObject("bobCredits");
      builder.buildEqualityConstraint(bobCredits, 10.0);
      builder.buildPatternLink(bobPO, "*", bobCredits);

      // We expect that studyRight has some link to alice and bob.
      builder.buildPatternLink(studyRightPO, "*", alicePO);
      builder.buildPatternLink(studyRightPO, "*", bobPO);

      PatternMatcher matcher = FulibTables.matcher(builder.getPattern());
      matcher.withRootPatternObjects(studyRightPO);
      matcher.withRootObjects(this.all);
      matcher.match();

      final Object studyRight = matcher.findOne(studyRightPO);
      final Object alice = matcher.findOne(alicePO);
      final Object bob = matcher.findOne(bobPO);

      // sanity check:
      assertSame(this.studyRight, studyRight);
      assertSame(this.alice, alice);
   }

   @Test(expected = AmbiguousMatchException.class)
   public void ambiguousMatch()
   {
      // We expect that there is some object a20 that has some attribute with value 20.
      // (that would be Alice and R2 both with 20 credits)

      final PatternBuilder builder = FulibTables.patternBuilder();

      final PatternObject a20PO = builder.buildPatternObject("a20");
      final PatternObject a20Attr1 = builder.buildPatternObject("a20Attr1");

      builder.buildEqualityConstraint(a20Attr1, 20.0);
      builder.buildPatternLink(a20PO, "*", a20Attr1);

      final PatternMatcher matcher = FulibTables.matcher(builder.getPattern());
      matcher.withRootPatternObjects(a20PO);
      matcher.withRootObjects(this.all);
      matcher.match();
      final Object a20 = matcher.findOne(a20PO);
   }

   @Test
   public void multiMatch()
   {
      // We expect that there are some objects a20 and r20 that have some attribute with value 20.
      // (that would be Alice and R2 both with 20 credits)

      final PatternBuilder builder = FulibTables.patternBuilder();

      final PatternObject a20 = builder.buildPatternObject("a20");
      final PatternObject a20Attr1 = builder.buildPatternObject("a20Attr1");

      builder.buildEqualityConstraint(a20Attr1, 20.0);
      builder.buildPatternLink(a20, "*", a20Attr1);

      final PatternObject r20 = builder.buildPatternObject("r20");
      final PatternObject r20Attr1 = builder.buildPatternObject("r20Attr1");

      builder.buildEqualityConstraint(r20Attr1, 20.0);
      builder.buildPatternLink(r20, "*", r20Attr1);

      // because a20 and r20 are used in the same sentence, we interpret them as being two different objects:
      builder.buildInequalityConstraint(a20, r20);
      // had the sentence(s) been:
      // We expect that there is some object a20 that has some attribute with value 20.
      // We expect that there is some object r20 that has some attribute with value 20.
      // a20 would have been allowed to be the same as r20.

      final PatternMatcher matcher = FulibTables.matcher(builder.getPattern());
      matcher.withRootPatternObjects(a20);
      matcher.withRootPatternObjects(r20);
      matcher.withRootObjects(this.all);
      matcher.match();
      final ObjectTable a20Result = matcher.getMatchTable(a20);
      final ObjectTable r20Result = matcher.getMatchTable(r20);

      // there can be 2 results,
      // - a20=Alice and r20=R2
      // - a20=R2 and r20=Alice
      // both are equally valid without further specification
      assertEquals(2, a20Result.rowCount());

      // sanity check:

      assertNotSame(a20Result.iterator().next(), r20Result.iterator().next());
   }

   @Test
   public void testTypeMatch()
   {
      // We expect that there is a student s20 with some attribute with value 20.

      final PatternBuilder builder = FulibTables.patternBuilder();

      final PatternObject s20PO = builder.buildPatternObject("s20");
      builder.buildInstanceOfConstraint(s20PO, Student.class);

      final PatternObject s20Attr1 = builder.buildPatternObject("s20Attr1");

      builder.buildEqualityConstraint(s20Attr1, 20.0);
      builder.buildPatternLink(s20PO, "*", s20Attr1);

      final PatternMatcher matcher = FulibTables.matcher(builder.getPattern());
      matcher.withRootPatternObjects(s20PO);
      matcher.withRootObjects(this.all);
      matcher.match();
      final Student s20 = matcher.findOne(s20PO);

      // sanity check:
      assertEquals(this.alice, s20);
      assertNotEquals(this.studyRight.getRooms().get(2), s20);
   }

   @Test
   public void anyLink()
   {
      // We expect that there is some object alice where some attribute is 'Alice'
      // and some object studyRight with some link to s20.

      final PatternBuilder builder = FulibTables.patternBuilder();

      final PatternObject alicePO = builder.buildPatternObject("alice");
      final PatternObject aliceAttr1 = builder.buildPatternObject("aliceAttr1");
      builder.buildEqualityConstraint(aliceAttr1, "Alice");
      builder.buildPatternLink(alicePO, "*", aliceAttr1);

      final PatternObject studyRightPO = builder.buildPatternObject("studyRight");
      builder.buildPatternLink(studyRightPO, "*", alicePO);

      final PatternMatcher matcher = FulibTables.matcher(builder.getPattern());
      matcher.withRootPatternObjects(alicePO);
      matcher.withRootPatternObjects(studyRightPO);
      matcher.withRootObjects(this.all);
      matcher.match();
      final Object alice = matcher.findOne(alicePO);
      final Object studyRight = matcher.findOne(studyRightPO);

      // sanity check:
      assertEquals(this.alice, alice);
      assertEquals(this.studyRight, studyRight);
   }

   // --------------- Helper Methods ---------------

   private static Object[] findAll(Object... roots)
   {
      // TODO consider other package names
      final ReflectorMap reflectorMap = new ReflectorMap(roots[0].getClass().getPackage().getName());
      final Set<Object> out = new HashSet<>();
      for (final Object root : roots)
      {
         findNeighbors(reflectorMap, root, out);
      }
      return out.toArray();
   }

   // TODO maybe this would be a good addition to Reflector, e.g. getTransitiveNeighbors()?
   private static void findNeighbors(ReflectorMap map, Object root, Set<Object> out)
   {
      if (root == null || !map.canReflect(root))
      {
         return;
      }

      final Reflector reflector = map.getReflector(root);

      // doing this after the reflector prevents values from being added to the set
      if (!out.add(root))
      {
         return;
      }

      // TODO maybe this would be a good addition to Reflector, e.g. getNeighbors()?
      for (final String property : reflector.getAllProperties())
      {
         final Object value = reflector.getValue(root, property);
         if (value instanceof Collection)
         {
            for (Object item : (Collection<?>) value)
            {
               findNeighbors(map, item, out);
            }
         }
         else
         {
            findNeighbors(map, value, out);
         }
      }
   }
}
