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
package serp.util;

import java.lang.ref.ReferenceQueue;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

abstract class RefValueMap
  implements RefMap
{
  private Map _map = null;
  private ReferenceQueue _queue = new ReferenceQueue();
  
  public RefValueMap()
  {
    this(new HashMap());
  }
  
  public RefValueMap(Map map)
  {
    this._map = map;
    this._map.clear();
  }
  
  public boolean makeHard(Object key)
  {
    removeExpired();
    if (!containsKey(key)) {
      return false;
    }
    Object value = this._map.get(key);
    if ((value instanceof RefValueMap.RefMapValue))
    {
      RefValueMap.RefMapValue ref = (RefValueMap.RefMapValue)value;
      value = ref.getValue();
      if (value == null) {
        return false;
      }
      ref.invalidate();
      this._map.put(key, value);
    }
    return true;
  }
  
  public boolean makeReference(Object key)
  {
    removeExpired();
    
    Object value = this._map.get(key);
    if (value == null) {
      return false;
    }
    if (!(value instanceof RefValueMap.RefMapValue)) {
      put(key, value);
    }
    return true;
  }
  
  public void clear()
  {
    Collection values = this._map.values();
    for (Iterator itr = values.iterator(); itr.hasNext();)
    {
      Object value = itr.next();
      if ((value instanceof RefValueMap.RefMapValue)) {
        ((RefValueMap.RefMapValue)value).invalidate();
      }
      itr.remove();
    }
  }
  
  public boolean containsKey(Object key)
  {
    return this._map.containsKey(key);
  }
  
  public boolean containsValue(Object value)
  {
    return values().contains(value);
  }
  
  public Set entrySet()
  {
    return new RefValueMap.EntrySet(this, null);
  }
  
  public boolean equals(Object other)
  {
    return this._map.equals(other);
  }
  
  public Object get(Object key)
  {
    Object value = this._map.get(key);
    if (!(value instanceof RefValueMap.RefMapValue)) {
      return value;
    }
    return ((RefValueMap.RefMapValue)value).getValue();
  }
  
  public boolean isEmpty()
  {
    return this._map.isEmpty();
  }
  
  public Set keySet()
  {
    return new RefValueMap.KeySet(this, null);
  }
  
  public Object put(Object key, Object value)
  {
    removeExpired();
    
    Object replaced = putFilter(key, value);
    if (!(replaced instanceof RefValueMap.RefMapValue)) {
      return replaced;
    }
    return ((RefValueMap.RefMapValue)replaced).getValue();
  }
  
  public void putAll(Map map)
  {
    removeExpired();
    for (Iterator itr = map.entrySet().iterator(); itr.hasNext();)
    {
      Map.Entry entry = (Map.Entry)itr.next();
      putFilter(entry.getKey(), entry.getValue());
    }
  }
  
  private Object putFilter(Object key, Object value)
  {
    Object replaced;
    Object replaced;
    if (value == null) {
      replaced = this._map.put(key, null);
    } else {
      replaced = this._map.put(key, createRefMapValue(key, value, this._queue));
    }
    if ((replaced instanceof RefValueMap.RefMapValue)) {
      ((RefValueMap.RefMapValue)replaced).invalidate();
    }
    return replaced;
  }
  
  public Object remove(Object key)
  {
    removeExpired();
    
    Object value = this._map.remove(key);
    if (!(value instanceof RefValueMap.RefMapValue)) {
      return value;
    }
    RefValueMap.RefMapValue ref = (RefValueMap.RefMapValue)value;
    ref.invalidate();
    return ref.getValue();
  }
  
  public int size()
  {
    return this._map.size();
  }
  
  public Collection values()
  {
    return new RefValueMap.ValueCollection(this, null);
  }
  
  public String toString()
  {
    return this._map.toString();
  }
  
  protected abstract RefValueMap.RefMapValue createRefMapValue(Object paramObject1, Object paramObject2, ReferenceQueue paramReferenceQueue);
  
  private void removeExpired()
  {
    RefValueMap.RefMapValue ref;
    while ((ref = (RefValueMap.RefMapValue)this._queue.poll()) != null)
    {
      RefValueMap.RefMapValue ref;
      try
      {
        this._queue.remove(1L);
      }
      catch (InterruptedException localInterruptedException) {}
      if (ref.isValid()) {
        this._map.remove(ref.getKey());
      }
    }
  }
}
