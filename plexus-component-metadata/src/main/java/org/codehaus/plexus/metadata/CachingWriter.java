package org.codehaus.plexus.metadata;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class CachingWriter extends StringWriter
{
    private final Path path;
    private final Charset charset;

    public CachingWriter( Path path, Charset charset )
    {
        this.path = Objects.requireNonNull( path );
        this.charset = Objects.requireNonNull( charset );
    }

    @Override
    public void close() throws IOException
    {
        String str = getBuffer().toString();
        if ( Files.exists( path ) )
        {
            String old = readString( path, charset );
            if ( str.equals( old ) )
            {
                return;
            }
        }
        writeString( path, str, charset );
    }

    private static String readString( Path path, Charset charset ) throws IOException
    {
        byte[] ba = Files.readAllBytes( path );
        return new String( ba, charset );
    }

    private static void writeString( Path path, String str, Charset charset ) throws IOException
    {
        byte[] ba = str.getBytes( charset );
        Files.write( path, ba );
    }
}
