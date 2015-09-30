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
package tyRuBa.tests;

import tyRuBa.engine.FrontEnd;
import tyRuBa.modes.TypeModeError;
import tyRuBa.parser.ParseException;

public class IgnoredVariableTest
  extends TyrubaTest
{
  public IgnoredVariableTest(String arg0)
  {
    super(arg0);
  }
  
  protected void setUp()
    throws Exception
  {
    TyrubaTest.initfile = false;
    tyRuBa.engine.RuleBase.useCache = false;
    super.setUp();
  }
  
  public void testIgnoredVars1()
    throws ParseException, TypeModeError
  {
    this.frontend.parse("test :: ( ) \nMODES () IS DET END\n");
    
    this.frontend.parse("foobar :: String, Integer\nMODES (F,F) IS MULTI END\n");
    
    this.frontend.parse("test() :- foobar(?,?).");
  }
  
  public void testIgnoredVars2()
    throws ParseException, TypeModeError
  {
    this.frontend.parse("foo :: String, String\nMODES (F,F) IS NONDET END");
    
    this.frontend.parse("foo(a,b).");
    this.frontend.parse("foo(aa,bb).");
    test_must_findall("foo(?x,?)", "?x", new String[] { "a", "aa" });
    test_must_findall("foo(?,?x)", "?x", new String[] { "b", "bb" });
  }
}
