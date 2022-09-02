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

public class FilterableList<T> extends ArrayList<T>{
    
    public FilterableList(){

    }

    public FilterableList(List<T> items){
        for(T item : items){
            this.add(item);
        }
    }

    public FilterableList<T> with(List<T> items){
        FilterableList<T> filteredList = new FilterableList<T>(items);
        return filteredList;
    }

    public FilterableList<T> reduce(Filter filter){
        return new FilterableList<T>(filter.filter(this));
    }

    public FilterableList<T> reduce(Pagination pagination){
        FilterableList<T> paginatedList = new FilterableList<T>();
        for(int i = 0; i < this.size(); i++){
            if(!pagination.skipItem(i)){
                paginatedList.add(this.get(i));
            }
        }
        return paginatedList;
    }

    public void clearAndDeploy(List<T> target){
        target.clear();
        target.addAll(this);
    }
}
