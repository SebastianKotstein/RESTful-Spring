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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RestAssert {
    
    public static void Found(Object object, String message){
        if(Objects.isNull(object)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,message);
        }
    }

    public static void isSet(Object object, String message){
        if(Objects.isNull(object)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
        }
    }

    public static void isNotBlank(String value, String message){
        if(Objects.isNull(value) || value.isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
        }
    }

    public static void isUri(String uri, String message){
        try {
            URL url = new URL(uri);
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
        }
    }

    public static void isValueOfEnum(String propertyValue, String[] enumValues, boolean caseSensitive, String message){
        for (String value : enumValues) {
            if(caseSensitive){
                if(propertyValue.compareTo(value)==0){
                    return;
                }
            }else{
                if(propertyValue.compareToIgnoreCase(value)==0){
                    return;
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
    }

    /**
     * Throws a {@link ResponseStatusException} having the passed message, if the passed IP address (String) does not match the IP v4 address format.
     * A valid IP v4 address must consist of four segments separated by dots '.' containing a numeric value between 0 and 255, e.g. "192.168.0.1".
     * @param ipAddress
     * @param message
     */
    public static void isIpV4Address(String ipAddress, String message){
        if(!Objects.isNull(ipAddress) && !ipAddress.isBlank()){
            String[] segments = ipAddress.split("\\.");
            if(segments.length == 4){
                for(String segmentAsString : segments){
                    try{
                        int segment = Integer.parseInt(segmentAsString);
                        if(segment<0 || segment > 255){
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
                        }
                    }catch(NumberFormatException nfe){
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
                    }
                }
                return; //success case
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
    }

    /**
     * Throws a {@link ResponseStatusException} having the passed message, if the passed IP address schema (String) does not match the following format:
     * A valid IP v4 address schema must consist of at most four segments separated by dots. Each segment must contain between and three digits or wildcard symbols ('*').
     * The numeric segment value must be between 0 and 255 where a wildcard counts as a '0', e.g.: "10.0.*.1*1"
     * @param ipSchema
     * @param message
     */
    public static void isIpV4Schema(String ipSchema, String message){
        ipSchema = ipSchema.trim();
        if(!Objects.isNull(ipSchema) && !ipSchema.isBlank()){
            String[] segments = ipSchema.split("\\.");
            if(segments.length <= 4){
                for(String segmentAsString : segments){
                    if(segmentAsString.length()<1 || segmentAsString.length()>3){
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
                    }
                    int segmentValue = 0;
                    for(int i = 0; i < segmentAsString.length(); i++){
                        char c = segmentAsString.charAt(i);
                        if(Character.isDigit(c)){
                            int cValue = Integer.parseInt(c+"");
                            segmentValue= segmentValue*10;
                            segmentValue+= cValue;
                        }else if(c == '*'){
                            segmentValue= segmentValue*10;
                        }else{
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
                        }
                    }
                    if(segmentValue > 255){
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
                    }
                }
                return; //success case
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
    }

      /**
     * Throws a {@link ResponseStatusException} having the passed message, if the passed MAC address (string) does not match the following format:
     * A valid MAC address must consist of 12 hexadecimal digits that may be separated by '-' or ':'
     * @param macAddress
     * @param message
     */
    public static void isMacAddress(String macAddress, String message){
        if(!Objects.isNull(macAddress) && !macAddress.isBlank()){
            macAddress = macAddress.toLowerCase().replace(":", "").replace("-", "");
            if(macAddress.length() == 12){
                for(int i = 0; i < 12; i++){
                    char c = macAddress.charAt(i);
                    if(!(c >= 48 && c <= 57) && !(c >= 97 && c <= 104)){
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
                    }
                }
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
    }
}
