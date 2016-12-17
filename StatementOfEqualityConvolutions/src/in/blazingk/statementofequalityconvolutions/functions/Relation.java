package in.blazingk.statementofequalityconvolutions.functions;

import in.blazingk.statementofequalityconvolutions.Statement;

public abstract class Relation {
	//Does this relation work on this statement?
	public abstract boolean isRelationApplicable(Statement s);
	
	//Apply this relation to the statement
	public abstract Statement applyRelation(Statement s);
}
