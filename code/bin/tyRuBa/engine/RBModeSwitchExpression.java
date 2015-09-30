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
package tyRuBa.engine;

import java.util.Collection;
import java.util.Vector;
import tyRuBa.engine.compilation.CompilationContext;
import tyRuBa.engine.compilation.Compiled;
import tyRuBa.engine.visitor.ExpressionVisitor;
import tyRuBa.modes.ErrorMode;
import tyRuBa.modes.Factory;
import tyRuBa.modes.ModeCase;
import tyRuBa.modes.ModeCheckContext;
import tyRuBa.modes.PredInfoProvider;
import tyRuBa.modes.TypeEnv;
import tyRuBa.modes.TypeModeError;

public class RBModeSwitchExpression
  extends RBExpression
{
  Vector modeCases = new Vector();
  RBExpression defaultExp = null;
  
  public RBModeSwitchExpression(ModeCase mc)
  {
    this.modeCases.add(mc);
  }
  
  public void addModeCase(ModeCase mc)
  {
    this.modeCases.add(mc);
  }
  
  public void addDefaultCase(RBExpression exp)
  {
    this.defaultExp = exp;
  }
  
  public ModeCase getModeCaseAt(int pos)
  {
    return (ModeCase)this.modeCases.elementAt(pos);
  }
  
  public int getNumModeCases()
  {
    return this.modeCases.size();
  }
  
  public boolean hasDefaultExp()
  {
    return this.defaultExp != null;
  }
  
  public RBExpression getDefaultExp()
  {
    return this.defaultExp;
  }
  
  public String toString()
  {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < getNumModeCases(); i++)
    {
      if (i > 0) {
        result.append("\n| ");
      }
      result.append(getModeCaseAt(i).toString());
    }
    if (this.defaultExp != null) {
      result.append("\n| DEFAULT: " + this.defaultExp);
    }
    return result.toString();
  }
  
  public Compiled compile(CompilationContext c)
  {
    throw new Error("Should not happen: a mode case should have been selected before any compilation is performed");
  }
  
  public TypeEnv typecheck(PredInfoProvider predinfo, TypeEnv startEnv)
    throws TypeModeError
  {
    try
    {
      TypeEnv resultEnv = null;
      for (int i = 0; i < getNumModeCases(); i++)
      {
        RBExpression currExp = getModeCaseAt(i).getExp();
        TypeEnv currEnv = currExp.typecheck(predinfo, startEnv);
        if (resultEnv == null) {
          resultEnv = currEnv;
        } else {
          resultEnv = resultEnv.union(currEnv);
        }
      }
      TypeEnv currEnv;
      if (hasDefaultExp())
      {
        currEnv = getDefaultExp().typecheck(predinfo, startEnv);
        if (resultEnv == null) {
          resultEnv = currEnv;
        }
      }
      return resultEnv.union(currEnv);
    }
    catch (TypeModeError e)
    {
      throw new TypeModeError(e, this);
    }
  }
  
  public RBExpression convertToMode(ModeCheckContext context, boolean rearrange)
    throws TypeModeError
  {
    for (int i = 0; i < getNumModeCases(); i++)
    {
      ModeCase currModeCase = getModeCaseAt(i);
      Collection boundVars = currModeCase.getBoundVars();
      if (context.checkIfAllBound(boundVars))
      {
        RBExpression converted = 
          currModeCase.getExp().convertToMode(context, false);
        return Factory.makeModedExpression(converted, 
          converted.getMode(), converted.getNewContext());
      }
    }
    if (hasDefaultExp())
    {
      RBExpression converted = 
        getDefaultExp().convertToMode(context, rearrange);
      return Factory.makeModedExpression(
        converted, converted.getMode(), converted.getNewContext());
    }
    return Factory.makeModedExpression(
      this, 
      new ErrorMode("There is a missing mode case in " + this), 
      context);
  }
  
  public Object accept(ExpressionVisitor v)
  {
    return v.visit(this);
  }
}
