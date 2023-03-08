import java.math.*; // used for the Math.pow function as well as the log function

public class Payload implements Comparable<Payload>{
	private int coefficient;
	private int exponent;
	private String fractionCoefficient = "";
	private boolean hasX = true;
	private boolean lnx = false;
	
	// initializes the payload's coefficient and exponent to 0
	public Payload() {
		coefficient = 0;
		exponent = 0;
	}
	// constructor that initializes the payload object with the coefficient and exponent values being passed in
	public Payload(int coefficient, int exponent) {
		this.coefficient = coefficient;
		this.exponent = exponent;
	}
	// returns coefficient
	public int getCoefficient() {
		return coefficient;
	}
	// returns exponent
	public int getExponent() {
		return exponent;
	}
	// returns if hasX is true/false
	public boolean getHasX() {
		return hasX;
	}
	// returns if lnx is true/false
	public boolean getlnx() {
		return lnx;
	}
	// returns fractionCoefficient string
	public String getFractionCoefficient() {
		return this.fractionCoefficient;
	}
	// SETTERS
	public void setHasX() {
		this.hasX = false;
	}
	public void setlnx() {
		this.lnx = true;
	}
	public void setCoefficient(int coefficient) {
		this.coefficient = coefficient;
	}
	public void setFractionCoefficient(String fractionCoefficient) {
		this.fractionCoefficient = fractionCoefficient;
	}
	public void setExponent(int exponent) {
		this.exponent = exponent;
	}
	// increments the payload's exponent by 1
	public void findAntiDerivative() {
		exponent++;
	}
	// finds the greatest common denominator
	public int GCD(int a, int b) {
		   if (b==0) return a;
		   return GCD(b,a%b);
	}
	public String fractionCoefficient() {
		// finds the GCD of the coefficient and exponent 
		int GCD = GCD(coefficient, exponent);
		// if the exponent/GCD == 1 or -1 (for cases that ends up in a whole number)
		if ((exponent/GCD) == 1 || (exponent/GCD) == -1) {
			
			// if the coefficient divided by the exponent is 1 set fractionCoefficient to 1
			if ((coefficient/exponent) == 1) {
				this.fractionCoefficient = "1";
			}
			// if the remainder of the coefficient / exponent = 0, that means there's no need for a fraction
			else if ((coefficient%exponent) == 0) {
				this.fractionCoefficient = "" + coefficient/exponent;
			}
			else {
				this.fractionCoefficient += coefficient;
			}
			
			if (coefficient < 0 && exponent < 0) {
				if ((coefficient/exponent) == 1) {
					this.fractionCoefficient = "-1";
				}
				// if the remainder of the coefficient / exponent = 0, that means there's no need for a fraction
				else if ((coefficient%exponent) == 0) {
					this.fractionCoefficient = "-" + coefficient/exponent;
				}
				else {
					this.fractionCoefficient = "" + coefficient * -1;
				}
			}
		}
		// otherwise set fractionCoefficient to a simplified fractional string
		else {
			this.fractionCoefficient = "(" + (coefficient / GCD)  + "/" +  (exponent / GCD) + ")";
		}
		// if the fractionCoefficient is 1, just store a empty value
		if (fractionCoefficient.equals("1")) {
			this.fractionCoefficient = "";
		}
		// if the fractionCoefficient is -, just store a negative sign
		else if (fractionCoefficient.equals("-1")) {
			this.fractionCoefficient = "-";
		}
		return fractionCoefficient;
	}
	// combines like terms of the current payload's coefficient with the payload being passed into
	public void combineLikeTerms(Payload o) {
		this.coefficient += o.getCoefficient();
	}
	// calculates the upperbound of the definite integral
	public double calculateUpperBound(double defB) {
		double upperBound = 0;
		// for the case the current expression being lnx
		if (lnx) {
			upperBound = coefficient * Math.log(defB);
		}
		else {
			// defB is basically being plugged into x within the expression
			upperBound = ((double)coefficient/exponent) * Math.pow(defB, exponent);
		}
		return upperBound;
	}
	// calculates the lowerbound of the definite integral
	public double calculateLowerBound(double defA) {
		double lowerBound = 0;
		// for the case the current expression being lnx
		if (lnx) {
			lowerBound = -1 * coefficient * Math.log(defA);
		}
		else {
			// defA is basically being plugged into x within the expression, with the catch of the expression being the multiplied by -1
			lowerBound = -1 * ((double)coefficient/exponent) * Math.pow(defA, exponent);
		}
		return lowerBound;
	}
	
	@Override
	// compareTo that compares the current payload's coefficient with another payload's coefficient
	public int compareTo(Payload o) {
		// if this payload's exponent is less than the other payload's exponent
		if (this.exponent < o.getExponent()) {
			return 1;
		}
		// if this payload's exponent is greater than the other payload's exponent
		else if (this.exponent > o.getExponent()) {
			return -1;
		}
		// otherwise both payloads' exponents are the same, thus combine like terms with the current coefficient with the coefficient of the other
		// payload. update fraction coefficient
		else {
			combineLikeTerms(o);
			fractionCoefficient = fractionCoefficient();
			return 0;
		}
	}
	
	@Override
	// prints the current expression along with a + or - before the expression depending on the circumstances
	public String toString() {
		// if the expression isn't negative AND has a x variable
		if (coefficient == 0) {
			return "+ " + 0;
		}
		else if (fractionCoefficient.contains("-") == false && hasX == true) {
			// check if the exponent is -1, if so output ln(x)
			if (getlnx()) {
				return "+ " + this.coefficient + "ln(x)";
			}
			else if (exponent < 0 && coefficient < 0) {
				return "+ " + this.fractionCoefficient.replaceAll("-", "") + "x^" + getExponent();
			}
			// if exponent is negative
			else if (exponent < 0) {
				// return a negative sign with the expression
				return "- " + this.fractionCoefficient + "x^" + getExponent();
			}
			// otherwise return a plus sign with the expression
			return "+ " + this.fractionCoefficient + "x^" + getExponent();
		}
		// if the expression is negative AND has a x variable
		else if (fractionCoefficient.contains("-") && hasX == true) {
			if (exponent < 0 && coefficient < 0) {
				return "+ " + this.fractionCoefficient.replaceAll("-", "") + "x^" + getExponent();
			}
			else {
				return "- " + this.fractionCoefficient.replaceAll("-", "") + "x^" + getExponent();
			}
		}
		// if the expression is positive AND doesn't have a x variable
		else if (fractionCoefficient.contains("-") == false && hasX == false) {
			return "+ " + this.fractionCoefficient + "x";
		}
		// if the expression is negative AND doesn't have a x variable
		else if (fractionCoefficient.contains("-") && hasX == false) {
			return "- " + this.fractionCoefficient.replaceAll("-", "") + "x";
		}
		return this.fractionCoefficient + "x^" + getExponent();
	}
	
	
}
