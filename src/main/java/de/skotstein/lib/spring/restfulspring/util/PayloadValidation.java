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


public class PayloadValidation {
    
    public static boolean isSet(Object property){
        return !Objects.isNull(property);
    }

    public static boolean isBlank(String property){
        return Objects.isNull(property) || property.isBlank();
    }

    public static boolean hasValue(String property, String targetValue, boolean caseSensitive){
        if(!isSet(property) && !isSet(targetValue)){
            return true;
        }
        if(isSet(property) && !isSet(targetValue)){
            return false;
        }
        if(!isSet(property) && isSet(targetValue)){
            return false;
        }
        if(caseSensitive){
            return property.compareTo(targetValue) == 0;
        }else{
            return property.compareToIgnoreCase(targetValue) ==0;
        }

    }

    public static boolean isValueOfEnum(String propertyValue, String[] enumValues, boolean caseSensitive){
        for (String value : enumValues) {
            if(caseSensitive){
                if(propertyValue.compareTo(value)==0){
                    return true;
                }
            }else{
                if(propertyValue.compareToIgnoreCase(value)==0){
                    return true;
                }
            }
        }
        return false;
    }

}
