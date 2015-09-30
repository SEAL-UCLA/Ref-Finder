package tyRuBa.engine.factbase;

import tyRuBa.engine.Validator;

public abstract interface ValidatorManager
{
  public abstract void add(Validator paramValidator, String paramString);
  
  public abstract void update(long paramLong, Boolean paramBoolean1, Boolean paramBoolean2);
  
  public abstract void remove(long paramLong);
  
  public abstract void remove(String paramString);
  
  public abstract Validator get(long paramLong);
  
  public abstract Validator get(String paramString);
  
  public abstract String getIdentifier(long paramLong);
  
  public abstract void printOutValidators();
  
  public abstract void backup();
  
  public abstract long getLastInvalidatedTime();
}


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/factbase/ValidatorManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */