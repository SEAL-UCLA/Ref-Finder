package tyRuBa.util;

import tyRuBa.engine.RBExpression;

public abstract class QueryLogger
{
  public abstract void close();
  
  public abstract void logQuery(RBExpression paramRBExpression);
}


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/tyRuBa/util/QueryLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */