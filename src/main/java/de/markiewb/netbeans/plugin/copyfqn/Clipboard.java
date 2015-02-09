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

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Message;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExClipboard;

/**
 *
 * @author benno.markiewicz@googlemail.com
 */
public class Clipboard {

    private java.awt.datatransfer.Clipboard clipboard;

    public Clipboard() {
	clipboard = Lookup.getDefault().lookup(ExClipboard.class);
	if (clipboard == null) {
	    clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
    }

    /**
     * Joins a list of strings using a separator and copies the result to the
     * system clipboard.
     *
     * @param resultList
     * @param endOfLineSeparator
     */
    public void copyToClipboard(final java.util.List<java.lang.String> resultList, String endOfLineSeparator) {
	if (null != resultList && !resultList.isEmpty()) {
	    // copy collected fqns to clipboard 
	    String text = StringUtils.join(resultList, endOfLineSeparator);
	    int size = resultList.size();
	    StringSelection content = new StringSelection(text);
	    if (size >= 2) {
		StatusDisplayer.getDefault().setStatusText("Copied " + size + " fully qualified names to clipboard");
	    } else {
		StatusDisplayer.getDefault().setStatusText("Copied '" + text + "' to clipboard");
	    }

	    clipboard.setContents(content, null);
	} else {
//	            if (null == fqn || "".equals(fqn)) {
	    Message message = new NotifyDescriptor.Message("No fully qualified name for selected element found.");
	    DialogDisplayer.getDefault().notify(message);
//        }
	}
    }

    /**
     * Copies a text to the system clipboard.
     *
     * @param text
     */
    public void copyToClipboard(String text) {
	copyToClipboard(Arrays.asList(text), null);
    }
}
