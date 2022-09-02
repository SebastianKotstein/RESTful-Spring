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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

public class CachingEntityStore<T, ID> extends EntityStore<T, ID>{
    
    @Autowired
    private CacheManager cacheManager;

    private String cacheName = null;

    private final String LIST_POSTFIX = "-list";

    public CachingEntityStore(JpaRepository<T, ID> repository, String cacheName) {
        super(repository);
        this.cacheName = cacheName;
    }

    @Override
    public List<T> getAllEntites() {
        ValueWrapper cachedValue = getListCache().get("default");
        if(Objects.isNull(cachedValue)){
            return super.getAllEntites();
        }else{
            return (List<T>)cachedValue.get();
        }
    }

    @Override
    public T storeEntity(T value) {
        getListCache().invalidate();
        T savedEntity = super.storeEntity(value);
        getEntityCache().put(savedEntity,getCachingKey(savedEntity));
        return savedEntity;
    }

    @Override
    public T getEntityById(ID id) {
        ValueWrapper cachedValue = getEntityCache().get(id);
        if(Objects.isNull(cachedValue)){
            return super.getEntityById(id);
        }else{
            return (T)cachedValue.get();
        }
    }

    @Override
    public void deleteEntity(T value) {
        getListCache().invalidate();
        getEntityCache().evictIfPresent(getCachingKey(value));
        super.deleteEntity(value);
    }

    private Cache getEntityCache(){
        return cacheManager.getCache(cacheName);
    }

    private Cache getListCache(){
        return cacheManager.getCache(cacheName+LIST_POSTFIX);
    }

    private ID getCachingKey(T entity){
        for(Method method : entity.getClass().getDeclaredMethods()){
            if(!Objects.isNull(method.getAnnotation(CachingKey.class))){
                method.setAccessible(true);
                try {
                    return (ID) method.invoke(entity);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } 
            }
        }
        throw new RuntimeException("No compatible key found for caching '"+entity.getClass().getName()+"'");
    }
}
