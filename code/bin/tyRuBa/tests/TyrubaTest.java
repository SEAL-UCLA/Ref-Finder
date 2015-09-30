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

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;
import tyRuBa.engine.FrontEnd;
import tyRuBa.engine.ProgressMonitor;
import tyRuBa.engine.QueryEngine;
import tyRuBa.engine.RBTerm;
import tyRuBa.modes.TypeModeError;
import tyRuBa.parser.ParseException;
import tyRuBa.util.ElementSource;

public abstract class TyrubaTest
  extends TestCase
{
  FrontEnd frontend;
  public static boolean initfile = true;
  
  public TyrubaTest(String arg0)
  {
    super(arg0);
  }
  
  protected void setUp()
    throws Exception
  {
    super.setUp();
    this.frontend = new FrontEnd(initfile, true);
  }
  
  protected void setUpNoFrontend()
    throws Exception
  {
    super.setUp();
  }
  
  protected void setUp(ProgressMonitor mon)
    throws Exception
  {
    super.setUp();
    this.frontend = new FrontEnd(initfile, mon);
  }
  
  void test_must_succeed(String query)
    throws ParseException, TypeModeError
  {
    ElementSource result = this.frontend.frameQuery(query);
    assertTrue(result.hasMoreElements());
  }
  
  void test_must_succeed(String query, QueryEngine qe)
    throws ParseException, TypeModeError
  {
    ElementSource result = qe.frameQuery(query);
    assertTrue(result.hasMoreElements());
  }
  
  void test_must_equal(String query, String var, String expected)
    throws ParseException, TypeModeError
  {
    ElementSource result = this.frontend.varQuery(query, var);
    assertEquals(this.frontend.makeTermFromString(expected), result.nextElement());
    assertFalse(result.hasMoreElements());
  }
  
  void test_must_equal(String query, String var, Object expected)
    throws ParseException, TypeModeError
  {
    ElementSource result = this.frontend.varQuery(query, var);
    assertEquals(expected, ((RBTerm)result.nextElement()).up());
    assertFalse(result.hasMoreElements());
  }
  
  void test_must_equal(String query, String[] vars, String[] expected)
    throws ParseException, TypeModeError
  {
    for (int i = 0; i < vars.length; i++)
    {
      ElementSource result = this.frontend.varQuery(query, vars[i]);
      assertEquals(this.frontend.makeTermFromString(expected[i]), result.nextElement());
      assertFalse(result.hasMoreElements());
    }
  }
  
  void test_must_fail(String query)
    throws ParseException, TypeModeError
  {
    ElementSource result = this.frontend.frameQuery(query);
    assertFalse(result.hasMoreElements());
  }
  
  void test_must_fail(String query, QueryEngine qe)
    throws ParseException, TypeModeError
  {
    ElementSource result = qe.frameQuery(query);
    assertFalse(result.hasMoreElements());
  }
  
  void test_must_findall(String query, String var, String[] expected)
    throws ParseException, TypeModeError
  {
    Set expectedSet = new HashSet();
    for (int i = 0; i < expected.length; i++) {
      expectedSet.add(this.frontend.makeTermFromString(expected[i]));
    }
    ElementSource result = this.frontend.varQuery(query, var);
    while (result.hasMoreElements())
    {
      Object res = result.nextElement();
      boolean ok = expectedSet.remove(res);
      assertTrue("Unexpected result: " + res, ok);
    }
    assertTrue("Expected results not found: " + expectedSet, expectedSet.isEmpty());
  }
  
  void test_must_findall(String query, String[] vars, String[][] expected)
    throws ParseException, TypeModeError
  {
    for (int i = 0; i < vars.length; i++)
    {
      ElementSource result = this.frontend.varQuery(query, vars[i]);
      for (int j = 0; j < expected.length; j++)
      {
        assertTrue(result.hasMoreElements());
        assertEquals(this.frontend.makeTermFromString(expected[j][i]), 
          result.nextElement());
      }
      assertFalse(result.hasMoreElements());
    }
  }
  
  protected void test_resultcount(String query, int numresults)
    throws ParseException, TypeModeError
  {
    int counter = get_resultcount(query);
    assertEquals("Expected number of results:", numresults, counter);
  }
  
  protected int get_resultcount(String query)
    throws ParseException, TypeModeError
  {
    ElementSource result = this.frontend.frameQuery(query);
    int counter = 0;
    while (result.hasMoreElements())
    {
      counter++;
      result.nextElement();
    }
    return counter;
  }
  
  protected void deleteDirectory(File dir)
  {
    if (dir.isDirectory())
    {
      File[] children = dir.listFiles();
      for (int i = 0; i < children.length; i++) {
        deleteDirectory(children[i]);
      }
    }
    dir.delete();
  }
}
