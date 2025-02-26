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
package org.broadleafcommerce.common.workflow;

import org.broadleafcommerce.core.workflow.BaseActivity;
import org.broadleafcommerce.core.workflow.ProcessContext;

import java.util.List;

public class SimpleActivity extends BaseActivity<ProcessContext<List<String>>> {

    protected String name;

    public SimpleActivity(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public ProcessContext<List<String>> execute(ProcessContext<List<String>> context) throws Exception {
        context.getSeedData().add(name);
        return context;
    }
    
    @Override
    public boolean getAutomaticallyRegisterRollbackHandler() {
        return true;
    }
    
}
