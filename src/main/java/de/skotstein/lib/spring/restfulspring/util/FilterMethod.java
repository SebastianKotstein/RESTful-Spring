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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import org.springframework.web.server.ResponseStatusException;

public abstract class FilterMethod {
    
    /**
     * Checks whether the passed entity matches the given {@link FilterCriterion}. The method returns true if (1) a match is given, (2) the query value is null, or (3)
     * the passed entity does not contain any method that has a {@link Filterable} annotation that has the name specified in the passed {@link FilterCriterion}.
     * The method returns false, if no match is given.
     * @param filterCriterion the filter criterion holding all relevant meta information
     * @param entity the entity that should be filtered
     * @return true if the passed entity matches the filter criterion, else false.
     */
    boolean applyFilterMethod(FilterCriterion filterCriterion, Object entity){
        if(Objects.isNull(entity) || Objects.isNull(filterCriterion) || Objects.isNull(filterCriterion.getName()) || filterCriterion.getName().isBlank()){
            throw new RuntimeException("The passed entity is null");
        }
        if(!Objects.isNull(filterCriterion.getScope()) && !entity.getClass().isInstance(filterCriterion.getScope())){
            return true;
        }
        if(Objects.isNull(filterCriterion.getQueryParameterValue())){
            return true;
        }
        Class<?> clazz = entity.getClass();
        for(Method method : clazz.getDeclaredMethods()){
            Filterable annotation = method.getAnnotation(Filterable.class);
            if(!Objects.isNull(annotation)){
                if(annotation.query().toLowerCase().trim().compareTo(filterCriterion.getName().toLowerCase().trim())==0){
                    try{
                        method.setAccessible(true);
                        return match(filterCriterion.getQueryParameterKey(),filterCriterion.getQueryParameterValue(),method.invoke(entity,new Object[0]));
                    }catch(ResponseStatusException rse){
                        throw rse;
                    }catch(IllegalAccessException ie){
                        return true; //ignore
                    }catch(IllegalArgumentException iae){
                        return true; //ignore
                    }catch(InvocationTargetException ite){
                        return true; //ignore
                    }

                }
            }
        }
        return true;
    }

    /**
     * Returns true, if the passed entity value matches the passed filter criterion (key and value), else false.
     * @param queryParameterKey the query parameter key
     * @param queryParameterValue the query parameter value
     * @param entityValue the entity value
     * @return true if the passed entity value matches the passed query parameter key and value
     */
    protected abstract boolean match(String queryParameterKey, Object queryParameterValue, Object entityValue);

}

