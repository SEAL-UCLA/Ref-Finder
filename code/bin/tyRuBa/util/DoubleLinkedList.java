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

import java.util.Iterator;

public class DoubleLinkedList
{
  private DoubleLinkedList.Entry head = null;
  private DoubleLinkedList.Entry tail = null;
  private int size = 0;
  
  public boolean isEmpty()
  {
    return this.size == 0;
  }
  
  public int size()
  {
    return this.size;
  }
  
  public DoubleLinkedList.Entry head()
  {
    return this.head;
  }
  
  public DoubleLinkedList.Entry tail()
  {
    return this.tail;
  }
  
  public void clear()
  {
    this.head = null;
    this.tail = null;
    this.size = 0;
  }
  
  public void enqueue(DoubleLinkedList.Entry entry)
  {
    if (this.head != null)
    {
      DoubleLinkedList.Entry.access$0(entry, null);
      DoubleLinkedList.Entry.access$1(entry, this.head);
      
      DoubleLinkedList.Entry.access$0(this.head, entry);
      this.head = entry;
    }
    else
    {
      DoubleLinkedList.Entry.access$0(entry, null);
      DoubleLinkedList.Entry.access$1(entry, null);
      
      this.head = entry;
      this.tail = this.head;
    }
    this.size += 1;
  }
  
  public void addLast(DoubleLinkedList.Entry entry)
  {
    if (this.tail != null)
    {
      DoubleLinkedList.Entry.access$0(entry, this.tail);
      DoubleLinkedList.Entry.access$1(entry, null);
      
      DoubleLinkedList.Entry.access$1(this.tail, entry);
      this.tail = entry;
    }
    else
    {
      DoubleLinkedList.Entry.access$0(entry, null);
      DoubleLinkedList.Entry.access$1(entry, null);
      
      this.tail = entry;
      this.head = this.tail;
    }
    this.size += 1;
  }
  
  public void addAll(DoubleLinkedList list)
  {
    if (list.head != null) {
      if (this.head != null)
      {
        DoubleLinkedList.Entry.access$0(this.head, list.tail);
        DoubleLinkedList.Entry.access$1(list.tail, this.head);
        this.head = list.head;
        
        this.size += list.size;
      }
      else
      {
        this.head = list.head;
        this.tail = list.tail;
        
        this.size = list.size;
      }
    }
  }
  
  public void addAfter(DoubleLinkedList list, DoubleLinkedList.Entry entry)
  {
    if (list.head != null)
    {
      if (entry != this.tail)
      {
        DoubleLinkedList.Entry.access$1(list.tail, DoubleLinkedList.Entry.access$2(entry));
        DoubleLinkedList.Entry.access$0(DoubleLinkedList.Entry.access$2(entry), list.tail);
        DoubleLinkedList.Entry.access$1(entry, list.head);
        DoubleLinkedList.Entry.access$0(list.head, entry);
      }
      else
      {
        this.tail = list.tail;
        DoubleLinkedList.Entry.access$1(entry, list.head);
        DoubleLinkedList.Entry.access$0(list.head, entry);
      }
      this.size += list.size;
    }
  }
  
  public void addAfter(DoubleLinkedList.Entry after, DoubleLinkedList.Entry entry)
  {
    if (entry != this.tail)
    {
      DoubleLinkedList.Entry.access$1(after, DoubleLinkedList.Entry.access$2(entry));
      DoubleLinkedList.Entry.access$0(DoubleLinkedList.Entry.access$2(entry), after);
      DoubleLinkedList.Entry.access$1(entry, after);
      DoubleLinkedList.Entry.access$0(after, entry);
    }
    else
    {
      DoubleLinkedList.Entry.access$1(after, null);
      this.tail = after;
      DoubleLinkedList.Entry.access$1(entry, after);
      DoubleLinkedList.Entry.access$0(after, entry);
    }
    this.size += 1;
  }
  
  public void addBefore(DoubleLinkedList list, DoubleLinkedList.Entry entry)
  {
    if (list.head != null)
    {
      if (entry != this.head)
      {
        DoubleLinkedList.Entry.access$0(list.head, DoubleLinkedList.Entry.access$3(entry));
        DoubleLinkedList.Entry.access$1(DoubleLinkedList.Entry.access$3(entry), list.head);
        DoubleLinkedList.Entry.access$0(entry, list.tail);
        DoubleLinkedList.Entry.access$1(list.tail, entry);
      }
      else
      {
        this.head = list.head;
        DoubleLinkedList.Entry.access$0(entry, list.tail);
        DoubleLinkedList.Entry.access$1(list.tail, entry);
      }
      this.size += list.size;
    }
  }
  
  public void addBefore(DoubleLinkedList.Entry before, DoubleLinkedList.Entry entry)
  {
    if (entry != this.head)
    {
      DoubleLinkedList.Entry.access$0(before, DoubleLinkedList.Entry.access$3(entry));
      DoubleLinkedList.Entry.access$1(DoubleLinkedList.Entry.access$3(entry), before);
      DoubleLinkedList.Entry.access$0(entry, before);
      DoubleLinkedList.Entry.access$1(before, entry);
    }
    else
    {
      DoubleLinkedList.Entry.access$0(before, null);
      this.head = before;
      DoubleLinkedList.Entry.access$0(entry, before);
      DoubleLinkedList.Entry.access$1(before, entry);
    }
    this.size += 1;
  }
  
  public void remove(DoubleLinkedList.Entry entry)
  {
    if (entry != this.head)
    {
      if (entry != this.tail)
      {
        DoubleLinkedList.Entry.access$1(DoubleLinkedList.Entry.access$3(entry), DoubleLinkedList.Entry.access$2(entry));
        DoubleLinkedList.Entry.access$0(DoubleLinkedList.Entry.access$2(entry), DoubleLinkedList.Entry.access$3(entry));
      }
      else
      {
        DoubleLinkedList.Entry.access$1(DoubleLinkedList.Entry.access$3(entry), null);
        this.tail = DoubleLinkedList.Entry.access$3(entry);
      }
    }
    else if (entry != this.tail)
    {
      DoubleLinkedList.Entry.access$0(DoubleLinkedList.Entry.access$2(entry), null);
      this.head = DoubleLinkedList.Entry.access$2(entry);
    }
    else
    {
      this.head = null;
      this.tail = null;
    }
    DoubleLinkedList.Entry.access$0(entry, null);
    DoubleLinkedList.Entry.access$1(entry, null);
    
    this.size -= 1;
  }
  
  public DoubleLinkedList.Entry dequeue()
  {
    DoubleLinkedList.Entry result = this.tail;
    remove(result);
    return result;
  }
  
  public DoubleLinkedList.Entry peek()
  {
    return this.tail;
  }
  
  public String toString()
  {
    String result = "DoubleLL( ";
    DoubleLinkedList.Entry current = this.head;
    while (current != null)
    {
      result = result + current + " ";
      current = DoubleLinkedList.Entry.access$2(current);
    }
    return result + ")";
  }
  
  public Iterator iterator()
  {
    return new DoubleLinkedList.1(this);
  }
}
