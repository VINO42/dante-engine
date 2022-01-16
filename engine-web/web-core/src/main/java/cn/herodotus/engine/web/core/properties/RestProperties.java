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

package cn.herodotus.engine.web.core.properties;

import cn.herodotus.engine.web.core.constants.WebConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Description: Rest相关配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2020/5/31 16:39
 */
@ConfigurationProperties(prefix = WebConstants.PROPERTY_PLATFORM_REST)
public class RestProperties {

    private RequestMapping requestMapping = new RequestMapping();
    private RestTemplate restTemplate = new RestTemplate();

    public RequestMapping getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static class RequestMapping implements Serializable {
        /**
         * 指定扫描的命名空间。未指定的命名空间中，即使包含RequestMapping，也不会被添加进来。
         */
        private List<String> scanGroupIds;
        /**
         * Spring 中会包含 Controller和 RestController，
         * 如果该配置设置为True，那么就只扫描RestController
         * 如果该配置设置为False，那么Controller和 RestController斗扫描。
         */
        private boolean justScanRestController = false;

        public void setScanGroupIds(List<String> scanGroupIds) {
            this.scanGroupIds = scanGroupIds;
        }

        public List<String> getScanGroupIds() {
            List<String> defaultGroupIds = Stream.of("cn.herodotus.engine", "cn.herodotus.cloud").collect(Collectors.toList());

            if (CollectionUtils.isEmpty(this.scanGroupIds)) {
                this.scanGroupIds = new ArrayList<>();
            }

            this.scanGroupIds.addAll(defaultGroupIds);
            return scanGroupIds;
        }

        public boolean isJustScanRestController() {
            return justScanRestController;
        }

        public void setJustScanRestController(boolean justScanRestController) {
            this.justScanRestController = justScanRestController;
        }
    }

    public static class RestTemplate implements Serializable {
        /**
         * RestTemplate 读取超时5秒,默认无限限制,单位：毫秒
         */
        private int readTimeout = 15000;
        /**
         * 连接超时15秒，默认无限制，单位：毫秒
         */
        private int connectTimeout = 15000;

        public int getReadTimeout() {
            return readTimeout;
        }

        public void setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }

        public void setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
        }
    }
}
