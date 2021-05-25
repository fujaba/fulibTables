# Object Tables

fulibTables provides the class [`ObjectTable`](https://javadoc.io/doc/org.fulib/fulibTables/latest/org/fulib/tables/ObjectTable.html), which allows us to perform some table operations:

<!-- insert_code_fragment: objectTables.universityTable | fenced:java -->
```java
ObjectTable<University> universityTable = new ObjectTable<>("University", studyRight);
```
<!-- end_code_fragment: -->

The first line generates a table with just one `University` column and one row containing the `StudyRight` object.

<!-- insert_code_fragment: objectTables.universityTableResult -->
| University 	|
| --- |
| Study Right 	|
<!-- end_code_fragment: -->
