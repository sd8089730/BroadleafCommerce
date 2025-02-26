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
/**
 * 
 */
package org.broadleafcommerce.core.order.service.workflow;

import org.broadleafcommerce.core.order.strategy.FulfillmentGroupItemStrategy;
import org.broadleafcommerce.core.workflow.BaseActivity;
import org.broadleafcommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("blRemoveWorkflowVerifyFulfillmentGroupItemsActivity")
public class RemoveWorkflowVerifyFulfillmentGroupItemsActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {

    public static final int ORDER = 6000;
    
    @Resource(name = "blFulfillmentGroupItemStrategy")
    protected FulfillmentGroupItemStrategy fgItemStrategy;
    
    public RemoveWorkflowVerifyFulfillmentGroupItemsActivity() {
        setOrder(ORDER);
    }
    
    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        
        request = fgItemStrategy.verify(request);
        
        context.setSeedData(request);
        return context;
    }

}
