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

package org.codehaus.plexus.component.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a field as a configuration element with a default value.
 *
 * @deprecated see <a href="https://github.com/eclipse/sisu.plexus/wiki/Plexus-to-JSR330">Plexus-to-JSR330</a> instead.
 * @since 1.0-alpha-33
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Inherited
@Deprecated
public @interface Configuration {
    String name() default "";

    String value();
}
