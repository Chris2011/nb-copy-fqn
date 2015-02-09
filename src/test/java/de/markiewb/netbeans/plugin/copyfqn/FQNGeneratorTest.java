/**
 * Copyright 2014 markiewb
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

import static de.markiewb.netbeans.plugin.copyfqn.FQNGenerator.Option.OPTION_NOFQN;
import static de.markiewb.netbeans.plugin.copyfqn.FQNGenerator.Option.OPTION_ABREVIATE;
import java.util.EnumSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author markiewb
 */
public class FQNGeneratorTest {
    

    @Test
    public void formatType_noFQN() {
        final EnumSet<FQNGenerator.Option> options = EnumSet.of(OPTION_NOFQN);
        assertEquals("", FQNGenerator.formatType("", options));
        assertEquals("Foo", FQNGenerator.formatType("Foo", options));
        assertEquals("Foo", FQNGenerator.formatType("com.Foo", options));
        assertEquals("String", FQNGenerator.formatType("java.lang.String", options));
        assertEquals("", FQNGenerator.formatType("java.lang.String.", options));
    }
    @Test
    public void formatType_shortenup() {
        final EnumSet<FQNGenerator.Option> options = EnumSet.of(OPTION_ABREVIATE);
        assertEquals("", FQNGenerator.formatType("", options));
        assertEquals("Foo", FQNGenerator.formatType("Foo", options));
        assertEquals("c.Foo", FQNGenerator.formatType("com.Foo", options));
        assertEquals("j.l.String", FQNGenerator.formatType("java.lang.String", options));
        assertEquals("", FQNGenerator.formatType("java.lang.String.", options));
    }
    
}
