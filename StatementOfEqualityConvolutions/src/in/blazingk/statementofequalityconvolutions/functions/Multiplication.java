package in.blazingk.statementofequalityconvolutions.functions;

import in.blazingk.statementofequalityconvolutions.ConvolutionGenerator;
import in.blazingk.statementofequalityconvolutions.Statement;

public class Multiplication extends Function{

	public Statement argA, argB;
	public Multiplication(Statement a, Statement b){
		argA = a;
		argB = b;
		identities = new Relation[7];
		identities[0] = new multByOne();
		identities[1] = new commutiveProperty();
		identities[2] = new collapseConstants();
		//identities[3] = new distributiveProperty();
		identities[3] = new expIdentity();
		identities[4] = new multExps();
		identities[5] = new multOneExp();
		identities[6] = new distributiveMult();
	}
	@Override
	public double evaluate() {
		if (argA.evaluatesToConstant() && argB.evaluatesToConstant()){
			//If both of them can be evaluated, do so
			return argA.evaluate() * argB.evaluate();
		}else {
			//Otherwise, we got to this point because one of the variables is 0
			//So we can just return the result of our multiplication, or 0
			return 0;
		}
	}
	
	public boolean evaluatesToConstant(){
		if (argA.evaluatesToConstant() && argB.evaluatesToConstant()){
			return true;
		}
		if (argA.evaluatesToConstant() && argA.evaluate() == 0){
			return true;
		}
		if (argB.evaluatesToConstant() && argB.evaluate() == 0){
			return true;
		}
		return false;
	}

	@Override
	public String generateString() {
		return "("+argA.operation.generateString()+" * "+argB.operation.generateString()+")";
	}
	
	@Override
	public boolean equalToOtherStatement(Statement s) {
		if (s.operation instanceof Multiplication){
			Multiplication mult = (Multiplication) s.operation;
			return argA.equalToOtherStatement(mult.argA) && argB.equalToOtherStatement(mult.argB);
		}
		return false;
	}
	
	public void convoluteChildren(){
		argA.operation.convoluteChildren();
		argB.operation.convoluteChildren();
		argA = ConvolutionGenerator.convoluteStatement(argA);
		argB = ConvolutionGenerator.convoluteStatement(argB);
	}
	
