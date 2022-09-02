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

package de.skotstein.lib.spring.restfulspring.storage.repositories;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.util.Assert;

public class RelationEntityRepositoryImpl<T, FFID , SFID > implements RelationEntityRepositoryExtension<T, FFID , SFID >{

    @PersistenceContext
    private EntityManager entityManager;

    private String firstForeignKeyFieldName = null;
    private String secondForeignKeyFieldName = null;
    private T dummyObject = null;

    public void advertiseForeignKeys(T item){
        dummyObject = item;
        for(Field field : item.getClass().getDeclaredFields()){
            EntityForeignKey relationForeignKey = field.getAnnotation(EntityForeignKey.class);
            if(!Objects.isNull(relationForeignKey)){
                
                Column column = field.getAnnotation(Column.class);
                if(!Objects.isNull(column) && !Objects.isNull(column.name()) && !column.name().isBlank()){
                    switch(relationForeignKey.number()){
                        case 0:
                            firstForeignKeyFieldName = field.getName();
                            break;
                        case 1:
                            secondForeignKeyFieldName = field.getName();
                            break;
                    }
                }
            }
        }
    }

    private Class<T> getDomainClass(){
        return (Class<T>)dummyObject.getClass();
    }


    @Override
    @Transactional
    public List<T> findByFirstForeignKey(FFID firstForeignKey) {
        Assert.notNull(firstForeignKey,"Foreign key must not be null");
        Assert.notNull(firstForeignKeyFieldName, "First foreign key field name is not specified. Make sure to call 'advertiseForeignKeys' before using this repository.");
        
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cQuery = builder.createQuery(getDomainClass());
        Root<T> root = cQuery.from(getDomainClass());
        cQuery.select(root).where(builder.equal(root.get(firstForeignKeyFieldName), firstForeignKey));
        TypedQuery<T> query = entityManager.createQuery(cQuery);
        return query.getResultList();
    }

    @Override
    @Transactional
    public List<T> findBySecondForeignKey(SFID secondForeignKey) {
        Assert.notNull(secondForeignKey,"Foreign key must not be null");
        Assert.notNull(secondForeignKeyFieldName, "Second foreign key field name is not specified. Make sure to call 'advertiseForeignKeys' before using this repository.");

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cQuery = builder.createQuery(getDomainClass());
        Root<T> root = cQuery.from(getDomainClass());
        cQuery.select(root).where(builder.equal(root.get(secondForeignKeyFieldName), secondForeignKey));
        TypedQuery<T> query = entityManager.createQuery(cQuery);

        return query.getResultList();
    }

    @Override
    @Transactional
    public Optional<T> findByForeignKeys(FFID firstForeignKey, SFID secondForeignKey) {
        Assert.notNull(firstForeignKey,"Foreign key must not be null");
        Assert.notNull(firstForeignKeyFieldName, "First foreign key field name is not specified. Make sure to call 'advertiseForeignKeys' before using this repository.");
        Assert.notNull(secondForeignKey,"Foreign key must not be null");
        Assert.notNull(secondForeignKeyFieldName, "Second foreign key field name is not specified. Make sure to call 'advertiseForeignKeys' before using this repository.");

        if(Objects.isNull(firstForeignKeyFieldName) || Objects.isNull(secondForeignKeyFieldName)){
            throw new RuntimeException("Foreign key field names are not specified. Make sure to call 'advertiseForeignKeys' before using this repository.");
        }
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cQuery = builder.createQuery(getDomainClass());
        Root<T> root = cQuery.from(getDomainClass());
        cQuery.select(root).where(builder.equal(root.get(firstForeignKeyFieldName), firstForeignKey),builder.equal(root.get(secondForeignKeyFieldName), secondForeignKey));
        TypedQuery<T> query = entityManager.createQuery(cQuery);
        return query.getResultList().stream().findFirst();
    }
    
}
