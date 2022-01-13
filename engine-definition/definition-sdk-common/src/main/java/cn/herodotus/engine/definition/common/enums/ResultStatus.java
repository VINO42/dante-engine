/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2019-2021 Zhenggengwei<码匠君>, herodotus@aliyun.com
 *
 * This file is part of Herodotus Cloud.
 *
 * Herodotus Cloud is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * Herodotus Cloud is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with with Herodotus Cloud;
 * if no see <https://gitee.com/herodotus/herodotus-cloud>
 *
 * - Author: Zhenggengwei<码匠君>
 * - Contact: herodotus@aliyun.com
 * - License: GNU Lesser General Public License (LGPL)
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.definition.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 自定义返回码
 * <p>
 * 主要分类说明：
 * 2**.**   成功，操作被成功接收并处理
 * 3**.**	需要后续操作，需要进一步的操作以完成请求
 * 4**.**	HTTP请求错误，请求包含语法错误或无法完成请求，
 * 5**.**   平台错误，平台相关组件运行及操作错误。
 * 6**.**	关系数据库错误，服务器在处理请求的过程中发生了数据SQL操作等底层错误
 * 600.**	JDBC错误，服务器在处理请求的过程中发生了JDBC底层错误。
 * 601.**	JPA错误，服务器在处理请求的过程中发生了JPA错误。
 * 602.**	Hibernate错误，服务器在处理请求的过程中发生了Hibernate操作错误。
 * 603.**   接口参数Validation错误
 * <p>
 * 其它内容逐步补充
 *
 * @author gengwei.zheng
 */
@Schema(title = "响应结果状态", description = "自定义错误码以及对应的、友好的错误信息")
public enum ResultStatus {

    /**
     * 2*.** 成功
     */
    OK(20000, "成功"),
    NO_CONTENT(20400, "无内容"),

    /**
     * 4*.** Java常规错误
     */
    FAIL(40000, "失败"),
    WARNING(40001, "警告"),

    /**
     * 401.** 未经授权 Unauthorized	请求要求用户的身份认证
     */
    UNAUTHORIZED(40101, "未经授权，无法访问"),
    UNAUTHORIZED_CLIENT(40102, "未经授权的 Client"),
    ACCESS_DENIED(40103, "拒绝访问"),
    ACCESS_DENIED_AUTHORITY_LIMITED(40104, "权限不足，拒绝访问"),
    BAD_CREDENTIALS(40105, "无效的凭证"),
    ACCOUNT_DISABLED(40106, "账户禁用"),
    ACCOUNT_EXPIRED(40107, "账户过期"),
    CREDENTIALS_EXPIRED(40108, "凭证过期"),
    ACCOUNT_LOCKED(40109, "账户被锁定"),
    INTERNAL_AUTHENTICATION(40110, "内部调用未授权接口，请检查接口权限"),
    /**
     * 本质是用户不存在，但是返回信息不应该太清晰，以防被攻击
     */
    USERNAME_NOT_FOUND(40110, "用户名或密码错误！"),
    USER_IS_DISABLED(40111, "用户被禁用"),
    /**
     * 403.** 禁止的请求，与403对应
     */
    REPEAT_SUBMISSION(40301, "请不要重复提交"),
    FREQUENT_REQUESTS(40302, "请求过于频繁请稍后再试"),
    SQL_INJECTION_REQUEST(40303, "疑似SQL注入请求"),

    /**
     *
     */
    NOT_FOUND(40400, "资源未找到"),
    HANDLER_NOT_FOUND(40401, "处理器未找到"),

