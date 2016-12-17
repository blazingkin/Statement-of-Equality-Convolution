package in.blazingk.statementofequalityconvolutions.functions;

import in.blazingk.statementofequalityconvolutions.ConvolutionGenerator;
import in.blazingk.statementofequalityconvolutions.Statement;

public class Subtraction extends Function {

	Statement argA, argB;
	public Subtraction(Statement a, Statement b){
		identities = new Relation[3];
		identities[0] = new thingMinusItself();
		identities[1] = new thingMinusZero();
		identities[2] = new changeToAddition();
		this.argA = a;
		this.argB = b;
	}
	
	
	@Override
	public double evaluate() {
		if (argA.equalToOtherStatement(argB)){
			return 0;
		}
		return argA.evaluate() - argB.evaluate();
	}

	@Override
	public boolean evaluatesToConstant() {
		// TODO Auto-generated method stub
		return (argA.equalToOtherStatement(argB)) || (argA.evaluatesToConstant() && argB.evaluatesToConstant());
	}

	@Override
	public String generateString() {
		// TODO Auto-generated method stub
		return "("+argA.operation.generateString()+" - "+argB.operation.generateString()+")";
	}

	@Override
	public boolean equalToOtherStatement(Statement s) {
		if (s.operation instanceof Subtraction){
			Subtraction s2 = (Subtraction) s.operation;
			return (s2.argA.equalToOtherStatement(argA) && s2.argB.equalToOtherStatement(argB));
		}
		return false;
	}

	@Override
	public void convoluteChildren() {
		argA = ConvolutionGenerator.convoluteStatement(argA);
		argB = ConvolutionGenerator.convoluteStatement(argB);
	}
	
	class thingMinusItself extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Subtraction){
				Subtraction sub = (Subtraction) s.operation;
				return (sub.argA.equalToOtherStatement(sub.argB));
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				s.operation = new Constant(0);
				return s;
			}
			return s;
		}
		
	}
	
	class thingMinusZero extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Subtraction){
				Subtraction sub = (Subtraction) s.operation;
				return (sub.argB.evaluatesToConstant() && sub.argB.evaluate() == 0);
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Subtraction sub = (Subtraction) s.operation;
				return sub.argA;
			}
			return s;
		}
		
	}
	
	class changeToAddition extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			return s.operation instanceof Subtraction;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Subtraction sub = (Subtraction) s.operation;
				Statement mul = new Statement();
				Statement negOne = new Statement();
				negOne.operation = new Constant(-1);
				mul.operation = new Multiplication(negOne, sub.argB);
				s.operation = new Addition(sub.argA, mul);
			}
			return s;
		}
		
	}

}
