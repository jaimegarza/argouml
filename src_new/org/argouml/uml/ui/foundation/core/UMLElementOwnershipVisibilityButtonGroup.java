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

// $header$
package org.argouml.uml.ui.foundation.core;

import org.argouml.model.ModelFacade;
import org.argouml.uml.ui.UMLUserInterfaceContainer;

/**
 * Model for namespace visibility.
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLElementOwnershipVisibilityButtonGroup extends UMLVisibilityButtonGroup {
        
    /**
     * Constructor for UMLVisibilityButtonGroup.
     */
    public UMLElementOwnershipVisibilityButtonGroup(UMLUserInterfaceContainer container) {
        super(container);
    }
    
    

    /**
     * @see org.argouml.uml.ui.UMLButtonGroup#buildModel()
     */
    public void buildModel() {
        if (ModelFacade.isPrivate(getTarget())) {
            getPrivateButton().setSelected(true);
        } else {
	    if (ModelFacade.isProtected(getTarget())) {
		getProtectedButton().setSelected(true);
	    } else {
		getPublicButton().setSelected(true);
            }
        }
    }

    /**
     * @see org.argouml.uml.ui.foundation.core.UMLVisibilityButtonGroup#setActions()
     */
    protected void setActions() {
        add(getPublicButton(), new ActionSetElementOwnershipVisibility(this));
        add(getProtectedButton(), new ActionSetElementOwnershipVisibility(this));
        add(getPrivateButton(), new ActionSetElementOwnershipVisibility(this));
        
        getPublicButton().setActionCommand(ActionSetElementOwnershipVisibility.PUBLIC_ACTION_COMMAND);
        getProtectedButton().setActionCommand(ActionSetElementOwnershipVisibility.PROTECTED_ACTION_COMMAND);
        getPrivateButton().setActionCommand(ActionSetElementOwnershipVisibility.PRIVATE_ACTION_COMMAND);
    }

}
