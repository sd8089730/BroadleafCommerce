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
package org.broadleafcommerce.openadmin.server.dao.provider.metadata.request;

import org.broadleafcommerce.openadmin.server.dao.DynamicEntityDao;

import java.lang.reflect.Field;

/**
 * Contains the requested field, metadata and support classes.
 *
 * @author Chris Kittrell
 */
public class AddFieldMetadataRequest extends AddMetadataRequest{

    private final Field requestedField;

    public AddFieldMetadataRequest(Field requestedField, Class<?> parentClass, Class<?> targetClass, DynamicEntityDao dynamicEntityDao, String prefix) {
        super(parentClass, targetClass, dynamicEntityDao, prefix);
        this.requestedField = requestedField;
    }

    public Field getRequestedField() {
        return requestedField;
    }
}
