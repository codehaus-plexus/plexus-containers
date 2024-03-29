/*
 * Copyright (C) 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.plexus.metadata.gleaner;

import java.util.List;

import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentRequirement;
import org.codehaus.plexus.component.repository.ComponentRequirementList;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.metadata.gleaner.ann.AnnotatedComponent;
import org.codehaus.plexus.metadata.gleaner.ann.AnnotatedComponentRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Eugene Kuleshov
 */
class AnnotationComponentGleanerTest {

    @Test
    void testGlean() throws Exception {
        AnnotationComponentGleaner gleaner = new AnnotationComponentGleaner();
        Class<AnnotatedComponent> c = AnnotatedComponent.class;
        ComponentDescriptor<?> descriptor = gleaner.glean(c.getName(), c.getClassLoader());

        assertEquals("foo", descriptor.getComponentType());
        assertEquals(AnnotatedComponentRole.class.getName(), descriptor.getRole());

        List<ComponentRequirement> requirements = descriptor.getRequirements();
        assertEquals(2, requirements.size(), requirements.toString());

        ComponentRequirement requirement = requirements.get(0);
        assertEquals("dependency", requirement.getFieldName());
        assertEquals("default", requirement.getRoleHint());

        ComponentRequirement requirement2 = requirements.get(1);
        assertEquals("dependency2", requirement2.getFieldName());
        assertTrue(requirement2 instanceof ComponentRequirementList);
        assertEquals(
                "release,latest,snapshot", String.join(",", ((ComponentRequirementList) requirement2).getRoleHints()));

        PlexusConfiguration configuration = descriptor.getConfiguration();
        assertEquals(1, configuration.getChildCount());
        PlexusConfiguration child = configuration.getChild(0);
        assertEquals("param", child.getName());
        assertEquals("value", child.getValue());
    }
}
