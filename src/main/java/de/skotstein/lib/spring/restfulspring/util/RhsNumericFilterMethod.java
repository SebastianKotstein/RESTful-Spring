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


import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RhsNumericFilterMethod extends FilterMethod{

    @Override
    protected boolean match(String queryParameterKey, Object queryParameterValue, Object entityValue) {
        Integer queryValue = 0;
        Integer entityNumericValue = 0;
        String operator = null;
        
        //check query parameter value
        if(Objects.isNull(queryParameterValue)){ //if parameter is not set
            return true;
        }else{
            if(queryParameterValue instanceof String){
                String queryParameterValueAsString = (String)queryParameterValue;
                queryParameterValueAsString = queryParameterValueAsString.trim();
                if(queryParameterValueAsString.isBlank()){ //if parameter is blank
                    return true;
                }
                //parse query parameter
                if(queryParameterValueAsString.startsWith("eq:")){
                    operator = "eq";
                    queryParameterValueAsString = queryParameterValueAsString.substring(3);
                }else if(queryParameterValueAsString.startsWith("gt:")){
                    operator = "gt";
                    queryParameterValueAsString = queryParameterValueAsString.substring(3);
                }
                else if(queryParameterValueAsString.startsWith("gte:")){
                    operator = "gte";
                    queryParameterValueAsString = queryParameterValueAsString.substring(4);
                }
                else if(queryParameterValueAsString.startsWith("lt:")){
                    operator = "lt";
                    queryParameterValueAsString = queryParameterValueAsString.substring(3);
                }
                else if(queryParameterValueAsString.startsWith("lte:")){
                    operator = "lte";
                    queryParameterValueAsString = queryParameterValueAsString.substring(4);
                }else{
                    operator = "eq";
                }
                try{
                    queryValue = Integer.parseInt(queryParameterValueAsString);
                }catch(NumberFormatException nfe){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Numeric filter does not contain a numeric value. Make sure that the filter is a numeric value optionally prefixed by one of the following operators: 'eq:' (default), 'lt:', 'lte:', 'gt:', or 'gte:'");
                }

            }else if(queryParameterValue instanceof Integer){
                operator = "eq";
                queryValue = (Integer)queryParameterValue;
            }else{
                throw new RuntimeException("Query parameter for filtering IP addresses must by type of string or integer");
            }
        }

        //check entity value
        if(Objects.isNull(entityValue)){
            return false;
        }else{
            if(entityValue instanceof Integer){
                entityNumericValue = (Integer)entityValue;
            }else{
                throw new RuntimeException("Entity value must be type of Integer");
            }
        }

        //compare values
        switch(operator){
            case "eq":
                return entityNumericValue.intValue() == queryValue.intValue();
            case "gt":
                return entityNumericValue.intValue() > queryValue.intValue();
            case "gte":
                return entityNumericValue.intValue() >= queryValue.intValue();
            case "lt":
                return entityNumericValue.intValue() < queryValue.intValue();
            case "lte":
                return entityNumericValue.intValue() <= queryValue.intValue();
            default:
                return false;
        }
    }
    
}
