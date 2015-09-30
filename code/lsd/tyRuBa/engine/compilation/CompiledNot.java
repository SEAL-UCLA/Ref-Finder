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
package tyRuBa.engine.compilation;

import tyRuBa.engine.Frame;
import tyRuBa.engine.RBContext;

public class CompiledNot extends SemiDetCompiled {

	//TODO: Find a way to turn the negated predicate into nondet mode and then take advantage of that.
	private Compiled negated;

	public CompiledNot(Compiled negated) {
		super(negated.getMode().negate());
		this.negated = negated;
	}

	public Frame runSemiDet(Object input, RBContext context) {
		if (negated.runNonDet(input, context).hasMoreElements())
			return null;
		else {
			return (Frame)input;
		}
	}
	
	public Compiled negate() {
		return new CompiledTest(negated);
	}

	public String toString() {
		return "NOT(" + negated + ")";
	}

}
