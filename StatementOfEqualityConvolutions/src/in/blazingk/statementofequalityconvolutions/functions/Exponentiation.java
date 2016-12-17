package in.blazingk.statementofequalityconvolutions.functions;

import in.blazingk.statementofequalityconvolutions.ConvolutionGenerator;
import in.blazingk.statementofequalityconvolutions.Statement;

public class Exponentiation extends Function {
	public Statement base, exponent;
	public Exponentiation(Statement base, Statement exponent){
		this.base = base;
		this.exponent = exponent;
		identities = new Relation[2];
		identities[0] = new Identity();
		identities[1] = new collapseExponents();
		
		
		//identities[1] = new multIdentity();

		
	}
	
	@Override
	public double evaluate() {
		return Math.pow(this.base.evaluate(), this.exponent.evaluate());
	}

	@Override
	public boolean evaluatesToConstant() {
		return base.evaluatesToConstant() && exponent.evaluatesToConstant();
	}

	@Override
	public String generateString() {
		return "("+base.operation.generateString()+" ^ "+exponent.operation.generateString()+")";
	}

	@Override
	public boolean equalToOtherStatement(Statement s) {
		if (s.operation instanceof Exponentiation){
			Exponentiation exp = (Exponentiation) s.operation;
			return exp.base.equalToOtherStatement(base) && exp.exponent.equalToOtherStatement(exponent);
		}
		return false;
	}
	
	public void convoluteChildren(){
		base.operation.convoluteChildren();
		exponent.operation.convoluteChildren();
		base = ConvolutionGenerator.convoluteStatement(base);
		exponent = ConvolutionGenerator.convoluteStatement(exponent);
	}
	
	class Identity extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			// TODO Auto-generated method stub
			return (s.operation instanceof Exponentiation);
		}

		@Override
		public Statement applyRelation(Statement s) {
			// TODO Auto-generated method stub
			return s;
		}
		
	}
	
	class multIdentity extends Relation {

		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Exponentiation){
				Exponentiation exp = (Exponentiation) s.operation;
				if (exp.exponent.evaluatesToConstant()){
					return (exp.exponent.evaluate() == Math.floor(exp.exponent.evaluate()) && exp.exponent.evaluate() > 1);
				}
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Exponentiation exp = (Exponentiation) s.operation;
				Statement finalStatement = exp.base;
				for (int i = 1; i < exp.exponent.evaluate(); i++){
					Statement st = new Statement();
					st.operation = new Multiplication(finalStatement, exp.base);
					finalStatement = st;
				}
				return finalStatement;
			}
			return s;
		}
		
	}
	
	class collapseExponents extends Relation{

		@Override
		public boolean isRelationApplicable(Statement s) {
			if (s.operation instanceof Exponentiation){
				Exponentiation exp = (Exponentiation) s.operation;
				return (exp.base.operation instanceof Exponentiation);
			}
			return false;
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Exponentiation exp = (Exponentiation) s.operation;
				Exponentiation chld = (Exponentiation) exp.base.operation;
				Statement s1 = new Statement();
				Statement temp = new Statement();
				temp.operation = new Multiplication(exp.exponent, chld.exponent);
				s1.operation = new Exponentiation(chld.base, temp);
				return s1;
			}
			return s;
		}
		
	}
	
}
