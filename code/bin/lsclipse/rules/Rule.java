package lsclipse.rules;

import lsclipse.RefactoringQuery;
import tyRuBa.tdbc.ResultSet;
import tyRuBa.tdbc.TyrubaException;

public abstract interface Rule
{
  public abstract String getName();
  
  public abstract String getRefactoringString();
  
  public abstract RefactoringQuery getRefactoringQuery();
  
  public abstract String checkAdherence(ResultSet paramResultSet)
    throws TyrubaException;
}


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/lsclipse/rules/Rule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */