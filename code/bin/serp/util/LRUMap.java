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

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class LRUMap
  implements SortedMap
{
  private Map _orders = new HashMap();
  private TreeMap _values = new TreeMap();
  private int _order = Integer.MAX_VALUE;
  
  public Comparator comparator()
  {
    return null;
  }
  
  public Object firstKey()
  {
    return ((LRUMap.OrderKey)this._values.firstKey()).key;
  }
  
  public Object lastKey()
  {
    return ((LRUMap.OrderKey)this._values.lastKey()).key;
  }
  
  public SortedMap headMap(Object toKey)
  {
    throw new UnsupportedOperationException();
  }
  
  public SortedMap subMap(Object fromKey, Object toKey)
  {
    throw new UnsupportedOperationException();
  }
  
  public SortedMap tailMap(Object fromKey)
  {
    throw new UnsupportedOperationException();
  }
  
  public void clear()
  {
    this._orders.clear();
    this._values.clear();
  }
  
  public boolean containsKey(Object key)
  {
    return this._orders.containsKey(key);
  }
  
  public boolean containsValue(Object value)
  {
    return this._values.containsValue(value);
  }
  
  public Set entrySet()
  {
    return new LRUMap.EntrySet(this, null);
  }
  
  public boolean equals(Object other)
  {
    if (other == this) {
      return true;
    }
    if (!(other instanceof Map)) {
      return false;
    }
    return new HashMap(this).equals(other);
  }
  
  public Object get(Object key)
  {
    Object order = this._orders.remove(key);
    if (order == null) {
      return null;
    }
    Object value = this._values.remove(order);
    order = nextOrderKey(key);
    this._orders.put(key, order);
    this._values.put(order, value);
    
    return value;
  }
  
  public boolean isEmpty()
  {
    return this._orders.isEmpty();
  }
  
  public Set keySet()
  {
    return new LRUMap.KeySet(this, null);
  }
  
  public Object put(Object key, Object value)
  {
    Object order = nextOrderKey(key);
    Object oldOrder = this._orders.put(key, order);
    
    Object rem = null;
    if (oldOrder != null) {
      rem = this._values.remove(oldOrder);
    }
    this._values.put(order, value);
    return rem;
  }
  
  public void putAll(Map map)
  {
    for (Iterator itr = map.entrySet().iterator(); itr.hasNext();)
    {
      Map.Entry entry = (Map.Entry)itr.next();
      put(entry.getKey(), entry.getValue());
    }
  }
  
  public Object remove(Object key)
  {
    Object order = this._orders.remove(key);
    if (order != null) {
      return this._values.remove(order);
    }
    return null;
  }
  
  public int size()
  {
    return this._orders.size();
  }
  
  public Collection values()
  {
    return new LRUMap.ValueCollection(this, null);
  }
  
  public String toString()
  {
    return entrySet().toString();
  }
  
  private synchronized LRUMap.OrderKey nextOrderKey(Object key)
  {
    LRUMap.OrderKey ok = new LRUMap.OrderKey(null);
    ok.key = key;
    ok.order = (this._order--);
    return ok;
  }
}
