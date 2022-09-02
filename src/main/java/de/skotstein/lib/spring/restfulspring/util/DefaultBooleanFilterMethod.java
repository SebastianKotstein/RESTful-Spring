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

public class DefaultBooleanFilterMethod extends FilterMethod{

    @Override
    protected boolean match(String key, Object filterValue, Object entityValue) {
        if(Objects.isNull(entityValue)){
            return false;
        }
        if(entityValue instanceof Boolean){
            Boolean entityValueAsBoolean = (Boolean)entityValue;
            Boolean filterValueAsBoolean = null;
            if(filterValue instanceof Boolean){
                filterValueAsBoolean = (Boolean)filterValue;
            }else{
                return true;
            }
            return entityValueAsBoolean.booleanValue() == filterValueAsBoolean.booleanValue();
        }
        return true;
    }
    
}
