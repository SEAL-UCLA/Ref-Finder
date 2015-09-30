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
package tyRuBa.modes;

import tyRuBa.engine.RBComponent;
import tyRuBa.engine.RBExpression;
import tyRuBa.engine.RBTerm;

public class TypeModeError
  extends Exception
{
  public TypeModeError(String arg0)
  {
    super(arg0);
  }
  
  public TypeModeError(TypeModeError e, String str)
  {
    this(e.getMessage() + "\nin " + str);
  }
  
  public TypeModeError(TypeModeError e, RBComponent r)
  {
    this(e, r.toString());
  }
  
  public TypeModeError(TypeModeError e, RBTerm t)
  {
    this(e, t.toString());
  }
  
  public TypeModeError(TypeModeError e, RBExpression exp)
  {
    this(e, exp.toString());
  }
  
  public TypeModeError(TypeModeError e, Type t)
  {
    this(e, "Type: " + t.toString());
  }
  
  public TypeModeError(TypeModeError e, TupleType types)
  {
    this(e, types.toString());
  }
}
