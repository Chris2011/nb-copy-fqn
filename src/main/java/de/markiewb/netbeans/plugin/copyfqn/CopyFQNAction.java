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

import com.sun.source.util.TreePath;
import de.markiewb.netbeans.plugin.copyfqn.FQNGenerator.AbstractMemberVisitor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.*;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.CompilationInfo;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.Task;
import org.netbeans.api.java.source.support.CaretAwareJavaSourceTaskFactory;
import org.openide.awt.*;
import org.openide.loaders.DataObject;

import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

@ActionID(category = "Edit", id = "de.markiewb.netbeans.plugin.copyfqn.CopyFQNAction")
@ActionRegistration(displayName = "#CTL_CopyFQNAction")
@ActionReferences({
    @ActionReference(path = "Editors/text/x-java/Popup", position = 1455, separatorAfter = 1467),
    @ActionReference(path = "Shortcuts", name = "C-J C-C")
})
/**
 * Copies the fully qualified name of the java class/member/method/constructor
 * under the caret (in the java source editor).
 *
 * Based on http://platform.netbeans.org/tutorials/nbm-copyfqn.html. Nearly the
 * same code as
 * http://plugins.netbeans.org/PluginPortal/faces/PluginDetailPage.jsp?pluginid=2753,
 * but this code is compatible to NB 7.1.RC2 and merged into the original file
 * structure.
 *
 */
public final class CopyFQNAction implements ActionListener {

    private final DataObject context;

    public CopyFQNAction(DataObject context) {
	this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {

	final List<String> resultList = new ArrayList<String>();

	{
	    final FileObject fileObject = context.getPrimaryFile();
	    if (null != fileObject) {

		JavaSource javaSource = JavaSource.forFileObject(fileObject);
		if (null != javaSource) {
		    //recognized java source
		    try {
			Task<CompilationController> task = new Task<CompilationController>() {
			    @Override
			    public void run(CompilationController compilationController) throws Exception {
				compilationController.toPhase(JavaSource.Phase.ELEMENTS_RESOLVED);

				//collects fqn if supported
				final AbstractMemberVisitor visitor = new AbstractMemberVisitor(compilationController) {
				    @Override
				    public Element getElement(CompilationInfo info) {
					TreePath tp = info.getTreeUtilities().pathFor(CaretAwareJavaSourceTaskFactory.getLastPosition(fileObject));
					Element element = info.getTrees().getElement(tp);
					return element;
				    }
				};
				visitor.scan(compilationController.getCompilationUnit(), null);

				resultList.addAll(visitor.getResultList());

			    }
			};
			javaSource.runUserActionTask(task, true);
		    } catch (IOException ex) {
			Exceptions.printStackTrace(ex);
		    }
		}
	    }
	}
	// copy collected fqns to clipboard
	new Clipboard().copyToClipboard(resultList, System.getProperty("line.separator", "\n"));
    }
}
