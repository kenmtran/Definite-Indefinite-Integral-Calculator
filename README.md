# Definite-Indefinite-Integral-Calculator
Reads from a text file and returns the answer to a definite/indefinite integral using a binary search tree method.


This code requires the user to input the name of the text file that contains the definite/indefinite integrals that are going to be solved. (the program will not prompt the user to type anything)

The text file should only contain integral expressions with each expression having a "|" in it, even if it's an indefinite integral. An example of a valid test case would be: "| 2x^3 + 2 dx" or "2|3 2x^3 +2x dx" (spaces do not matter). The number before the pipe represents the upper bound while the number after the pipe represents the lower bound.

The program utilizes a binary search tree in order to parse through the integral expressions.
