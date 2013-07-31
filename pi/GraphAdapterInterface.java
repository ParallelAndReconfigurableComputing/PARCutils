/*
 *  Copyright (C) 2009 Lama Akeila, Oliver Sinnen, Nasser Giacaman
 *
 *  This file is part of Parallel Iterator.
 *
 *  Parallel Iterator is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or (at 
 *  your option) any later version.
 *
 *  Parallel Iterator is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
 *  Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along 
 *  with Parallel Iterator. If not, see <http://www.gnu.org/licenses/>.
 */

package pi;

import java.util.ArrayList;
import java.util.Collection;


public interface GraphAdapterInterface<V, E> {

	//returns the successors of v
	public ArrayList<V> getChildrenList(Object v);
	
	//returns the parent nodes of v
	public ArrayList<V> getParentsList(Object v);

	//returns a collection of all the nodes in the graph
	public Collection<V> verticesSet();

	//returns a collection of all the edges in the graph
	public Collection<E> edgesSet();

}

