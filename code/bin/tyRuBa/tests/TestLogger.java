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

import java.io.PrintStream;
import java.io.PrintWriter;
import junit.framework.Assert;

public class TestLogger
{
  public static final boolean logging = false;
  private int logtime = 0;
  PrintWriter logFile;
  PrintStream console = System.err;
  
  public TestLogger(PrintWriter writer)
  {
    this.logFile = writer;
  }
  
  TestLogger.LogEntry current = new TestLogger.LogEntry(this, null, "BIGBANG", 0);
  
  public TestLogger.LogEntry enter(String kind)
  {
    this.current = new TestLogger.LogEntry(this, this.current, kind, ++this.logtime);
    return this.current;
  }
  
  public TestLogger.LogEntry enter(String kind, String params)
  {
    this.current = new TestLogger.LogEntry(this, this.current, kind, ++this.logtime);
    this.current.addInfo(params);
    return this.current;
  }
  
  public TestLogger.LogEntry enter(String kind, int info)
  {
    return enter(kind, info);
  }
  
  public void logNow(String kind, String params)
  {
    TestLogger.LogEntry enter = enter(kind, params);
    enter.exit();
  }
  
  void close()
  {
    while (this.current != null) {
      this.current.exit();
    }
    if (this.logFile != null) {
      this.logFile.close();
    }
  }
  
  private static boolean loading = false;
  
  public synchronized TestLogger.LogEntry enterLoad(String path)
  {
    Assert.assertFalse("Reentrant load should not happen", loading);
    Assert.assertFalse("Load inside storeAll", this.current.kind.equals("storeAll"));
    loading = true;
    return enter("load", "\"" + path + "\"");
  }
  
  public synchronized void exitLoad(TestLogger.LogEntry entry)
  {
    Assert.assertTrue("Must enter load before exit load", loading);
    entry.exit();
    loading = false;
  }
  
  public void assertTrue(String msg, boolean b)
  {
    if (!b)
    {
      logNow("assertionFailed", "\"" + msg + "\"");
      Assert.fail(msg);
    }
  }
}
