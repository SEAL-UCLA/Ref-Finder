package tyRuBa.util;

public abstract interface Statistics
{
  public abstract void stopGathering();
  
  public abstract void startGathering();
  
  public abstract void reset();
  
  public abstract int getIntStat(String paramString);
  
  public abstract long getLongStat(String paramString);
  
  public abstract float getFloatStat(String paramString);
  
  public abstract Object getObjectStat(String paramString);
}


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/util/Statistics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */