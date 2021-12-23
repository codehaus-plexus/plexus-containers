package org.codehaus.plexus.logging;

/*
 * Copyright 2001-2006 Codehaus Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 *
 * @author peter at realityforge.org Peter Donald
 */
public class LogEnabledTest
{
    @Test
    public void testGetLogger()
    {
        MockLogEnabled logEnabled = new MockLogEnabled();
        MockLogger logger = new MockLogger( "base" );
        logEnabled.enableLogging( logger );
        assertEquals( "logger", logger, logEnabled.getLogger() );
    }

    @Test
    public void testSetupLoggerOnLogEnabled()
    {
        MockLogEnabled logEnabled = new MockLogEnabled();
        MockLogEnabled childLogEnabled = new MockLogEnabled();
        MockLogger logger = new MockLogger( "base" );
        logEnabled.enableLogging( logger );
        logEnabled.setupLogger( childLogEnabled );
        assertEquals( "logEnabled.logger", logger, logEnabled.getLogger() );
        assertEquals( "childLogEnabled.logger", logger, childLogEnabled.getLogger() );
    }

    @Test
    public void testSetupLoggerOnNonLogEnabled()
    {
        MockLogEnabled logEnabled = new MockLogEnabled();
        MockLogger logger = new MockLogger( "base" );
        logEnabled.enableLogging( logger );
        logEnabled.setupLogger( new Object() );
    }

    @Test
    public void testSetupLoggerWithNameOnLogEnabled()
    {
        MockLogEnabled logEnabled = new MockLogEnabled();
        MockLogEnabled childLogEnabled = new MockLogEnabled();
        MockLogger logger = new MockLogger( "base" );
        logEnabled.enableLogging( logger );
        logEnabled.setupLogger( childLogEnabled, "child" );
        assertEquals( "logEnabled.logger", logger, logEnabled.getLogger() );
        assertEquals( "childLogEnabled.logger.name",
                "base.child",
                childLogEnabled.getLogger().getName() );
    }

    @Test
    public void testSetupLoggerWithNullName()
    {
        MockLogEnabled logEnabled = new MockLogEnabled();
        MockLogEnabled childLogEnabled = new MockLogEnabled();
        MockLogger logger = new MockLogger( "base" );
        logEnabled.enableLogging( logger );
        try
        {
            logEnabled.setupLogger( childLogEnabled, (String) null );
        }
        catch ( IllegalStateException npe )
        {
            return;
        }
        fail( "Expected to fail setting up child logger with null name" );
    }
}
