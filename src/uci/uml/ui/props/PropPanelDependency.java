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




package uci.uml.ui.props;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.util.collections.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
//import javax.swing.border.*;

import uci.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;
import uci.uml.ui.*;


public class PropPanelDependency extends PropPanelTwoEnds {

  ////////////////////////////////////////////////////////////////
  // constructors

  public PropPanelDependency() {
    super("Dependency");

    remove(_nameField);
    remove(_nameLabel);
  }


  public String getSourceLabel() {
    if (!(_target instanceof MDependency)) return "non dep";
    return "Supplier:";
  }
  public String getSourceValue() {
    if (!(_target instanceof MDependency)) return "non dep";
    MDependency d = (MDependency) _target;
    Vector suppliers = new Vector(d.getSuppliers());
    if (suppliers == null) return "null suppliers";
    if (suppliers.size() == 0) return "no suppliers";
    MModelElement sup = (MModelElement) suppliers.elementAt(0);
    if (sup == null) return "null";
    return sup.getName();
  }
  public String getDestLabel() {
    if (!(_target instanceof MDependency)) return "non dep";
    return "Client:";
  }
  public String getDestValue() {
    if (!(_target instanceof MDependency)) return "non dep";
    MDependency d = (MDependency) _target;
    Vector clients = new Vector(d.getClients());
    if (clients == null) return "null clients";
    if (clients.size() == 0) return "no clients";
    MModelElement tar = (MModelElement) clients.elementAt(0);
    if (tar == null) return "null";
    return tar.getName();
  }
  

} /* end class PropPanelDependency */
