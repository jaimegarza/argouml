// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

// File: StateDiagramGraphModel.java
// Classes: StateDiagramGraphModel
// Original Author: your email address here
// $Id$
package org.argouml.uml.diagram.state;

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Category;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesFactory;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesHelper;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MFinalState;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;
import ru.novosoft.uml.model_management.MElementImport;

/** This class defines a bridge between the UML meta-model
 *  representation of the design and the GraphModel interface used by
 *  GEF.  This class handles only UML MState Digrams.  */

public class StateDiagramGraphModel extends UMLMutableGraphSupport
    implements VetoableChangeListener 
{
    protected static Category cat = 
        Category.getInstance(StateDiagramGraphModel.class);


    /** The "home" UML model of this diagram, not all ModelElements in this
     *  graph are in the home model, but if they are added and don't
     *  already have a model, they are placed in the "home model".
     *  Also, elements from other models will have their FigNodes add a
     *  line to say what their model is. */

    protected MNamespace _namespace;

    /** The statemachine we are diagramming */
    protected MStateMachine _machine;

    ////////////////////////////////////////////////////////////////
    // accessors

    public MNamespace getNamespace() { return _namespace; }
    public void setNamespace(MNamespace m) {
	_namespace = m;
    }

    public MStateMachine getMachine() { return _machine; }
    public void setMachine(MStateMachine sm) {
	if (sm != null) {
	    _machine = sm;
	}
    }

    ////////////////////////////////////////////////////////////////
    // GraphModel implementation

  

    /** Return all ports on node or edge */
    public Vector getPorts(Object nodeOrEdge) {
	Vector res = new Vector();  //wasteful!
	if (nodeOrEdge instanceof MState) res.addElement(nodeOrEdge);
	if (nodeOrEdge instanceof MPseudostate) res.addElement(nodeOrEdge);
	return res;
    }

    /** Return the node or edge that owns the given port */
    public Object getOwner(Object port) {
	return port;
    }

    /** Return all edges going to given port */
    public Vector getInEdges(Object port) {
	if (port instanceof MStateVertex) {
	    return new Vector(((MStateVertex) port).getIncomings());
	}
	cat.debug("TODO getInEdges of MState");
	return new Vector(); //wasteful!
    }

    /** Return all edges going from given port */
    public Vector getOutEdges(Object port) {
	if (port instanceof MStateVertex) {
	    return new Vector(((MStateVertex) port).getOutgoings());
	}
	cat.debug("TODO getOutEdges of MState");
	return new Vector(); //wasteful!
    }

    /** Return one end of an edge */
    public Object getSourcePort(Object edge) {
	if (edge instanceof MTransition) {
	    return StateMachinesHelper.getHelper()
		.getSource((MTransition) edge);
	}
	cat.debug("TODO getSourcePort of MTransition");
	return null;
    }

    /** Return  the other end of an edge */
    public Object getDestPort(Object edge) {
	if (edge instanceof MTransition) {
	    return StateMachinesHelper.getHelper()
		.getDestination((MTransition) edge);
	}
	cat.debug("TODO getDestPort of MTransition");
	return null;
    }


    ////////////////////////////////////////////////////////////////
    // MutableGraphModel implementation

    /** Return true if the given object is a valid node in this graph */
    public boolean canAddNode(Object node) {
	if (node == null) return false;
	if (_nodes.contains(node)) return false;
	return (node instanceof MStateVertex);
    }

    /** Return true if the given object is a valid edge in this graph */
    public boolean canAddEdge(Object edge)  {
	if (edge == null) return false;
	if (_edges.contains(edge)) return false;
	Object end0 = null, end1 = null;
	if (edge instanceof MTransition) {
	    MTransition tr = (MTransition) edge;
	    end0 = tr.getSource();
	    end1 = tr.getTarget();
	}

	if (end0 == null || end1 == null) return false;
	if (!_nodes.contains(end0)) return false;
	if (!_nodes.contains(end1)) return false;
	return true;
    }



    /** Add the given node to the graph, if valid. */
    public void addNode(Object node) {
	cat.debug("adding state diagram node: " + node);
	if (!canAddNode(node)) return;
	if (!(node instanceof MStateVertex)) {
	    cat.error("internal error: got past canAddNode");
	    return;
	}
	MStateVertex sv = (MStateVertex) node;

	if (_nodes.contains(sv)) return;
	_nodes.addElement(sv);
	// TODO: assumes public, user pref for default visibility?
	//if (sv.getNamespace() == null)
	//_namespace.addOwnedElement(sv);
	// TODO: assumes not nested in another composite state
	MCompositeState top = (MCompositeState) _machine.getTop();

	top.addSubvertex(sv);
	//       sv.setParent(top); this is done in setEnclosingFig!!
	//      if ((sv instanceof MState) &&
	//      (sv.getNamespace()==null))
	//      ((MState)sv).setStateMachine(_machine);
	fireNodeAdded(node);
    }

    /** Add the given edge to the graph, if valid. */
    public void addEdge(Object edge) {
	cat.debug("adding state diagram edge!!!!!!");
   
	if (!canAddEdge(edge)) return;
	MTransition tr = (MTransition) edge;
	_edges.addElement(tr);
	fireEdgeAdded(edge);
    }

    public void addNodeRelatedEdges(Object node) {
	if ( node instanceof MStateVertex ) {
	    Vector transen = new Vector(((MStateVertex) node).getOutgoings());
	    transen.addAll(((MStateVertex) node).getIncomings());
	    Iterator iter = transen.iterator();
	    while (iter.hasNext()) {
		MTransition dep = (MTransition) iter.next();
		if (canAddEdge(dep))
		    addEdge(dep);
	    }
	}
    }



    /** Return true if the two given ports can be connected by a
     * kind of edge to be determined by the ports. */
    public boolean canConnect(Object fromPort, Object toPort) {
	if (!(fromPort instanceof MStateVertex)) {
	    cat.error("internal error not from sv");
	    return false;
	}
	if (!(toPort instanceof MStateVertex)) {
	    cat.error("internal error not to sv");
	    return false;
	}
	MStateVertex fromSV = (MStateVertex) fromPort;
	MStateVertex toSV = (MStateVertex) toPort;

	if (fromSV instanceof MFinalState)	return false;
	if (toSV instanceof MPseudostate)
	    if (MPseudostateKind.INITIAL
		.equals(((MPseudostate) toSV).getKind()))
		return false;

	return true;
    }

    /** Contruct and add a new edge of the given kind */
    public Object connect(Object fromPort, Object toPort,
			  java.lang.Class edgeClass)
    {
	//    try {
	if (!(fromPort instanceof MStateVertex)) {
	    cat.error("internal error not from sv");
	    return null;
	}
	if (!(toPort instanceof MStateVertex)) {
	    cat.error("internal error not to sv");
	    return null;
	}
	MStateVertex fromSV = (MStateVertex) fromPort;
	MStateVertex toSV = (MStateVertex) toPort;

	if (fromSV instanceof MFinalState)
	    return null;
	if (toSV instanceof MPseudostate)
	    if (MPseudostateKind.INITIAL
		.equals(((MPseudostate) toSV).getKind()))
		return null;

	if (edgeClass == MTransition.class) {
	    MTransition tr = null;
	    MCompositeState comp = fromSV.getContainer();
	    tr = StateMachinesFactory.getFactory().buildTransition(fromSV,
								   toSV);
	    addEdge(tr);
	    return tr;
	}
	else {
	    cat.debug("wrong kind of edge in StateDiagram connect3 "
		      + edgeClass);
	    return null;
	}
    }


    ////////////////////////////////////////////////////////////////
    // VetoableChangeListener implementation

    public void vetoableChange(PropertyChangeEvent pce) {
	//throws PropertyVetoException

	if ("ownedElement".equals(pce.getPropertyName())) {
	    Vector oldOwned = (Vector) pce.getOldValue();
	    MElementImport eo = (MElementImport) pce.getNewValue();
	    MModelElement me = eo.getModelElement();
	    if (oldOwned.contains(eo)) {
		cat.debug("model removed " + me);
		if (me instanceof MState) removeNode(me);
		if (me instanceof MPseudostate) removeNode(me);
		if (me instanceof MTransition) removeEdge(me);
	    }
	    else {
		cat.debug("model added " + me);
	    }
	}
    }


    static final long serialVersionUID = -8056507319026044174L;

    /**
     * @param newNode this is the new node that one of the ends is dragged to.
     * @param oldNode this is the existing node that is already connected.
     * @param edge this is the edge that is being dragged/rerouted
     * @return true if a transition is being rerouted between two states.
     */
    public boolean canChangeConnectedNode(Object newNode, Object oldNode,
					  Object edge)
    {
    	// prevent no changes... 
	if ( newNode == oldNode)
	    return false;
            
	// check parameter types:
	if ( !(newNode instanceof MState ||
	       oldNode instanceof MState ||
	       edge instanceof MTransition ) )
	    return false;
            
	return true;
    }

    /**
     * Reroutes the connection to the old node to be connected to
     * the new node.
     *
     * @param newNode this is the new node that one of the ends is dragged to.
     * @param oldNode this is the existing node that is already connected.
     * @param edge this is the edge that is being dragged/rerouted
     * @param isSource tells us which end is being rerouted.
     */
    public void changeConnectedNode(Object newNode, Object oldNode,
				    Object edge, boolean isSource)
    {
      
	if (isSource)
	    ((MTransition) edge).setSource((MState) newNode);
	else
	    ((MTransition) edge).setTarget((MState) newNode);
          
    }
  
} /* end class StateDiagramGraphModel */

