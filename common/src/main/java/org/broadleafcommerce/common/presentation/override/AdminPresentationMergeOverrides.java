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
package org.broadleafcommerce.common.presentation.override;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows a non-comprehensive override of admin presentation annotation
 * property values for a target entity field.
 *
 * @see org.broadleafcommerce.common.presentation.AdminPresentation
 * @see org.broadleafcommerce.common.presentation.AdminPresentationToOneLookup
 * @see org.broadleafcommerce.common.presentation.AdminPresentationDataDrivenEnumeration
 * @see org.broadleafcommerce.common.presentation.AdminPresentationCollection
 * @see org.broadleafcommerce.common.presentation.AdminPresentationAdornedTargetCollection
 * @see org.broadleafcommerce.common.presentation.AdminPresentationMap
 * @author Jeff Fischer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AdminPresentationMergeOverrides {

    /**
     * The new configurations for each field targeted for override
     *
     * @return field specific overrides
     */
    AdminPresentationMergeOverride[] value();

}
