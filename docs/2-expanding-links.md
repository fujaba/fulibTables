# Expanding Links

We continue with the `universityTable` from the [ObjectTables](1-object-tables.md) page, which was created like this:

<!-- insert_code_fragment: expandingLinks.universityTable | fenced:java -->
```java
ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
```
<!-- end_code_fragment: -->

As reminder, this is what it looked like:

<!-- insert_code_fragment: expandingLinks.universityTableResult -->
| University 	|
| --- |
| Study Right 	|
<!-- end_code_fragment: -->

Now we can *expand* this table to produce additional columns:

<!-- insert_code_fragment: expandingLinks.roomsTable | fenced:java -->
```java
ObjectTable<Room> roomsTable = universityTable.expandAll("Room", University::getRooms);
```
<!-- end_code_fragment: -->

This extends the university table with a `Room` column.
For each row of the old table we look up the university `u` contained in the `University` column (in our case there is just one row containing the `studyRight` object in its `University` column).
For each university object `u`, we look up each room `r` attached to it.
For each university object `u` and room `r` pair, we create a new row in the resulting room table.

<!-- insert_code_fragment: expandingLinks.roomsTableResult -->
| University 	| Room 	|
| --- | --- |
| Study Right 	| wa1337 Math 	|
| Study Right 	| wa1338 Arts 	|
| Study Right 	| wa1339 Football 	|
<!-- end_code_fragment: -->

We can repeat this step with the assignments attached to rooms:

<!-- insert_code_fragment: expandingLinks.assignmentsTable | fenced:java -->
```java
ObjectTable<Assignment> assignmentsTable = roomsTable.expandAll("Assignment", Room::getAssignments);
```
<!-- end_code_fragment: -->

This expands the room table with the attached assignments.
Again, we loop through the rows of the room table and look up the university `u` contained in the `University` column and the room `r` contained in the `Room` column.
Then, for each assignment `a` attached to room `r`, we create a result row containing `u`, `r`, and `a`.

The table below shows the current result:

<!-- insert_code_fragment: expandingLinks.assignmentsTableResult -->
| University 	| Room 	| Assignment 	|
| --- | --- | --- |
| Study Right 	| wa1337 Math 	| integrals 	|
| Study Right 	| wa1337 Math 	| matrices 	|
| Study Right 	| wa1338 Arts 	| drawings 	|
| Study Right 	| wa1338 Arts 	| sculptures 	|
<!-- end_code_fragment: -->

Note that all three variables `universityTable`, `roomsTable`, and `assignmentsTable` refer to the same internal table object.
However, they each refer to the specific column where the next expand operation applies.
