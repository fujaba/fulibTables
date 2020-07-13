# fulibTables v1.0.0

# fulibTables v1.0.1

# fulibTables v1.0.2

# fulibTables v1.1.0

* Bumped version number.

# fulibTables v1.2.0

## New Features

+ Added the generic `Table<T>` class as a supertype of all existing table classes.
+ Added the `BooleanTable` class.
+ Added an `average` method to all numeric table classes.
+ Added a few new convenience APIs.

## Improvements

* The `ObjectTable` class is now generic.
* Improved generics for many Table-related APIs, improving type safety.
* Improved the `PatternBuilder` API.
* Interacting with tables pointing to columns that were removed with `dropColumns` or `selectColumns` now throws an exception. #5
* Naming a column twice in `selectColumns` now throws an exception.

## Bugfixes

* The `doubleTable.max` and `floatTable.max` methods now return their respective negative `MAX_VALUE` instead of `MIN_VALUE`.

## General

* Transitioned many APIs from concrete implementation types like `ArrayList` or `LinkedHashMap` to their respective 
  interfaces like `List` or `Map`.
  > In places where this could not be done in-place, new APIs were added and the old ones deprecated.
  > See the respective Javadocs for migration info.
* Deprecated some misplaced or accidentally public APIs.
  > Check for deprecation warnings and see the respective Javadocs for migration info.
* General code cleanup and minor optimizations.

# fulibTables v1.3.0

## New Features

+ Added the name-agnostic `ObjectTable.expandAll` and `.hasAnyLink` methods.
+ Added additional constraint building APIs to `PatternBuilder`.
+ Added debugging support to `PatternMatcher`.
+ Added support for multiple root patterns to `PatternMatcher`.
+ Added support for "any"-links to `PatternMatcher` by setting a role name to `*`.

## Improvements

* `ObjectTable` no longer throws exceptions when an object cannot be reflected.
* General improvements to the `PatternMatcher` API.
* The `ObjectTable.hasLink` method now throws an `IllegalArgumentException` if the other table does not share the same underlying data structure.
