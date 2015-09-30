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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class IdentityMap
  extends HashMap
{
  public IdentityMap() {}
  
  public IdentityMap(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public IdentityMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }
  
  public IdentityMap(Map map)
  {
    putAll(map);
  }
  
  public Object clone()
  {
    return new IdentityMap(this);
  }
  
  public boolean containsKey(Object key)
  {
    return super.containsKey(createKey(key));
  }
  
  public Set entrySet()
  {
    return new IdentityMap.EntrySet(this, super.entrySet());
  }
  
  public Object get(Object key)
  {
    return super.get(createKey(key));
  }
  
  public Set keySet()
  {
    return new IdentityMap.KeySet(this, super.keySet());
  }
  
  public Object put(Object key, Object value)
  {
    return super.put(createKey(key), value);
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
    return super.remove(createKey(key));
  }
  
  private static Object createKey(Object key)
  {
    if (key == null) {
      return key;
    }
    return new IdentityMap.IdentityKey(key);
  }
}
