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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class ElementSource
{
  public static final int ELEMENT_READY = 1;
  public static final int NO_ELEMENTS_READY = 0;
  public static final int NO_MORE_ELEMENTS = -1;
  
  public boolean isEmpty()
  {
    return false;
  }
  
  public abstract void print(PrintingState paramPrintingState);
  
  public abstract int status();
  
  public abstract Object nextElement();
  
  public boolean hasMoreElements()
  {
    return status() == 1;
  }
  
  public static ElementSource singleton(Object e)
  {
    return new ElementSource.1(e);
  }
  
  public static final ElementSource theEmpty = EmptySource.the;
  
  public ElementSource append(ElementSource other)
  {
    if (other.isEmpty()) {
      return this;
    }
    return new AppendSource(this, other);
  }
  
  public ElementSource map(Action what)
  {
    return new MapElementSource(this, what);
  }
  
  public static ElementSource with(Object[] els)
  {
    return new ElementSource.2(els);
  }
  
  public static ElementSource with(ArrayList els)
  {
    if (els.isEmpty()) {
      return theEmpty;
    }
    return new ArrayListSource(els);
  }
  
  public static ElementSource with(Iterator it)
  {
    return new ElementSource.3(it);
  }
  
  public void forceAll()
  {
    while (hasMoreElements()) {
      nextElement();
    }
  }
  
  public String toString()
  {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    print(new PrintingState(new PrintStream(result)));
    return result.toString();
  }
  
  public ElementSource first()
  {
    return new First(this);
  }
  
  public ElementSource immediateFirst()
  {
    int stat = status();
    if (stat == 1) {
      return singleton(nextElement());
    }
    if (stat == -1) {
      return theEmpty;
    }
    return first();
  }
  
  public ElementSource flatten()
  {
    return new FlattenElementSource(this);
  }
  
  public SynchronizedElementSource synchronizeOn(SynchResource resource)
  {
    return new SynchronizedElementSource(resource, this);
  }
  
  public int countElements()
  {
    int result = 0;
    while (hasMoreElements())
    {
      nextElement();
      result++;
    }
    return result;
  }
  
  public void release() {}
  
  public Object firstElementOrNull()
  {
    if (hasMoreElements())
    {
      Object result = nextElement();
      release();
      return result;
    }
    return null;
  }
}
