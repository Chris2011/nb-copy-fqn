/**
 * Copyright 2013 markiewb
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.markiewb.netbeans.plugin.copyfqn;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import org.netbeans.api.java.source.TreePathHandle;
import org.openide.awt.*;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.openide.util.actions.Presenter;

@ActionID(category = "Edit",
	id = "org.netbeans.module.copyfqn.actions.CopyFQNActionForMemberView")
@ActionRegistration(displayName = "CopyFQNActionForMemberView")
@ActionReferences({
    @ActionReference(path = "Navigator/Actions/Members/text/x-java", position = 1150),
    @ActionReference(path = "Navigator/Actions/Hierarchy/text/x-java", position = 1150),
    @ActionReference(path = "Shortcuts", name = "C-J C-I")
})
/**
 * Copies the fully qualified name of the selected java element(s) in the
 * members and hierarchy view.
 *
 * @author benno.markiewicz@googlemail.com
 */
public final class CopyFQNActionForMemberView extends CookieAction implements Presenter.Popup {

    public CopyFQNActionForMemberView() {
	putValue(NAME, Bundle.CopyFQNActionForMemberView_label());
	putValue(SMALL_ICON, ImageUtilities.loadImageIcon("de/markiewb/netbeans/plugin/copyfqn/fqn.gif", false));
    }

    @NbBundle.Messages({
	"CopyFQNActionForMemberView.name=Copy qualified name (members view)",
	"CopyFQNActionForMemberView.label=Copy qualified name"
    })
    @Override
    public String getName() {
	return Bundle.CopyFQNActionForMemberView_name();
    }

    @Override
    public JMenuItem getPopupPresenter() {
	return new JMenuItem(this);
    }

    @Override
    public HelpCtx getHelpCtx() {
	return null;
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
	//.. use tph from lookup in node
	for (Node node : activatedNodes) {
	    if (null != node.getLookup().lookup(TreePathHandle.class)) {
		return true;
	    };
	}
	return false;

    }

    @Override
    protected int mode() {
	return CookieAction.MODE_ALL;
    }

    @Override
    protected Class[] cookieClasses() {
	return new Class[]{Node.class};
    }

    @Override
    protected void performAction(Node[] nodes) {

	List<TreePathHandle> treePathHandles = new ArrayList<TreePathHandle>();
	for (Node node : nodes) {
	    treePathHandles.add(node.getLookup().lookup(TreePathHandle.class));
	}
	List<String> fqns = FQNGenerator.getAllFQNFor(treePathHandles);
	// copy collected fqns to clipboard 
	new Clipboard().copyToClipboard(fqns, System.getProperty("line.separator", "\n"));

    }
}
