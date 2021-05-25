# Iterable Tables

Again, we use the following code to create the university, room and assignment tables:

<!-- insert_code_fragment: iterableTables.tables | fenced:java -->
```java
ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
```
<!-- end_code_fragment: -->

Each table wrapper is also an `Iterable` listing all objects of the corresponding column.
Thus, to sum up the points of all assignments of our table, we can use a `for` loop:

<!-- insert_code_fragment: iterableTables.loop | fenced:java -->
```java
double sum = 0;
for (Assignment a : assignmentsTable)
{
   sum += a.getPoints();
}
```
<!-- end_code_fragment: -->

Keep in mind that iterating over these tables is not always the best solution.
The next page, [Expanding Attributes](4-expanding-attributes.md), explains a better alternative using only tables.
