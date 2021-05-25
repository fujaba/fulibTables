If you want to update all elements of a certain column, nested tables may come in handy:

<!-- insert_code_fragment: nestedTables.nestedTables | fenced:java -->
```java
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
```
<!-- end_code_fragment: -->

In the third last line the `expandTopic` operation adds a column with the topic names of the corresponding assignments to the local table.
On string columns, you can call `join` to concatenate all strings.

<!-- insert_code_fragment: nestedTables.nestedTablesResult -->
| University 	| Students 	| Credits 	| Done 	|
| --- | --- | --- | --- |
| Study Right 	| Alice m4242 	| 65.0 	| integrals, matrices 	|
| Study Right 	| Bobby m2323 	| 24.0 	| drawings, sculptures 	|
| Study Right 	| Carli m2323 	| 65.0 	| integrals, matrices 	|
<!-- end_code_fragment: -->
