# fulibTables v1.0.0

# fulibTables v1.0.1

# fulibTables v1.0.2

# fulibTables v1.1.0

* Bumped version number.

# fulibTables v1.2.0

+ Added the generic `Table<T>` class as a supertype of all existing table classes.
+ Added the `BooleanTable` class.
+ Added an `average` method to all numeric table classes.
+ Added a few new convenience APIs.

* The `ObjectTable` class is now generic.
* Improved generics for many Table-related APIs, improving type safety.
* Improved the `PatternBuilder` API.
* Transitioned many APIs from concrete implementation types like `ArrayList` or `LinkedHashMap` to their respective 
  interfaces like `List` or `Map`.
  > In places where this could not be done in-place, new APIs were added and the old ones deprecated.
  > See the respective Javadocs for migration info.
* Deprecated some misplaced or accidentally public APIs.
  > Check for deprecation warnings and see the respective Javadocs for migration info.
* General code cleanup and minor optimizations.
