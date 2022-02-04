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

package cn.herodotus.engine.pay.aliypay.configuration;

import cn.herodotus.engine.pay.aliypay.annotation.ConditionalOnAlipay;
import cn.herodotus.engine.pay.aliypay.definition.AlipayPaymentTemplate;
import cn.herodotus.engine.pay.aliypay.definition.AlipayProfileStorage;
import cn.herodotus.engine.pay.aliypay.properties.AlipayProperties;
import cn.herodotus.engine.pay.aliypay.support.AlipayDefaultProfileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * <p>Description: 支付宝配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/1/8 21:19
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnAlipay
@EnableConfigurationProperties(AlipayProperties.class)
@ComponentScan(basePackages = {
        "cn.herodotus.engine.pay.aliypay.controller"
})
public class AlipayConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AlipayConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Pay Alipay] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public AlipayProfileStorage alipayProfileDefaultStorage(AlipayProperties alipayProperties) {
        AlipayDefaultProfileStorage alipayDefaultProfileStorage = new AlipayDefaultProfileStorage(alipayProperties);
        log.debug("[Herodotus] |- Bean [Alipay Profile Default Storage] Auto Configure.");
        return alipayDefaultProfileStorage;
    }

    @Bean
    @ConditionalOnMissingBean
    public AlipayPaymentTemplate alipayPaymentTemplate(AlipayProfileStorage alipayProfileStorage, AlipayProperties alipayProperties) {
        AlipayPaymentTemplate alipayPaymentTemplate = new AlipayPaymentTemplate(alipayProfileStorage, alipayProperties);
        log.trace("[Herodotus] |- Bean [Alipay Payment Template] Auto Configure.");
        return alipayPaymentTemplate;
    }

}
