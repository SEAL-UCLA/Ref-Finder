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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import junit.framework.Assert;
import org.apache.regexp.RE;
import tyRuBa.engine.compilation.CompilationContext;
import tyRuBa.engine.compilation.Compiled;
import tyRuBa.modes.BindingList;
import tyRuBa.modes.Factory;
import tyRuBa.modes.Mode;
import tyRuBa.modes.ModeCheckContext;
import tyRuBa.modes.PredInfo;
import tyRuBa.modes.PredInfoProvider;
import tyRuBa.modes.PredicateMode;
import tyRuBa.modes.TupleType;
import tyRuBa.modes.Type;
import tyRuBa.modes.TypeConstructor;
import tyRuBa.modes.TypeModeError;

public class NativePredicate
  extends RBComponent
{
  private PredInfo predinfo;
  private ArrayList implementations = new ArrayList();
  private PredicateIdentifier predId;
  
  public Mode getMode()
  {
    return null;
  }
  
  public NativePredicate(String name, TupleType argtypes)
  {
    this.predId = new PredicateIdentifier(name, argtypes.size());
    this.predinfo = Factory.makePredInfo(null, name, argtypes);
  }
  
  public NativePredicate(String name, Type t)
  {
    this(name, Factory.makeTupleType(t));
  }
  
  public NativePredicate(String name, Type t1, Type t2)
  {
    this(name, Factory.makeTupleType(t1, t2));
  }
  
  public NativePredicate(String name, Type t1, Type t2, Type t3)
  {
    this(name, Factory.makeTupleType(t1, t2, t3));
  }
  
  public NativePredicate(String name, Type t1, Type t2, Type t3, Type t4)
  {
    this(name, Factory.makeTupleType(t1, t2, t3, t4));
  }
  
  public void addMode(Implementation imp)
  {
    this.predinfo.addPredicateMode(imp.getPredicateMode());
    this.implementations.add(imp);
  }
  
  private void addToRuleBase(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    rb.insert(this.predinfo);
    rb.insert(this);
  }
  
  public Compiled compile(CompilationContext c)
  {
    throw new Error("Compilation only works after this has been converted to a proper mode.");
  }
  
  public TupleType typecheck(PredInfoProvider predinfo)
    throws TypeModeError
  {
    return getPredType();
  }
  
  public int getNumImplementation()
  {
    return this.implementations.size();
  }
  
  public Implementation getImplementationAt(int pos)
  {
    return (Implementation)this.implementations.get(pos);
  }
  
  public TupleType getPredType()
  {
    return this.predinfo.getTypeList();
  }
  
  public PredicateIdentifier getPredId()
  {
    return this.predId;
  }
  
  public RBTuple getArgs()
  {
    throw new Error("getArgs cannot be called until an implementation has been selected");
  }
  
  public RBComponent convertToMode(PredicateMode mode, ModeCheckContext context)
    throws TypeModeError
  {
    BindingList targetBindingList = mode.getParamModes();
    Implementation result = null;
    for (int i = 0; i < getNumImplementation(); i++)
    {
      Implementation candidate = getImplementationAt(i);
      if ((targetBindingList.satisfyBinding(candidate.getBindingList())) && (
        (result == null) || 
        (candidate.getMode().isBetterThan(result.getMode())))) {
        result = candidate;
      }
    }
    if (result == null) {
      throw new TypeModeError("Cannot find an implementation for " + 
        getPredName() + " :: " + mode);
    }
    return result;
  }
  
  public String toString()
  {
    StringBuffer result = new StringBuffer(this.predinfo.getPredId().toString());
    for (Iterator iter = this.implementations.iterator(); iter.hasNext();)
    {
      Implementation element = (Implementation)iter.next();
      result.append("\n" + element);
    }
    return result.toString();
  }
  
  public static void defineStringAppend(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate string_append = new NativePredicate("string_append", 
      Type.string, Type.string, Type.string);
    
    string_append.addMode(new NativePredicate.1("BBF", "DET"));
    
    string_append.addMode(new NativePredicate.2("FFB", "MULTI"));
    
    string_append.addMode(new NativePredicate.3("BFB", "SEMIDET"));
    
    string_append.addMode(new NativePredicate.4("FBB", "SEMIDET"));
    
    string_append.addToRuleBase(rb);
  }
  
  public static void defineStringLength(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate string_length = new NativePredicate("string_length", 
      Type.string, Type.integer);
    
    string_length.addMode(new NativePredicate.5("BF", "DET"));
    
    string_length.addToRuleBase(rb);
  }
  
  public static void defineStringIndexSplit(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate string_index_split = new NativePredicate("string_index_split", 
      Type.integer, Type.string, Type.string, Type.string);
    
    string_index_split.addMode(new NativePredicate.6("BBFF", "SEMIDET"));
    
    string_index_split.addToRuleBase(rb);
  }
  
  public static void defineStringSplitAtLast(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate string_split_at_last = new NativePredicate("string_split_at_last", 
      Type.string, Type.string, Type.string, Type.string);
    
    string_split_at_last.addMode(new NativePredicate.7("BBFF", "DET"));
    
    string_split_at_last.addMode(new NativePredicate.8("BFBB", "DET"));
    
    string_split_at_last.addToRuleBase(rb);
  }
  
  public static String stringReplace(String r1, String r2, String s)
  {
    StringBuffer buf = new StringBuffer();
    int start = 0;
    do
    {
      int end = s.indexOf(r1, start);
      if (end == -1)
      {
        buf.append(s.substring(start));
        return buf.toString();
      }
      buf.append(s.substring(start, end));
      buf.append(r2);
      start = end + r1.length();
    } while (start < s.length());
    return buf.toString();
  }
  
  public static void defineStringReplace(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate string_replace = new NativePredicate("string_replace", 
      Type.string, Type.string, Type.string, Type.string);
    
    string_replace.addMode(new NativePredicate.9("BBBF", "DET"));
    
    string_replace.addMode(new NativePredicate.10("BBFB", "DET"));
    
    string_replace.addToRuleBase(rb);
  }
  
  public static void defineToLowerCase(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate to_lower_case = new NativePredicate("to_lower_case", 
      Type.string, Type.string);
    
    to_lower_case.addMode(new NativePredicate.11("BF", "DET"));
    
    to_lower_case.addToRuleBase(rb);
  }
  
  public static void defineToUpperCase(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate to_upper_case = new NativePredicate("to_upper_case", 
      Type.string, Type.string);
    
    to_upper_case.addMode(new NativePredicate.12("BF", "DET"));
    
    to_upper_case.addToRuleBase(rb);
  }
  
  public static void defineCapitalize(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate capitalize = new NativePredicate("capitalize", 
      Type.string, Type.string);
    
    capitalize.addMode(new NativePredicate.13("BF", "DET"));
    
    capitalize.addToRuleBase(rb);
  }
  
  public static void defineDecapitalize(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate decapitalize = new NativePredicate("decapitalize", 
      Type.string, Type.string);
    
    decapitalize.addMode(new NativePredicate.14("BF", "DET"));
    
    decapitalize.addToRuleBase(rb);
  }
  
  public static void defineStringRepeat(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate string_repeat = new NativePredicate("string_repeat", 
      Type.integer, Type.string, Type.string);
    
    string_repeat.addMode(new NativePredicate.15("BBF", "DET"));
    
    string_repeat.addToRuleBase(rb);
  }
  
  public static void defineTypeTest(ModedRuleBaseIndex rb, PredicateIdentifier id, TypeConstructor t)
    throws TypeModeError
  {
    Assert.assertEquals(1, id.getArity());
    String javaName = t.getName();
    NativePredicate type_test = new NativePredicate(id.getName(), Factory.makeAtomicType(t));
    
    type_test.addMode(new NativePredicate.16("B", "SEMIDET", t));
    
    type_test.addToRuleBase(rb);
  }
  
  public static void defineRange(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate range = new NativePredicate("range", 
      Type.integer, Type.integer, Type.integer);
    
    range.addMode(new NativePredicate.17("BBF", "NONDET"));
    
    range.addToRuleBase(rb);
  }
  
  public static void defineGreater(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate greater = new NativePredicate("greater", Type.integer, Type.integer);
    
    greater.addMode(new NativePredicate.18("BB", "SEMIDET"));
    
    greater.addToRuleBase(rb);
  }
  
  public static void defineSum(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate sum = new NativePredicate("sum", 
      Type.integer, Type.integer, Type.integer);
    
    sum.addMode(new NativePredicate.19("BBF", "DET"));
    
    sum.addMode(new NativePredicate.20("BFB", "DET"));
    
    sum.addMode(new NativePredicate.21("FBB", "DET"));
    
    sum.addToRuleBase(rb);
  }
  
  public static void defineMul(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate mul = new NativePredicate("mul", 
      Type.integer, Type.integer, Type.integer);
    
    mul.addMode(new NativePredicate.22("BBF", "DET"));
    
    mul.addMode(new NativePredicate.23("FBB", "SEMIDET"));
    
    mul.addMode(new NativePredicate.24("BFB", "SEMIDET"));
    
    mul.addToRuleBase(rb);
  }
  
  public static void debug_print(Object ob)
  {
    System.err.println(ob.toString());
  }
  
  public static void defineDebugPrint(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate debug_print = new NativePredicate("debug_print", Type.object);
    
    debug_print.addMode(new NativePredicate.25("B", "DET"));
    
    debug_print.addToRuleBase(rb);
  }
  
  public static void defineThrowError(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate throw_error = new NativePredicate("throw_error", Type.string);
    
    throw_error.addMode(new NativePredicate.26("B", "FAIL"));
    
    throw_error.addToRuleBase(rb);
  }
  
  public static void defineWriteFile(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate write_file = new NativePredicate("write_file", 
      Type.string, Type.string);
    
    write_file.addMode(new NativePredicate.27("BB", "SEMIDET"));
    
    write_file.addToRuleBase(rb);
  }
  
  public static void defineWriteOutput(QueryEngine qe, ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate write_output = new NativePredicate("write_output", Type.string);
    
    write_output.addMode(new NativePredicate.28("B", "DET", qe));
    
    write_output.addToRuleBase(rb);
  }
  
  public static void defineFileseparator(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate fileseparator = new NativePredicate("fileseparator", Type.string);
    
    fileseparator.addMode(new NativePredicate.29("F", "DET"));
    
    fileseparator.addToRuleBase(rb);
  }
  
  public static void defineHashValue(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate hash_value = new NativePredicate("hash_value", 
      Type.object, Type.integer);
    
    hash_value.addMode(new NativePredicate.30("BF", "DET"));
    
    hash_value.addToRuleBase(rb);
  }
  
  public static void defineLength(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate length = new NativePredicate("length", 
      Factory.makeListType(Factory.makeTVar("element")), 
      Type.integer);
    
    length.addMode(new NativePredicate.31("BF", "DET"));
    
    length.addToRuleBase(rb);
  }
  
  public static void defineReMatch(ModedRuleBaseIndex rb)
    throws TypeModeError
  {
    NativePredicate re_match = new NativePredicate("re_match", 
      Factory.makeStrictAtomicType(Factory.makeTypeConstructor(RE.class)), 
      Type.string);
    
    re_match.addMode(new NativePredicate.32("BB", "SEMIDET"));
    
    re_match.addToRuleBase(rb);
  }
  
  public static void defineConvertTo(ModedRuleBaseIndex rb, TypeConstructor t)
    throws TypeModeError
  {
    NativePredicate convertTo = new NativePredicate("convertTo" + t.getName(), 
      Factory.makeAtomicType(t), Factory.makeStrictAtomicType(t));
    
    convertTo.addMode(new NativePredicate.33("BF", "SEMIDET", t));
    
    convertTo.addMode(new NativePredicate.34("FB", "DET"));
    
    convertTo.addToRuleBase(rb);
  }
  
  public static void defineNativePredicates(QueryEngine qe)
    throws TypeModeError
  {
    ModedRuleBaseIndex rules = qe.rulebase();
    
    defineStringAppend(rules);
    defineStringLength(rules);
    defineStringSplitAtLast(rules);
    defineStringIndexSplit(rules);
    defineStringReplace(rules);
    defineToLowerCase(rules);
    defineToUpperCase(rules);
    defineCapitalize(rules);
    defineDecapitalize(rules);
    defineStringRepeat(rules);
    
    defineRange(rules);
    defineGreater(rules);
    defineSum(rules);
    defineMul(rules);
    defineDebugPrint(rules);
    defineThrowError(rules);
    defineWriteFile(rules);
    defineWriteOutput(qe, rules);
    defineHashValue(rules);
    defineLength(rules);
    defineFileseparator(rules);
    
    defineReMatch(rules);
  }
}