    /**
     * 405.** 方法不允许 与405对应
     */
    METHOD_NOT_ALLOWED(40501, "请求方法不支持"),
    /**
     * 406.** 不接受的请求，与406对应
     */
    UNSUPPORTED_GRANT_TYPE(40601, "不支持的 Grant Type"),
    UNSUPPORTED_RESPONSE_TYPE(40602, "不支持的 Response Type"),
    ILLEGAL_STAMP_PARAMETER(40603, "缺少签章身份标记参数"),
    STAMP_DELETE_FAILED(40604, "从缓存中删除签章失败"),
    STAMP_HAS_EXPIRED(40605, "签章已过期"),
    STAMP_MISSMATCH(40606, "签章信息无法匹配"),
    ILLEGAL_CAPTCHA_PARAMETER(40607, "验证码参数格式错误"),
    CAPTCHA_CATEGORY_INCORRECT(40608, "验证码分类错误"),
    CAPTCHA_HANDLER_NOT_EXIST(40609, "验证码处理器不存在"),
    CAPTCHA_MISSMATCH(40610, "验证码不匹配"),
    CAPTCHA_HAS_EXPIRED(40611, "验证码已过期"),
    CAPTCHA_IS_EMPTY(40612, "验证码不能为空"),

    /**
     * 412.* 未经授权 Precondition Failed 客户端请求信息的先决条件错误
     */
    INVALID_TOKEN(41201, "无法解析的Token，也许Token已经失效"),
    INVALID_GRANT(41202, "用户名或密码错误！"),
    INVALID_SCOPE(41203, "授权范围错误"),
    INVALID_CLIENT(41204, "非法的客户端"),
    INVALID_REQUEST(41205, "无效的请求，参数使用错误或配置无效."),
    INVALID_ARGUMENT(41206, "认证请求参数值错误或者参数缺失."),
    INVALID_REDIRECT_URI(41207, "重定向地址不匹配"),
    /**
     * 415.*	Unsupported Media Type	服务器无法处理请求附带的媒体格式
     */
    UNSUPPORTED_MEDIA_TYPE(41501, "不支持的 Media Type"),

    /**
     * 5*.00 系统错误
     */
    ERROR(50000, "Error"),
    NULL_POINTER_EXCEPTION(50001, "后台代码出现了空值"),
    IO_EXCEPTION(50002, "IO异常"),
    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(50003, "JSON转换为实体出错！"),
    TYPE_MISMATCH_EXCEPTION(50004, "类型不匹配"),
    MISSING_SERVLET_REQUEST_PARAMETER_EXCEPTION(50005, "接口参数使用错误或必要参数缺失，请查阅接口文档！"),
    ILLEGAL_ARGUMENT(50006, "非法参数错误"),

    SERVICE_UNAVAILABLE(50301, "Service Unavailable"),

    CLIENT_ABORT(50400, "与后端服务建立的连接已挂起"),
    /**
     * 6*.* 为数据操作相关错误
     */
    BAD_SQL_GRAMMAR(60000, "低级SQL语法错误，检查SQL能否正常运行或者字段名称是否正确"),
    /**
     * 62.* 数据库操作相关错误
     */
    DATA_INTEGRITY_VIOLATION(62000, "该数据正在被其它数据引用，请先删除引用关系，再进行数据删除操作"),
    TRANSACTION_ROLLBACK(62001, "数据事务处理失败，数据回滚"),
    /**
     * 63.* Spring Boot Validation校验相关操作
     */
    METHOD_ARGUMENT_NOT_VALID(63000, "接口参数校验错误"),
    /**
     * 64.* 临时数据操作相关错误
     */
    TOKEN_DELETE_FAILED(64000, "Token 删除失败"),

    /**
     * 7*.* 基础设施交互错误
     * 71.* Redis 操作出现错误
     * 72.* Cache 操作出现错误
     */
    PIPELINE_INVALID_COMMANDS(71000, "Redis管道包含一个或多个无效命令"),
    CACHE_CONFIG_NOT_FOUND(72000, "服务需要使用缓存，但是未找到*-cache.yaml配置");

    @Schema(title = "结果代码")
    private final int code;
    @Schema(title = "结果信息")
    private final String message;


    ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultStatus getResultEnum(int code) {
        for (ResultStatus type : ResultStatus.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return ERROR;
    }

    public static ResultStatus getResultEnum(String message) {
        for (ResultStatus type : ResultStatus.values()) {
            if (type.getMessage().equals(message)) {
                return type;
            }
        }
        return ERROR;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
