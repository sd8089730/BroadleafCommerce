/*-
 * #%L
 * BroadleafCommerce Integration
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
package org.broadleafcommerce.security.service.dataprovider;

import org.broadleafcommerce.openadmin.server.security.domain.AdminRole;
import org.broadleafcommerce.openadmin.server.security.domain.AdminRoleImpl;
import org.testng.annotations.DataProvider;

public class AdminRoleDataProvider {
    @DataProvider(name = "setupAdminRole")
    public static Object[][] createAdminRole() {
        AdminRole adminRole = new AdminRoleImpl();
        adminRole.setName("TestAdminUserRole");
        adminRole.setDescription("Test Admin Role");

        return new Object[][] { new Object[] { adminRole } };
    }
}
