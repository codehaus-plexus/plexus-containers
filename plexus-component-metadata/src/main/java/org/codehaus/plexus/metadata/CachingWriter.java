package org.codehaus.plexus.metadata;

/*
 * Copyright (c) 2022, Codehaus.org
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

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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
        byte[] data = getBuffer().toString().getBytes( charset );
        if ( Files.exists( path ) && Files.size( path ) == data.length )
        {
            byte[] ba = Files.readAllBytes( path );
            if ( Arrays.equals( data, ba ) )
            {
                return;
            }
        }
        Files.write( path, data );
    }

}
