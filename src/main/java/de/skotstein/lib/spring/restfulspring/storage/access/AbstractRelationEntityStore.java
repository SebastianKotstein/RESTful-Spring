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

package de.skotstein.lib.spring.restfulspring.storage.access;

import java.util.List;

public interface AbstractRelationEntityStore<T, ID, FFID, SFID> extends AbstractEntityStore<T, ID> {
    
    public List<T> getAllRelationEntitesByFirstForeignKey(FFID firstForeignKey);

    public List<T> getAllRelationEntitesBySecondForeignKey(SFID secondForeignKey);

    public T getRelationEntityByForeignKeys(FFID firstForeignKey, SFID secondForeignKey);

    public void deleteAllRelationEntitesByFirstForeignKey(FFID firstForeignKey);

    public void deleteAllRelationEntitesBySecondForeignKey(SFID secondForeignKey);
}
