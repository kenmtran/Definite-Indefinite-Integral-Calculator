// Kenny Tran - KMT170003
// CS 2336 - Project 3 - Preventing a Paradox (Binary Search Trees)
// This program reads in definite/indefinite integrals from an input file and breaks up each expression within the integral and stores them within
// nodes and then stores them inside a binary search tree (assuming that each expression isn't a duplicate, if it is the program will combine like 
// terms). The program also finds the antiderivative of each expression before inserting the node within the BST.  The program will then traverse 
// through the binary search tree comparing the exponents of each node and ordering the nodes by largest exponent to smallest exponent. The program
// will display the node's new coefficient with a fraction or with a whole number. For definite integrals, the program will calculate the definite
// value using the upper bound and lower bound given by the definite integral.

import java.io.*;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException
	{
		
		String fileName, integral, antiderivative;
		// all these double variables used in calculating the definite value
		double definiteA = 0, definiteB = 0, upperBound = 0, lowerBound = 0, definiteValue = 0;
		int exponent = 0, coefficient = 0;
		// boolean used to differentiate between a definite or indefinite integral
		boolean integralType = false;
		// scanner used for inputting the filename
		Scanner input = new Scanner(System.in);
		// prompt for user to enter the file name to be used
		// System.out.println("Enter the file name: ");
		fileName = input.nextLine();
		
		// closes the scanner input
		input.close();
		
		// opens input file
		File inputFile = new File(fileName);
		Scanner inFile = new Scanner(inputFile);
		// checks if file has contents and checks if the file exists
		if (inputFile.canRead() && inputFile.exists()) 
		{
			// checks until end of file
			while (inFile.hasNext()) 
			{
				// creates new binary search tree
				BinTree tree = new BinTree();
				// creates new payload variable
				Payload payloadVar = new Payload();
				// creates new node
				Node<Payload> node = new Node<>();
				// reads the entire integral into one string
				integral = inFile.nextLine();
				//System.out.println(integral);
				// finds if the integral is definite/indefinite
				integralType = findIntegralType(integral);
				// if the integral is definite
				if (integralType) {
					//System.out.println("definite");
					// store the value before the pipe, this is the lower bound
					definiteA = findDefiniteA(integral);
					// store the value after the pipe, this is the upper bound
					definiteB = findDefiniteB(integral);
					// recreates the integral string as if it wasn't a definite integral
					integral = removeDefiniteValues(integral);
					// while the integral is greater than " dx", as there's still an expression to be evaluated if the integral is still of a length
					// > 3
					//System.out.println(integral);
					while(integral.length() > 3) {
						// find coefficient of the current expression
						coefficient = findCoefficient(integral);
						// find exponent of the current expression
						exponent = findExponent(integral);
						// delete the most recent evaluated expression from the string
						integral = editString(integral);
						//System.out.println(integral + "hi");
						// trim the white space from the beginning of the new integral string
						if (integral.charAt(0) == ' ') {
							integral = integral.trim();
						}
						// create new payload variable, using the coefficient and exponent values from that expression
						payloadVar = new Payload(coefficient, exponent);
						// special case if exponent is -1, that means the antiderivative for that expression will be ln(x)
						if (exponent == -1) {
							payloadVar.setlnx();
						}
						// increment the exponent by 1
						payloadVar.findAntiDerivative();
						// find the new coefficient using the current coefficient and the new exponent
						payloadVar.fractionCoefficient();
						// check for the case if the expression has an X variable
						if (exponent == 0) {
							payloadVar.setHasX();
						}
						// creates a node with the information of the expression
						node = new Node<Payload>(payloadVar);
						// check if there's a duplicate expression within the Binary Search Tree, if so do not insert the current expression into 
						// the tree
						if (tree.search(node)) {
						} 
						else { // if no duplicate, insert node into tree
							tree.insert(node);
						}
						//payloadVar.fractionCoefficient();
						// calculate upperbound of the antiderivative 
						upperBound += payloadVar.calculateUpperBound(definiteB);
						// calculate lowerbound of the antiderivative
						lowerBound += payloadVar.calculateLowerBound(definiteA);
					}
					// add both upperbound and lowerbound values together to find the definite value of the definite integral
					definiteValue = upperBound + lowerBound;
					//System.out.println(definiteValue + "hi");
					// sorts the expressions within the tree from highest exponent to lowest exponent
					tree.inorder();
					// stores the sorted expressions within a string
					antiderivative = tree.getString();
					// removes excess addition operators from the beginning of the antiderivative
					if (antiderivative.charAt(0) == '+') {
						antiderivative = antiderivative.substring(2);
					}
					// if the first value has a negative, adjust it so the negative is infront of the coefficient
					else if (antiderivative.charAt(0) == '-') {
						antiderivative = antiderivative.substring(2);
						if (antiderivative.charAt(0) == '(') {
							antiderivative = antiderivative.substring(1);
							antiderivative = "(-" + antiderivative; 
						}
						else {
							antiderivative = "-" + antiderivative;
						}
					}
					// output antiderivative with no excess whitespace and output definite value with 3 decimal places
					System.out.print(antiderivative.trim() + ", " + (int)definiteA + "|" + (int)definiteB + " = ");
					System.out.printf("%.3f\n", definiteValue);
					// reset upperbound and lowerbounds back to 0 for the next integral
					upperBound = 0;
					lowerBound = 0;
				}
				// else the integral is an indefinite integral, so just find the antiderivative
				else {
					//System.out.println("not definite");
					// while the integral is greater than " dx", as there's still an expression to be evaluated if the integral is still of a length
					// > 3
					while(integral.length() > 3) {
						//System.out.println(integral + "hi");
						// find coefficient of the current expression
						coefficient = findCoefficient(integral);
						// find exponent of the current expression
						exponent = findExponent(integral);
						// delete the most recent evaluated expression from the string
						integral = editString(integral);
						// trim the white space from the beginning of the new integral string
						if (integral.charAt(0) == ' ') {
							integral = integral.trim();
						}
						payloadVar = new Payload(coefficient, exponent);
						// special case if exponent is -1, that means the antiderivative for that expression will be ln(x)
						if (exponent == -1) {
							payloadVar.setlnx();
						}
						// increment the exponent by 1
						payloadVar.findAntiDerivative();
						//payloadVar.fractionCoefficient();
						// check for the case if the expression has an X variable
						if (exponent == 0) {
							payloadVar.setHasX();
						}
						// creates a node with the information of the expression
						node = new Node<Payload>(payloadVar);
						// check if there's a duplicate expression within the Binary Search Tree, if so do not insert the current expression into 
						// the tree
						if (tree.search(node)) {
						} 
						else { // if no duplicate, insert node into tree
							tree.insert(node);
						}
						// find the new coefficient using the current coefficient and the new exponent
						payloadVar.fractionCoefficient();
						//System.out.println(integral + "hi");
					}
					// sorts the expressions within the tree from highest exponent to lowest exponent
					tree.inorder();
					// stores the sorted expressions within a string
					antiderivative = tree.getString();
					// removes excess addition operators from the beginning of the antiderivative
					if (antiderivative.charAt(0) == '+') {
						antiderivative = antiderivative.substring(2);
					}
					// if the first value has a negative, adjust it so the negative is infront of the coefficient
					else if (antiderivative.charAt(0) == '-') {
						antiderivative = antiderivative.substring(2);
						if (antiderivative.charAt(0) == '(') {
							antiderivative = antiderivative.substring(1);
							antiderivative = "(-" + antiderivative; 
						}
						else {
						antiderivative = "-" + antiderivative;
						}
					}
					// output antiderivative with no excess whitespace along with + C to signify for a possible constant
					System.out.println(antiderivative + "+ C");
				}
			}
		}
	}
	
	// Definition of the function findIntegralType
	// Returns a boolean value that will indicate whether or not the integral being passed into the function is indefinite(false)
	// or definite(true)
	public static boolean findIntegralType(String integral) {
		boolean integralType = false;
		// if the first character of the integral string is a pipe, then that means there's no definite values and therefore the integral
		// is indefinite
		if (integral.charAt(0) == '|') {
			integralType = false;
		}
		else // otherwise any other character in the 0 position in the integral string will indicate definite integral
		{
			integralType = true;
		}
		
		return integralType;
	}
	
	// Definition of the function findCoefficient
	// Finds the coefficient of the current expression of the integral, the function takes the string with the integral and identifies
	// the proper coefficient using multiple if statements for multiple potential cases, the function returns an integer value
	public static int findCoefficient(String integral) {
		// isNegative used to indicate whether or not the coefficient is negative, and will insure the coefficient will be converted to a negative
		// value, xPresent is used to check whether or not the current expression has a x variable
		boolean isNegative = false, xPresent = false;
		// various integer variables, used to either hold the coefficient or used to find the coefficient
		int coefficient = 0, xIndex, tempIndex = 0, count = 1;
		// holds a number string that will be converted into a integer value
		String numHold = "";
		// if the integral contains both positive and negative expressions
		if (integral.contains("-") && integral.contains("+")  && integral.contains(" ")) {
			// if statement for the case that the whitespace for the very first expression within the integral is the expression being
			// currently evaluated
			if ( integral.indexOf(" ") < integral.indexOf("-") && integral.indexOf(" ") < integral.indexOf("+")) {
				// store the index of the whitespace
				tempIndex = integral.indexOf(" ");
			}
			// if statement for the case that expression contains a positive integer that is before the expression being evaluated
			else if (integral.indexOf("+") < integral.indexOf("-") && integral.indexOf("+") < integral.indexOf(" ")) {
				// take the index of the plus sign
				tempIndex = integral.indexOf("+");
			}
			else { // otherwise the expression is negative, thus take the index of the negative sign
				tempIndex = integral.indexOf("-");
				// isNegative is true, as the expression is negative so the coefficient will be too
				isNegative = true;
				if (integral.charAt(tempIndex+1) == ' ') {
					tempIndex += 1;
				}
			}
		}
		// if the current integral only contains a positive and whitespace
		else if (integral.contains("+")  && integral.contains(" ")) {
			// if statement for the case that the whitespace for the very first expression within the integral is the expression being
			// currently evaluated
			if (integral.indexOf(" ") < integral.indexOf("+")) {
				tempIndex = integral.indexOf(" ");
			}
			// else take the index of the addition sign
			else if (integral.indexOf("+") < integral.indexOf(" ")) {
				tempIndex = integral.indexOf("+");
				if (integral.charAt(tempIndex+1) == ' ') {
					tempIndex += 1;
				}
			}
		}
		// if the current integral only contains a negative and whitespace
		else if (integral.contains("-")  && integral.contains(" ")) {
			// if statement for the case that the whitespace for the very first expression within the integral is the expression being
			// currently evaluated
			if (integral.indexOf(" ") < integral.indexOf("-")) {
				tempIndex = integral.indexOf(" ");
			}
			// else take the index of the minus sign
			else if (integral.indexOf("-") < integral.indexOf(" ")) {
				tempIndex = integral.indexOf("-");
				isNegative = true;
			}
		}
		
		// this checks if the first expression of the integral has an X or not
		if (integral.contains("|")) {
			xPresent = checkFirstExpression(integral); }
		
		// checks if the expression has a x variable or not
		xPresent = isXPresent(integral, xPresent);
		
		// a counter for a while loop set to 1 for the position after the plus or minus sign
		count = 1;
		// if the expression does have an x variable, and the x value being checked isn't part of the dx
		if (xPresent == true && integral.contains("x") && integral.charAt(integral.indexOf("x") - 1) != 'd') {
			// take the index of the x variable
			xIndex = integral.indexOf("x");
			// store the substring from the tempIndex which would be from a plus/minus sign or from a white space to the x variable
			numHold = integral.substring(tempIndex+1, xIndex);
			// if the string ends up being empty or has whitespace, that means the coefficient is most likely just 1 and isn't specified
			if (numHold.isEmpty() || isWhiteSpace(numHold)) {
				numHold = "1";
			}
			// if the string contains only a negative, that means the coefficient is -1
			else if (numHold.equals("-")) {
				numHold = "-1";
			}
			// convert string into integer value
			coefficient = Integer.parseInt(numHold.trim());
		}
		// if the integral contains a plus sign as its first character and there's no x present in the expression
		else if (integral.charAt(0) == '+'){
			// while loop that repeatedly adds onto a string whatever characters is between the initial operator until it hits a +/-/d
			while (integral.charAt(count) != '+' && integral.charAt(count) != '-' && integral.charAt(count) != 'd') {
				numHold += integral.charAt(count);
				// increment counter
				count++;
			}
			// convert string to int (also trim any whitespace that is in the string)
			coefficient = Integer.parseInt(numHold.trim());
		}
		// if the integral contains a plus sign as its first character and there's no x present in the expression
		else if (integral.charAt(0) == '-'){
			// while loop that repeatedly adds onto a string whatever characters is between the initial operator until it hits a +/-/d
			while (integral.charAt(count) != '+' && integral.charAt(count) != '-' && integral.charAt(count) != 'd') {
				numHold += integral.charAt(count);
				// increment counter
				count++;
			}
			// convert string to int (also trim any whitespace that is in the string)
			coefficient = Integer.parseInt(numHold.trim());
			// if the first character of the integral is a negative that means the current expression is negative
			isNegative = true;
		}
		// if statement for the specific case in which the very first expression within the integral is a negative value
		else if ((integral.indexOf("-") - integral.indexOf(" ")) == 1) {
			// if the integral doesn't contain x
			if (xPresent == false) {
				// numHold equals the substring between the negative sign and the "d"
				numHold = integral.substring(integral.indexOf("-")+1, integral.indexOf("d"));
				coefficient = Integer.parseInt(numHold.trim());
				isNegative = true;
			}
			// if the operator sign after the expression is a plus sign or a negative sign other than the negative sign before the first expression
			else if (integral.contains("+") || (integral.contains("-") && integral.indexOf("-", integral.indexOf("-") +1) > 0)) {
				if ((integral.indexOf("-") - integral.indexOf(" ")) == 1) {
					if (integral.contains("+")) {
						numHold = integral.substring(integral.indexOf("-")+1, integral.indexOf("+"));
					}
					else {
						numHold = integral.substring(integral.indexOf("-"), integral.indexOf("-")+1);
					}
					coefficient = Integer.parseInt(numHold.trim());
					isNegative = true;
				}
				else if (integral.contains("+")) {
					numHold = integral.substring(integral.indexOf(" "), integral.indexOf("+"));
					coefficient = Integer.parseInt(numHold.trim());
				}
				else if (integral.contains("-")) {
					numHold = integral.substring(integral.indexOf(" "), integral.indexOf("-"));
					coefficient = Integer.parseInt(numHold.trim());
				}
			}
		}
		else if (xPresent == false){
			if (integral.contains("+") && integral.contains("-")) {
				if (integral.indexOf("-") < integral.indexOf("+") ) {
					numHold = integral.substring(integral.indexOf(" "), integral.indexOf("-"));
				}
				else if (integral.indexOf("+") < integral.indexOf("-")) {
					numHold = integral.substring(integral.indexOf(" "), integral.indexOf("+"));
				}
			}
			coefficient = Integer.parseInt(numHold.trim());
		}
		else {
			numHold = integral.substring(integral.indexOf(" ")+1, integral.indexOf("d"));
			coefficient = Integer.parseInt(numHold.trim());
		}
		
		// if isNegative == true, set coefficient to -1 (assuming it's a positive number)
		if (isNegative) {
			if (coefficient > 0) {
				coefficient = coefficient * -1;
			}
		}
		//System.out.println(coefficient + " ");
		
		return coefficient;
	}
	
	// Definition of the function findExponent
	// This function finds the exponent of the expression currently being evaluated from the integral being passed into the function. The function
	// returns an integer.
	public static int findExponent(String integral) {
		int exponent = 0;
		// the position within the string that contains a "^" representing an exponent
		int exponentIndex;
		// string to be converted to a integer
		String numHold = "";
		// boolean value to determine if x exists within the expression
		boolean xPresent = false;
		// this checks if the very first expression of the integral has an X or not
		if (integral.contains("|")) {
			xPresent = checkFirstExpression(integral);}
		
		// this checks if the current expression of the integral has an X or not
		xPresent = isXPresent(integral, xPresent);
		// if an x is present within the current expression and the integral contains a ^ and that the current expression being evaluated
		// has a ^ with it
		if(xPresent == true && integral.contains("^") && integral.charAt(integral.indexOf("x")+1) == '^') {
			// store exponent index
			exponentIndex = integral.indexOf("^");
			// for the specific case that the exponent is 10
			if(integral.charAt(exponentIndex+1) == '1' && integral.charAt(exponentIndex+2) == '0') {
				exponent = 10;
			}
			// for the case that the exponent is negative
			else if(integral.charAt(exponentIndex+1) == '-') {
				// for the very specific case that the exponent is negative AND 10
				if(integral.charAt(exponentIndex+2) == '1' && integral.charAt(exponentIndex+3) == '0') {
					exponent = -10;
				}
				// otherwise, take the substring for the value after the - sign, as well as make the exponent negative
				else {
					numHold = integral.substring(exponentIndex+2, exponentIndex+3);
					exponent = Integer.parseInt(numHold.trim());
					exponent = exponent * -1;
				}
			}
			// otherwise the exponent will be a single digit number without a negative sign, store that number into a string and convert to int
			else {
				numHold = integral.substring(exponentIndex+1, exponentIndex+2);
				exponent = Integer.parseInt(numHold.trim());
			}
		}
		// for the case that the current expression doesn't have a ^, that means the exponent is a 0
		else if(xPresent == true && integral.contains("x") && integral.charAt(integral.indexOf("x") - 1) != 'd') {
			exponent = 1;
		}
		//System.out.println(exponent + "expo");
		return exponent;
	}
	
	// Definition of the function editString
	// This function takes the integral string being passed into the function and creates a new string using the substring of the old integral.
	// This function will remove the expression from the string that was already evaluated and returns the string that doesn't contain the expression.
	public static String editString(String integral) {
		// int variables for exponentIndex(^) and for a counter
		int exponentIndex, count = 1;
		String tempHold = "";
		// boolean value whether or not a x variable exists in the expression being deleted
		boolean xPresent = false;
		// this checks if the current expression of the integral has an X or not
		if (integral.contains("|")) {
			xPresent = checkFirstExpression(integral);}
		
		// this checks if the current expression of the integral has an X or not
		xPresent = isXPresent(integral, xPresent);
		// if the expression being deleted contains a x variable along with a exponent
		if (xPresent == true && integral.contains("^") && (integral.indexOf("^") - integral.indexOf("x")) == 1 ) {
			// take the index of the "^"
			exponentIndex = integral.indexOf("^");
			// specific case if the exponent is 10
			if(integral.charAt(exponentIndex+1) == '1' && integral.charAt(exponentIndex+2) == '0') {
				// create a new string containing everything after the exponent number of 10
				integral = integral.substring(exponentIndex+3);
			}
			// for the case the exponent is negative
			else if (integral.charAt(exponentIndex+1) == '-') {
				// for the very specific case that the exponent is negative AND 10
				if (integral.charAt(exponentIndex+2) == '1' && integral.charAt(exponentIndex+3) == '0') {
					// create a new string containing everything after the exponent number of -10
					integral = integral.substring(exponentIndex+4);
				}
				else { // otherwise create a new string containing everything after the negative exponent value
					integral = integral.substring(exponentIndex+3);
				}
			}
			else { // otherwise create a new string containing everything after the single digit exponent
				integral = integral.substring(exponentIndex+2);
			}
		}
		// for the case that the expression contains x but doesn't have an expressed exponent
		else if (xPresent == true && integral.contains("x") && integral.charAt(integral.indexOf("x") - 1) != 'd') {
			// create a new string containing everything after the x variable
			integral = integral.substring(integral.indexOf("x")+1);
		}
		// for the case that the expression has no x variable
		else if (xPresent == false) {
			// if the initial value is a plus sign
			if (integral.charAt(0) == '+'){
				// store the characters between the initial plus sign to the +/-/d
				while (integral.charAt(count) != '+' && integral.charAt(count) != '-' && integral.charAt(count) != 'd') {
					//System.out.println(count);
					tempHold += integral.charAt(count);
					count++;
				}
				// create a new string removing everything that was in between the initial + to the +/-/d
				integral = integral.substring(tempHold.length() + 1);
			}
			// if the initial value is a minus sign
			else if (integral.charAt(0) == '-'){
				// store the characters between the initial minus sign to the +/-/d
				while (integral.charAt(count) != '+' && integral.charAt(count) != '-' && integral.charAt(count) != 'd') {
					tempHold += integral.charAt(count);
					count++;
				}
				// create a new string removing everything that was in between the initial - to the +/-/d
				integral = integral.substring(tempHold.length() + 1);
			}
			// for deleting the very first expression that contains a negative value
			else if (integral.contains("+") || (integral.contains("-") && integral.indexOf("-", integral.indexOf("-") +1) > 0)) {
				if ((integral.indexOf("-") - integral.indexOf(" ")) == 1) {
					if (integral.contains("+")) {
						integral = integral.substring(integral.indexOf("+"));
					}
					else {
						integral = integral.substring(integral.indexOf("-")+1);
					}
				}
				else if (integral.indexOf("-") < integral.indexOf("+")) {
					integral = integral.substring(integral.indexOf("-"));
				}
				else if (integral.contains("+")) {
					integral = integral.substring(integral.indexOf(" "), integral.indexOf("+"));
				}
			}
			else {
				integral = integral.substring(integral.length()-2);
			}
		}
		// the integral string now contains only dx, marking the end of evaluating the string
		else {
			integral = integral.substring(integral.length()-2);
		}
		//System.out.println(integral + "hi");
		return integral;
	}
	
	// Definition of the function checkFirstExpression
	// Function that checks specifically on whether or not the very first expression within the integral has a x variable
	public static boolean checkFirstExpression(String integral) {
		int count = 0, first, next;
		String tempHold = "";
		// boolean value representing if x is present in the expression or if there's multiple minus signs within the entire integral
		boolean xPresent = false, multipleMinus = false;
		// if the integral string contains both + and -
		if (integral.contains("+") || integral.contains("-")) {
			// if the very first expression is negative
			if ((integral.indexOf("-") - integral.indexOf(" ")) == 1){
				
				// loop that checks if there's a minus beyond the initial minus sign
				for (int i = 0; i < integral.length(); i++) {
					if (integral.charAt(i) == '-') {
						count++;
					}
					if (count >= 2) {
						multipleMinus = true;
					}
				}
				// also makes sure that if there's multiple minus signs other than the initial minus sign, that if there's a negative exponent
				// it doesn't count in that counter
				if (integral.contains("^-")) {
					count--;
					if (count < 2) {
						multipleMinus = false;
					}
				}
				// if there are multiple minus signs
				if (multipleMinus) {
					first = integral.indexOf("-");
					next = integral.indexOf("-", first + 1);
					// if the positive sign is closer than the closest minus sign (not counting the initial minus sign)
					if (integral.indexOf("+") < next && integral.indexOf("+") > 0) {
						// store substring
						tempHold = integral.substring(first, integral.indexOf("+"));
					}
					// if the closest minus sign (not counting the initial minus sign) is closer than the plus sign (assuming there is a plus sign)
					else if (next < integral.indexOf("+") || integral.indexOf("+") == -1){ 
						tempHold = integral.substring(first, next);
					}
				}
				// if there is no minus sign, just find the substring of the closest + sign
				else if (integral.contains("+")){
					tempHold = integral.substring(integral.indexOf("-"), integral.indexOf("+"));
				}
				else
				{
					first = integral.indexOf(" ");
					next = integral.indexOf(" ", first + 1);
					tempHold = integral.substring(integral.indexOf("-"), next);
				}
			}
			// various cases if the initial expression isn't negative
			else if (!(integral.contains("+")) && multipleMinus == false) {
				tempHold = integral.substring(integral.indexOf(" "), integral.indexOf("d"));
			}
			else if (integral.indexOf("+") < integral.indexOf("-") || integral.indexOf("-") == -1) {
				tempHold = integral.substring(integral.indexOf(" "), integral.indexOf("+"));
			}
			else if (integral.indexOf("-") < integral.indexOf("+") || integral.indexOf("+") == -1) {
				tempHold = integral.substring(integral.indexOf(" "), integral.indexOf("-"));
			}
			// check if the temp string has a x or not
			if (tempHold.contains("x")) {
				xPresent = true;
			}
		}
		else {
			tempHold = integral.substring(integral.indexOf(" "), integral.indexOf("d"));
			// check if the temp string has a x or not
			if (tempHold.contains("x")) {
				xPresent = true;
			}
		}
		return xPresent;
	}
	
	// Definition of the function isXPresent
	// Checks if the current expression being evaluated has a x variable or not
	public static boolean isXPresent(String integral, boolean xPresent) {
		int count = 1;
		String tempHold = "";
		// if the initial character is a + sign
		if (integral.charAt(0) == '+'){
			// while loop that stores everything in between the + sign and the +/-/d after the expression into a string
			while (integral.charAt(count) != '+' && integral.charAt(count) != '-' && integral.charAt(count) != 'd') {
				//System.out.println(count);
				tempHold += integral.charAt(count);
				count++;
			}
			// check if that string has x or not
			if (tempHold.contains("x")) {
				xPresent = true;
			}
		}
		// if the initial character is a - sign
		else if (integral.charAt(0) == '-'){
			// while loop that stores everything in between the - sign and the +/-/d after the expression into a string
			while (integral.charAt(count) != '+' && integral.charAt(count) != '-' && integral.charAt(count) != 'd') {
				tempHold += integral.charAt(count);
				count++;
			}
			// check if that string has x or not
			if (tempHold.contains("x")) {
				xPresent = true;
			}
		}
		return xPresent;
	}
	
	// Definition of the function findDefiniteA
	// Stores the everything before the pipe (|) into a string and converts that string into a double
	public static double findDefiniteA(String integral) {
		double definiteA = 0;
		String tempHold = "";
		// tempHold holds a string containing everything before the pipe
		tempHold = integral.substring(0, integral.indexOf("|"));
		// convert that string into a double
		definiteA = Double.parseDouble(tempHold);
		return definiteA;
	}
	
	
	// Definition of the function findDefiniteB
	// Stores the everything between the pipe (|) and the first whitespace into a string and converts that string into a double
	public static double findDefiniteB(String integral) {
		double definiteB = 0;
		String tempHold = "";
		// tempHold holds everything between the pipe (|) and the first whitespace
		tempHold = integral.substring(integral.indexOf("|") + 1, integral.indexOf(" "));
		// convert string into a double
		definiteB = Double.parseDouble(tempHold);
		return definiteB;
	}
	
	// Definition of the function removeDefiniteValues
	// This function deletes the upperbound and lowerbound values from the integral string and recreates the string with just the pipe
	// and without the upperbound and lowerbound
	public static String removeDefiniteValues(String integral) {
		// creates new string with everything including the first whitespace and after
		integral = integral.substring(integral.indexOf(" "));
		// adds back the pipe to the integral
		integral = "|" + integral;
		
		return integral;
	}
	
	public static boolean isWhiteSpace(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}

}
