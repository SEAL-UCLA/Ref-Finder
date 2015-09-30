package tyRuBa.engine;

public abstract interface ProgressMonitor
{
  public abstract void beginTask(String paramString, int paramInt);
  
  public abstract void worked(int paramInt);
  
  public abstract void done();
}


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/tyRuBa/engine/ProgressMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */