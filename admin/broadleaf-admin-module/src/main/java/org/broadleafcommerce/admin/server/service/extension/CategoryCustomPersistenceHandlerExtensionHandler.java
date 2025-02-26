/*-
 * #%L
 * BroadleafCommerce Admin Module
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
package org.broadleafcommerce.admin.server.service.extension;

import org.broadleafcommerce.common.exception.ServiceException;
import org.broadleafcommerce.common.extension.ExtensionHandler;
import org.broadleafcommerce.common.extension.ExtensionResultStatusType;
import org.broadleafcommerce.core.catalog.domain.Category;
import org.broadleafcommerce.openadmin.dto.PersistencePackage;

/**
 * Extension handler for {@link org.broadleafcommerce.admin.server.service.handler.CategoryCustomPersistenceHandler}
 *
 * @author Jeff Fischer
 */
public interface CategoryCustomPersistenceHandlerExtensionHandler extends ExtensionHandler {

    /**
     * Perform any special handling for the parent category during a category add
     *
     * @param category
     * @return
     */
    ExtensionResultStatusType manageParentCategoryForAdd(PersistencePackage persistencePackage, Category category) throws ServiceException;

    /**
     * Perform any special handling for the parent category during a category update
     *
     * @param category
     * @return
     */
    ExtensionResultStatusType manageParentCategoryForUpdate(PersistencePackage persistencePackage, Category category) throws ServiceException;
}
