package org.codehaus.plexus.metadata;

/*
 * The MIT License
 * 
 * Copyright (c) 2004-2006, The Codehaus
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.component.repository.ComponentDependency;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.component.repository.ComponentSetDescriptor;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.metadata.gleaner.AnnotationComponentGleaner;
import org.codehaus.plexus.metadata.merge.Merger;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

/**
 * @author Jason van Zyl
 */
@Component(role = MetadataGenerator.class)
public class DefaultMetadataGenerator
    extends AbstractLogEnabled
    implements MetadataGenerator
{
    @Requirement
    private Merger merger;

    private ComponentDescriptor<?>[] roleDefaults;

    @Requirement
    private Map<String, ComponentDescriptorExtractor> extractorMap;

    // should be a component
    private ComponentDescriptorWriter writer = new DefaultComponentDescriptorWriter();

    public void generateDescriptor( MetadataGenerationRequest request )
        throws Exception
    {
        assert request.outputFile != null;

        List<String> extractorHints = request.extractors;
        
        final Collection<ComponentDescriptorExtractor> extractors;
        if ( extractorHints == null || extractorHints.size() == 0 )
        {
            extractors = extractorMap.values();
        }
        else
        {
            extractors = new ArrayList<ComponentDescriptorExtractor>( extractorHints.size() );
            
            for ( String hint : extractorHints )
            {
                extractors.add( extractorMap.get( hint ) );
            }
        }

        List<ComponentDescriptor<?>> descriptors = new ArrayList<ComponentDescriptor<?>>();

        for ( ComponentDescriptorExtractor extractor : extractors )
        {
            try
            {
                List<ComponentDescriptor<?>> list = extractor.extract( request, roleDefaults );
                if ( list != null && !list.isEmpty() )
                {
                    descriptors.addAll( list );
                }
            }
            catch ( Exception e )
            {
                throw new Exception( "Failed to extract descriptors", e );
            }
        }

        // Sort the descriptors by key to make the output reproducible
        Collections.sort( descriptors, new Comparator<ComponentDescriptor>()
        {
            public int compare( ComponentDescriptor d1, ComponentDescriptor d2 )
            {
                return d1.getHumanReadableKey().compareTo( d2.getHumanReadableKey() );
            }
        });

        List<File> componentDescriptors = new ArrayList<File>();        
        
        //
        // If we found descriptors, write out the discovered descriptors
        //
        if ( descriptors.size() > 0 )
        {
            getLogger().info( "Discovered " + descriptors.size() + " component descriptors(s)" );

            ComponentSetDescriptor set = new ComponentSetDescriptor();
            set.setComponents( descriptors );
            set.setDependencies( Collections.<ComponentDependency> emptyList() );
            
            if ( request.componentDescriptorDirectory == null )
            {
                writeDescriptor( set, request.outputFile );
            }
            else
            {
                if ( request.intermediaryFile == null )
                {
                    request.intermediaryFile = File.createTempFile( "plexus-metadata", "xml" );
                    request.intermediaryFile.deleteOnExit();
                }
                writeDescriptor( set, request.intermediaryFile );
                componentDescriptors.add( request.intermediaryFile );
            }
        }
        

        //
        // Deal with merging
        //
        if ( request.componentDescriptorDirectory != null && request.componentDescriptorDirectory.isDirectory() )
        {
            File[] files = request.componentDescriptorDirectory.listFiles();

            // Sort the files by name to make the output reproducible
            Arrays.sort( files, new Comparator<File>()
            {
                public int compare( File f1, File f2 )
                {
                    return f1.getName().compareTo( f2.getName() );
                }
            });

            int added = 0;
            for ( File file : files )
            {
                if ( file.getName().endsWith( ".xml" ) && !file.getName().equals( "plexus.xml" ) )
                {
                    componentDescriptors.add( file );
                    added++;
                }
            }

            getLogger().info( "Merging " + added + " manually-crafted descriptor file(s)" );
        }

        if ( componentDescriptors.size() > 0 )
        {
            merger.mergeDescriptors( request.outputFile, componentDescriptors );
        }
    }

    private void writeDescriptor( ComponentSetDescriptor desc, File outputFile )
        throws Exception
    {
        assert desc != null;
        assert outputFile != null;

        FileUtils.forceMkdir( outputFile.getParentFile() );

        BufferedWriter output = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( outputFile ), "UTF-8" ) );

        try
        {
            writer.writeDescriptorSet( output, desc, false );
            output.flush();
        }
        finally
        {
            IOUtil.close( output );
        }

        getLogger().debug( "Wrote: " + outputFile );
    }
}
