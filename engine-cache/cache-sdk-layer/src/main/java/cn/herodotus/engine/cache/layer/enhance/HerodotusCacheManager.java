/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.cache.layer.enhance;

import cn.herodotus.engine.cache.core.properties.Expire;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: 自定义多级缓存管理器 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/7/12 17:46
 */
public class HerodotusCacheManager implements CacheManager {

    private static final Logger log = LoggerFactory.getLogger(HerodotusCacheManager.class);

    private RedisCacheManager redisCacheManager;
    private CaffeineCacheManager caffeineCacheManager;
    private boolean desensitization = true;
    private boolean clearRemoteOnExit = false;
    private boolean allowNullValues = true;
    private Map<String, Expire> expires = new HashMap<>();

    private boolean dynamic = true;

    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    public HerodotusCacheManager() {
    }

    public HerodotusCacheManager(String... cacheNames) {
        setCacheNames(Arrays.asList(cacheNames));
    }

    public void setRedisCacheManager(RedisCacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
    }

    public void setCaffeineCacheManager(CaffeineCacheManager caffeineCacheManager) {
        this.caffeineCacheManager = caffeineCacheManager;
    }

    public void setExpires(Map<String, Expire> expires) {
        this.expires = expires;
    }

    public void setDesensitization(boolean desensitization) {
        this.desensitization = desensitization;
    }

    public void setClearRemoteOnExit(boolean clearRemoteOnExit) {
        this.clearRemoteOnExit = clearRemoteOnExit;
    }

    public boolean isAllowNullValues() {
        return allowNullValues;
    }

    public void setAllowNullValues(boolean allowNullValues) {
        this.allowNullValues = allowNullValues;
    }

    /**
     * Specify the set of cache names for this CacheManager's 'static' mode.
     * <p>The number of caches and their names will be fixed after a call to this method,
     * with no creation of further cache regions at runtime.
     * <p>Calling this with a {@code null} collection argument resets the
     * mode to 'dynamic', allowing for further creation of caches again.
     */
    public void setCacheNames(@Nullable Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createHerodotusCache(name));
            }
            this.dynamic = false;
        } else {
            this.dynamic = true;
        }
    }

    protected Cache createHerodotusCache(String name) {
        CaffeineCache caffeineCache = (CaffeineCache) this.caffeineCacheManager.getCache(name);
        RedisCache redisCache = (RedisCache) this.redisCacheManager.getCache(name);
        log.debug("[Herodotus] |- CACHE - Herodotus cache [{}] is CREATED.", name);
        return new HerodotusCache(name, caffeineCache, redisCache, desensitization, clearRemoteOnExit, isAllowNullValues());
    }

    @Override
    @Nullable
    public Cache getCache(String name) {
        return this.cacheMap.computeIfAbsent(name, cacheName ->
                this.dynamic ? createHerodotusCache(cacheName) : null);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }
}
