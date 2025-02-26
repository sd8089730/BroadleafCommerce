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
package org.broadleafcommerce.core.offer.dao;

import org.broadleafcommerce.common.persistence.EntityConfiguration;
import org.broadleafcommerce.core.offer.domain.CustomerOffer;
import org.broadleafcommerce.core.offer.domain.CustomerOfferImpl;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository("blCustomerOfferDao")
public class CustomerOfferDaoImpl implements CustomerOfferDao {

    @PersistenceContext(unitName="blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public CustomerOffer create() {
        return ((CustomerOffer) entityConfiguration.createEntityInstance(CustomerOffer.class.getName()));
    }

    public void delete(CustomerOffer customerOffer) {
        if (!em.contains(customerOffer)) {
            customerOffer = readCustomerOfferById(customerOffer.getId());
        }
        em.remove(customerOffer);
    }

    public CustomerOffer save(final CustomerOffer customerOffer) {
        return em.merge(customerOffer);
    }

    public CustomerOffer readCustomerOfferById(final Long customerOfferId) {
        return (CustomerOffer) em.find(CustomerOfferImpl.class, customerOfferId);
    }

    @SuppressWarnings("unchecked")
    public List<CustomerOffer> readCustomerOffersByCustomer(final Customer customer) {
        final Query query = em.createNamedQuery("BC_READ_CUSTOMER_OFFER_BY_CUSTOMER_ID");
        query.setParameter("customerId", customer.getId());
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Offer");

        return query.getResultList();
    }

}
