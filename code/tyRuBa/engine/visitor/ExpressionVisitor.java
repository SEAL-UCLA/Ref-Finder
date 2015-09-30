package tyRuBa.engine.visitor;

import tyRuBa.engine.RBConjunction;
import tyRuBa.engine.RBCountAll;
import tyRuBa.engine.RBDisjunction;
import tyRuBa.engine.RBExistsQuantifier;
import tyRuBa.engine.RBFindAll;
import tyRuBa.engine.RBModeSwitchExpression;
import tyRuBa.engine.RBNotFilter;
import tyRuBa.engine.RBPredicateExpression;
import tyRuBa.engine.RBTestFilter;
import tyRuBa.engine.RBUniqueQuantifier;

public abstract interface ExpressionVisitor
{
  public abstract Object visit(RBConjunction paramRBConjunction);
  
  public abstract Object visit(RBDisjunction paramRBDisjunction);
  
  public abstract Object visit(RBExistsQuantifier paramRBExistsQuantifier);
  
  public abstract Object visit(RBFindAll paramRBFindAll);
  
  public abstract Object visit(RBCountAll paramRBCountAll);
  
  public abstract Object visit(RBModeSwitchExpression paramRBModeSwitchExpression);
  
  public abstract Object visit(RBNotFilter paramRBNotFilter);
  
  public abstract Object visit(RBPredicateExpression paramRBPredicateExpression);
  
  public abstract Object visit(RBTestFilter paramRBTestFilter);
  
  public abstract Object visit(RBUniqueQuantifier paramRBUniqueQuantifier);
}


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/visitor/ExpressionVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */