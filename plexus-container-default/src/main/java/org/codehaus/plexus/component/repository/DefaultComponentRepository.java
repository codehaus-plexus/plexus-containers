package org.codehaus.plexus.component.repository;

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

import static org.codehaus.plexus.component.CastUtils.isAssignableFrom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.codehaus.plexus.ClassRealmUtil;
import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.composition.CycleDetectedInComponentGraphException;
import org.codehaus.plexus.component.composition.CompositionResolver;
import org.codehaus.plexus.component.composition.DefaultCompositionResolver;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.StringUtils;

/**
 * @author Jason van Zyl
 */
public class DefaultComponentRepository
    extends AbstractLogEnabled
    implements ComponentRepository
{
    private final Map<ClassRealm, SortedMap<String, Map<String, Set<ComponentDescriptor<?>>>>> index =
            new LinkedHashMap<ClassRealm, SortedMap<String, Map<String, Set<ComponentDescriptor<?>>>>>();

    private final CompositionResolver compositionResolver = new DefaultCompositionResolver();

    public DefaultComponentRepository()
    {
    }

    // ----------------------------------------------------------------------
    // Accessors
    // ----------------------------------------------------------------------

    private Map<String, Set<ComponentDescriptor<?>>> getComponentDescriptors( String role )
    {
        // verify arguments
        if ( role == null )
        {
            throw new NullPointerException( "role is null" );
        }

        // determine realms to search
        Set<ClassRealm> realms = ClassRealmUtil.getContextRealms( null );
        if ( realms.isEmpty() )
        {
            realms.addAll( index.keySet() );
        }

        
        // Get all valid component descriptors
        Map<String, Set<ComponentDescriptor<?>>> roleHintIndex =
                new LinkedHashMap<String, Set<ComponentDescriptor<?>>>();
        for ( ClassRealm realm : realms )
        {
            SortedMap<String, Map<String, Set<ComponentDescriptor<?>>>> roleIndex = index.get( realm );
            if (roleIndex != null) {
                Map<String, Set<ComponentDescriptor<?>>> descriptors = roleIndex.get( role );
                if ( descriptors != null )
                {
                    for ( Entry<String, Set<ComponentDescriptor<?>>> descriptor : descriptors.entrySet() )
                    {
                        Set<ComponentDescriptor<?>> componentDescriptors = roleHintIndex.get( descriptor.getKey() );
                        if ( componentDescriptors == null )
                        {
                            componentDescriptors = new LinkedHashSet<ComponentDescriptor<?>>();
                            roleHintIndex.put( descriptor.getKey(), componentDescriptors );
                        }
                        componentDescriptors.addAll( descriptor.getValue() );
                    }

                }
            }
        }
        return Collections.unmodifiableMap( roleHintIndex );
    }

    public <T> ComponentDescriptor<T> getComponentDescriptor( Class<T> type, String role, String roleHint )
    {
        Map<String, Set<ComponentDescriptor<?>>> roleHintIndex = getComponentDescriptors( role );

        Collection<ComponentDescriptor<?>> descriptors;

        if ( StringUtils.isNotEmpty( roleHint ) )
        {
            // specific role hint -> get only those
            descriptors = roleHintIndex.get( roleHint );
        }
        else
        {
            // missing role hint -> get all (wildcard)
            Collection<ComponentDescriptor<?>> allDescriptors = new ArrayList<ComponentDescriptor<?>>();

            descriptors = roleHintIndex.get( PlexusConstants.PLEXUS_DEFAULT_HINT );
            if ( descriptors != null )
            {
                allDescriptors.addAll( descriptors );
            }

            for ( String hint : roleHintIndex.keySet() )
            {
                descriptors = roleHintIndex.get( hint );
                if ( descriptors != null )
                {
                    allDescriptors.addAll( descriptors );
                }
            }

            descriptors = allDescriptors;
        }

        if ( descriptors!= null )
        {
            for ( ComponentDescriptor<?> descriptor : descriptors )
            {
                Class<?> implClass = descriptor.getImplementationClass();
                if ( isAssignableFrom( type, implClass ) || Object.class == implClass && role.equals( type.getName() ) )
                {
                    return (ComponentDescriptor<T>) descriptor;
                }
            }
        }
                
        return null;
    }

    public <T> Map<String, ComponentDescriptor<T>> getComponentDescriptorMap( Class<T> type, String role )
    {
        Map<String, ComponentDescriptor<T>> descriptors = new TreeMap<String, ComponentDescriptor<T>>();
        for ( Set<ComponentDescriptor<?>> componentDescriptors : getComponentDescriptors( role ).values() )
        {
            for ( ComponentDescriptor<?> descriptor : componentDescriptors )
            {
                if ( !descriptors.containsKey( descriptor.getRoleHint() ) && isAssignableFrom( type,
                        descriptor.getImplementationClass() ) )
                {
                    descriptors.put( descriptor.getRoleHint(), (ComponentDescriptor<T>) descriptor );
                }
            }
        }
        return descriptors;
    }

    public <T> List<ComponentDescriptor<T>> getComponentDescriptorList( Class<T> type, String role )
    {
        List<ComponentDescriptor<T>> descriptors = new ArrayList<ComponentDescriptor<T>>();
        for ( Set<ComponentDescriptor<?>> componentDescriptors : getComponentDescriptors( role ).values() )
        {
            for ( ComponentDescriptor<?> descriptor : componentDescriptors )
            {
                if ( isAssignableFrom( type, descriptor.getImplementationClass() ) )
                {
                    descriptors.add( (ComponentDescriptor<T>) descriptor );
                }
            }
        }
        return descriptors;
    }

    @Deprecated
    public ComponentDescriptor<?> getComponentDescriptor( String role, String roleHint, ClassRealm realm )
    {
        // find all realms from our realm to the root realm
        Set<ClassRealm> realms = new HashSet<ClassRealm>();
        for ( ClassRealm r = realm; r != null; r = r.getParentRealm() )
        {
            realms.add( r );
        }

        // get the component descriptors by roleHint
        for ( ComponentDescriptor<?> componentDescriptor : getComponentDescriptors( role ).get( roleHint ) )
        {
            // return the first descriptor from our target realms
            if ( realms.contains( componentDescriptor.getRealm() ) )
            {
                return componentDescriptor;
            }
        }

        return null;
    }

    public void removeComponentRealm( ClassRealm classRealm )
    {
        index.remove( classRealm );
    }

    // ----------------------------------------------------------------------
    // Lifecylce Management
    // ----------------------------------------------------------------------

    // ----------------------------------------------------------------------
    // Component Descriptor processing.
    // ----------------------------------------------------------------------

    public void addComponentDescriptor( ComponentDescriptor<?> componentDescriptor ) 
        throws CycleDetectedInComponentGraphException
    {
        ClassRealm classRealm = componentDescriptor.getRealm();
        SortedMap<String, Map<String, Set<ComponentDescriptor<?>>>> roleIndex = index.get( classRealm );
        if (roleIndex == null) {
            roleIndex = new TreeMap<String, Map<String, Set<ComponentDescriptor<?>>>>();
            index.put(classRealm,  roleIndex);
        }

        String role = componentDescriptor.getRole();
        Map<String, Set<ComponentDescriptor<?>>> roleHintIndex = roleIndex.get( role );
        if ( roleHintIndex == null )
        {
            roleHintIndex = new LinkedHashMap<String, Set<ComponentDescriptor<?>>>();
            roleIndex.put( role, roleHintIndex );
        }
        String roleHint = componentDescriptor.getRoleHint();
        Set<ComponentDescriptor<?>> componentDescriptors = roleHintIndex.get( roleHint );
        if ( componentDescriptors == null )
        {
            componentDescriptors = new LinkedHashSet<ComponentDescriptor<?>>();
            roleHintIndex.put( roleHint, componentDescriptors );
        }
        componentDescriptors.add(componentDescriptor);

        compositionResolver.addComponentDescriptor( componentDescriptor );
    }
}
