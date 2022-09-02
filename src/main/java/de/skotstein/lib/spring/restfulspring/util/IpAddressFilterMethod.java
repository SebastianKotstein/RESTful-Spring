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

public class IpAddressFilterMethod extends FilterMethod{

    @Override
    protected boolean match(String queryParameterKey, Object queryParameterValue, Object entityValue) {
        String entityIpAddress = null;
        String queryIpAddress = null;
        
        //check query parameter value
        if(Objects.isNull(queryParameterValue)){ //if parameter is not set
            return true;
        }else{
            if(queryParameterValue instanceof String){
                queryIpAddress = (String)queryParameterValue;
                queryIpAddress = queryIpAddress.trim();
                if(queryIpAddress.isBlank()){ //if parameter is empty
                    return true;
                }
            }else{
                throw new RuntimeException("Query parameter for filtering IP addresses must by type of string");
            }
        }

        //check entity
        if(Objects.isNull(entityValue)){ //if entity value is not set
            return false;
        }else{
            if(entityValue instanceof String){
                entityIpAddress = (String)entityValue;
                entityIpAddress = entityIpAddress.trim();
                if(entityIpAddress.isBlank()){ //if entity value is empty
                    return false;
                }
            }else{
                throw new RuntimeException("IP address (entity value) must by type of string");
            }
        }

        //step 1: read IP Filter
        String[] ipFilterSegments = new String[4];
        for(int i = 0; i < ipFilterSegments.length; i++){
            ipFilterSegments[i] = "";
        }
        int segmentCounter = 0;
        for(int i = 0; i < queryIpAddress.length(); i++){
            if(queryIpAddress.charAt(i) == '.'){
                segmentCounter++;
                if(segmentCounter > 3){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid IP Filtering Schema");
                }
            }else if(queryIpAddress.charAt(i)=='*'){
                ipFilterSegments[segmentCounter] = "*";
            }else{
                char digit = queryIpAddress.charAt(i);
                if(!Character.isDigit(digit)){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid IP Filtering Schema");
                }else{
                    if(ipFilterSegments[segmentCounter].compareTo("*")!=0){
                        ipFilterSegments[segmentCounter]+=digit;
                    }
                }
            }
        }
        //finally replace all untouched segments with *
        for(int i = 0; i < ipFilterSegments.length; i++){
            if(ipFilterSegments[i].compareTo("")==0){
                ipFilterSegments[i] = "*";
            }
        }

        //step 2: read IP Address
        String[] ipAddressSegments = entityIpAddress.split("\\.");
        if(ipAddressSegments.length != 4){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"The client IP is invalid");
        }

        //step 3: compare IP Address with IP Filter
        for(int i = 0; i < 4; i++){
            if(ipFilterSegments[i].compareTo("*")==0){
                continue;
            }
            if(ipFilterSegments[i].compareTo(ipAddressSegments[i])!=0){
                return false;
            }
        }
        return true;
    }

}
