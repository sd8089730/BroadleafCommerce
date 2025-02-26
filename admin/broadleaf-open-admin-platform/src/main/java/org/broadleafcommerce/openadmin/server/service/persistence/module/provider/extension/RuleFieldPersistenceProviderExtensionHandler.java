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
package org.broadleafcommerce.openadmin.server.service.persistence.module.provider.extension;

import org.broadleafcommerce.common.extension.ExtensionHandler;
import org.broadleafcommerce.common.extension.ExtensionResultHolder;
import org.broadleafcommerce.common.extension.ExtensionResultStatusType;
import org.broadleafcommerce.common.rule.QuantityBasedRule;

/**
 * For internal usage. Allows extending API calls without subclassing the entity.
 *
 * @author Jeff Fischer
 */
public interface RuleFieldPersistenceProviderExtensionHandler extends ExtensionHandler {

    ExtensionResultStatusType transformId(Object rule, ExtensionResultHolder<Long> resultHolder);

    ExtensionResultStatusType postAdd(Object rule, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType postUpdate(Object rule);

    ExtensionResultStatusType establishDirtyState(Object rule, ExtensionResultHolder<Boolean> resultHolder);

    public static final int DEFAULT_PRIORITY = Integer.MAX_VALUE;

}
