package org.codehaus.plexus;

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

import junit.framework.TestCase;

/**
 * This is the start of the sketch which outlines some of the things
 * I would like to do with components during runtime.
 *
 * @author Jason van Zyl
 *
 */
public class DyanamicComponentKungFuTest
    extends TestCase
{
    /**
     * Component additions during container operation.
     *
     * <ol>
     * <li>Add a component at runtime
     *    <ul>
     *       <li>Additions could be made by specifying an URL which will be compatible with Wagon
     *           and specifically Maven's way of using Wagon.</li>
     *    </ul>
     * </li>
     *
     * <li>Configure the dynamically added component
     *    <ul>
     *      <li>We need to be able to deal with different flavours of components but we can focus
     *       on Plexus components to start with. But some components may have meta information
     *       and some may not like pico components. But one of the first flavours I want to
     *       support is phoenix components because I specifically need the FTP server.</li>
     *    </ul>
     * </li>
     *
     * <li>Let the component perform its role</li>
     * <li>Suspend the component
     *    <ol type="a">
     *      <li>Define the criteria for which we can suspend a component
     *          <ul>
     *            <li>When there are no client connections?</li>
     *            <li>Even when there are no connections and a client tries to obtain a connection what do we do?</li>
     *            <li>If we are in desperate need to suspend the component, say for urgent security requirement, and
     *              clients simply won't bugger off what do we do?</li>
     *          </ul>
     *      </li>
     *    </ol>
     * </li>
     *
     * <li>Reconfigure the component</li>
     *
     * <li>Resume the component</li>
     *
     * <li>Let the component perform its role</li>
     *
     * <li>Release the component</li>
     * </ol>
     * 
     * @throws Exception in case of a failure.
     */
    public void testAdditionOfComponentDuringContainerOperation()
        throws Exception
    {
    }

    /**
     * Component replacement during container operation.
     *
     * This will force the design of a mechanism where the components communicate
     * with one another via a connector. In order for components to be replaced
     * dynamically the components cannot be directly coupled to one another.
     *
     * How to decide if a component is a suitable replacement given the versions
     * of the specifications of the component and any required components if the
     * component is a composite component.
     *
     * Definitely need to simulate the connection (a MockConnection) during
     * runtime to make sure that in the event something goes wrong the container
     * can just refuse to allow the component substitution. This shouldn't be trial
     * and error but until much field testing has occurred I'm sure there will be
     * instances where miscalculations happen simply due to lack of experience and
     * usage with dynamic component replacement.
     * 
     * @throws Exception in case of a failure.
     */
    public void testComponentReplacementDuringContainerOperation()
        throws Exception
    {

    }
}
