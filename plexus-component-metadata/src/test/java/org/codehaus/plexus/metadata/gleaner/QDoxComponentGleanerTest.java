/*
 * Copyright (C) 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.codehaus.plexus.metadata.gleaner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentRequirement;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link QDoxComponentGleaner} class.
 *
 * @version $Rev$ $Date$
 */
class QDoxComponentGleanerTest {
    private QDoxComponentGleaner gleaner;

    private JavaProjectBuilder builder;

    @BeforeEach
    public void setUp() {
        gleaner = new QDoxComponentGleaner();
        builder = new JavaProjectBuilder();
    }

    @AfterEach
    protected void tearDown() {
        gleaner = null;
        builder = null;
    }

    private JavaSource addSource(final String name) throws IOException {
        File url = new File(
                ".", "src/test/java/" + this.getClass().getPackage().getName().replace('.', '/') + "/" + name);
        assertTrue(url.exists());
        return builder.addSource(url);
    }

    private JavaClass loadJavaClass(final String name) throws IOException {
        JavaSource source = addSource(name);
        assertNotNull(source);

        List<JavaClass> classes = source.getClasses();
        assertNotNull(classes);
        assertEquals(1, classes.size());

        assertNotNull(classes.get(0));

        return classes.get(0);
    }

    private ComponentDescriptor<?> glean(final String name, final String[] supporting) throws Exception {
        if (supporting != null) {
            for (String aSupporting : supporting) {
                addSource(aSupporting);
            }
        }

        return gleaner.glean(builder, loadJavaClass(name));
    }

    private ComponentDescriptor<?> glean(final String name) throws Exception {
        return glean(name, null);
    }

    @Test
    void testNoAnnotationsClass() throws Exception {
        ComponentDescriptor<?> component = glean("NoAnnotationsClass.java");
        assertNull(component);
    }

    @Test
    void testAbstractClass() throws Exception {
        ComponentDescriptor<?> component = glean("AbstractClass.java");
        assertNull(component);
    }

    /*
    public void testAbstractWithAnnoClass() throws Exception {
        ComponentDescriptor component = glean("AbstractWithAnnoClass.java");
        assertNull(component);
    }
    */

    @Test
    void testNoAnnotationsIntf() throws Exception {
        ComponentDescriptor<?> component = glean("NoAnnotationsIntf.java");
        assertNull(component);
    }

    @Test
    void testMyComponent() throws Exception {
        addSource("ChildComponent.java");
        ComponentDescriptor<?> component = glean("MyComponent.java");
        assertNotNull(component);

        assertEquals(MyComponent.class.getName(), component.getRole());
        assertEquals("foo", component.getRoleHint());

        List<ComponentRequirement> requirements = component.getRequirements();
        assertNotNull(requirements);
        assertEquals(1, requirements.size());

        ComponentRequirement requirement = requirements.get(0);
        assertNotNull(requirement);
        assertEquals(ChildComponent.class.getName(), requirement.getRole());

        PlexusConfiguration config = component.getConfiguration();
        assertNotNull(config);
        assertEquals(1, config.getChildCount());

        PlexusConfiguration child = config.getChild(0);
        assertNotNull(child);
        assertEquals("foo", child.getName());
        assertEquals("bar", child.getValue());
    }
}
