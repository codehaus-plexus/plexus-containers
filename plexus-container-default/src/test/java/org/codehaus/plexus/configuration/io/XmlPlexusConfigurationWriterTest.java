package org.codehaus.plexus.configuration.io;

import java.io.StringReader;
import java.io.StringWriter;

import org.codehaus.plexus.configuration.ConfigurationTestHelper;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.junit.Test;

public class XmlPlexusConfigurationWriterTest
{

    @Test
    public void testWrite()
        throws Exception
    {
        PlexusConfiguration c = ConfigurationTestHelper.getTestConfiguration();

        XmlPlexusConfigurationWriter cw = new XmlPlexusConfigurationWriter();

        StringWriter writer = new StringWriter();

        cw.write( writer, c );

        Xpp3Dom dom = Xpp3DomBuilder.build( new StringReader( writer.toString() ) );

        XmlPlexusConfiguration c1 = new XmlPlexusConfiguration( dom );
        
        ConfigurationTestHelper.testConfiguration( c1 );
    }

}
