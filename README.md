## Ranking Table for a Soccer League


command-line application that will calculate the ranking table for a soccer league.

### Java
---------
java version "1.8.0_60"
Java(TM) SE Runtime Environment (build 1.8.0_60-b27)
Java HotSpot(TM) 64-Bit Server VM (build 25.60-b23, mixed mode)


### Input/output
----------------
* The input and output will be text. Takes filenames on the command line.
* The input contains results of games, one per line. See “Sample input” for details.
* The output should be ordered from most to least points, following the format specified in “Expected output”.


### Getting up and running
---------------------------

##### Basics

Help:

Run **SoccerTable.java** for a file input from the command line:

*  $ java com.ontiyonke.soccertable.SoccerTable.java sample-input.txt


### Results
-----------
The input is game results, one per line (see sample-input.txt). The output is a ranking of teams and their points,
one per line (see expected-output.txt). We expect that the input will be well-formed.

In this league, teams accumulate points by winning (3 points) or drawing (1point).
If two teams have the same number of points, they are ranked equally and are output in alphabetical order.
