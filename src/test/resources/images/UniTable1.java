// start_code_fragment: FulibTables.uniTable1 
| University 	| 
|  --- 	| 
| Study Right 	| 

// end_code_fragment: 
// start_code_fragment: FulibTables.uniTable2 
| University 	| Room 	| 
|  --- 	|  --- 	| 
| Study Right 	| wa1337 Math 	| 
| Study Right 	| wa1338 Arts 	| 
| Study Right 	| wa1339 Football 	| 

// end_code_fragment: 
// start_code_fragment: FulibTables.uniTable3 
| University 	| Room 	| Assignment 	| 
|  --- 	|  --- 	|  --- 	| 
| Study Right 	| wa1337 Math 	| integrals 	| 
| Study Right 	| wa1337 Math 	| matrices 	| 
| Study Right 	| wa1338 Arts 	| drawings 	| 
| Study Right 	| wa1338 Arts 	| sculptures 	| 

// end_code_fragment: 
// start_code_fragment: StudyRightTables.pointsTableResult 
| University 	| Room 	| Assignment 	| Points 	| 
|  --- 	|  --- 	|  --- 	|  --- 	| 
| Study Right 	| wa1337 Math 	| integrals 	| 42.0 	| 
| Study Right 	| wa1337 Math 	| matrices 	| 23.0 	| 
| Study Right 	| wa1338 Arts 	| drawings 	| 12.0 	| 
| Study Right 	| wa1338 Arts 	| sculptures 	| 12.0 	| 

// end_code_fragment: 
// start_code_fragment: FulibTables.studentsTableResult 
| University 	| Room 	| Assignment 	| Points 	| Student 	| 
|  --- 	|  --- 	|  --- 	|  --- 	|  --- 	| 
| Study Right 	| wa1337 Math 	| integrals 	| 42.0 	| Alice m4242 	| 
| Study Right 	| wa1337 Math 	| integrals 	| 42.0 	| Carli m2323 	| 
| Study Right 	| wa1337 Math 	| matrices 	| 23.0 	| Alice m4242 	| 
| Study Right 	| wa1337 Math 	| matrices 	| 23.0 	| Carli m2323 	| 
| Study Right 	| wa1338 Arts 	| drawings 	| 12.0 	| Bobby m2323 	| 
| Study Right 	| wa1338 Arts 	| sculptures 	| 12.0 	| Bobby m2323 	| 

// end_code_fragment: 
// start_code_fragment: FulibTables.filterAssignmentsTableResult 
| University 	| Room 	| Assignment 	| Points 	| Student 	| 
|  --- 	|  --- 	|  --- 	|  --- 	|  --- 	| 
| Study Right 	| wa1337 Math 	| matrices 	| 23.0 	| Alice m4242 	| 
| Study Right 	| wa1337 Math 	| matrices 	| 23.0 	| Carli m2323 	| 
| Study Right 	| wa1338 Arts 	| drawings 	| 12.0 	| Bobby m2323 	| 
| Study Right 	| wa1338 Arts 	| sculptures 	| 12.0 	| Bobby m2323 	| 

// end_code_fragment: 
// start_code_fragment: FulibTables.filterRowTableResult 
| University 	| Room 	| Student 	| Assignment 	| 
|  --- 	|  --- 	|  --- 	|  --- 	| 
| Study Right 	| wa1337 Math 	| Alice m4242 	| integrals 	| 

// end_code_fragment: 
// start_code_fragment: FulibTables.filterHasDoneResult 
| University 	| Room 	| Student 	| Assignment 	| 
|  --- 	|  --- 	|  --- 	|  --- 	| 
| Study Right 	| wa1337 Math 	| Alice m4242 	| integrals 	| 

// end_code_fragment: 
// start_code_fragment: FulibTables.doCurrentAssignmentsResult 
| University 	| Room 	| Student 	| Assignment 	| noOfDone 	| Done 	| 
|  --- 	|  --- 	|  --- 	|  --- 	|  --- 	|  --- 	| 
| Study Right 	| wa1337 Math 	| Alice m4242 	| integrals 	| 2 	| integrals 	| 
| Study Right 	| wa1337 Math 	| Alice m4242 	| integrals 	| 2 	| matrices 	| 
| Study Right 	| wa1337 Math 	| Alice m4242 	| matrices 	| 2 	| integrals 	| 
| Study Right 	| wa1337 Math 	| Alice m4242 	| matrices 	| 2 	| matrices 	| 
| Study Right 	| wa1337 Math 	| Carli m2323 	| integrals 	| 2 	| integrals 	| 
| Study Right 	| wa1337 Math 	| Carli m2323 	| integrals 	| 2 	| matrices 	| 
| Study Right 	| wa1337 Math 	| Carli m2323 	| matrices 	| 2 	| integrals 	| 
| Study Right 	| wa1337 Math 	| Carli m2323 	| matrices 	| 2 	| matrices 	| 
| Study Right 	| wa1338 Arts 	| Bobby m2323 	| drawings 	| 2 	| drawings 	| 
| Study Right 	| wa1338 Arts 	| Bobby m2323 	| drawings 	| 2 	| sculptures 	| 
| Study Right 	| wa1338 Arts 	| Bobby m2323 	| sculptures 	| 2 	| drawings 	| 
| Study Right 	| wa1338 Arts 	| Bobby m2323 	| sculptures 	| 2 	| sculptures 	| 

// end_code_fragment: 
// start_code_fragment: FulibTables.dropColumnsAssignmentResult 
| University 	| Room 	| Student 	| noOfDone 	| Done 	| 
|  --- 	|  --- 	|  --- 	|  --- 	|  --- 	| 
| Study Right 	| wa1337 Math 	| Alice m4242 	| 2 	| integrals 	| 
| Study Right 	| wa1337 Math 	| Alice m4242 	| 2 	| matrices 	| 
| Study Right 	| wa1337 Math 	| Carli m2323 	| 2 	| integrals 	| 
| Study Right 	| wa1337 Math 	| Carli m2323 	| 2 	| matrices 	| 
| Study Right 	| wa1338 Arts 	| Bobby m2323 	| 2 	| drawings 	| 
| Study Right 	| wa1338 Arts 	| Bobby m2323 	| 2 	| sculptures 	| 

// end_code_fragment: 
// start_code_fragment: FulibTables.selectColumnsResult 
| Student 	| Done 	| 
|  --- 	|  --- 	| 
| Alice m4242 	| integrals 	| 
| Alice m4242 	| matrices 	| 
| Carli m2323 	| integrals 	| 
| Carli m2323 	| matrices 	| 
| Bobby m2323 	| drawings 	| 
| Bobby m2323 	| sculptures 	| 

// end_code_fragment: 
// start_code_fragment: FulibTables.nestedTablesResult 
| University 	| Students 	| Credits 	| Done 	| 
|  --- 	|  --- 	|  --- 	|  --- 	| 
| Study Right 	| Alice m4242 	| 65.0 	| integrals, matrices 	| 
| Study Right 	| Bobby m2323 	| 24.0 	| drawings, sculptures 	| 
| Study Right 	| Carli m2323 	| 65.0 	| integrals, matrices 	| 

// end_code_fragment: 
