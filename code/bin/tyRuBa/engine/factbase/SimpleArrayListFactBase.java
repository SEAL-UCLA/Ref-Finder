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
package tyRuBa.engine.factbase;

import java.util.ArrayList;
import tyRuBa.engine.RBComponent;
import tyRuBa.engine.compilation.CompilationContext;
import tyRuBa.engine.compilation.Compiled;
import tyRuBa.modes.Mode;
import tyRuBa.modes.Multiplicity;
import tyRuBa.modes.PredInfo;
import tyRuBa.modes.PredicateMode;

public class SimpleArrayListFactBase
  extends FactBase
{
  ArrayList facts = new ArrayList();
  
  public SimpleArrayListFactBase(PredInfo info) {}
  
  public boolean isEmpty()
  {
    return this.facts.isEmpty();
  }
  
  public boolean isPersistent()
  {
    return false;
  }
  
  public void insert(RBComponent f)
  {
    this.facts.add(f);
  }
  
  public Compiled basicCompile(PredicateMode mode, CompilationContext context)
  {
    if (mode.getMode().hi.compareTo(Multiplicity.one) <= 0) {
      return new SimpleArrayListFactBase.1(this, mode.getMode());
    }
    return new SimpleArrayListFactBase.2(this, mode.getMode());
  }
  
  public void backup() {}
}
