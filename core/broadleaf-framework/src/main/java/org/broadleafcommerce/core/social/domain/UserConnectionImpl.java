/*-
 * #%L
 * BroadleafCommerce Framework
 * %%
 * Copyright (C) 2009 - 2022 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package org.broadleafcommerce.core.social.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * This class creates the following BLC domain object for the Spring Social User Connection.
 * The following is the SQL that is needed for Spring Social to achieve JDBC-based persistence.
 * http://static.springsource.org/spring-social/docs/1.0.x/reference/html/serviceprovider.html#service-providers-persisting-connections
 *
 * Spring Social expects the following table be created:
 * -----------------------------------------------------
 * create table UserConnection (userId varchar(255) not null,
 *  providerId varchar(255) not null,
 *  providerUserId varchar(255),
 *  rank int not null,
 *  displayName varchar(255),
 *  profileUrl varchar(512),
 *  imageUrl varchar(512),
 *  accessToken varchar(255) not null,
 *  secret varchar(255),
 *  refreshToken varchar(255),
 *  expireTime bigint,
 *  primary key (userId, providerId, providerUserId));
 *
 * create unique index UserConnectionRank on UserConnection(userId, providerId, rank);
 * ------------------------------------------------------
 *
 * NOTE: We are prefixing the table with "BLC_" to be consistent with the rest of the framework.
 * The prefix is injected into JdbcUsersConnectionRepository
 * @see org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository
 *
 * @author elbertbautista
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BLC_UserConnection")
public class UserConnectionImpl implements UserConnection {

    @EmbeddedId
    UserConnectionPK userConnectionPK;

    @Column(name = "`rank`", nullable = false)
    private Integer rank;

    @Column(name = "displayName")
    private String displayName;

    @Column(name = "profileUrl")
    private String profileUrl;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "accessToken", nullable = false)
    private String accessToken;

    @Column(name = "secret")
    private String secret;

    @Column(name = "refreshToken")
    private String refreshToken;

    @Column(name = "expireTime")
    private Long expireTime;

    @Override
    public UserConnectionPK getUserConnectionPK() {
        return userConnectionPK;
    }

    @Override
    public void setUserConnectionPK(UserConnectionPK userConnectionPK) {
        this.userConnectionPK = userConnectionPK;
    }

    @Override
    public Integer getRank() {
        return rank;
    }

    @Override
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getProfileUrl() {
        return profileUrl;
    }

    @Override
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public Long getExpireTime() {
        return expireTime;
    }

    @Override
    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public static class UserConnectionPK implements Serializable {
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        @Column(name = "userId", nullable = false)
        private String userId;

        @Column(name = "providerId", nullable = false)
        private String providerId;

        @Column(name = "providerUserId")
        private String providerUserId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public String getProviderUserId() {
            return providerUserId;
        }

        public void setProviderUserId(String providerUserId) {
            this.providerUserId = providerUserId;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            else if (!getClass().isAssignableFrom(obj.getClass())) return false;

            return userId.equals(((UserConnectionPK) obj).getUserId()) &&
                    providerId.equals(((UserConnectionPK) obj).getProviderId()) &&
                    providerUserId.equals(((UserConnectionPK) obj).getProviderUserId());
        }

        @Override
        public int hashCode() {
            return userId.hashCode() + providerId.hashCode() + providerUserId.hashCode();
        }
    }

}
