/**
 * Copyright 2015 markiewb
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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author markiewb
 */
public class FQNGeneratorTest {

    @Test
    public void formatType_shortenJavaLang() {
        assertEquals("String", FQNGenerator.formatType("java.lang.String"));
        assertEquals("XXX", FQNGenerator.formatType("XXX"));
        assertEquals("java.util.XXX", FQNGenerator.formatType("java.util.XXX"));
    }

}
