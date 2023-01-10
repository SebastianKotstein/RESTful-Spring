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
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;

import de.skotstein.lib.spring.restfulspring.model.entities.Hypermedia;

public class Pagination {

    private int itemsBefore = 0;
    private int itemsAfter = 0;

    private int start = 0;
    private Integer limit = null;

    public Pagination(Integer start, Integer limit){
        if(start == null){
            this.start = 0;
        }else{
            this.start = start;
        }
        this.limit = limit;
    }
    
    public int getItemsAfter() {
        return itemsAfter;
    }

    public int getItemsBefore() {
        return itemsBefore;
    }

    public boolean skipItem(int index){
        //check whether item is in range
        if(index < start){
            itemsBefore++;
            return true;
        }
        if(limit != null && index >= start+limit){
            itemsAfter++;
            return true;
        }
        return false;
    }

    public boolean addHyperlinksIfUsed(Hypermedia representation, Filter filter){
        if((itemsAfter == 0 && itemsBefore == 0) || limit == null){
            return false;
        }else{
            String query = "";
            if(!Objects.isNull(filter)){
                for (Entry<String,Object> entry : filter.getQueryParameter().entrySet()) {
                    if(entry.getValue()!=null){
                        query+="&"+entry.getKey()+"="+entry.getValue();
                    }
                }
            }
            if(itemsBefore > 0){
                int startIndex = itemsBefore - limit;
                if(startIndex < 0){
                    query+="&start=0&limit="+itemsBefore;
                }else{
                    query+="&start="+startIndex+"&limit="+limit;
                }
                
                if(query.charAt(0) == '&'){
                    query = "?"+query.substring(1);
                }
                representation.addHyperlink("previous", representation.getHyperlink("self").getHref()+query);
            }
            query = "";
            if(!Objects.isNull(filter)){
                for (Entry<String,Object> entry : filter.getQueryParameter().entrySet()) {
                    if(entry.getValue()!=null){
                        query+="&"+entry.getKey()+"="+entry.getValue();
                    }
                }
            }
            if(itemsAfter > 0){
                int startIndex = start+limit;
                /*
                if(itemsAfter < limit){
                    query+="&start="+startIndex+"&limit="+itemsAfter;
                }else{
                    query+="&start="+startIndex+"&limit="+limit;
                }
                */
                query+="&start="+startIndex+"&limit="+limit;
                
                if(query.charAt(0) == '&'){
                    query = "?"+query.substring(1);
                }
                representation.addHyperlink("next",representation.getHyperlink("self").getHref()+query);
            }
            return true;
        }
    }

    /**
     * Returns the queries for all possible pages. For instance, if the limit is '10' and the total set of items is '42', the method will return the following queries:
     * - index 0: "?start=0&limit=10"
     * - index 1: "?start=10&limit=10"
     * - index 2: "?start=20&limit=10"
     * - index 3: "?start=30&limit=10"
     * - index 4: "?start=40&limit=10"
     * The method returns null, if the specified limit (see {@link Pagination} is zero or null.
     * @param filter
     * @return
     */
    public List<String> getPageQueries(Filter filter){
        if(Objects.isNull(limit) || limit == 0){
            return null;
        }else{
            List<String> result = new ArrayList<String>();
            int totalSize = itemsBefore+limit+itemsAfter;
            int fragments = totalSize/limit + (totalSize%limit!=0?1:0);
            for(int i = 0; i < fragments; i++){
                String query = "?start="+(i*limit)+"&limit="+limit;
                for (Entry<String,Object> entry : filter.getQueryParameter().entrySet()) {
                    if(entry.getValue()!=null){
                        query+="&"+entry.getKey()+"="+entry.getValue();
                    }
                }
                result.add(query);
            }
            return result;
        }
    }

    /**
     * Returns the current zero-based index
     * @return
     */
    public int getCurrentIndex(){
        if(Objects.isNull(limit) || limit == 0){
           return 0;
        }else{
            int index = itemsBefore/limit + (itemsBefore%limit!=0?1:0);
            return index;
        }
    }

    /**
     * Returns the specified limit
     * @return
     */
    public Integer getLimit(){
        return limit;
    }

    public static Pagination of(Integer start, Integer limit){
        return new Pagination(start,limit);
    }

}
