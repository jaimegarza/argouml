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

package org.argouml.uml.diagram.static_structure.ui;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.argouml.application.api.Argo;
import org.argouml.api.FacadeManager;
import org.argouml.model.uml.NsumlModelFacade;
import org.argouml.model.uml.UmlHelper;
import org.argouml.ui.AbstractGoRule;

/**
 * This class is a Go Rule for the "Class - centric" Navigation perspective.
 *
 * $Revision$
 *
 * @author  alexb, $Author$
 * @since argo 0.13.4, Created on 21 March 2003, 23:18
 */
public class GoSummaryToInheritance extends AbstractGoRule {

  public String getRuleName() {
    return Argo.localize ("Tree", "misc.class.attribute");
  }

  public Collection getChildren(Object parent) {
      if ( parent instanceof InheritanceNode) {
          
          List list = new ArrayList();
          
          Iterator it = FacadeManager.getUmlFacade().getSupplierDependencies(
                                        ((InheritanceNode)parent).getParent());
          
          while(it.hasNext()){
              
              Object next = it.next();
              if(FacadeManager.getUmlFacade().isAAbstraction(next))
                list.add(next);
          }
          
          it = FacadeManager.getUmlFacade().getClientDependencies(
                            ((InheritanceNode)parent).getParent());
          
          while(it.hasNext()){
              
              Object next = it.next();
              if(FacadeManager.getUmlFacade().isAAbstraction(next))
                list.add(next);
          }
          
          Iterator generalizationsIt = FacadeManager.getUmlFacade().getGeneralizations(
                                            ((InheritanceNode)parent).getParent());
          Iterator specializationsIt = FacadeManager.getUmlFacade().getSpecializations(
                                            ((InheritanceNode)parent).getParent());
          
          while(generalizationsIt.hasNext())
              list.add(generalizationsIt.next());
          
          while(specializationsIt.hasNext())
              list.add(specializationsIt.next());
          
          return list;
      }
      return null;
  }

}
