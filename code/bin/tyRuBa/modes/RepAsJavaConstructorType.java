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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import tyRuBa.engine.FunctorIdentifier;
import tyRuBa.engine.RBCompoundTerm;
import tyRuBa.engine.RBJavaObjectCompoundTerm;
import tyRuBa.engine.RBTerm;

public class RepAsJavaConstructorType
  extends ConstructorType
  implements Serializable
{
  FunctorIdentifier functorId;
  CompositeType result;
  Type repAsType;
  
  public RepAsJavaConstructorType(FunctorIdentifier functorId, Type repAs, CompositeType result)
  {
    this.functorId = functorId;
    this.result = result;
    this.repAsType = repAs;
  }
  
  public FunctorIdentifier getFunctorId()
  {
    return this.functorId;
  }
  
  public TypeConstructor getTypeConst()
  {
    return this.result.getTypeConstructor();
  }
  
  public int getArity()
  {
    return 1;
  }
  
  public RBTerm apply(RBTerm term)
  {
    if ((term instanceof RBJavaObjectCompoundTerm)) {
      return RBCompoundTerm.makeRepAsJava(this, ((RBJavaObjectCompoundTerm)term).getObject());
    }
    return RBCompoundTerm.make(this, term);
  }
  
  public RBTerm apply(ArrayList terms)
  {
    throw new Error("RepAsJava Constructors can only be applied a single term");
  }
  
  public Type apply(Type argType)
    throws TypeModeError
  {
    Map renamings = new HashMap();
    Type iargs = this.repAsType.clone(renamings);
    CompositeType iresult = (CompositeType)this.result.clone(renamings);
    argType.checkEqualTypes(iargs);
    return iresult.getTypeConstructor().apply(iresult.getArgs(), true);
  }
  
  public boolean equals(Object other)
  {
    if (other.getClass() != getClass()) {
      return false;
    }
    RepAsJavaConstructorType ctOther = (RepAsJavaConstructorType)other;
    return (this.repAsType.equals(ctOther.repAsType)) && (this.functorId.equals(ctOther.functorId)) && (this.result.equals(ctOther.result));
  }
  
  public int hashCode()
  {
    return this.repAsType.hashCode() + 13 * this.functorId.hashCode() + 31 * this.result.hashCode();
  }
}
