// Copyright (c) 1996-01 The Regents of the University of California. All
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

package org.argouml.ui;

import java.util.*;
import java.util.zip.*;
import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.filechooser.*;
import javax.help.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;
import org.tigris.gef.util.*;

import org.argouml.kernel.*;
import org.argouml.persistence.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.ui.*;
import org.argouml.cognitive.critics.ui.*;

//
//   Template reader, has nothing to do with OCL
//
import org.tigris.gef.ocl.*;
import org.argouml.uml.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.generator.ui.*;
import org.argouml.uml.reveng.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.activity.ui.*;
import org.argouml.uml.diagram.collaboration.ui.*;
import org.argouml.uml.diagram.deployment.ui.*;
import org.argouml.uml.diagram.state.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.diagram.use_case.ui.*;
import org.argouml.uml.diagram.sequence.ui.*;
import org.argouml.util.*;
import org.argouml.xml.argo.ArgoParser;

public class Actions {

  static Vector _allActions = new Vector(100);


  public static UMLAction Print = new ActionPrint();

  public static UMLAction Undo = new ActionUndo();
  public static UMLAction Redo = new ActionRedo();

  public static UMLAction NavBack = new ActionNavBack();
  public static UMLAction NavForw = new ActionNavForw();
  //public static UMLAction NavFavs = new ActionNavFavs();
  public static UMLAction NavConfig = new ActionNavConfig();

  public static UMLAction Find = new ActionFind();
  public static UMLAction GotoDiagram = new ActionGotoDiagram();

  public static UMLAction NextEditTab = new ActionNextEditTab();
  public static UMLAction NextDetailsTab = new ActionNextDetailsTab();
  public static UMLAction ShowRapidButtons = new ActionShowRapidButtons();

  public static UMLAction CreateMultiple = new ActionCreateMultiple();

  public static UMLAction AutoCritique = new ActionAutoCritique();
  public static UMLAction OpenDecisions = new ActionOpenDecisions();
  public static UMLAction OpenGoals = new ActionOpenGoals();
  public static UMLAction OpenCritics = new ActionOpenCritics();

  public static UMLAction FlatToDo = new ActionFlatToDo();

  public static UMLAction NewToDoItem = new ActionNewToDoItem();
  public static UMLAction Resolve = new ActionResolve();
  public static UMLAction EmailExpert = new ActionEmailExpert();
  public static UMLAction MoreInfo = new ActionMoreInfo();
  public static UMLAction Snooze = new ActionSnooze();

  public static UMLAction AboutArgoUML = new ActionAboutArgoUML();
  public static UMLAction Quickguide = 
      new ActionHelp("QuickGuide","quickguide.hs");
  public static UMLAction Manual = 
      new ActionHelp("Manual","manual.hs");
  public static UMLAction FAQ = 
      new ActionHelp("FAQ","faq.hs");
  public static UMLAction CookBook =
      new ActionHelp("Cookbook", "cookbook.hs");

  public static void updateAllEnabled() {
    java.util.Enumeration actions = _allActions.elements();
    while (actions.hasMoreElements()) {
      UMLAction a = (UMLAction) actions.nextElement();
      a.updateEnabled();
    }
  }

	public static void addAction(AbstractAction newAction) {
		_allActions.addElement(newAction);
	}

}  /* end class Actions */


////////////////////////////////////////////////////////////////
// file menu actions

class ActionPrint extends UMLAction {
  public ActionPrint() { super("Print..."); }

  public void actionPerformed(ActionEvent ae) {
    CmdPrint cmd = new CmdPrint();
    Object target = ProjectBrowser.TheInstance.getTarget();
    if (target instanceof Diagram) {
      String n = ((Diagram)target).getName();
      cmd.setDiagramName(n);
      cmd.doIt();
    }
  }
} /* end class ActionPrint */


////////////////////////////////////////////////////////////////
// generic editing actions

class ActionUndo extends UMLAction {
  public ActionUndo() { super("Undo"); }
  public boolean shouldBeEnabled() { return false; }
} /* end class ActionUndo */

class ActionRedo extends UMLAction {
  public ActionRedo() { super("Redo"); }
  public boolean shouldBeEnabled() { return false; }
} /* end class ActionRedo */


////////////////////////////////////////////////////////////////
// items on view menu

class ActionFind extends UMLAction {
  public ActionFind() { super("Find..."); }
  public void actionPerformed(ActionEvent ae) {
    FindDialog.SINGLETON.setVisible(true);
  }
} /* end class ActionFind */

