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
package org.broadleafcommerce.core.catalog.dao;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.core.catalog.domain.CategoryProductXref;
import org.broadleafcommerce.core.catalog.domain.CategoryXref;
import org.broadleafcommerce.core.catalog.domain.CategoryXrefImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Jeff Fischer
 */
@Repository("blCategoryXrefDao")
public class CategoryXrefDaoImpl implements CategoryXrefDao {

    @PersistenceContext(unitName="blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public List<CategoryXref> readXrefsByCategoryId(Long categoryId) {
        TypedQuery<CategoryXref> query = em.createNamedQuery("BC_READ_CATEGORY_XREF_BY_CATEGORYID", CategoryXref.class);
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }

    @Override
    public List<CategoryXref> readXrefsBySubCategoryId(Long subCategoryId) {
        TypedQuery<CategoryXref> query = em.createNamedQuery("BC_READ_CATEGORY_XREF_BY_SUBCATEGORYID", CategoryXref.class);
        query.setParameter("subCategoryId", subCategoryId);
        return query.getResultList();
    }

    @Override
    public CategoryXref readXrefByIds(Long categoryId, Long subCategoryId) {
        Query query = em.createNamedQuery("BC_READ_CATEGORY_XREF_BY_IDS");
        query.setParameter("categoryId", categoryId);
        query.setParameter("subCategoryId", subCategoryId);
        return (CategoryXref) query.getSingleResult();
    }

    @Override
    public CategoryXref save(CategoryXrefImpl categoryXref){
        return em.merge(categoryXref);
    }

    @Override
    public void delete(CategoryXref categoryXref) {
        if (!em.contains(categoryXref)) {
            categoryXref = readXrefByIds(categoryXref.getCategory().getId(), categoryXref.getSubCategory().getId());
        }
        em.remove(categoryXref);        
    }

    @Override
    public CategoryProductXref save(CategoryProductXref categoryProductXref) {
        return em.merge(categoryProductXref);
    }
    
}
