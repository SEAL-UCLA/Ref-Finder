package serp.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public abstract interface Pool
  extends Set
{
  public abstract int getMaxPool();
  
  public abstract void setMaxPool(int paramInt);
  
  public abstract int getMinPool();
  
  public abstract void setMinPool(int paramInt);
  
  public abstract int getWait();
  
  public abstract void setWait(int paramInt);
  
  public abstract int getAutoReturn();
  
  public abstract void setAutoReturn(int paramInt);
  
  public abstract Iterator iterator();
  
  public abstract Object get();
  
  public abstract Object get(Object paramObject);
  
  public abstract Object get(Object paramObject, Comparator paramComparator);
  
  public abstract Set takenSet();
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}


/* Location:              /Users/UCLAPLSE/Downloads/LSclipse_1.0.4.jar!/serp/util/Pool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */