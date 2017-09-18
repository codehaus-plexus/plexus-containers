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

package org.codehaus.plexus.metadata;

import java.io.File;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.repository.ComponentSetDescriptor;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.io.PlexusTools;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.metadata.merge.ComponentsXmlMerger;
import org.codehaus.plexus.metadata.merge.Merger;
import org.codehaus.plexus.metadata.merge.PlexusXmlMerger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * Test for the {@link DefaultComponentDescriptorWriter} class.
 *
 * @version $Rev$ $Date$
 */
public class DefaultComponentDescriptorWriterTest
    extends PlexusTestCase
{
    private DefaultComponentDescriptorWriter descriptorWriter;

    // @Override
    protected void setUp() throws Exception {
        super.setUp();

        descriptorWriter = (DefaultComponentDescriptorWriter) lookup(ComponentDescriptorWriter.class);
        assertNotNull(descriptorWriter);
    }

    // @Override
    protected void tearDown() throws Exception {
        descriptorWriter = null;

        super.tearDown();
    }

    public void testBasic() throws Exception {
        ComponentSetDescriptor set = new ComponentSetDescriptor();

        ComponentDescriptor component = new ComponentDescriptor();
        component.setImplementation("java.lang.String");
        component.setRole("foo");
        component.setRoleHint("bar");
        component.setComponentFactory("baz");

        set.addComponentDescriptor(component);

        StringWriter writer = new StringWriter();
        descriptorWriter.writeDescriptorSet(writer, set, false);
        writer.flush();
        writer.close();
        
        String xml = writer.toString();

        assertTrue(xml.length() != 0);

        PlexusConfiguration config = PlexusTools.buildConfiguration(xml);
        assertNotNull(config);
        
        ClassWorld classWorld = new ClassWorld( "test", Thread.currentThread().getContextClassLoader() );
        ClassRealm realm = classWorld.getRealm( "test" );
        org.codehaus.plexus.component.repository.ComponentSetDescriptor set2 = PlexusTools.buildComponentSet(config, realm);
        assertNotNull(set2);

        List components = set2.getComponents();
        assertNotNull(components);
        assertEquals(1, components.size());

        org.codehaus.plexus.component.repository.ComponentDescriptor component2 =
                (org.codehaus.plexus.component.repository.ComponentDescriptor) components.get(0);
        assertNotNull(component2);
        
        assertEquals(component.getRole(), component2.getRole());
        assertEquals(component.getRoleHint(), component2.getRoleHint());
        assertEquals(component.getComponentFactory(), component2.getComponentFactory());

        //
        // TODO: Verify requirements and configuration too... but I'm too lazy for that right now
        //
    }

    public void testComponentsOrder() throws Exception {
        MetadataGenerator generator = (MetadataGenerator) lookup(MetadataGenerator.class);
        assertNotNull(generator);

        MetadataGenerationRequest request = new MetadataGenerationRequest();
        request.sourceDirectories = Collections.singletonList("src/main/java");
        request.classesDirectory = new File("target/classes");
        request.outputFile = new File("target/test-classes/components-sorted.xml");
        request.sourceEncoding = "UTF-8";
        request.useContextClassLoader = true;

        generator.generateDescriptor(request);

        assertTrue("Descriptor not generated", request.outputFile.exists());

        Document doc = new SAXBuilder().build(request.outputFile);

        // check if the components are sorted by role+impl
        List<Element> components = doc.getRootElement().getChild("components").getChildren();
        assertEquals("Number of components", 5, components.size());

        assertEquals("Component 1 role", ComponentDescriptorExtractor.class.getName(), components.get(0).getChild("role").getText());
        assertEquals("Component 1 impl", ClassComponentDescriptorExtractor.class.getName(), components.get(0).getChild("implementation").getText());

        assertEquals("Component 2 role", ComponentDescriptorExtractor.class.getName(), components.get(1).getChild("role").getText());
        assertEquals("Component 2 impl", SourceComponentDescriptorExtractor.class.getName(), components.get(1).getChild("implementation").getText());

        assertEquals("Component 3 role", MetadataGenerator.class.getName(), components.get(2).getChild("role").getText());
        assertEquals("Component 3 impl", DefaultMetadataGenerator.class.getName(), components.get(2).getChild("implementation").getText());

        assertEquals("Component 4 role", Merger.class.getName(), components.get(3).getChild("role").getText());
        assertEquals("Component 4 impl", ComponentsXmlMerger.class.getName(), components.get(3).getChild("implementation").getText());

        assertEquals("Component 5 role", Merger.class.getName(), components.get(4).getChild("role").getText());
        assertEquals("Component 5 impl", PlexusXmlMerger.class.getName(), components.get(4).getChild("implementation").getText());
    }
}
