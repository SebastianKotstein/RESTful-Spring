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

public class DefaultStringFilterMethod extends FilterMethod{

    @Override
    protected boolean match(String key, Object filterValue, Object entityValue) {
        if(Objects.isNull(entityValue)){
            return false;
        }
        if(entityValue instanceof String){
            String entityValueAsString = (String)entityValue;
            String filterValueAsString = "";
            if(filterValue instanceof String){
                filterValueAsString = (String)filterValue;
                
            }else{
                filterValueAsString = filterValue+"";
            }
            if(filterValueAsString.isEmpty()){ //skip empty strings
                return true;
            }
            return entityValueAsString.trim().toLowerCase().contains(filterValueAsString.trim().toLowerCase());
        }
        return true;
    }
    
}
