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

import java.util.List;

/**
 *
 * @author benno.markiewicz@googlemail.com
 */
public abstract class StringUtils {

    public static String join(List<?> resultList, String sep) {
	if (null == resultList) {
	    return "";
	}
	if (resultList.isEmpty()) {
	    return "";
	}

	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < resultList.size(); i++) {
	    Object item = resultList.get(i);
	    if (i > 0) {
		sb.append(sep);
	    }
	    sb.append(item.toString());
	}
	return sb.toString();


    }
}
