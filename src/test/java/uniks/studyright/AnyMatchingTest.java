package uniks.studyright;

import org.fulib.FulibTables;
import org.fulib.patterns.PatternBuilder;
import org.fulib.patterns.PatternMatcher;
import org.fulib.patterns.model.PatternObject;
import org.fulib.tables.ObjectTable;
import org.junit.Test;
import uniks.studyright.model.Student;
import uniks.studyright.model.University;

public class AnyMatchingTest
{
   @Test
   public void test()
   {
      final String packageName = University.class.getPackage().getName();

      // --- Solution ---

      Object[] roots = this.solution();

      // --- Verification ---

      final PatternBuilder builder = FulibTables.patternBuilder(packageName);

      // We expect that there is some object studyRight that has some attribute with value "StudyRight".

      final PatternObject studyRight = builder.buildPatternObject("studyRight");
      final PatternObject studyRightName = builder.buildPatternObject("studyRightName");

      builder.buildAttributeConstraint(studyRightName, "StudyRight"::equals);

      // TODO "*"
      builder.buildPatternLink(studyRight, null, "name", studyRightName);

      // We expect that there is some object alice that has some attribute with value "Alice"
      // and that has some attribute with value 10.

      final PatternObject alice = builder.buildPatternObject("alice");
      final PatternObject aliceName = builder.buildPatternObject("aliceName");
      final PatternObject aliceCredits = builder.buildPatternObject("aliceCredits");

      builder.buildAttributeConstraint(aliceName, "Alice"::equals);
      builder.buildAttributeConstraint(aliceCredits, Double.valueOf(20)::equals);

      // TODO "*"
      builder.buildPatternLink(alice, null, "name", aliceName);
      builder.buildPatternLink(alice, null, "credits", aliceCredits);

      // We expect that there is some object bob that has some attribute with value "Bob"
      // and that has some attribute with value 20.

      final PatternObject bob = builder.buildPatternObject("bob");
      final PatternObject bobName = builder.buildPatternObject("bobName");
      final PatternObject bobCredits = builder.buildPatternObject("bobCredits");

      builder.buildAttributeConstraint(bobName, "Bob"::equals);
      builder.buildAttributeConstraint(bobCredits, Double.valueOf(10)::equals);

      // TODO "*"
      builder.buildPatternLink(bob, null, "name", bobName);
      builder.buildPatternLink(bob, null, "credits", bobCredits);
      
      // We expect that studyRight has some link to alice and bob.

      // TODO "*"
      builder.buildPatternLink(studyRight, null, "students", alice);
      builder.buildPatternLink(studyRight, null, "students", bob);

      // Do match

      PatternMatcher matcher = FulibTables.matcher(builder.getPattern());

      ObjectTable start = matcher.match("studyRight", roots);

      System.out.println(start);
   }

   private Object[] solution()
   {
      University studyRight = new University();
      studyRight.setName("StudyRight");

      Student alice = new Student();
      alice.setName("Alice");
      alice.setCredits(20);

      Student bob = new Student();
      bob.setName("Bob");
      bob.setCredits(10);

      studyRight.withStudents(alice, bob);

      return new Object[] { studyRight, alice, bob };
   }
}
