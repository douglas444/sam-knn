# sam-knn
A classifier for heterogeneous concept drift inspired in the biologically memory model.

Losing, V., Hammer, B., & Wersing, H. (2016, December). KNN classifier with self adjusting memory for heterogeneous concept drift. In 2016 IEEE 16th International Conference on Data Mining (ICDM) (pp. 291-300). IEEE.


Generate javadoc site
```mvn javadoc:javadoc```

TODO

1 - Correct STM model adaptation: To every new point in the data stream, 
evaluate differently sized STM and shrunk the memory if necessary

2- Revise the ITTE calculation in STM