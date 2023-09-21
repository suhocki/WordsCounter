# Teladoc Health Interview Assessment 

The project is created by Maksim Sukhotski as part of the `Teladoc Health Senior Mobile Engineer Interview Assessment`.

# Task

Algorithms and Data Structures

Included with this assignment is the file Romeo-and-Juliet.txt. 
It is a classic tale of family, death and forbidden love! 

Write an application that will read this file and generate a list of all word occurrences.
Label each word with the frequency with which it is used. 
The application should display the results in a TableView / RecyclerView ordered from most to least frequently used.

Extra credit: Build a control that allows the table view to sort itself instead alphabetically or by character length of the word.

# Implementation

- [x] Extract words frequency from Romeo-and-Juliet.txt
- [x] Populate `RecyclerView` with words and their frequency
- [x] Control for changing sort type: by frequency, by length and alphabetically

# Demo

<img src="./demo.gif"  alt="drawing" width="500"/>

# Unit tests

Run `./gradlew test` from the command line to execute all tests.

# IDE

The project can be compiled using the Android Studio Giraffe | 2022.3.1 Patch 1.
