package org.codehaus.plexus.metadata;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.tools.cli.AbstractCli;

public class PlexusMetadataGeneratorCli
    extends AbstractCli
{
    public static final char SOURCE_DIRECTORY = 's';
    public static final char CLASSES_DIRECTORY = 'c';
    public static final char OUTPUT_FILE = 'o';
    public static final char DESCRIPTORS_DIRECTORY = 'm';

    public static void main( String[] args )
        throws Exception
    {
        new PlexusMetadataGeneratorCli().execute( args );
    }

    @Override
    public String getPomPropertiesPath()
    {
        return "META-INF/maven/org.codehaus.plexus/plexus-metadata-generator/pom.properties";
    }

    @Override
    @SuppressWarnings("static-access")
    public Options buildCliOptions( Options options )
    {
        options.addOption( OptionBuilder.withLongOpt( "source" ).hasArg().withDescription( "Source directory." ).create( SOURCE_DIRECTORY ) );
        options.addOption( OptionBuilder.withLongOpt( "classes" ).hasArg().withDescription( "Classes directory." ).create( CLASSES_DIRECTORY ) );
        options.addOption( OptionBuilder.withLongOpt( "output" ).hasArg().withDescription( "Output directory." ).create( OUTPUT_FILE ) );
        options.addOption( OptionBuilder.withLongOpt( "descriptors" ).hasArg().withDescription( "Descriptors directory." ).create( DESCRIPTORS_DIRECTORY ) );
        return options;
    }    

    public void invokePlexusComponent( CommandLine cli, PlexusContainer plexus )
        throws Exception
    {
        MetadataGenerator metadataGenerator = (MetadataGenerator) plexus.lookup( MetadataGenerator.class );
        
        MetadataGenerationRequest request = new MetadataGenerationRequest();        
        request.classesDirectory = new File( cli.getOptionValue( CLASSES_DIRECTORY ) );
        request.classpath = Collections.EMPTY_LIST;
        request.sourceDirectories = Arrays.asList( new String[]{ new File( cli.getOptionValue( SOURCE_DIRECTORY ) ).getAbsolutePath() } );
        request.useContextClassLoader = true;
        request.outputFile = new File( cli.getOptionValue( OUTPUT_FILE ) );
        request.componentDescriptorDirectory = new File( cli.getOptionValue( DESCRIPTORS_DIRECTORY ) );
        
        metadataGenerator.generateDescriptor( request );
    }
}