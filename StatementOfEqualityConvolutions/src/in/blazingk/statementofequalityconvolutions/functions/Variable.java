package in.blazingk.statementofequalityconvolutions.functions;

import in.blazingk.statementofequalityconvolutions.ConvolutionGenerator;
import in.blazingk.statementofequalityconvolutions.Statement;

public class Variable extends Function {
	public String varName = "x";
	public Variable(String name){
		varName = name;
		identities = new Relation[1];
		identities[0] = new Identity();
	}
	
	@Override
	public double evaluate() {
		//A variable has no value
		return ConvolutionGenerator.variableValues.get(varName);
	}

	@Override
	public boolean evaluatesToConstant() {
		return ConvolutionGenerator.variableValues.containsKey(varName);
	}

	@Override
	public String generateString() {
		return varName;
	}
	
	public void convoluteChildren(){
		
	}
	
	@Override
	public boolean equalToOtherStatement(Statement s) {
		if (s.operation instanceof Variable){
			Variable var = (Variable) s.operation;
			return var.varName.equals(varName);
		}
		return false;
	}
	
	
	class Identity extends Relation{

		@Override
		public boolean isRelationApplicable(Statement s) {
			// TODO Auto-generated method stub
			return (s.operation instanceof Variable);
		}

		@Override
		public Statement applyRelation(Statement s) {
			// TODO Auto-generated method stub
			return s;
		}
		
		
	}

}
