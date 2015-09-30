package tyRuBa.modes;

import tyRuBa.engine.PredicateIdentifier;

public abstract interface PredInfoProvider
{
  public abstract PredInfo getPredInfo(PredicateIdentifier paramPredicateIdentifier)
    throws TypeModeError;
  
  public abstract PredInfo maybeGetPredInfo(PredicateIdentifier paramPredicateIdentifier);
}


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/modes/PredInfoProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */