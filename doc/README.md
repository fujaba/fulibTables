# fulibTables

Welcome to the fulibTables documentation.
In the following pages, we will explain how to create and work with fulib tables.
All pages share a common class model which we define using fulib:

<!-- insert_code_fragment: studyRight.GenModel | fenced:java -->
```java
class University
{
   String name;

   @Link("uni")
   List<Student> students;

   @Link("uni")
   List<Room> rooms;
}

class Student
{
   String name;
   String studentId;
   double credits;
   double points;
   double motivation;

   @Link("students")
   University uni;

   @Link("students")
   Room in;

   @Link("students")
   List<Assignment> done;

   @Link("friends")
   List<Student> friends;
}

class Room
{
   String roomNo;
   String topic;
   double credits;

   @Link("rooms")
   University uni;

   @Link("in")
   List<Student> students;

   @Link("room")
   List<Assignment> assignments;
}

class Assignment
{
   String task;
   double points;

   @Link("assignments")
   Room room;

   @Link("done")
   List<Student> students;
}
```
<!-- end_code_fragment: -->

Rendered as a class diagram the class model looks like:

![simple class diagram](../src/test/java/uniks/studyright/model/classDiagram.svg)

Once the generated code is compiled, we may construct some objects:

<!-- insert_code_fragment: FulibTables.objectModel | fenced:java -->
```java
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
```
<!-- end_code_fragment: -->

This results in:

![object diagram](images/studyRightObjects.svg)

For reference, this object diagram will be repeated on each page.

1. [Object Tables](1-object-tables.md)
2. [Expanding Links](2-expanding-links.md)
3. [Iterable Tables](3-iterable-tables.md)
4. [Expanding Attributes](4-expanding-attributes.md)
5. [Filters](5-filters.md)
6. [Selecting Columns](6-selecting-columns.md)
7. [Nested Tables](7-nested-tables.md)
