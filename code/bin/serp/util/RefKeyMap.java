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

abstract class RefKeyMap
  implements RefMap
{
  private Map _map = null;
  private ReferenceQueue _queue = new ReferenceQueue();
  private boolean _identity = false;
  
  public RefKeyMap()
  {
    this(new HashMap());
  }
  
  public RefKeyMap(Map map)
  {
    if ((map instanceof IdentityMap))
    {
      this._identity = true;
      this._map = new HashMap();
    }
    else
    {
      this._map = map;
      this._map.clear();
    }
  }
  
  public boolean makeHard(Object key)
  {
    removeExpired();
    if (!containsKey(key)) {
      return false;
    }
    Object value = remove(key);
    this._map.put(key, value);
    return true;
  }
  
  public boolean makeReference(Object key)
  {
    removeExpired();
    if (key == null) {
      return false;
    }
    if (!containsKey(key)) {
      return false;
    }
    Object value = remove(key);
    put(key, value);
    return true;
  }
  
  public void clear()
  {
    this._map.clear();
  }
  
  public boolean containsKey(Object key)
  {
    if (key == null) {
      return this._map.containsKey(null);
    }
    return this._map.containsKey(createRefMapKey(key, null, this._identity));
  }
  
  public boolean containsValue(Object value)
  {
    return this._map.containsValue(value);
  }
  
  public Set entrySet()
  {
    return new RefKeyMap.EntrySet(this, null);
  }
  
  public boolean equals(Object other)
  {
    return this._map.equals(other);
  }
  
  public Object get(Object key)
  {
    if (key == null) {
      return this._map.get(null);
    }
    return this._map.get(createRefMapKey(key, null, this._identity));
  }
  
  public boolean isEmpty()
  {
    return this._map.isEmpty();
  }
  
  public Set keySet()
  {
    return new RefKeyMap.KeySet(this, null);
  }
  
  public Object put(Object key, Object value)
  {
    removeExpired();
    return putFilter(key, value);
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
    if (key == null) {
      return this._map.put(null, value);
    }
    key = createRefMapKey(key, this._queue, this._identity);
    Object ret = this._map.remove(key);
    this._map.put(key, value);
    return ret;
  }
  
  public Object remove(Object key)
  {
    removeExpired();
    if (key == null) {
      return this._map.remove(null);
    }
    return this._map.remove(createRefMapKey(key, null, this._identity));
  }
  
  public int size()
  {
    return this._map.size();
  }
  
  public Collection values()
  {
    return new RefKeyMap.ValueCollection(this, null);
  }
  
  public String toString()
  {
    return this._map.toString();
  }
  
  protected abstract RefKeyMap.RefMapKey createRefMapKey(Object paramObject, ReferenceQueue paramReferenceQueue, boolean paramBoolean);
  
  private void removeExpired()
  {
    Object key;
    while ((key = this._queue.poll()) != null)
    {
      Object key;
      try
      {
        this._queue.remove(1L);
      }
      catch (InterruptedException localInterruptedException) {}
      this._map.remove(key);
    }
  }
}
