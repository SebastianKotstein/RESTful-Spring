/* 
 *  Copyright 2022 Sebastian Kotstein
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.   
 */

package de.skotstein.lib.spring.restfulspring.util;


public class FilterCriterion {
    private String name;
    private String key;
    private Object value;
    private FilterMethod method;
    private Class scope;

    /**
     * Initializes an instance of this class. The passed name argument is used for both name and query parameter key.
     * @param name the name of this {@link FilterCriterion}, which is linked to a {@link Filterable} annotation.
     * @param value the query parameter value
     * @param method the filtering method
     */
    public FilterCriterion(String name, Object value, FilterMethod method) {
        this(name,name,value,method);
    }

    /**
     * Initializes an instance of this class.
     * @param name the name of this {@link FilterCriterion}, which is linked to a {@link Filterable} annotation.
     * @param key the query parameter key
     * @param value the query parameter value
     * @param method the filtering method
     */
    public FilterCriterion(String name, String key, Object value, FilterMethod method) {
        this.name = name;
        this.key = key;
        this.value = value;
        this.method = method;
    }

    /**
     * Initializes an instance of this class. The passed name argument is used for both name and query parameter key.
     * @param name the name of this {@link FilterCriterion}, which is linked to a {@link Filterable} annotation.
     * @param value the query parameter value
     * @param method the filtering method
     * @param scope the scope (class) this filter should be applied to
     */
    public FilterCriterion(String name, Object value, FilterMethod method, Class scope) {
        this(name,name,value,method,scope);
    }

    /**
     * Initializes an instance of this class.
     * @param name the name of this {@link FilterCriterion}, which is linked to a {@link Filterable} annotation.
     * @param key the query parameter key
     * @param value the query parameter value
     * @param method the filtering method
     * @param scope the scope (class) this filter should be applied to
     */
    public FilterCriterion(String name, String key, Object value, FilterMethod method, Class scope) {
        this(name,key,value,method);
        this.scope = scope;
    }
    
    /**
     * Returns the name of this {@link FilterCriterion}, which is linked to a {@link Filterable} annotation.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the query parameter key
     * @return
     */
    public String getQueryParameterKey() {
        return key;
    }
    
    /**
     * Returns the query parameter value
     * @return
     */
    public Object getQueryParameterValue() {
        return value;
    }

    /**
     * Returns the filtering method, which should be applied to this {@link FilterCriterion}
     * @return
     */
    public FilterMethod getMethod() {
        return method;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setQueryParameterKey(String key) {
        this.key = key;
    }
    public void setQueryParameterValue(Object value) {
        this.value = value;
    }
    public void setMethod(FilterMethod method) {
        this.method = method;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Class getScope() {
        return scope;
    }

    public void setScope(Class scope) {
        this.scope = scope;
    }
    
}
