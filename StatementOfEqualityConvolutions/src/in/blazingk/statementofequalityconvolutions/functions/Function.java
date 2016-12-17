package in.blazingk.statementofequalityconvolutions.functions;

import in.blazingk.statementofequalityconvolutions.Statement;

public abstract class Function {
	public Relation identities[];
	public abstract double evaluate();
	public abstract boolean evaluatesToConstant();
	public abstract String generateString();
	public abstract boolean equalToOtherStatement(Statement s);
	public abstract void convoluteChildren();
}
