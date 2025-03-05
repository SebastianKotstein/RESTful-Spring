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

    public static Filter create(){
        return new Filter();
    }
    
    public Filter add(String name, Object value){
        this.add(new FilterCriterion(name,value,Filter.DefaultFilterMethod()));
        return this;
    }
    public Filter add(String name, Object value, FilterMethod method){
        this.add(new FilterCriterion(name,value,method));
        return this;
    }

    public Filter add(String name, String key, Object value, FilterMethod method){
        this.add(new FilterCriterion(name,key,value,method));
        return this;
    }

    public Filter add(String name, Object value, Class scope){
        this.add(new FilterCriterion(name,value,Filter.DefaultFilterMethod()));
        return this;
    }
    public Filter add(String name, Object value, FilterMethod method, Class scope){
        this.add(new FilterCriterion(name,value,method));
        return this;
    }

    public Filter add(String name, String key, Object value, FilterMethod method, Class scope){
        this.add(new FilterCriterion(name,key,value,method));
        return this;
    }

    public Filter add(FilterCriterion item){
        items.add(item);
        return this;
    }

    public Filter remove(String name){
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getName().compareTo(name)==0){
                items.remove(i);
                return this;
            }
        }
        return this;
    }

    public Filter remove(FilterCriterion item){
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getName().compareTo(item.getName())==0){
                items.remove(i);
                return this;
            }
        }
        return this;
    }

    public List<FilterCriterion> getItemsAsReadOnly(){
        return Collections.unmodifiableList(this.items);
    }

    public FilterCriterion get(String key){
        for (FilterCriterion filterCriterion : items) {
            if(filterCriterion.getQueryParameterKey().compareTo(key)==0){
                return filterCriterion;
            }
        }
        return null;
    }

    public Filter clear(){
        return this;
    }

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

