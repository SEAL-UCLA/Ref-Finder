/* 
*    Ref-Finder
*    Copyright (C) <2015>  <PLSE_UCLA>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package tyRuBa.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class CountedHashMap
  implements Serializable
{
  private static final int DEFAULT_INITIAL_SIZE = 32;
  private static final int MAX_CONCURRENCY = 32;
  private static final double DEFAULT_LOAD_FACTOR = 0.75D;
  private transient Object[] mutex = new Object[32];
  private CountedHashMap.Entry[] table;
  private int size;
  private double loadFactor;
  private int threshold;
  
  private void rehash(boolean grow)
  {
    rehash(0, grow);
  }
  
  private void rehash(int obtained, boolean grow)
  {
    if (obtained == 32)
    {
      CountedHashMap.Entry[] ntable = grow ? new CountedHashMap.Entry[this.table.length * 2] : new CountedHashMap.Entry[this.table.length / 2];
      int location = 0;
      while (location < this.table.length)
      {
        CountedHashMap.Entry bucket = this.table[location];
        while (bucket != null)
        {
          CountedHashMap.Entry next = bucket.next;
          
          int nlocation = (bucket.key % ntable.length + ntable.length) % ntable.length;
          
          bucket.next = ntable[nlocation];
          ntable[nlocation] = bucket;
          
          bucket = next;
        }
        location++;
      }
      this.table = ntable;
      
      this.threshold = ((int)(this.table.length * this.loadFactor));
    }
    else
    {
      synchronized (this.mutex[(obtained++)])
      {
        rehash(obtained, grow);
      }
    }
  }
  
  public CountedHashMap()
  {
    this(32, 0.75D);
  }
  
  public CountedHashMap(int initialSize)
  {
    this(initialSize, 0.75D);
  }
  
  public CountedHashMap(double loadFactor)
  {
    this(32, loadFactor);
  }
  
  public CountedHashMap(int initialSize, double loadFactor)
  {
    int i = 32;
    while (i-- != 0) {
      this.mutex[i] = new Object();
    }
    this.table = new CountedHashMap.Entry[initialSize];
    this.size = 0;
    this.loadFactor = loadFactor;
    this.threshold = ((int)(this.table.length * loadFactor));
  }
  
  public boolean containsKey(int key)
  {
    int length = this.table.length;
    int location = (key % length + length) % length;
    synchronized (this.mutex[(location % 32)])
    {
      if (length == this.table.length)
      {
        CountedHashMap.Entry bucket = this.table[location];
        while (bucket != null)
        {
          if (key == bucket.key) {
            return true;
          }
          bucket = bucket.next;
        }
        return false;
      }
    }
    return containsKey(key);
  }
  
  public boolean containsValue(Object value)
  {
    throw new UnsupportedOperationException();
  }
  
  public Object get(int key)
  {
    int length = this.table.length;
    int location = (key % length + length) % length;
    synchronized (this.mutex[(location % 32)])
    {
      if (length == this.table.length)
      {
        CountedHashMap.Entry bucket = this.table[location];
        while (bucket != null)
        {
          if (key == bucket.key) {
            return bucket.value;
          }
          bucket = bucket.next;
        }
        return null;
      }
    }
    return get(key);
  }
  
  public boolean isEmpty()
  {
    return this.size == 0;
  }
  
  public Object put(int key, Object value)
  {
    int length = this.table.length;
    int location = (key % length + length) % length;
    synchronized (this.mutex[(location % 32)])
    {
      if (length == this.table.length)
      {
        CountedHashMap.Entry bucket = this.table[location];
        while (bucket != null)
        {
          if (key == bucket.key)
          {
            Object previous = bucket.value;
            bucket.value = value;
            bucket.preIncrementCount();
            return previous;
          }
          bucket = bucket.next;
        }
        this.table[location] = new CountedHashMap.Entry(key, value, this.table[location]);
        synchronized (this.mutex)
        {
          if (++this.size > this.threshold) {
            rehash(true);
          }
        }
        return null;
      }
    }
    return put(key, value);
  }
  
  public Object remove(int key)
  {
    int length = this.table.length;
    int location = (key % length + length) % length;
    synchronized (this.mutex[(location % 32)])
    {
      if (length == this.table.length)
      {
        CountedHashMap.Entry previous = null;
        CountedHashMap.Entry bucket = this.table[location];
        while (bucket != null)
        {
          if (key == bucket.key)
          {
            if (bucket.preDecrementCount() == 0)
            {
              if (previous == null) {
                this.table[location] = bucket.next;
              } else {
                previous.next = bucket.next;
              }
              synchronized (this.mutex)
              {
                if (--this.size < this.threshold / 3) {
                  rehash(false);
                }
              }
            }
            return bucket.value;
          }
          previous = bucket;
          bucket = bucket.next;
        }
        return null;
      }
    }
    return remove(key);
  }
  
  public boolean setFlags(int key, int flags)
  {
    int length = this.table.length;
    int location = (key % length + length) % length;
    synchronized (this.mutex[(location % 32)])
    {
      if (length == this.table.length)
      {
        CountedHashMap.Entry bucket = this.table[location];
        while (bucket != null)
        {
          if (key == bucket.key)
          {
            bucket.setFlags(flags);
            return true;
          }
          bucket = bucket.next;
        }
        return false;
      }
    }
    return setFlags(key, flags);
  }
  
  public int getFlags(int key)
  {
    int length = this.table.length;
    int location = (key % length + length) % length;
    synchronized (this.mutex[(location % 32)])
    {
      if (length == this.table.length)
      {
        CountedHashMap.Entry bucket = this.table[location];
        while (bucket != null)
        {
          if (key == bucket.key) {
            return bucket.getFlags();
          }
          bucket = bucket.next;
        }
        return 0;
      }
    }
    return getFlags(key);
  }
  
  public int size()
  {
    return this.size;
  }
  
  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    
    this.mutex = new Object[32];
    int i = 32;
    while (i-- != 0) {
      this.mutex[i] = new Object();
    }
  }
}
