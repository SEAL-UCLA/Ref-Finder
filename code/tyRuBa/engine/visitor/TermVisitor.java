package tyRuBa.engine.visitor;

import tyRuBa.engine.RBCompoundTerm;
import tyRuBa.engine.RBIgnoredVariable;
import tyRuBa.engine.RBPair;
import tyRuBa.engine.RBQuoted;
import tyRuBa.engine.RBTemplateVar;
import tyRuBa.engine.RBTuple;
import tyRuBa.engine.RBVariable;

public abstract interface TermVisitor
{
  public abstract Object visit(RBCompoundTerm paramRBCompoundTerm);
  
  public abstract Object visit(RBIgnoredVariable paramRBIgnoredVariable);
  
  public abstract Object visit(RBPair paramRBPair);
  
  public abstract Object visit(RBQuoted paramRBQuoted);
  
  public abstract Object visit(RBTuple paramRBTuple);
  
  public abstract Object visit(RBVariable paramRBVariable);
  
  public abstract Object visit(RBTemplateVar paramRBTemplateVar);
}


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/engine/visitor/TermVisitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */