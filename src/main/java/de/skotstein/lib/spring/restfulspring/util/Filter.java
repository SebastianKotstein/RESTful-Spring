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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Filter {

    private List<FilterCriterion> items = new ArrayList<FilterCriterion>();

    private Class lastScope = null;

    public static Filter create(){
        return new Filter();
    }
    
    /**
     * Adds a {@link DefaultFilterMethod} to this filter. 
     * If you use this method, the passed name must be equal to the query parameter name. If the specified name should differ from the query parameter name, use {@link Filter#add(String, String, Object, FilterMethod)} instead.
     * Use {@link Filter#withScope(Class)} in advance to specify a scope for this and all subsequently added filter methods. 
     * Consider using {@link Filter#add(String, Object, FilterMethod, Class)} or {@link Filter#add(String, String, Object, FilterMethod, Class)} to set an individual scope other than the scope specified with {@link Filter#withScope(Class)}. 
     * In general, if no scope is set, the filter is applied to any class.
     * 
     * @param name the individual name that is assigned to the added filter method. To apply the added filter to a specific entity and its properties, use the same name for the {@link Filterable} annotations.
     * @param value the value of the query parameter. If the query parameter is unassigned, i.e., not set by the client, the passed value can be null.
     * @return this {@link Filter} instance with the added filter method.
     */
    public Filter add(String name, Object value){
        this.add(new FilterCriterion(name,value,Filter.DefaultFilterMethod(),lastScope));
        return this;
    }

    /**
     * Adds the passed {@link FilterMethod} to this filter. 
     * If you use this method, the passed name must be equal to the query parameter name. If the specified name should differ from the query parameter name, use {@link Filter#add(String, String, Object, FilterMethod)} instead.
     * Use {@link Filter#withScope(Class)} in advance to specify a scope for this and all subsequently added filter methods. 
     * Consider using {@link Filter#add(String, Object, FilterMethod, Class)} or {@link Filter#add(String, String, Object, FilterMethod, Class)} to set an individual scope other than the scope specified with {@link Filter#withScope(Class)}. 
     * In general, if no scope is set, the filter is applied to any class.
     * 
     * @param name the individual name that is assigned to the added filter method. To apply the added filter to a specific entity and its properties, use the same name for the {@link Filterable} annotations.
     * @param value the value of the query parameter. If the query parameter is unassigned, i.e., not set by the client, the passed value can be null.
     * @param method the filter method that should be added
     * @return this {@link Filter} instance with the added filter method.
     */
    public Filter add(String name, Object value, FilterMethod method){
        this.add(new FilterCriterion(name,value,method,lastScope));
        return this;
    }

    /**
     * Adds the passed {@link FilterMethod} to this filter. 
     * Use {@link Filter#withScope(Class)} in advance to specify a scope for this and all subsequently added filter methods. 
     * Consider using {@link Filter#add(String, Object, FilterMethod, Class)} or {@link Filter#add(String, String, Object, FilterMethod, Class)} to set an individual scope other than the scope specified with {@link Filter#withScope(Class)}. 
     * In general, if no scope is set, the filter is applied to any class.
     * 
     * @param name the individual name that is assigned to the added filter method. To apply the added filter to a specific entity and its properties, use the same name for the {@link Filterable} annotations.
     * @param key the query parameter name
     * @param value the value of the query parameter. If the query parameter is unassigned, i.e., not set by the client, the passed value can be null.
     * @param method the filter method that should be added
     * @return this {@link Filter} instance with the added filter method.
     */
    public Filter add(String name, String key, Object value, FilterMethod method){
        this.add(new FilterCriterion(name,key,value,method,lastScope));
        return this;
    }

    /**
     * Adds a {@link DefaultFilterMethod} to this filter. 
     * If you use this method, the passed name must be equal to the query parameter name. If the specified name should differ from the query parameter name, use {@link Filter#add(String, String, Object, FilterMethod)} instead.
     * 
     * @param name the individual name that is assigned to the added filter method. To apply the added filter to a specific entity and its properties, use the same name for the {@link Filterable} annotations.
     * @param value the value of the query parameter. If the query parameter is unassigned, i.e., not set by the client, the passed value can be null.
     * @param scope if set (i.e., other than null), the added filter method is only applied to entities that are instance of the specified class. 
     * @return this {@link Filter} instance with the added filter method.
     */
    public Filter add(String name, Object value, Class scope){
        this.add(new FilterCriterion(name,value,Filter.DefaultFilterMethod(),scope));
        return this;
    }

    /**
     * Adds the passed {@link FilterMethod} to this filter. 
     * If you use this method, the passed name must be equal to the query parameter name. If the specified name should differ from the query parameter name, use {@link Filter#add(String, String, Object, FilterMethod)} instead.
     * 
     * @param name the individual name that is assigned to the added filter method. To apply the added filter to a specific entity and its properties, use the same name for the {@link Filterable} annotations.
     * @param value the value of the query parameter. If the query parameter is unassigned, i.e., not set by the client, the passed value can be null.
     * @param method the filter method that should be added
     * @param scope if set (i.e., other than null), the added filter method is only applied to entities that are instance of the specified class. 
     * @return this {@link Filter} instance with the added filter method.
     */
    public Filter add(String name, Object value, FilterMethod method, Class scope){
        this.add(new FilterCriterion(name,value,method,scope));
        return this;
    }

    /**
     * Adds the passed {@link FilterMethod} to this filter. 
     * 
     * @param name the individual name that is assigned to the added filter method. To apply the added filter to a specific entity and its properties, use the same name for the {@link Filterable} annotations.
     * @param key the query parameter name
     * @param value the value of the query parameter. If the query parameter is unassigned, i.e., not set by the client, the passed value can be null.
     * @param method the filter method that should be added
     * @param scope if set (i.e., other than null), the added filter method is only applied to entities that are instance of the specified class. 
     * @return this {@link Filter} instance with the added filter method.
     */
    public Filter add(String name, String key, Object value, FilterMethod method, Class scope){
        this.add(new FilterCriterion(name,key,value,method,scope));
        return this;
    }

    /**
     * Adds a filter method to this filter nested in the passed {@link FilterCriterion}
     * @param item {@link FilterCriterion} nesting the filter method that should be added.
     * @return this {@link Filter} instance with the added filter method.
     */
    public Filter add(FilterCriterion item){
        items.add(item);
        return this;
    }

    /**
     * Removes the first {@link FilterMethod} having the passed name.
     * @param name name of the {@link FilterMethod} to be removed
     * @return this {@link Filter} instance 
     */
    public Filter remove(String name){
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getName().compareTo(name)==0){
                items.remove(i);
                return this;
            }
        }
        return this;
    }

    /**
     * Removes the first {@link FilterMethod} having the name nested in the passed {@link FilterCriterion}.
     * @param item {@link FilterCriterion} nesting the name of the {@link FilterMethod} to be removed
     * @return this {@link Filter} instance 
     */
    public Filter remove(FilterCriterion item){
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getName().compareTo(item.getName())==0){
                items.remove(i);
                return this;
            }
        }
        return this;
    }

    /**
     * Specifies a scope that is applied to all subsequently added {@link FilterMethod}.
     * Added filter methods are only applied to entities that are instance of the specified class. 
     * @param scope the scope
     * @return this {@link Filter} instance 
     */
    public Filter withScope(Class scope){
        this.lastScope = scope;
        return this;
    }

    /**
     * Resets the scope so that all subsequently added {@link FilterMethod} are applied to entities of any class.
     * @return this {@link Filter} instance 
     */
    public Filter resetScope(){
        this.lastScope = null;
        return this;
    }

    /**
     * Returns all {@link FilterCriterion} of this filter as read only collection.
     * @return all {@link FilterCriterion} of this filter as read only collection
     */
    public List<FilterCriterion> getItemsAsReadOnly(){
        return Collections.unmodifiableList(this.items);
    }

    /**
     * Returns the first {@link FilterCriterion} that matches the passed query parameter name (key).
     * @param key the query parameter name
     * @return the first {@link FilterCriterion} matching the passed query parameter name or null if no such item exists
     */
    public FilterCriterion get(String key){
        for (FilterCriterion filterCriterion : items) {
            if(filterCriterion.getQueryParameterKey().compareTo(key)==0){
                return filterCriterion;
            }
        }
        return null;
    }

    /**
     * Removes all {@link FilterMethod} from this filter. 
     * @return this {@link Filter} instance 
     */
    public Filter clear(){
        return this;
    }

    /**
     * Returns the number of {@link FilterMethod} of this filter. 
     * @return number of {@link FilterMethod}
     */
    public int size(){
        return items.size();
    }

    public boolean has(String key){
        return !Objects.isNull(this.get(key));
    }

    public boolean hasValue(String key){
        FilterCriterion filterCriterion = this.get(key);
        if(!Objects.isNull(filterCriterion)){
            return !Objects.isNull(filterCriterion.getQueryParameterValue());
        }else{
            return false;
        }
    }

    public Map<String,Object> getQueryParameter(){
        Map<String,Object> queryParameter = new HashMap<String,Object>();
        for(FilterCriterion filterCriterion : items){
            if(!queryParameter.containsKey(filterCriterion.getQueryParameterKey())){
                queryParameter.put(filterCriterion.getQueryParameterKey(), filterCriterion.getQueryParameterValue());
            }
        }
        return queryParameter;
    }

    private boolean matches(Object entity){
        boolean match = true;
        for(FilterCriterion filterCriterion : items){
            if(!filterCriterion.getMethod().applyFilterMethod(filterCriterion, entity)){
                match = false;
            }
        }
        return match;
    }

    public <T> List<T> filter(List<T> input){
        List<T> results = new ArrayList<T>();
        for(T entity : input){
            if(matches(entity)){
                results.add(entity);
            }
        }
        return results;
    }

    public <T> boolean filter(T input){
        return matches(input);
    }

    public static FilterMethod DefaultFilterMethod(){
        return new DefaultFilterMethod();
    }

    public static FilterMethod AlwaysMatchFilterMethod(){
        return new AlwaysMatchFilterMethod();
    }

    public static FilterMethod IpAddressFilterMethod(){
        return new IpAddressFilterMethod();
    }

    public static FilterMethod RhsNumericFilterMethod(){
        return new RhsNumericFilterMethod();
    }

    public static FilterMethod GreaterThanFilterMethod(){
        return new MinMaxFilterMethod(true, false);
    }

    public static FilterMethod LessThanFilterMethod(){
        return new MinMaxFilterMethod(false, false);
    }

    public static FilterMethod GreaterThanOrEqualToFilterMethod(){
        return new MinMaxFilterMethod(true, true);
    }

    public static FilterMethod LessThanOrEqualToFilterMethod(){
        return new MinMaxFilterMethod(false, true);
    }

    public static FilterMethod GreaterThanFilterMethod(boolean matchNullValues){
        return new MinMaxFilterMethod(true, false, matchNullValues);
    }

    public static FilterMethod LessThanFilterMethod(boolean matchNullValues){
        return new MinMaxFilterMethod(false, false, matchNullValues);
    }

    public static FilterMethod GreaterThanOrEqualToFilterMethod(boolean matchNullValues){
        return new MinMaxFilterMethod(true, true,matchNullValues);
    }

    public static FilterMethod LessThanOrEqualToFilterMethod(boolean matchNullValues){
        return new MinMaxFilterMethod(false, true,matchNullValues);
    }

}

