1.2.4 (2017-08-28)
====
- refactoring modifications (method names) were done!

1.2.2 (2016-11-18)
===
- New reduction classes were added for List<T> (including Intersection, Union and Join).

1.2.1 (2016-08-03)
===
- The loop scheduler package was extended to address static and guided loop scheduling policies as well.

1.2.0 (2016-07-20)
===
- Initial version of loop schedulers were added to the utility package. The current version only provided dynamic loop scheduling policy. 

1.1.0 (2016-03-30)
===
- Key changes to ParallelIterator. ParallelIterator is now changed to PARCutils, and ParallelIterator is instead a sub-package of PARCutils. At this stage, "pu" is the top package of the framework, "pi" and "RedLib" are the sub-packages.

1.0.4 (2014-10-29)
===
- Added new implementations to the reduction library, also suggested using "Operand" objects for reduction.

1.0.3 (2014-03-12)
===
- Change the default chunk size for DYNAMIC Scheduling into 1, which is consistent with the OpenMP specification.

1.0.2 (2014-01)
===
- Support for DynamicList.subList and DynamicList.iterator added

1.0.1 (2013-12)
===
- default chunk size bug fixed

1.0.0 (2013-07)
===
- Refactoring the PI
- Trim out the redundant logic and method

0.9.8
===