class ActionGotoDiagram extends UMLAction {
  public ActionGotoDiagram() { super("Goto Diagram...", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    //needs-more-work: class TearOffHostFrame and TearOffManager.
    //idea: pop-up on tab that lists docking locations, new window.
    JFrame f = new JFrame("Goto Diagram...");
    f.getContentPane().setLayout(new BorderLayout());
    JTabbedPane tabs = new JTabbedPane();
    f.getContentPane().add(tabs, BorderLayout.CENTER);
    TabResults allDiagrams = new TabResults(false); // no related
    allDiagrams.setResults(p.getDiagrams(), p.getDiagrams());
    tabs.addTab("All Diagrams", allDiagrams);
    //needs-more-work: tabs for class, state, usecase, help
    f.setSize(500, 300);
    f.setLocation(pb.getLocation().x + 100, pb.getLocation().y + 100);
    f.setVisible(true);
  }
} /* end class ActionGotoDiagram */

class ActionNavBack extends UMLAction {
  public ActionNavBack() { super("Navigate Back"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    if (!(super.shouldBeEnabled() && p != null)) return false;
    NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
	if ((np == null)) return false;
	boolean b = np.canNavBack();
    return b;
  }
  public void actionPerformed(ActionEvent ae) {
    NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
    np.navBack();
  }
} /* end class ActionNavBack */

class ActionNavForw extends UMLAction {
  public ActionNavForw() { super("Navigate Forward"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    if (!(super.shouldBeEnabled() && p != null)) return false;
    NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
    return np.canNavForw();
  }
  public void actionPerformed(ActionEvent ae) {
    NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
    np.navForw();
  }
} /* end class ActionNavForw */

// class ActionNavFavs extends UMLAction {
//   public ActionNavFavs() { super("Favorites"); }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionNavFavs */

class ActionNavConfig extends UMLAction {
  public ActionNavConfig() { super("NavConfig"); }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    NavigatorPane nav = pb.getNavPane();
    NavigatorConfigDialog ncd = new NavigatorConfigDialog(pb);
    ncd.setVisible(true);
  }
} /* end class ActionNavConfig */

class ActionNextEditTab extends UMLAction {
  public ActionNextEditTab() { super("Next Editing Tab", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    MultiEditorPane mep = pb.getEditorPane();
    mep.selectNextTab();
  }
} /* end class ActionNextEditTab */

// class ActionAddToFavs extends UMLAction {
//   public ActionAddToFavs() { super("Add To Favorites"); }
// } /* end class ActionAddToFavs */

class ActionNextDetailsTab extends UMLAction {
  public ActionNextDetailsTab() { super("Next Details Tab", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    DetailsPane dp = pb.getDetailsPane();
    dp.selectNextTab();
  }
} /* end class ActionNextDetailsTab */

// class ActionPrevDetailsTab extends UMLAction {
//   public ActionPrevDetailsTab() { super("Previous Details Tab"); }
// } /* end class ActionPrevDetailsTab */


class ActionShowRapidButtons extends UMLAction {
  public ActionShowRapidButtons() {
    super("Buttons on Selection", NO_ICON);
  }
  public void actionPerformed(ActionEvent ae) {
    SelectionWButtons.toggleShowRapidButtons();
  }
} /* end class ActionShowRapidButtons */


////////////////////////////////////////////////////////////////
// items on create menu

class ActionCreateMultiple extends UMLAction {
  public ActionCreateMultiple() { super("Create Multiple...", NO_ICON); }
  public boolean shouldBeEnabled() {
    //Project p = ProjectBrowser.TheInstance.getProject();
    //return super.shouldBeEnabled() && p != null;
    return false;
  }
} /* end class ActionCreateMultiple */


////////////////////////////////////////////////////////////////
// generate menu actions

// class ActionGenerateWeb extends UMLAction {
//   public ActionGenerateWeb() { super("Generate Web Site", NO_ICON); }

//   public void actionPerformed(ActionEvent ae) {

//   }

//   public boolean shouldBeEnabled() {
//     return false;
//   }
// } /* end class ActionGenerateWeb */

////////////////////////////////////////////////////////////////
// critiquing related actions

class ActionAutoCritique extends UMLAction {
  public ActionAutoCritique() {
    super("Toggle Auto-Critique", NO_ICON);
  }
  public void actionPerformed(ActionEvent ae) {
    Designer d = Designer.TheDesigner;
    boolean b = d.getAutoCritique();
    d.setAutoCritique(!b);
  }
} /* end class ActionAutoCritique */

class ActionOpenDecisions extends UMLAction {
  public ActionOpenDecisions() { super("Design Issues...", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    DesignIssuesDialog d = new DesignIssuesDialog(ProjectBrowser.TheInstance);
    d.show();
  }
} /* end class ActionOpenDecisions */

class ActionOpenGoals extends UMLAction {
  public ActionOpenGoals() { super("Design Goals...", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    GoalsDialog d = new GoalsDialog(ProjectBrowser.TheInstance);
    d.show();
  }
} /* end class ActionOpenGoals */

class ActionOpenCritics extends UMLAction {
  public ActionOpenCritics() { super("Browse Critics...", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    CriticBrowserDialog dialog = new CriticBrowserDialog();
    dialog.show();
  }

} /* end class ActionOpenCritics */


class ActionFlatToDo extends UMLAction {
  public ActionFlatToDo() { super("Toggle Flat View", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    ToDoPane pane = pb.getToDoPane();
    pane.toggleFlat();
  }
} /* end class ActionFlatToDo */

class ActionNewToDoItem extends UMLAction {
  public ActionNewToDoItem() { super("New To Do Item..."); }
  public void actionPerformed(ActionEvent ae) {
    AddToDoItemDialog dialog = new AddToDoItemDialog();
    dialog.show();
  }
} /* end class ActionNewToDoItem */

class ToDoItemAction extends UMLAction {
  Object _target = null;
  public ToDoItemAction(String name) { super(name, false, HAS_ICON); }
  public ToDoItemAction(String name, boolean hasIcon) {
    super(name, false, hasIcon);
  }

