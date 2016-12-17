package in.blazingk.statementofequalityconvolutions.functions;

import in.blazingk.statementofequalityconvolutions.ConvolutionGenerator;
import in.blazingk.statementofequalityconvolutions.Statement;

public class Addition extends Function {
	public Statement argA;
	public Statement argB;
	public Addition(Statement a, Statement b){
		this.identities = new Relation[6];
		identities[0] = new additionByZero();
		identities[1] = new commutiveProperty();
		identities[2] = new collapseConstants();
		identities[3] = new additionOfTheSameKind();
		identities[4] = new likeTerms();
		identities[5] = new changeToSubtraction();
		argA = a;
		argB = b;
	}
	
	public double evaluate(){
		return argA.evaluate() + argB.evaluate();
	}
	
	public boolean evaluatesToConstant(){
		return argA.evaluatesToConstant() && argB.evaluatesToConstant();
	}
	
	public String generateString(){
		return "("+argA.operation.generateString()+" + "+argB.operation.generateString()+")";
	}
	
	@Override
	public boolean equalToOtherStatement(Statement s) {
		if (s.operation instanceof Addition){
			Addition add = (Addition) s.operation;
			return (argA.equalToOtherStatement(add.argA) && argB.equalToOtherStatement(add.argB))
					|| (argB.equalToOtherStatement(add.argA) && argA.equalToOtherStatement(add.argB));
		}
		return false;
	}
	
	public void convoluteChildren(){
		argA.operation.convoluteChildren();
		argB.operation.convoluteChildren();
		argA = ConvolutionGenerator.convoluteStatement(argA);
		argB = ConvolutionGenerator.convoluteStatement(argB);
	}
	
	class additionByZero extends Relation {

		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Addition){
				Addition add = (Addition) s.operation;
				if (argA.operation.evaluatesToConstant() && argB.operation.evaluatesToConstant()){
					return add.argA.evaluate() == 0 || add.argB.evaluate() == 0;
				}
				if (add.argA.operation.evaluatesToConstant() && add.argA.operation.evaluate() == 0){
					return true;
				}
				if (add.argB.operation.evaluatesToConstant() && add.argB.operation.evaluate() == 0){
					return true;
				}
			}
			return false;
		}
		public Statement applyRelation(Statement s) {
			if (s.operation instanceof Addition){
				Addition add = (Addition) s.operation;
				if (add.argA.evaluatesToConstant() && add.argA.evaluate() == 0){
					return argB;
				}
				if (add.argB.evaluatesToConstant() && add.argB.evaluate() == 0){
					return argA;
				}
			}
			return s;
		}
		
		
	}
	
	class commutiveProperty extends Relation {

		public boolean isRelationApplicable(Statement s) {
			return (s.operation instanceof Addition);
		}

		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Addition oldAddition = (Addition)s.operation;
				s.operation = new Addition(oldAddition.argB, oldAddition.argA);
			}
			return s;
		}
		
	}
	
	class collapseConstants extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Addition){
				Addition add = (Addition) s.operation;
				return add.evaluatesToConstant();
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Addition add = (Addition) s.operation;
				s.operation = new Constant(add.evaluate());
			}
			return s;
		}
		
		
	}
	
	class additionOfTheSameKind extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Addition){
				Addition add = (Addition) s.operation;
				return add.argA.equalToOtherStatement(add.argB);
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Addition add = (Addition) s.operation;
				Statement temp = new Statement();
				temp.operation = new Constant(2);
				s.operation = new Multiplication(temp, add.argA);
				return s;
			}
			return s;
		}
		
	}
	
	//Allows things like (X + (F * X)) -> ((F+1) * X))
	class likeTerms extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Addition){
				Addition add = (Addition) s.operation;
				if (add.argA.operation instanceof Variable || add.argB.operation instanceof Variable){
					if (add.argA.operation instanceof Multiplication){
						Multiplication mult = (Multiplication) add.argA.operation;
						return mult.argA.operation instanceof Variable || mult.argB.operation instanceof Variable;
					}
					if (add.argB.operation instanceof Multiplication){
						Multiplication mult = (Multiplication) add.argB.operation;
						return mult.argA.operation instanceof Variable || mult.argB.operation instanceof Variable;
					}
				}
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Addition add = (Addition) s.operation;
				if (add.argA.operation instanceof Multiplication){
					Multiplication mult = (Multiplication) add.argA.operation;
					if (mult.argA.operation instanceof Variable){
						Variable v = (Variable) mult.argA.operation;
						if (v.equalToOtherStatement(add.argB)){
							Statement st = new Statement();
							Statement temp = new Statement();
							temp.operation = new Constant(1);
							st.operation = new Addition(temp, mult.argB);
							s.operation = new Multiplication(st, mult.argA);
							return s;
						}
					}else{
						Variable v = (Variable) mult.argB.operation;
						if (v.equalToOtherStatement(add.argB)){
							Statement st = new Statement();
							Statement temp = new Statement();
							temp.operation = new Constant(1);
							st.operation = new Addition(temp, mult.argA);
							s.operation = new Multiplication(st, mult.argB);
							return s;
						}
					}
				}
				if (add.argB.operation instanceof Multiplication){
					Multiplication mult = (Multiplication) add.argB.operation;
					if (mult.argA.operation instanceof Variable){
						Variable v = (Variable) mult.argA.operation;
						if (v.equalToOtherStatement(add.argA)){
							Statement st = new Statement();
							Statement temp = new Statement();
							temp.operation = new Constant(1);
							st.operation = new Addition(temp, mult.argB);
							s.operation = new Multiplication(st, mult.argA);
							return s;
						}
					}else{
						Variable v = (Variable) mult.argB.operation;
						if (v.equalToOtherStatement(add.argA)){
							Statement st = new Statement();
							Statement temp = new Statement();
							temp.operation = new Constant(1);
							st.operation = new Addition(temp, mult.argA);
							s.operation = new Multiplication(st, mult.argB);
							return s;
						}
					}
				}
			}
			return s;
		}
		
	}
	
	class changeToSubtraction extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			return s.operation instanceof Addition;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Addition add = (Addition) s.operation;
				Statement mul = new Statement();
				Statement negOne = new Statement();
				negOne.operation = new Constant(-1);
				mul.operation = new Multiplication(negOne, add.argB);
				s.operation = new Subtraction(add.argA, mul);
			}
			return s;
		}
		
	}

	
		
}