	class multByOne extends Relation{

		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Multiplication){
				Multiplication mult = (Multiplication) s.operation;
				if (mult.argA.evaluatesToConstant()){
				return (mult.argA.evaluate() == 1);
				}
				if (mult.argB.evaluatesToConstant()){
				return (mult.argB.evaluate() == 1);
				}
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Multiplication mult = (Multiplication) s.operation;
				if (mult.argA.evaluatesToConstant() && mult.argA.evaluate() == 1){
					return mult.argB;
				}else{
					return mult.argA;
				}
			}
			return s;
		}
		
	}
	
	class commutiveProperty extends Relation{

		@Override
		public boolean isRelationApplicable(Statement s) {
			return (s.operation instanceof Multiplication);
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Multiplication mult = (Multiplication)s.operation;
				s.operation = new Multiplication(mult.argB, mult.argA);
			}
			return s;
		}
		
	}
	
	class collapseConstants extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Multiplication){
				Multiplication mult = (Multiplication) s.operation;
				return mult.evaluatesToConstant();
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Multiplication mult = (Multiplication) s.operation;
				s.operation = new Constant(mult.evaluate());
			}
			return s;
		}
		
	}
	
	class distributiveProperty extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Multiplication){
				Multiplication mult = (Multiplication) s.operation;
				return ((mult.argA.operation instanceof Addition || mult.argB.operation instanceof Addition)
					&&(mult.argA.operation instanceof Addition || mult.argA.operation instanceof Constant || mult.argA.operation instanceof Variable)
					&&(mult.argB.operation instanceof Addition || mult.argB.operation instanceof Constant || mult.argB.operation instanceof Variable));
			}
			return true;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				System.out.println("Distributive property used!");
				Multiplication mult = (Multiplication) s.operation;
				if (mult.argA.operation instanceof Addition){
					Addition add = (Addition) mult.argA.operation;
					add.argA.operation = new Multiplication(mult.argB, add.argA);
					add.argB.operation = new Multiplication(mult.argB, add.argB);
					s.operation = add;
					return s;
				}else{
					Addition add = (Addition) mult.argB.operation;
					add.argA.operation = new Multiplication(mult.argA, add.argA);
					add.argB.operation = new Multiplication(mult.argA, add.argB);
					s.operation = add;
					return s;
				}
			}
			return s;
		}
		
	}
	
	class expIdentity extends Relation {
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Multiplication){
				Multiplication mult = (Multiplication) s.operation;
				return mult.argA.equalToOtherStatement(mult.argB);
			}
			return false;
		}
		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Multiplication mult = (Multiplication) s.operation;
				Statement temp = new Statement();
				temp.operation = new Constant(2);
				s.operation = new Exponentiation(mult.argA, temp);
				return s;
			}
			return s;
		}
		
	}
	
	class multExps extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Multiplication){
				Multiplication mult = (Multiplication) s.operation;
				if (mult.argA.operation instanceof Exponentiation && mult.argB.operation instanceof Exponentiation){
					Exponentiation exp1 = (Exponentiation) mult.argA.operation;
					Exponentiation exp2 = (Exponentiation) mult.argB.operation;
					return exp1.base.equalToOtherStatement(exp2.base);
				}
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				
				Multiplication mult = (Multiplication) s.operation;
				Exponentiation exp1 = (Exponentiation) mult.argA.operation;
				Exponentiation exp2 = (Exponentiation) mult.argB.operation;
				Statement temp = new Statement();
				temp.operation = new Addition(exp1.exponent, exp2.exponent);
				s.operation = new Exponentiation(exp1.base, temp);
				return s;
			}
			return s;
		}
		
	}
	
	class multOneExp extends Relation {
		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Multiplication){
				Multiplication mult = (Multiplication) s.operation;
				if (mult.argA.operation instanceof Exponentiation){
					Exponentiation exp = (Exponentiation) mult.argA.operation;
					return exp.base.equalToOtherStatement(mult.argB);
				}
				if (mult.argB.operation instanceof Exponentiation){
					Exponentiation exp = (Exponentiation) mult.argB.operation;
					return exp.base.equalToOtherStatement(mult.argA);
				}
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				
				Multiplication mult = (Multiplication) s.operation;
				Exponentiation exp;
				if (mult.argA.operation instanceof Exponentiation){
					exp = (Exponentiation) mult.argA.operation;
				}else{
					exp = (Exponentiation) mult.argB.operation;
				}
				Statement temp = new Statement();
				Statement tmp2 = new Statement();
				tmp2.operation = new Constant(1);
				temp.operation = new Addition(exp.exponent, tmp2);
				s.operation = new Exponentiation(exp.base, temp);
				return s;
			}
			return s;
		}
	}
	
	//(A) * (B * C) == (A * B) * C
	class distributiveMult extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Multiplication){
				Multiplication mult = (Multiplication) s.operation;
				return (mult.argA.operation instanceof Multiplication || mult.argB.operation instanceof Multiplication);
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Multiplication mult = (Multiplication) s.operation;
				if (mult.argA.operation instanceof Multiplication){
					Multiplication subMult = (Multiplication) mult.argA.operation;
					Statement s1 = new Statement();
					s1.operation = new Multiplication(mult.argB, subMult.argA);
					s.operation = new Multiplication(s1, subMult.argB);
					return s;
				}else{
					Multiplication subMult = (Multiplication) mult.argB.operation;
					Statement s1 = new Statement();
					s1.operation = new Multiplication(mult.argA, subMult.argA);
					s.operation = new Multiplication(s1, subMult.argB);
				}
			}
			return s;
		}
		
	}
	

}