  public void updateEnabled(Object target) {
    if (target == null) return;
    _target = target;
    setEnabled(shouldBeEnabled(target));
  }

  public boolean shouldBeEnabled(Object target) {
    return target instanceof ToDoItem;
  }
}

class ActionResolve extends ToDoItemAction {
  public ActionResolve() { super("Resolve Item..."); }
  public void actionPerformed(ActionEvent ae) {
    DismissToDoItemDialog dialog = new DismissToDoItemDialog();
    dialog.setTarget(_target);
    dialog.setVisible(true);
  }
} /* end class ActionResolve */

class ActionEmailExpert extends ToDoItemAction {
  public ActionEmailExpert() { super("Send Email To Expert..."); }
  public void actionPerformed(ActionEvent ae) {
    EmailExpertDialog dialog = new EmailExpertDialog();
    dialog.setTarget(_target);
    dialog.show();
  }
} /* end class ActionEmailExpert */

class ActionMoreInfo extends ToDoItemAction {
  public ActionMoreInfo() { super("More Info...", NO_ICON); }
} /* end class ActionMoreInfo */

class ActionSnooze extends ToDoItemAction {
  public ActionSnooze() { super("Snooze Critic"); }
  public void actionPerformed(ActionEvent ae) {
    if (!(_target instanceof ToDoItem)) return;
    ToDoItem item = (ToDoItem) _target;
    Poster p = item.getPoster();
    p.snooze();
    TabToDo._numHushes++;
  }
} /* end class ActionSnooze */


////////////////////////////////////////////////////////////////
// general user interface actions

class ActionAboutArgoUML extends UMLAction {
  public ActionAboutArgoUML() { super("About Argo/UML", NO_ICON); }

  public void actionPerformed(ActionEvent ae) {
    AboutBox box = new AboutBox();
    box.show();
  }
  public boolean shouldBeEnabled() { return true; }
} /* end class ActionAboutArgoUML */

/** display a help manual */
class ActionHelp extends UMLAction {

    private HelpSet mainHS = null;
    private HelpBroker mainHB = null;
    private String helpsetName = null;


    public ActionHelp(String menuName, String helpsetName) { 
        super(menuName, NO_ICON); 
        this.helpsetName = helpsetName;
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            ClassLoader cl = ActionHelp.class.getClassLoader();
            URL url = HelpSet.findHelpSet(cl, helpsetName);
            mainHS = new HelpSet(cl, url);
        } catch (Exception ee) {
            System.out.println ("Help Set "+helpsetName+" not found");
            return;
        } catch (ExceptionInInitializerError ex) {
            System.err.println("initialization error:");
            ex.getException().printStackTrace();
        }
        JFrame helpFrame = new JFrame();
        showHelp(helpFrame, mainHS, null);
    }

    public void showHelp(java.awt.Component comp, 
                            javax.help.HelpSet hs,
                         String helpID) {
        DefaultHelpBroker hb = new DefaultHelpBroker();
        hb.setHelpSet(hs);
        Window window = null;
        if (comp instanceof Window)
            window = (Window)comp;
        else
            window = (Window)SwingUtilities.getAncestorOfClass(Window.class,comp);
        if (window != null)
            hb.setActivationWindow(window);
        if (helpID != null)
            hb.setCurrentID(helpID);
        hb.setDisplayed(true);
    }
    public boolean shouldbeEnabled() { return true; }
}
