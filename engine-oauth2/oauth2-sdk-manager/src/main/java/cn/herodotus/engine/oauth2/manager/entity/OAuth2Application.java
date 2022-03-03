/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2020-2030 ZHENGGENGWEI<码匠君>. All rights reserved.
 *
 * - Author: ZHENGGENGWEI<码匠君>
 * - Contact: herodotus@aliyun.com
 * - Blog and source code availability: https://gitee.com/herodotus/herodotus-cloud
 */

package cn.herodotus.engine.oauth2.manager.entity;

import cn.herodotus.engine.data.core.entity.BaseSysEntity;
import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import cn.herodotus.engine.oauth2.core.enums.ApplicationType;
import cn.herodotus.engine.oauth2.core.enums.Signature;
import cn.hutool.core.util.IdUtil;
import com.google.common.base.MoreObjects;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Description: OAuth2 应用 </p>
 * <p>
 * Spring Authorization Server 默认的 RegisteredClient 不便于扩展。增加该类用于存储标准 RegisteredClient 表结构以外的扩展信息。
 *
 * @author : gengwei.zheng
 * @date : 2022/3/1 16:45
 */
@Schema(name = "OAuth2 应用")
@Entity
@Table(name = "oauth2_application", indexes = {@Index(name = "oauth2_application_id_idx", columnList = "application_id")})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = OAuth2Constants.REGION_OAUTH2_APPLICATION)
public class OAuth2Application extends BaseSysEntity {

    @Schema(name = "应用ID")
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "application_id", length = 64)
    private String applicationId;

    @Schema(name = "应用名称", required = true)
    @Column(name = "application_name", length = 128)
    private String applicationName;

    @Schema(name = "应用简称", title = "应用的简称、别名、缩写等信息")
    @Column(name = "abbreviation", length = 64)
    private String abbreviation;

    @Schema(name = "Logo", title = "Logo存储信息，可以是URL或者路径等")
    @Column(name = "logo", length = 1024)
    private String logo;

    @Schema(name = "主页信息", title = "应用相关的主页信息方便查询")
    @Column(name = "homepage", length = 1024)
    private String homepage;

    @Column(name = "application_type")
    @Enumerated(EnumType.ORDINAL)
    private ApplicationType applicationType = ApplicationType.WEB;

    @Schema(name = "客户端Id", title = "默认为系统自动生成")
    @Column(name = "client_id", length = 100)
    private String clientId = IdUtil.fastSimpleUUID();

    @Schema(name = "客户端秘钥", title = "这里存储的客户端秘钥是明文，方便使用。默认为系统自动生成")
    @Column(name = "client_secret", length = 100)
    private String clientSecret = IdUtil.fastSimpleUUID();

    @Schema(name = "回调地址", title = "支持多个值，以逗号分隔", required = true)
    @Column(name = "redirect_uris", length = 1000)
    private String redirectUris;

    @Schema(name = "认证模式", title = "支持多个值，以逗号分隔", required = true)
    @Column(name = "authorization_grant_types", length = 1000)
    private String authorizationGrantTypes;

    @Schema(name = "客户端认证模式", title = "支持多个值，以逗号分隔", required = true)
    @Column(name = "client_authentication_methods", length = 1000)
    private String clientAuthenticationMethods;

    @Schema(name = "需要证明Key", title = "如果客户端在执行授权码授予流时需要提供验证密钥质询和验证器, 默认False")
    @Column(name = "require_proof_key")
    private Boolean requireProofKey = Boolean.FALSE;

    @Schema(name = "需要认证确认", title = "如果客户端在执行授权码授予流时需要提供验证密钥质询和验证器, 默认False")
    @Column(name = "require_authorization_consent")
    private Boolean requireAuthorizationConsent = Boolean.TRUE;

    @Schema(name = "客户端JSON Web密钥集的URL", title = "客户端JSON Web密钥集的URL")
    @Column(name = "jwk_set_url", length = 1000)
    private String jwkSetUrl;

    @Schema(name = "accessToken 有效时间", title = "默认5分钟，使用 Duration 时间格式")
    @Column(name = "access_token_validity")
    private Duration accessTokenValidity = Duration.ofSeconds(5);

    @Schema(name = "是否重用 Refresh Token", title = "默认值 True")
    @Column(name = "reuse_refresh_tokens")
    private Boolean reuseRefreshTokens = Boolean.TRUE;

    @Schema(name = "refreshToken 有效时间", title = "默认60分钟，使用 Duration 时间格式")
    @Column(name = "refresh_token_validity")
    private Duration refreshTokenValidity = Duration.ofHours(1);

    @Schema(name = "JWS algorithm", title = "JWT 算法用于签名 ID Token， 默认值 RS256")
    @Column(name = "signature_algorithm")
    private Signature signature = Signature.RS256;

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = OAuth2Constants.REGION_OAUTH2_APPLICATION_SCOPE)
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "oauth2_application_scope",
            joinColumns = {@JoinColumn(name = "application_id")},
            inverseJoinColumns = {@JoinColumn(name = "scope_id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"application_id", "scope_id"})},
            indexes = {@Index(name = "oauth2_application_scope_aid_idx", columnList = "application_id"), @Index(name = "oauth2_application_scope_sid_idx", columnList = "scope_id")})
    private Set<OAuth2Scope> scopes = new HashSet<>();

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }

    public String getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    public Boolean getRequireProofKey() {
        return requireProofKey;
    }

    public void setRequireProofKey(Boolean requireProofKey) {
        this.requireProofKey = requireProofKey;
    }

    public Boolean getRequireAuthorizationConsent() {
        return requireAuthorizationConsent;
    }

    public void setRequireAuthorizationConsent(Boolean requireAuthorizationConsent) {
        this.requireAuthorizationConsent = requireAuthorizationConsent;
    }

    public String getJwkSetUrl() {
        return jwkSetUrl;
    }

    public void setJwkSetUrl(String jwkSetUrl) {
        this.jwkSetUrl = jwkSetUrl;
    }

    public Duration getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Duration accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Boolean getReuseRefreshTokens() {
        return reuseRefreshTokens;
    }

    public void setReuseRefreshTokens(Boolean reuseRefreshTokens) {
        this.reuseRefreshTokens = reuseRefreshTokens;
    }

    public Duration getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Duration refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public Set<OAuth2Scope> getScopes() {
        return scopes;
    }

    public void setScopes(Set<OAuth2Scope> scopes) {
        this.scopes = scopes;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("applicationId", applicationId)
                .add("applicationName", applicationName)
                .add("abbreviation", abbreviation)
                .add("logo", logo)
                .add("homepage", homepage)
                .add("applicationType", applicationType)
                .add("clientId", clientId)
                .add("clientSecret", clientSecret)
                .add("redirectUris", redirectUris)
                .add("authorizationGrantTypes", authorizationGrantTypes)
                .add("clientAuthenticationMethods", clientAuthenticationMethods)
                .add("requireProofKey", requireProofKey)
                .add("requireAuthorizationConsent", requireAuthorizationConsent)
                .add("jwkSetUrl", jwkSetUrl)
                .add("accessTokenValidity", accessTokenValidity)
                .add("reuseRefreshTokens", reuseRefreshTokens)
                .add("refreshTokenValidity", refreshTokenValidity)
                .add("signature", signature)
                .toString();
    }
}
