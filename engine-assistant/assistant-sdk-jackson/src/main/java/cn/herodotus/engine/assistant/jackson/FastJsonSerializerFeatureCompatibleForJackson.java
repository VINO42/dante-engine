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

package cn.herodotus.engine.assistant.jackson;

import cn.herodotus.engine.assistant.jackson.serializer.*;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Date;
import java.util.List;

/**
 * @author gengwei.zheng
 */
public class FastJsonSerializerFeatureCompatibleForJackson extends BeanSerializerModifier {

    final private JsonSerializer<Object> nullBooleanJsonSerializer;
    final private JsonSerializer<Object> nullNumberJsonSerializer;
    final private JsonSerializer<Object> nullListJsonSerializer;
    final private JsonSerializer<Object> nullStringJsonSerializer;
    final private JsonSerializer<Object> nullMapJsonSerializer;

    public FastJsonSerializerFeatureCompatibleForJackson(SerializerFeature... features) {
        int config = 0;
        for (SerializerFeature feature : features) {
            config |= feature.mask;
        }
        nullBooleanJsonSerializer = (config & SerializerFeature.WriteNullBooleanAsFalse.mask) != 0 ? new NullBooleanSerializer() : null;
        nullNumberJsonSerializer = (config & SerializerFeature.WriteNullNumberAsZero.mask) != 0 ? new NullNumberSerializer() : null;
        nullListJsonSerializer = (config & SerializerFeature.WriteNullListAsEmpty.mask) != 0 ? new NullListJsonSerializer() : null;
        nullStringJsonSerializer = (config & SerializerFeature.WriteNullStringAsEmpty.mask) != 0 ? new NullStringSerializer() : null;
        nullMapJsonSerializer = (config & SerializerFeature.WriteNullMapAsEmpty.mask) != 0 ? new NullMapSerializer() : null;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        for (BeanPropertyWriter writer : beanProperties) {
            final JavaType javaType = writer.getType();
            final Class<?> rawClass = javaType.getRawClass();
            if (javaType.isArrayType() || javaType.isCollectionLikeType()) {
                writer.assignNullSerializer(nullListJsonSerializer);
            } else if (Number.class.isAssignableFrom(rawClass) && rawClass.getName().startsWith("java.lang")) {
                writer.assignNullSerializer(nullNumberJsonSerializer);
            } else if (Boolean.class.equals(rawClass)) {
                writer.assignNullSerializer(nullBooleanJsonSerializer);
            } else if (String.class.equals(rawClass) || Date.class.equals(rawClass)) {
                writer.assignNullSerializer(nullStringJsonSerializer);
            } else {
                writer.assignNullSerializer(nullMapJsonSerializer);
            }
        }
        return beanProperties;
    }
}
