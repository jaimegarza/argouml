// Copyright (c) 1996-02 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.layout;

import org.tigris.gef.presentation.*;
import org.apache.log4j.Category;
import org.argouml.uml.diagram.ui.*;

/** a class to get the proper layouter for a Fig.
Currently this deals only with Generalizations and Realizations.

@author Markus Klink

*/
public class ClassdiagramModelElementFactory 
{
    protected static Category cat = Category.getInstance(ClassdiagramModelElementFactory.class);
    /** Singleton */
    public static ClassdiagramModelElementFactory SINGLETON = new ClassdiagramModelElementFactory();
    
    private ClassdiagramModelElementFactory() {}
    
    /** create layouter object from a Fig.*
     *
     * @param f Object which contains the Fig
     * @return Layouter for the Edge or null if none exists.
     */
    public ClassdiagramEdge getInstance(Object f) {
        if(f instanceof FigEdge) {
                if (f instanceof FigGeneralization)
                    return (new ClassdiagramGeneralizationEdge((FigGeneralization) f));
                if (f instanceof FigRealization)
                    return (new ClassdiagramRealizationEdge((FigRealization) f));
                if (f instanceof FigAssociation)
                    return (new ClassdiagramAssociationEdge((FigAssociation) f));
                cat.debug("Do not know how to deal with: " +
                                   f.getClass().getName() +
                                   "\nUsing standard layout");
        }
        return null;
    }
}
