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

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class OneMap
  implements Map, Serializable
{
  private Object key = null;
  private Object value = null;
  
  public void clear()
  {
    this.key = null;
    this.value = null;
  }
  
  public int size()
  {
    return (this.key == null) && (this.value == null) ? 0 : 1;
  }
  
  public boolean isEmpty()
  {
    return size() == 0;
  }
  
  public boolean containsKey(Object key)
  {
    return ((key == null) && (this.key == null)) || ((key != null) && (key.equals(this.key)));
  }
  
  public boolean containsValue(Object value)
  {
    return ((value == null) && (this.value == null)) || ((value != null) && (value.equals(this.value)));
  }
  
  public Object get(Object key)
  {
    return ((key == null) && (this.key == null)) || (
      (key != null) && (key.equals(this.key))) ? this.value : null;
  }
  
  public Object put(Object key, Object value)
  {
    Object previous = get(key);
    
    this.key = key;
    this.value = value;
    
    return previous;
  }
  
  public Object remove(Object key)
  {
    Object previous = get(key);
    if (containsKey(key))
    {
      this.key = null;
      this.value = null;
    }
    return previous;
  }
  
  public void putAll(Map t)
  {
    Iterator m = t.entrySet().iterator();
    if (m.hasNext())
    {
      Map.Entry e = (Map.Entry)m.next();
      put(e.getKey(), e.getValue());
    }
  }
  
  public Set keySet()
  {
    return new OneMap.KeySet(this, null);
  }
  
  public Set entrySet()
  {
    return new OneMap.EntrySet(this);
  }
  
  public Collection values()
  {
    return new OneMap.Values(this, null);
  }
}
