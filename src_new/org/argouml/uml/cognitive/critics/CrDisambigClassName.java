// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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



// File: CrDisambigClassName.java
// Classes: CrDisambigClassName
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import javax.swing.Icon;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.uml.cognitive.UMLToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.kernel.Wizard;
import org.argouml.model.ModelFacade;
/** Well-formedness rule [1] for MNamespace. See page 33 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

public class CrDisambigClassName extends CrUML {

    public CrDisambigClassName() {
	setHeadline("Choose a Unique Name for <ocl>self</ocl>");
	addSupportedDecision(CrUML.decNAMING);
	setKnowledgeTypes(Critic.KT_SYNTAX);
	addTrigger("name");
	addTrigger("elementOwnership");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
	if (!(ModelFacade.isAClassifier(dm))) return NO_PROBLEM;
	Object cls = /*(MClassifier)*/ dm;
	String myName = ModelFacade.getName(cls);
	//@ if (myName.equals(Name.UNSPEC)) return NO_PROBLEM;
	String myNameString = myName;
    
	if (myNameString != null && myNameString.length() == 0)
	    return NO_PROBLEM;

	Collection pkgs = ModelFacade.getElementImports2(cls);
	if (pkgs == null) return NO_PROBLEM;
	for (Iterator iter = pkgs.iterator(); iter.hasNext();) {
	    Object imp = /*(MElementImport)*/ iter.next();
	    Object ns = ModelFacade.getPackage(imp);
	    Collection siblings = ModelFacade.getOwnedElements(ns);
	    if (siblings == null) return NO_PROBLEM;
	    Iterator enum = siblings.iterator();
	    while (enum.hasNext()) {
		Object eo = /*(MElementImport)*/ enum.next();
		Object me = /*(MModelElement)*/ ModelFacade.getModelElement(eo);
		if (!(ModelFacade.isAClassifier(me))) continue;
		if (me == cls) continue;
		String meName = ModelFacade.getName(me);
		if (meName == null || meName.equals("")) continue;
		if (meName.equals(myNameString)) return PROBLEM_FOUND;
	    }
	}
	return NO_PROBLEM;
    }

    public Icon getClarifier() {
	return ClClassName.TheInstance;
    }

    public void initWizard(Wizard w) {
	if (w instanceof WizMEName) {
	    ToDoItem item = w.getToDoItem();
	    Object me = /*(MModelElement)*/ item.getOffenders().elementAt(0);
	    String sug = ModelFacade.getName(me);
	    String ins = "Change the name to something different.";
	    ((WizMEName) w).setInstructions(ins);
	    ((WizMEName) w).setSuggestion(sug);
	    ((WizMEName) w).setMustEdit(true);
	}
    }
    public Class getWizardClass(ToDoItem item) { return WizMEName.class; }


} /* end class CrDisambigClassName.java */