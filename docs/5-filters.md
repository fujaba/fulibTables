# Filters

To further expand our table we might add students that are in rooms:

<!-- insert_code_fragment: filters.tables | fenced:java -->
```java
ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
ObjectTable<Student> studentsTable = roomsTable.expandAll("Student", Room::getStudents);
```
<!-- end_code_fragment: -->

<!-- insert_code_fragment: filters.tablesResult -->
| University 	| Room 	| Assignment 	| Student 	|
| --- | --- | --- | --- |
| Study Right 	| wa1337 Math 	| integrals 	| Alice m4242 	|
| Study Right 	| wa1337 Math 	| integrals 	| Carli m2323 	|
| Study Right 	| wa1337 Math 	| matrices 	| Alice m4242 	|
| Study Right 	| wa1337 Math 	| matrices 	| Carli m2323 	|
| Study Right 	| wa1338 Arts 	| drawings 	| Bobby m2323 	|
| Study Right 	| wa1338 Arts 	| sculptures 	| Bobby m2323 	|
<!-- end_code_fragment: -->

The resulting table has the cross product of assignments and students for each room.

## Filter by Attribute

In addition to the cross product we may select a subset of the table rows using a filter operation:

<!-- insert_code_fragment: filters.filter | fenced:java -->
```java
assignmentsTable.filter(a -> a.getPoints() <= 30);
```
<!-- end_code_fragment: -->

<!-- insert_code_fragment: filters.filterResult -->
| University 	| Room 	| Assignment 	| Student 	|
| --- | --- | --- | --- |
| Study Right 	| wa1337 Math 	| matrices 	| Alice m4242 	|
| Study Right 	| wa1337 Math 	| matrices 	| Carli m2323 	|
| Study Right 	| wa1338 Arts 	| drawings 	| Bobby m2323 	|
| Study Right 	| wa1338 Arts 	| sculptures 	| Bobby m2323 	|
<!-- end_code_fragment: -->

# Filter involving multiple Columns

Alternatively, we may filter by rows:

<!-- insert_code_fragment: filterRows.filterRows | fenced:java -->
```java
students.filterRows(row -> {
   Student studi = (Student) row.get("Student");
   Assignment assignment = (Assignment) row.get("Assignment");
   return studi.getDone().contains(assignment);
});
```
<!-- end_code_fragment: -->

<!-- insert_code_fragment: filterRows.filterRowsResult -->
| University 	| Room 	| Student 	| Assignment 	|
| --- | --- | --- | --- |
| Study Right 	| wa1337 Math 	| Alice m4242 	| integrals 	|
<!-- end_code_fragment: -->

Note, when we did the filter by assignment, our internal table was reduced to 4 rows.
To have a full table for the filter by row operation, we had to reconstruct that full table.

# Filter by Link

Above row filter requires that the current student has done the current assignment.
This filter condition may also be expressed by a `hasLink` operation:

<!-- insert_code_fragment: hasLink.hasLink | fenced:java -->
```java
students.hasLink(Student.PROPERTY_done, assignmentsTable);
```
<!-- end_code_fragment: -->

<!-- insert_code_fragment: hasLink.hasLinkResult -->
| University 	| Room 	| Student 	| Assignment 	|
| --- | --- | --- | --- |
| Study Right 	| wa1337 Math 	| Alice m4242 	| integrals 	|
<!-- end_code_fragment: -->

# Filter by Subclass

Sometimes you'll end up in a situation where multiple objects of different types are in the same column.
For example, if you want to find all instances of some subclass.

<!-- insert_code_fragment: filterAs.tables | fenced:java -->
```java
Table<Object> objectsTable = new Table<>("Objects", studyRight, alice, bob, integrals);
```
<!-- end_code_fragment: -->

<!-- insert_code_fragment: filterAs.tablesResult -->
| Objects 	|
| --- |
| Study Right 	|
| Alice m4242 	|
| Bobby m2323 	|
| integrals 	|
<!-- end_code_fragment: -->

You can use `filterAs` to simultaneously filter by `instanceof` and cast the table to the right type:

<!-- insert_code_fragment: filterAs.filterAs | fenced:java -->
```java
final Table<Student> studentsTable = objectsTable.filterAs(Student.class);
```
<!-- end_code_fragment: -->

<!-- insert_code_fragment: filterAs.filterAsResult -->
| Objects 	|
| --- |
| Alice m4242 	|
| Bobby m2323 	|
<!-- end_code_fragment: -->

> #### ⓘ Information
>
> The equivalent operation using `filter` requires unchecked and ugly casts:
> <!-- insert_code_fragment: filterAs.alternative | fenced:java -->
> ```java
> final Table<Student> studentsTable2 = (Table<Student>) (Table) objectsTable.filter(s -> s instanceof Student);
> ```
> <!-- end_code_fragment: -->
