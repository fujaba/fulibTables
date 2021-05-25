package uniks.studyright;

import org.fulib.FulibTools;
import org.fulib.tables.ObjectTable;
import org.fulib.tables.StringTable;
import org.fulib.tables.Table;
import org.fulib.tables.doubleTable;
import org.fulib.tools.CodeFragments;
import org.hamcrest.MatcherAssert;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uniks.studyright.model.Assignment;
import uniks.studyright.model.Room;
import uniks.studyright.model.Student;
import uniks.studyright.model.University;

import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StudyRightTests
{
   private static CodeFragments fragments;

   private University studyRight;
   private Student alice;
   private Student bob;
   private Assignment integrals;

   @BeforeClass
   public static void setupClass()
   {
      fragments = FulibTools.codeFragments();
   }

   @Before
   @SuppressWarnings("unused")
   public void setup()
   {
      // @formatter:off
      // start_code_fragment: FulibTables.objectModel
      // build object structure
      University studyRight = new University().setName("Study Right");

      Room mathRoom = new Room().setRoomNo("wa1337").setTopic("Math").setCredits(42.0).setUni(studyRight);
      Room artsRoom = new Room().setRoomNo("wa1338").setTopic("Arts").setCredits(23.0).setUni(studyRight);
      Room sportsRoom = new Room().setRoomNo("wa1339").setTopic("Football").setUni(studyRight);

      Assignment integrals = new Assignment().setTask("integrals").setPoints(42).setRoom(mathRoom);
      Assignment matrix = new Assignment().setTask("matrices").setPoints(23).setRoom(mathRoom);
      Assignment drawings = new Assignment().setTask("drawings").setPoints(12).setRoom(artsRoom);
      Assignment sculptures = new Assignment().setTask("sculptures").setPoints(12).setRoom(artsRoom);

      Student alice = new Student().setStudentId("m4242").setName("Alice").setUni(studyRight).setIn(mathRoom).withDone(integrals);
      Student bob   = new Student().setStudentId("m2323").setName("Bobby").setUni(studyRight).setIn(artsRoom).withFriends(alice);
      Student carli = new Student().setStudentId("m2323").setName("Carli").setUni(studyRight).setIn(mathRoom);
      // end_code_fragment:
      // @formatter:on

      this.studyRight = studyRight;
      this.alice = alice;
      this.bob = bob;
      this.integrals = integrals;
   }

   @Test
   public void dumpObjectDiagram()
   {
      FulibTools.objectDiagrams().dumpSVG("docs/images/studyRightObjects.svg", studyRight);
   }

   @Test
   public void objectTables()
   {
      // start_code_fragment: objectTables.universityTable
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      // end_code_fragment:

      fragments.addFragment("objectTables.universityTableResult", universityTable.toString());
   }

   @Test
   public void expandingLinks()
   {
      // start_code_fragment: expandingLinks.universityTable
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      // end_code_fragment:

      fragments.addFragment("expandingLinks.universityTableResult", universityTable.toString());

      // start_code_fragment: expandingLinks.roomsTable
      ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
      // end_code_fragment:

      fragments.addFragment("expandingLinks.roomsTableResult", roomsTable.toString());

      // start_code_fragment: expandingLinks.assignmentsTable
      ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
      // end_code_fragment:

      fragments.addFragment("expandingLinks.assignmentsTableResult", assignmentsTable.toString());
   }

   @Test
   public void iterableTables()
   {
      // start_code_fragment: iterableTables.tables
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
      ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
      // end_code_fragment:

      // start_code_fragment: iterableTables.loop
      double sum = 0;
      for (Assignment a : assignmentsTable)
      {
         sum += a.getPoints();
      }
      // end_code_fragment:

      assertThat(sum, equalTo(89.0));
   }

   @Test
   public void expandingAttributes()
   {
      // start_code_fragment: expandingAttributes.tables
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
      ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
      // end_code_fragment:

      // start_code_fragment: expandingAttributes.pointsTable
      doubleTable pointsTable = assignmentsTable.expand("Points", Assignment::getPoints).as(doubleTable.class);
      // end_code_fragment:

      fragments.addFragment("expandingAttributes.pointsTableResult", pointsTable.toString());

      // start_code_fragment: expandingAttributes.pointsSum
      double sum = pointsTable.sum();
      // end_code_fragment:

      assertThat(roomsTable.rowCount(), equalTo(4));
      assertThat(assignmentsTable.rowCount(), equalTo(4));
      assertThat(sum, equalTo(89.0));
   }

   @Test
   public void filters()
   {
      // start_code_fragment: filters.tables
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
      ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
      ObjectTable<Student> studentsTable = roomsTable.expandAll("Student", Room::getStudents);
      // end_code_fragment:

      assertThat(studentsTable.rowCount(), equalTo(6));

      fragments.addFragment("filters.tablesResult", universityTable.toString());

      // start_code_fragment: filters.filter
      assignmentsTable.filter(a -> a.getPoints() <= 30);
      // end_code_fragment:

      assertThat(studentsTable.rowCount(), equalTo(4));
      fragments.addFragment("filters.filterResult", universityTable.toString());
   }

   @Test
   public void filterAs()
   {
      // start_code_fragment: filterAs.tables
      Table<Object> objectsTable = new Table<>("Objects", studyRight, alice, bob, integrals);
      // end_code_fragment:

      fragments.addFragment("filterAs.tablesResult", objectsTable.toString());

      // start_code_fragment: filterAs.filterAs
      final Table<Student> studentsTable = objectsTable.filterAs(Student.class);
      // end_code_fragment:

      assertThat(studentsTable.rowCount(), equalTo(2));
      fragments.addFragment("filterAs.filterAsResult", studentsTable.toString());

      @SuppressWarnings( { "unused", "rawtypes" })
      // start_code_fragment: filterAs.alternative
      final Table<Student> studentsTable2 = (Table<Student>) (Table) objectsTable.filter(s -> s instanceof Student);
      // end_code_fragment:
   }

   @Test
   public void filterRows()
   {
      final University studyRight = this.studyRight;

      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
      ObjectTable<Student> students = roomsTable.expandAll("Student", Room::getStudents);
      roomsTable.expandAll("Assignment", Room::getAssignments);

      // start_code_fragment: filterRows.filterRows
      students.filterRows(row -> {
         Student studi = (Student) row.get("Student");
         Assignment assignment = (Assignment) row.get("Assignment");
         return studi.getDone().contains(assignment);
      });
      // end_code_fragment:

      assertThat(students.rowCount(), equalTo(1));

      fragments.addFragment("filterRows.filterRowsResult", universityTable.toString());
   }

   @Test
   public void hasLink()
   {
      final University studyRight = this.studyRight;

      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
      ObjectTable<Student> students = roomsTable.expandAll("Student", Room::getStudents);
      ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);

      // start_code_fragment: hasLink.hasLink
      students.hasLink(Student.PROPERTY_done, assignmentsTable);
      // end_code_fragment:

      assertThat(students.rowCount(), equalTo(1));

      fragments.addFragment("hasLink.hasLinkResult", universityTable.toString());
   }

   @Test
   public void selectingColumns()
   {
      final University studyRight = this.studyRight;

      // start_code_fragment: selectingColumns.tables
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
      ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
      ObjectTable<Student> studentsTable = roomsTable.expandAll("Student", Room::getStudents);
      // end_code_fragment:

      fragments.addFragment("selectingColumns.tablesResult", studentsTable.toString());

      // start_code_fragment: selectingColumns.dropAssignment
      universityTable.dropColumns("Assignment");
      // end_code_fragment:

      fragments.addFragment("selectingColumns.dropAssignmentResult", universityTable.toString());

      // start_code_fragment: selectingColumns.selectStudentAndRoom
      studentsTable.selectColumns("Student", "Room");
      // end_code_fragment:

      assertThat(studentsTable.rowCount(), equalTo(3));

      fragments.addFragment("selectingColumns.selectStudentAndRoomResult", universityTable.toString());
   }

   @Test
   public void nestedTables()
   {
      final University studyRight = this.studyRight;

      // do current assignments
      for (final Student student : this.studyRight.getStudents())
      {
         student.withDone(student.getIn().getAssignments());
      }

      // start_code_fragment: nestedTables.nestedTables
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      ObjectTable<Student> students = universityTable.expandAll("Students", University::getStudents);
      students.derive("Credits", row -> {
         Student student = (Student) row.get("Students");
         double pointSum = new ObjectTable<>(student)
            .expandAll("Assignments", Student::getDone)
            .expand("Points", Assignment::getPoints)
            .as(doubleTable.class)
            .sum();
         student.setCredits(pointSum);
         return pointSum;
      });
      students.derive("Done", row -> {
         Student student = (Student) row.get("Students");
         return new ObjectTable<>("Students", student)
            .expandAll("Assignments", Student::getDone)
            .expand("Tasks", Assignment::getTask)
            .as(StringTable.class)
            .join(", ");
      });
      // end_code_fragment:

      fragments.addFragment("nestedTables.nestedTablesResult", universityTable.toString());
   }

   @Test
   public void testModelQueriesWithSource()
   {
      final University studyRight = this.studyRight;

      // start_code_fragment: FulibTables.createUniPathTable1
      // some table stuff
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      universityTable.expandAll("University", "Room", University::getRooms);
      universityTable.expandAll("Room", "Assignment", Room::getAssignments);
      // end_code_fragment:

      universityTable = new ObjectTable<>("University", studyRight);
      fragments.addFragment("FulibTables.uniPathTable1", universityTable.toString());

      universityTable.expandAll("University", "Room", University::getRooms);
      fragments.addFragment("FulibTables.uniPathTable2", universityTable.toString());

      universityTable.expandAll("Room", "Assignment", Room::getAssignments);
      fragments.addFragment("FulibTables.uniPathTable3", universityTable.toString());

      // start_code_fragment: FulibTables.loopThroughAssignmentsPath
      double sum = 0;
      for (Assignment a : universityTable.<Assignment>toSet("Assignment"))
      {
         sum += a.getPoints();
      }
      MatcherAssert.assertThat(sum, is(89.0));
      // end_code_fragment:

      // start_code_fragment: FulibTables.pointsPathTable
      universityTable.expandLink("Assignment", "Points", Assignment.PROPERTY_points);
      sum = universityTable.<Double>stream("Points").mapToDouble(Double::doubleValue).sum();
      assertThat(universityTable.rowCount(), equalTo(4));
      MatcherAssert.assertThat(sum, equalTo(89.0));
      // end_code_fragment:

      fragments.addFragment("FulibTables.pointsPathTableResult", universityTable.toString());

      // start_code_fragment: FulibTables.studentsPathTable
      universityTable.expandAll("Room", "Student", Room::getStudents);
      assertThat(universityTable.rowCount(), equalTo(6));
      // end_code_fragment:

      fragments.addFragment("FulibTables.studentsPathTableResult", universityTable.toString());

      // start_code_fragment: FulibTables.filterAssignmentsPathTable
      universityTable.filter("Assignment", (Assignment a) -> a.getPoints() <= 30);
      assertThat(universityTable.rowCount(), equalTo(4));
      // end_code_fragment:

      fragments.addFragment("FulibTables.filterAssignmentsPathTableResult", universityTable.toString());
   }

   @Test
   public void filterRowsWithSource()
   {
      final University studyRight = this.studyRight;

      ObjectTable<University> universityTable;

      // start_code_fragment: FulibTables.filterRowPathTable
      // filter row
      universityTable = new ObjectTable<>("University", studyRight);
      universityTable.expandLink("University", "Room", University.PROPERTY_rooms);
      universityTable.expandLink("Room", "Student", Room.PROPERTY_students);
      universityTable.expandLink("Room", "Assignment", Room.PROPERTY_assignments);

      universityTable.filterRows(row -> {
         Student studi = (Student) row.get("Student");
         Assignment assignment = (Assignment) row.get("Assignment");
         return studi.getDone().contains(assignment);
      });

      assertThat(universityTable.rowCount(), equalTo(1));
      // end_code_fragment:

      fragments.addFragment("FulibTables.filterRowPathTableResult", universityTable.toString());
   }

   @Test
   public void hasLinkWithColumn()
   {
      final University studyRight = this.studyRight;

      // start_code_fragment: FulibTables.filterPathTableHasDone
      // filter row
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      universityTable.expandLink("University", "Room", University.PROPERTY_rooms);
      universityTable.expandLink("Room", "Student", Room.PROPERTY_students);
      universityTable.expandLink("Room", "Assignment", Room.PROPERTY_assignments);
      universityTable.hasLink("Student", "Assignment", Student.PROPERTY_done);

      assertThat(universityTable.rowCount(), equalTo(1));
      // end_code_fragment:

      fragments.addFragment("FulibTables.filterPathTableHasDoneResult", universityTable.toString());
   }

   @Test
   public void doAssignmentsAndDropSelectWithColumn()
   {
      final University studyRight = this.studyRight;
      final Student alice = this.alice;
      final Assignment integrals = this.integrals;

      // start_code_fragment: FulibTables.pathTableDoCurrentAssignments
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      universityTable.expandLink("University", "Room", University.PROPERTY_rooms);
      universityTable.expandLink("Room", "Student", Room.PROPERTY_students);
      universityTable.expandLink("Room", "Assignment", Room.PROPERTY_assignments);

      // do current assignments
      universityTable.filterRows(row -> {
         Student studi = (Student) row.get("Student");
         Assignment assignment = (Assignment) row.get("Assignment");
         studi.withDone(assignment);
         return true;
      });

      MatcherAssert.assertThat(alice.getDone().size(), is(2));
      MatcherAssert.assertThat(integrals.getStudents().contains(alice), is(true));

      // show size of done
      universityTable.derive("noOfDone", row -> {
         Student studi = (Student) row.get("Student");
         return studi.getDone().size();
      });

      // show done
      universityTable.expandLink("Student", "Done", Student.PROPERTY_done);
      // end_code_fragment:

      fragments.addFragment("FulibTables.pathTableDoCurrentAssignmentsResult", universityTable.toString());
   }

   @Test
   public void nestedTablesWithColumn()
   {
      final University studyRight = this.studyRight;

      // do current assignments
      for (final Student student : this.studyRight.getStudents())
      {
         student.withDone(student.getIn().getAssignments());
      }

      // start_code_fragment: FulibTables.nestedPathTables
      ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
      universityTable.expandLink("University", "Students", University.PROPERTY_students);
      universityTable.derive("Credits", row -> {
         Student student = (Student) row.get("Students");
         double pointSum = new ObjectTable<>("Student", student)
            .expandLink("Student", "Assignments", Student.PROPERTY_done)
            .expandLink("Assignments", "Points", Assignment.PROPERTY_points)
            .<Double>stream("Points")
            .mapToDouble(Double::doubleValue)
            .sum();
         student.setCredits(pointSum);
         return pointSum;
      });
      universityTable.derive("Done", row -> {
         Student student = (Student) row.get("Students");
         return new ObjectTable<>("Student", student)
            .expandLink("Student", "Assignments", Student.PROPERTY_done)
            .expandLink("Assignments", "Tasks", Assignment.PROPERTY_task)
            .<String>stream("Tasks")
            .collect(Collectors.joining(", "));
      });
      // end_code_fragment:

      fragments.addFragment("FulibTables.nestedPathTablesResult", universityTable.toString());
   }

   @AfterClass
   public static void teardownClass()
   {
      fragments.update("docs/", "src/test/java/uniks/studyright/StudyRightTests.java",
                       "src/testGen/java/uniks/studyright/model/GenModel.java");
   }
}
