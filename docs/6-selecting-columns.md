# Selecting Columns

Let's start with some familiar tables, again:

<!-- insert_code_fragment: selectingColumns.tables | fenced:java -->
```java
ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
ObjectTable<Student> studentsTable = roomsTable.expandAll("Student", Room::getStudents);
```
<!-- end_code_fragment: -->

<!-- insert_code_fragment: selectingColumns.tablesResult -->
| University 	| Room 	| Assignment 	| Student 	|
| --- | --- | --- | --- |
| Study Right 	| wa1337 Math 	| integrals 	| Alice m4242 	|
| Study Right 	| wa1337 Math 	| integrals 	| Carli m2323 	|
| Study Right 	| wa1337 Math 	| matrices 	| Alice m4242 	|
| Study Right 	| wa1337 Math 	| matrices 	| Carli m2323 	|
| Study Right 	| wa1338 Arts 	| drawings 	| Bobby m2323 	|
| Study Right 	| wa1338 Arts 	| sculptures 	| Bobby m2323 	|
<!-- end_code_fragment: -->

As the current table contains some confusing cross products, let us drop the `Assignment` column:

<!-- insert_code_fragment: selectingColumns.dropAssignment | fenced:java -->
```java
universityTable.dropColumns("Assignment");
```
<!-- end_code_fragment: -->

<!-- insert_code_fragment: selectingColumns.dropAssignmentResult -->
| University 	| Room 	| Student 	|
| --- | --- | --- |
| Study Right 	| wa1337 Math 	| Alice m4242 	|
| Study Right 	| wa1337 Math 	| Carli m2323 	|
| Study Right 	| wa1338 Arts 	| Bobby m2323 	|
<!-- end_code_fragment: -->

Alternatively, we may select the columns we are interested in:

<!-- insert_code_fragment: selectingColumns.selectStudentAndRoom | fenced:java -->
```java
studentsTable.selectColumns("Student", "Room");
```
<!-- end_code_fragment: -->

<!-- insert_code_fragment: selectingColumns.selectStudentAndRoomResult -->
| Room 	| Student 	|
| --- | --- |
| wa1337 Math 	| Alice m4242 	|
| wa1337 Math 	| Carli m2323 	|
| wa1338 Arts 	| Bobby m2323 	|
<!-- end_code_fragment: -->
