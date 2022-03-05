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
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.assistant.json.jackson2.utils;

import cn.herodotus.engine.assistant.json.jackson2.deserializer.XssStringJsonDeserializer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * @author gengwei.zheng
 */
@Component
public class Jackson {

    private static final Logger logger = LoggerFactory.getLogger(Jackson.class);

    private static ObjectMapper OBJECT_MAPPER;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        OBJECT_MAPPER = this.objectMapper;
        settings(OBJECT_MAPPER);
    }

    private void settings(ObjectMapper objectMapper) {
        // 设置为中国上海时区
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        // 空值不序列化
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化时，日期的统一格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 排序key
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        // 忽略空bean转json错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 忽略在json字符串中存在，在java类中不存在字段，防止错误。
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 单引号处理
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        /**
         * 序列换成json时,将所有的long变成string
         * js中long过长精度丢失
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addDeserializer(String.class, new XssStringJsonDeserializer());

        objectMapper.registerModule(simpleModule);
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static ObjectMapper registerModule(Module module) {
        return getObjectMapper().registerModules(module);
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            return getObjectMapper().readValue(json, clazz);
        } catch (JsonParseException e) {
            logger.error("[Herodotus] |- Jackson toObject parse json error! {}", e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("[Herodotus] |- Jackson toObject mapping to object error! {}", e.getMessage());
        } catch (IOException e) {
            logger.error("[Herodotus] |- Jackson toObject read content error! {}", e.getMessage());
        }

        return null;
    }

    public static <T> String toJson(T entity) {
        try {
            return getObjectMapper().writeValueAsString(entity);
        } catch (JsonParseException e) {
            logger.error("[Herodotus] |- Jackson toCollection parse json error! {}", e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("[Herodotus] |- Jackson toCollection mapping to object error! {}", e.getMessage());
        } catch (IOException e) {
            logger.error("[Herodotus] |- Jackson toCollection read content error! {}", e.getMessage());
        }

        return null;
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, clazz);
        try {
            return getObjectMapper().readValue(json, javaType);
        } catch (JsonParseException e) {
            logger.error("[Herodotus] |- Jackson toCollection parse json error! {}", e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("[Herodotus] |- Jackson toCollection mapping to object error! {}", e.getMessage());
        } catch (IOException e) {
            logger.error("[Herodotus] |- Jackson toCollection read content error! {}", e.getMessage());
        }

        return null;
    }

    public static <T> T toCollection(String json, TypeReference<T> typeReference) {
        try {
            return getObjectMapper().readValue(json, typeReference);
        } catch (JsonParseException e) {
            logger.error("[Herodotus] |- Jackson toCollection parse json error! {}", e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("[Herodotus] |- Jackson toCollection mapping to object error! {}", e.getMessage());
        } catch (IOException e) {
            logger.error("[Herodotus] |- Jackson toCollection read content error! {}", e.getMessage());
        }

        return null;
    }
}
