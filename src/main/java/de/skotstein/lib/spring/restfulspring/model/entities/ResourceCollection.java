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

package de.skotstein.lib.spring.restfulspring.model.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.skotstein.lib.spring.restfulspring.util.Filter;
import de.skotstein.lib.spring.restfulspring.util.Pagination;

public abstract class ResourceCollection<T extends Hypermedia> extends Hypermedia{
    
    protected List<T> items = new ArrayList<T>();

    public void reduceItems(Filter filter, Pagination pagination){
        List<T> filteredList = new ArrayList<T>();
        if(!Objects.isNull(filter)){
            filteredList = filter.filter(items);
        }else{
            for(T item: items){
                filteredList.add(item);
            }
        }

        List<T> filteredAndPaginatedList = new ArrayList<T>();
        if(!Objects.isNull(pagination)){
            for(int i = 0; i < filteredList.size(); i++){
                if(!pagination.skipItem(i)){
                    filteredAndPaginatedList.add(filteredList.get(i));
                }
            }
        }else{
            for(T item: filteredList){
                filteredAndPaginatedList.add(item);
            }
        }
        
        items.clear();
        for(T item: filteredAndPaginatedList){
            items.add(item);
        }
    }

}
