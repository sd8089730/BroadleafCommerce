/*-
 * #%L
 * BroadleafCommerce Open Admin Platform
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
package org.broadleafcommerce.openadmin.server.security.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: jfischer
 * Date: 9/24/11
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BLC_ADMIN_PERMISSION_ENTITY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blAdminSecurity")
public class AdminPermissionQualifiedEntityImpl implements AdminPermissionQualifiedEntity, Serializable {

    private static final Log LOG = LogFactory.getLog(AdminPermissionQualifiedEntityImpl.class);
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "AdminPermissionEntityId")
    @GenericGenerator(
        name="AdminPermissionEntityId",
        strategy="org.broadleafcommerce.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="AdminPermissionEntityImpl"),
            @Parameter(name="entity_name", value="org.broadleafcommerce.openadmin.server.security.domain.AdminPermissionQualifiedEntityImpl")
        }
    )
    @Column(name = "ADMIN_PERMISSION_ENTITY_ID")
    protected Long id;

    @Column(name = "CEILING_ENTITY", nullable=false)
    @AdminPresentation(friendlyName = "AdminPermissionQualifiedEntityImpl_Ceiling_Entity_Name", order=1, group = "AdminPermissionQualifiedEntityImpl_Permission", prominent=true)
    protected String ceilingEntityFullyQualifiedName;

    @ManyToOne(targetEntity = AdminPermissionImpl.class)
    @JoinColumn(name = "ADMIN_PERMISSION_ID")
    protected AdminPermission adminPermission;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getCeilingEntityFullyQualifiedName() {
        return ceilingEntityFullyQualifiedName;
    }

    @Override
    public void setCeilingEntityFullyQualifiedName(String ceilingEntityFullyQualifiedName) {
        this.ceilingEntityFullyQualifiedName = ceilingEntityFullyQualifiedName;
    }

    @Override
    public AdminPermission getAdminPermission() {
        return adminPermission;
    }

    @Override
    public void setAdminPermission(AdminPermission adminPermission) {
        this.adminPermission = adminPermission;
    }

    public void checkCloneable(AdminPermissionQualifiedEntity qualifiedEntity) throws CloneNotSupportedException, SecurityException, NoSuchMethodException {
        Method cloneMethod = qualifiedEntity.getClass().getMethod("clone", new Class[]{});
        if (cloneMethod.getDeclaringClass().getName().startsWith("org.broadleafcommerce") && !qualifiedEntity.getClass().getName().startsWith("org.broadleafcommerce")) {
            //subclass is not implementing the clone method
            throw new CloneNotSupportedException("Custom extensions and implementations should implement clone.");
        }
    }

    @Override
    public AdminPermissionQualifiedEntity clone() {
        AdminPermissionQualifiedEntity clone;
        try {
            clone = (AdminPermissionQualifiedEntity) Class.forName(this.getClass().getName()).newInstance();
            try {
                checkCloneable(clone);
            } catch (CloneNotSupportedException e) {
                LOG.warn("Clone implementation missing in inheritance hierarchy outside of Broadleaf: " + clone.getClass().getName(), e);
            }
            clone.setId(id);
            clone.setCeilingEntityFullyQualifiedName(ceilingEntityFullyQualifiedName);

            //don't clone the AdminPermission, as it would cause a recursion
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return clone;
    }
}
