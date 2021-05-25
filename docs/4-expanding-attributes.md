# Expanding Attributes

As always, we begin this page by initializing some tables:

<!-- insert_code_fragment: expandingAttributes.tables | fenced:java -->
```java
ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
```
<!-- end_code_fragment: -->

We may expand the assignment table from the [Expanding Links](2-expanding-links.md) page by a `Points` column.

<!-- insert_code_fragment: expandingAttributes.pointsTable | fenced:java -->
```java
doubleTable pointsTable = assignmentsTable.expand("Points", Assignment::getPoints).as(doubleTable.class);
```
<!-- end_code_fragment: -->

<!-- insert_code_fragment: expandingAttributes.pointsTableResult -->
| University 	| Room 	| Assignment 	| Points 	|
| --- | --- | --- | --- |
| Study Right 	| wa1337 Math 	| integrals 	| 42.0 	|
| Study Right 	| wa1337 Math 	| matrices 	| 23.0 	|
| Study Right 	| wa1338 Arts 	| drawings 	| 12.0 	|
| Study Right 	| wa1338 Arts 	| sculptures 	| 12.0 	|
<!-- end_code_fragment: -->

The resulting points table has a `sum` method that sums up all double values contained in the corresponding column.

<!-- insert_code_fragment: expandingAttributes.pointsSum | fenced:java -->
```java
double sum = pointsTable.sum();
```
<!-- end_code_fragment: -->
