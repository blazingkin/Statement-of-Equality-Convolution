package in.blazingk.statementofequalityconvolutions;

import java.util.HashMap;
import java.util.Random;

import in.blazingk.statementofequalityconvolutions.functions.Addition;
import in.blazingk.statementofequalityconvolutions.functions.Constant;
import in.blazingk.statementofequalityconvolutions.functions.Multiplication;
import in.blazingk.statementofequalityconvolutions.functions.Subtraction;
import in.blazingk.statementofequalityconvolutions.functions.Variable;

public class ConvolutionGenerator {
	public static Statement left;
	public static Statement right;
	public static Random rand;
	
	public static HashMap<String, Double> variableValues = new HashMap<String, Double>();
	
	//Convolution Generator
	//The goal of this program is to produce a LOT of statements that are variations of 1 == 1
	//The way to do this is to do things to both sides and apply identities that make the right and left different
	public static void main(String[] args) {
		rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		
		left = new Statement();
		left.operation = new Variable("x");
		right = new Statement();
		right.operation = new Variable("x");
		//This will generate n equations
		int n = 25;
		//variableValues.put("x", 10d); n=500;
		for (int i = 0; i < n; i++){
			loop();
		}
	}
	
	//convoluteStatement takes a Statement and performs LOTS of convolutions on it
	//It will try lots of known identities for the operator and see if they apply
	//If those identities DO apply, then it will perform that convolution
	public static Statement convoluteStatement(Statement s){
		for (int i = 0; i < s.operation.identities.length; i++){
			if (rand.nextInt(2) == 0){
				continue;
			}
			if (s.operation.identities[i].isRelationApplicable(s)){
				s = s.operation.identities[i].applyRelation(s);
			}
		}
		s.operation.convoluteChildren();
		
		return s;
	}
	
	
	public static void loop(){
		//First we will convolute the left and right
		left = convoluteStatement(left);
		right = convoluteStatement(right);
		System.out.println(left.operation.generateString()+" == "+right.operation.generateString());
		

		//Next, we have to choose an operation to do to both sides
		switch (rand.nextInt(3)+2){
		case 0:
			//Add a random number in range [-5,5) to both sides
			{Statement s1 = new Statement();
			Statement temp = new Statement();
			temp.operation = new Constant(rand.nextInt(10)-5);
			s1.operation = new Addition(left, temp);
			left = s1;
			Statement s2 = new Statement();
			s2.operation = new Addition(right, temp);
			right = s2;}
			break;
		case 1:
			//Multiplies both sides by a random number in range [0,10)
			{Statement s1 = new Statement();
			Statement temp = new Statement();
			temp.operation = new Constant(rand.nextInt(10));
			s1.operation = new Multiplication(left, temp);
			left = s1;
			Statement s2 = new Statement();
			s2.operation = new Multiplication(right, temp);
			right = s2;}
			break;
		case 2:
			//Multiplies both sides by the variable "x"
			{Statement s1 = new Statement();
			Statement temp = new Statement();
			temp.operation = new Variable("x");
			s1.operation = new Multiplication(left, temp);
			left = s1;
			Statement s2 = new Statement();
			s2.operation = new Multiplication(right, temp);
			right = s2;}
			break;
		case 3:
			//Adds the variable "x" to both sides
			{Statement s1 = new Statement();
			Statement temp = new Statement();
			temp.operation = new Variable("x");
			s1.operation = new Addition(left, temp);
			left = s1;
			Statement s2 = new Statement();
			s2.operation = new Addition(right, temp);
			right = s2;}
			break;
		case 4:
			//Subtracts the right from the left and sets the right to 0
			{
			Statement temp = new Statement();
			temp.operation = new Subtraction(left, right);
			left = temp;
			right = new Statement();
			right.operation = new Constant(0);
			}
			break;
		default:
			break;
		}
		
		
		
	}
	

}
