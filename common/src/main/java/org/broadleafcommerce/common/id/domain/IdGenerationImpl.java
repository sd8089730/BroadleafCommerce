/*-
 * #%L
 * BroadleafCommerce Common Libraries
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
package org.broadleafcommerce.common.id.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "BLC_ID_GENERATION")
public class IdGenerationImpl implements IdGeneration {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_TYPE", nullable=false)
    protected String type;

    @Column(name = "ID_MIN", nullable = true)
    protected Long begin;

    @Column(name = "ID_MAX", nullable = true)
    protected Long end;

    @Column(name = "BATCH_START", nullable = false)
    protected Long batchStart;

    @Column(name = "BATCH_SIZE", nullable=false)
    protected Long batchSize;

    @Version
    protected Integer version;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getBegin() {
        return begin;
    }

    public void setBegin(Long begin) {
        this.begin = begin;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getBatchStart() {
        return batchStart;
    }

    public void setBatchStart(Long batchStart) {
        this.batchStart = batchStart;
    }

    public Long getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Long batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((batchSize == null) ? 0 : batchSize.hashCode());
        result = prime * result + ((batchStart == null) ? 0 : batchStart.hashCode());
        result = prime * result + ((begin == null) ? 0 : begin.hashCode());
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        IdGenerationImpl other = (IdGenerationImpl) obj;
        if (batchSize == null) {
            if (other.batchSize != null)
                return false;
        } else if (!batchSize.equals(other.batchSize))
            return false;
        if (batchStart == null) {
            if (other.batchStart != null)
                return false;
        } else if (!batchStart.equals(other.batchStart))
            return false;
        if (begin == null) {
            if (other.begin != null)
                return false;
        } else if (!begin.equals(other.begin))
            return false;
        if (end == null) {
            if (other.end != null)
                return false;
        } else if (!end.equals(other.end))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

}
