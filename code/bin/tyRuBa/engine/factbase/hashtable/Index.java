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
package tyRuBa.engine.factbase.hashtable;

import java.util.ArrayList;
import tyRuBa.engine.FrontEnd;
import tyRuBa.engine.QueryEngine;
import tyRuBa.engine.RBTerm;
import tyRuBa.engine.RBTuple;
import tyRuBa.engine.factbase.NamePersistenceManager;
import tyRuBa.engine.factbase.ValidatorManager;
import tyRuBa.modes.BindingList;
import tyRuBa.modes.BindingMode;
import tyRuBa.modes.Mode;
import tyRuBa.modes.PredicateMode;
import tyRuBa.util.ElementSource;
import tyRuBa.util.pager.Location;
import tyRuBa.util.pager.Pager;
import tyRuBa.util.pager.Pager.ResourceId;

public final class Index
{
  private final int[] freePlaces;
  private final int[] boundPlaces;
  private String predicateName;
  private boolean checkDet;
  private QueryEngine engine;
  private ValidatorManager validatorManager;
  private NamePersistenceManager nameManager;
  private Location storageLocation;
  
  public String toString()
  {
    String result = "Index(" + this.predicateName + " ";
    int arity = this.boundPlaces.length + this.freePlaces.length;
    char[] boundMap = new char[arity];
    for (int i = 0; i < boundMap.length; i++) {
      boundMap[i] = 'F';
    }
    for (int i = 0; i < this.boundPlaces.length; i++) {
      boundMap[this.boundPlaces[i]] = 'B';
    }
    return result + new String(boundMap) + ")";
  }
  
  Index(PredicateMode mode, Location storageLocation, QueryEngine engine, String predicateName)
  {
    this.validatorManager = engine.getFrontEndValidatorManager();
    this.engine = engine;
    this.storageLocation = storageLocation;
    this.predicateName = predicateName;
    this.nameManager = engine.getFrontendNamePersistenceManager();
    this.checkDet = ((mode.getMode().isDet()) || (mode.getMode().isSemiDet()));
    BindingList bl = mode.getParamModes();
    this.boundPlaces = new int[bl.getNumBound()];
    int boundPos = 0;
    this.freePlaces = new int[bl.getNumFree()];
    int freePos = 0;
    for (int i = 0; i < bl.size(); i++) {
      if (bl.get(i).isBound()) {
        this.boundPlaces[(boundPos++)] = i;
      } else {
        this.freePlaces[(freePos++)] = i;
      }
    }
  }
  
  Index(PredicateMode mode, Location storageLocation, QueryEngine engine, String predicateName, NamePersistenceManager nameManager, ValidatorManager validatorManager)
  {
    this(mode, storageLocation, engine, predicateName);
    this.nameManager = nameManager;
    this.validatorManager = validatorManager;
  }
  
  private Pager getPager()
  {
    return this.engine.getFrontEndPager();
  }
  
  private RBTuple extract(int[] toExtract, RBTuple from)
  {
    RBTerm[] extracted = new RBTerm[toExtract.length];
    for (int i = 0; i < extracted.length; i++) {
      extracted[i] = from.getSubterm(toExtract[i]);
    }
    return FrontEnd.makeTuple(extracted);
  }
  
  public RBTuple extractBound(RBTuple goal)
  {
    return extract(this.boundPlaces, goal);
  }
  
  public RBTuple extractFree(RBTuple goal)
  {
    return extract(this.freePlaces, goal);
  }
  
  public void addFact(IndexValue fact)
  {
    RBTuple parts = fact.getParts();
    RBTuple whole_key = extractBound(parts);
    RBTuple free = extractFree(parts);
    if (whole_key == RBTuple.theEmpty) {
      whole_key = free;
    }
    Object key = whole_key.getSecond();
    String topLevelKey = whole_key.getFirst();
    IndexValue value = IndexValue.make(fact.getValidatorHandle(), free);
    
    getPager().asynchDoTask(getResourceFromKey(whole_key), new Index.1(this, true, key, value));
    
    getPager().asynchDoTask(this.storageLocation.getResourceID("keys.data"), new Index.2(this, true, topLevelKey));
  }
  
  private Pager.ResourceId getResourceFromKey(RBTuple whole_key)
  {
    return this.storageLocation.getResourceID(this.nameManager.getPersistentName(whole_key.getFirst()));
  }
  
  public ElementSource getMatchElementSource(RBTuple inputPars)
  {
    if (inputPars == RBTuple.theEmpty) {
      return convertIndexValuesToRBTuples(values());
    }
    Object key = inputPars.getSecond();
    
    return (ElementSource)getPager().synchDoTask(getResourceFromKey(inputPars), new Index.3(this, false, key));
  }
  
  public RBTuple getMatchSingle(RBTuple inputPars)
  {
    return (RBTuple)getMatchElementSource(inputPars).firstElementOrNull();
  }
  
  public ElementSource values()
  {
    return 
    
      getTopLevelKeys().map(new Index.4(this)).flatten();
  }
  
  private ElementSource getTopLevelKeys()
  {
    Index.HashSetResource topLevelKeys = (Index.HashSetResource)getPager().synchDoTask(
      this.storageLocation.getResourceID("keys.data"), new Index.5(this, false));
    
    return ElementSource.with(topLevelKeys.iterator());
  }
  
  private ElementSource getTopKeyValues(String topkey)
  {
    ElementSource valid_values = (ElementSource)getPager().synchDoTask(
      this.storageLocation.getResourceID(this.nameManager.getPersistentName(topkey)), new Index.6(this, false));
    
    return valid_values;
  }
  
  public ElementSource convertIndexValuesToRBTuples(ElementSource source)
  {
    return source.map(new Index.7(this));
  }
  
  public ElementSource removeInvalids(Object values)
  {
    if (values == null) {
      return ElementSource.theEmpty;
    }
    if ((values instanceof ArrayList)) {
      return ElementSource.with((ArrayList)values).map(new Index.8(this));
    }
    if (((IndexValue)values).isValid(this.validatorManager)) {
      return ElementSource.singleton(values);
    }
    return ElementSource.theEmpty;
  }
  
  public void backup() {}
}
