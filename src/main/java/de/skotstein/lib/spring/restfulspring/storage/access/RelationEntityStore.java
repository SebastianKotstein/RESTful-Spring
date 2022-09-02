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
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.skotstein.lib.spring.restfulspring.storage.repositories.RelationEntityRepositoryExtension;

public class RelationEntityStore<T, ID, FFID, SFID> extends EntityStore<T, ID> implements AbstractRelationEntityStore<T, ID, FFID, SFID> {

    private RelationEntityRepositoryExtension<T, FFID, SFID> extensionRepository;

    public RelationEntityStore(JpaRepository<T, ID> repository, RelationEntityRepositoryExtension<T, FFID, SFID> extensionRepository) {
        super(repository);
        this.extensionRepository = extensionRepository;
    }

    @Override
    public List<T> getAllRelationEntitesByFirstForeignKey(FFID firstForeignKey) {
        return extensionRepository.findByFirstForeignKey(firstForeignKey);
    }

    @Override
    public List<T> getAllRelationEntitesBySecondForeignKey(SFID secondForeignKey) {
        return extensionRepository.findBySecondForeignKey(secondForeignKey);
    }

    @Override
    public T getRelationEntityByForeignKeys(FFID firstForeignKey, SFID secondForeignKey) {
        Optional<T> entity = extensionRepository.findByForeignKeys(firstForeignKey, secondForeignKey);
        if(entity.isPresent()){
            return entity.get();
        }else{
            return null;
        }
    }

    @Override
    public void deleteAllRelationEntitesByFirstForeignKey(FFID firstForeignKey) {
        List<T> relations = getAllRelationEntitesByFirstForeignKey(firstForeignKey);
        for(T relation : relations){
            deleteEntity(relation);
        }
    }

    @Override
    public void deleteAllRelationEntitesBySecondForeignKey(SFID secondForeignKey) {
        List<T> relations = getAllRelationEntitesBySecondForeignKey(secondForeignKey);
        for(T relation : relations){
            deleteEntity(relation);
        }
    }
    
}
