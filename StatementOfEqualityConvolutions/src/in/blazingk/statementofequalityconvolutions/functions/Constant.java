package in.blazingk.statementofequalityconvolutions.functions;

import in.blazingk.statementofequalityconvolutions.ConvolutionGenerator;
import in.blazingk.statementofequalityconvolutions.Statement;

public class Constant extends Function {

	public double value;
	public Constant(double val){
		value = val;
		this.identities = new Relation[1];
		identities[0] = new Identity();
	//	identities[1] = new AdditiveIdentity();
	//	identities[2] = new MultaplicativeIdentity();
	}
	
	public double evaluate() {
		return value;
	}
	
	public boolean evaluatesToConstant(){
		return true;
	}

	public String generateString() {
		if (value == Math.floor(value)){
			return ((int)value)+"";
		}
		return value+"";
	}
	public void convoluteChildren(){
		
	}
	
	@Override
	public boolean equalToOtherStatement(Statement s) {
		if (s.operation instanceof Constant){
			Constant con = (Constant) s.operation;
			return value == con.value;
		}
		return false;
	}
	
	class Identity extends Relation{

		@Override
		public boolean isRelationApplicable(Statement s) {
			return (s.operation instanceof Constant);
		}

		@Override
		public Statement applyRelation(Statement s) {
			return s;
		}
		
		
	}
	
	class AdditiveIdentity extends Relation{

		@Override
		public boolean isRelationApplicable(Statement s) {
			// TODO Auto-generated method stub
			return (s.operation instanceof Constant);
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Constant con = (Constant) s.operation;
				double val = ConvolutionGenerator.rand.nextDouble() * con.value;
				Statement v1 = new Statement();
				Statement v2 = new Statement();
				v1.operation = new Constant(con.value - val);
				v2.operation = new Constant(val);
				s.operation = new Addition(v1, v2);
				return s;
			}
			return s;
		}
		
	}
	
	class MultaplicativeIdentity extends Relation{

		@Override
		public boolean isRelationApplicable(Statement s) {
			// TODO Auto-generated method stub
			return (s.operation instanceof Constant);
		}

		@Override
		public Statement applyRelation(Statement s) {
			if (isRelationApplicable(s)){
				Constant con = (Constant) s.operation;
				if (!(con.value > 1)){
					return s;
				}
				double val = ConvolutionGenerator.rand.nextInt((int)con.value);
				if (val == 0){
					return s;
				}
				Statement v1 = new Statement();
				Statement v2 = new Statement();
				v1.operation = new Constant(con.value / val);
				v2.operation = new Constant(val);
				s.operation = new Multiplication(v1, v2);
				return s;
			}
			return s;
		}
		
	}

}
