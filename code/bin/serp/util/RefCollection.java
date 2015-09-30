package serp.util;

import java.util.Collection;

public abstract interface RefCollection
  extends Collection
{
  public abstract boolean makeHard(Object paramObject);
  
  public abstract boolean makeReference(Object paramObject);
}


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/bin/serp/util/RefCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */