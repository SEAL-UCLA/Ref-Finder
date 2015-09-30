package serp.util;

import java.util.Map;

public abstract interface RefMap
  extends Map
{
  public abstract boolean makeHard(Object paramObject);
  
  public abstract boolean makeReference(Object paramObject);
}


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/RefMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */