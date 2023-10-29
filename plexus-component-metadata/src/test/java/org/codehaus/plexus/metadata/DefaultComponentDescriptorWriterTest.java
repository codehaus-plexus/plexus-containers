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

import javax.inject.Inject;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.repository.*;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.metadata.merge.ComponentsXmlMerger;
import org.codehaus.plexus.metadata.merge.Merger;
import org.codehaus.plexus.metadata.merge.PlexusXmlMerger;
import org.codehaus.plexus.testing.PlexusTest;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for the {@link DefaultComponentDescriptorWriter} class.
 *
 * @version $Rev$ $Date$
 */
@PlexusTest
class DefaultComponentDescriptorWriterTest {

    @Inject
    private DefaultComponentDescriptorWriter descriptorWriter;

    @Inject
    private MetadataGenerator generator;

    @Test
    void testBasic() throws Exception {
        ComponentSetDescriptor set = new ComponentSetDescriptor();

        ComponentDescriptor<String> component = new ComponentDescriptor<>();
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

        PlexusConfiguration config = new XmlPlexusConfiguration(Xpp3DomBuilder.build(new StringReader(xml)));
        assertNotNull(config);

        ClassWorld classWorld = new ClassWorld("test", Thread.currentThread().getContextClassLoader());
        ClassRealm realm = classWorld.getRealm("test");
        ComponentSetDescriptor set2 = buildComponentSet(config, realm);
        assertNotNull(set2);

        List<ComponentDescriptor<?>> components = set2.getComponents();
        assertNotNull(components);
        assertEquals(1, components.size());

        ComponentDescriptor<?> component2 = components.get(0);
        assertNotNull(component2);

        assertEquals(component.getRole(), component2.getRole());
        assertEquals(component.getRoleHint(), component2.getRoleHint());
        assertEquals(component.getComponentFactory(), component2.getComponentFactory());

        //
        // TODO: Verify requirements and configuration too... but I'm too lazy for that right now
        //
    }

    @Test
    void testComponentsOrder() throws Exception {
        assertNotNull(generator);

        MetadataGenerationRequest request = new MetadataGenerationRequest();
        request.sourceDirectories = Collections.singletonList("src/main/java");
        request.classesDirectory = new File("target/classes");
        request.outputFile = new File("target/test-classes/components-sorted.xml");
        request.sourceEncoding = "UTF-8";
        request.useContextClassLoader = true;

        generator.generateDescriptor(request);

        assertTrue(request.outputFile.exists(), "Descriptor not generated");

        Document doc = new SAXBuilder().build(request.outputFile);

        // check if the components are sorted by role+impl
        List<Element> components = doc.getRootElement().getChild("components").getChildren();
        assertEquals(5, components.size(), "Number of components");

        assertEquals(
                ComponentDescriptorExtractor.class.getName(),
                components.get(0).getChild("role").getText(),
                "Component 1 role");
        assertEquals(
                ClassComponentDescriptorExtractor.class.getName(),
                components.get(0).getChild("implementation").getText(),
                "Component 1 impl");

        assertEquals(
                ComponentDescriptorExtractor.class.getName(),
                components.get(1).getChild("role").getText(),
                "Component 2 role");
        assertEquals(
                SourceComponentDescriptorExtractor.class.getName(),
                components.get(1).getChild("implementation").getText(),
                "Component 2 impl");

        assertEquals(
                MetadataGenerator.class.getName(),
                components.get(2).getChild("role").getText(),
                "Component 3 role");
        assertEquals(
                DefaultMetadataGenerator.class.getName(),
                components.get(2).getChild("implementation").getText(),
                "Component 3 impl");

        assertEquals(Merger.class.getName(), components.get(3).getChild("role").getText(), "Component 4 role");
        assertEquals(
                ComponentsXmlMerger.class.getName(),
                components.get(3).getChild("implementation").getText(),
                "Component 4 impl");

        assertEquals(Merger.class.getName(), components.get(4).getChild("role").getText(), "Component 5 role");
        assertEquals(
                PlexusXmlMerger.class.getName(),
                components.get(4).getChild("implementation").getText(),
                "Component 5 impl");
    }

    // TODO copied from PlexusTools.buildConfiguration() - find a better way to do this
    // we have duplication here, but we don't want to depend on plexus-container-default
    // similar code in AnnotationComponentGleaner.glean() and QDoxComponentGleaner.findRequirements()
    private static ComponentSetDescriptor buildComponentSet(PlexusConfiguration c, ClassRealm realm)
            throws PlexusConfigurationException {
        ComponentSetDescriptor csd = new ComponentSetDescriptor();
        for (PlexusConfiguration component : c.getChild("components").getChildren("component")) {
            csd.addComponentDescriptor(buildComponentDescriptorImpl(component, realm));
        }

        for (PlexusConfiguration d : c.getChild("dependencies").getChildren("dependency")) {
            ComponentDependency cd = new ComponentDependency();
            cd.setArtifactId(d.getChild("artifact-id").getValue());
            cd.setGroupId(d.getChild("group-id").getValue());
            String type = d.getChild("type").getValue();
            if (type != null) {
                cd.setType(type);
            }
            cd.setVersion(d.getChild("version").getValue());
            csd.addDependency(cd);
        }
        return csd;
    }

    private static ComponentDescriptor<?> buildComponentDescriptorImpl(
            PlexusConfiguration configuration, ClassRealm realm) throws PlexusConfigurationException {
        String implementation = configuration.getChild("implementation").getValue();
        if (implementation == null) {
            throw new PlexusConfigurationException("implementation is null");
        }

        ComponentDescriptor<?> cd;
        try {
            if (realm != null) {
                Class<?> implementationClass = realm.loadClass(implementation);
                cd = new ComponentDescriptor<>(implementationClass, realm);
            } else {
                cd = new ComponentDescriptor<>();
                cd.setImplementation(implementation);
            }
        } catch (Throwable e) {
            throw new PlexusConfigurationException(
                    "Can not load implementation class " + implementation + " from realm " + realm, e);
        }
        cd.setRole(configuration.getChild("role").getValue());
        cd.setRoleHint(configuration.getChild("role-hint").getValue());
        cd.setVersion(configuration.getChild("version").getValue());
        cd.setComponentType(configuration.getChild("component-type").getValue());
        cd.setInstantiationStrategy(
                configuration.getChild("instantiation-strategy").getValue());
        cd.setLifecycleHandler(configuration.getChild("lifecycle-handler").getValue());
        cd.setComponentProfile(configuration.getChild("component-profile").getValue());
        cd.setComponentComposer(configuration.getChild("component-composer").getValue());
        cd.setComponentConfigurator(
                configuration.getChild("component-configurator").getValue());
        cd.setComponentFactory(configuration.getChild("component-factory").getValue());
        cd.setDescription(configuration.getChild("description").getValue());
        cd.setAlias(configuration.getChild("alias").getValue());
        String s = configuration.getChild("isolated-realm").getValue();

        if (s != null) {
            cd.setIsolatedRealm(s.equals("true"));
        }

        // ----------------------------------------------------------------------
        // Here we want to look for directives for inlining external
        // configurations. we probably want to take them from files or URLs.
        // ----------------------------------------------------------------------
        cd.setConfiguration(configuration.getChild("configuration"));
        // ----------------------------------------------------------------------
        // Requirements
        // ----------------------------------------------------------------------
        for (PlexusConfiguration requirement :
                configuration.getChild("requirements").getChildren("requirement")) {
            ComponentRequirement cr;

            PlexusConfiguration[] hints = requirement.getChild("role-hints").getChildren("role-hint");
            if (hints != null && hints.length > 0) {
                cr = new ComponentRequirementList();

                List<String> hintList = new LinkedList<>();
                for (PlexusConfiguration hint : hints) {
                    hintList.add(hint.getValue());
                }

                ((ComponentRequirementList) cr).setRoleHints(hintList);
            } else {
                cr = new ComponentRequirement();

                cr.setRoleHint(requirement.getChild("role-hint").getValue());
            }
            cr.setRole(requirement.getChild("role").getValue());
            cr.setOptional(Boolean.parseBoolean(requirement.getChild("optional").getValue()));
            cr.setFieldName(requirement.getChild("field-name").getValue());
            cd.addRequirement(cr);
        }
        return cd;
    }
}
