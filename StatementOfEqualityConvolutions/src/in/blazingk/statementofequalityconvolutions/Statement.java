package in.blazingk.statementofequalityconvolutions;

import in.blazingk.statementofequalityconvolutions.functions.Function;

public class Statement {
	//These are the conditions that are true of the statement
	public ConditionsAboutFunction conditions[];
	
	//This is the operation being performed
	public Function operation;
	
	//Find a numerical answer
	public double evaluate(){
		return operation.evaluate();
	}
	
	//Is finding a numerical answer possible?
	public boolean evaluatesToConstant(){
		return operation.evaluatesToConstant();
	}
	
	//Is this statement the same as the other?
	public boolean equalToOtherStatement(Statement s){
		return operation.equalToOtherStatement(s);
	}
	
}
