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
package org.broadleafcommerce.core.pricing.service.workflow;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.payment.domain.OrderPayment;
import org.broadleafcommerce.core.workflow.BaseActivity;
import org.broadleafcommerce.core.workflow.ProcessContext;
import org.springframework.stereotype.Component;

/**
 * The AdjustOrderPaymentsActivity is responsible for adjusting any of the order payments
 * that have already been applied to the order. This happens when order payments have
 * been applied to the order but the order total has changed. In the case of a hosted
 * gateway solution like PayPal Express Checkout, the order payment is created when the
 * customer redirects to the Review Order Page (Checkout page) and the user selects
 * a shipping method which may affect the order total. Since the Hosted Order payment
 * is unconfirmed, we need to adjust the amount on this order payment before
 * we complete checkout and confirm the payment with PayPal again.
 *
 * Another scenario this handles is if there is an unconfirmed Credit Card applied to the order.
 * This can happen if the implementation is PCI-Compliant and the Credit Card number is sent to Broadleaf
 * and will make a server to server call to the configured Payment Gateway.
 *
 * For this default implementation,
 * This algorithm will add up all the active applied payments to the order that are not of type
 * 'UNCONFIRMED' AND (payment type 'THIRD_PARTY_ACCOUNT' OR 'CREDIT_CARD')
 * The order.getTotal() minus all the applied payments that are NOT Unconfirmed and of a Third Party account
 * will then be set as the new amount that should be processed by the Third Party Account.
 *
 * Third Party Account Example:
 * 1) Initial Checkout Step
 * Order - Total = $30
 * - Order Payment (PayPal Express Checkout) - [Unconfirmed] $10
 * - Gift Card - [Unconfirmed] $10
 * - Customer Credit - [Unconfirmed] $10
 *
 * 2) Shipping Method picked and changes the order total
 * Order - Total = $35
 * - Order Payment (PayPal Express Checkout) - [Unconfirmed] $10
 * - Gift Card - [Unconfirmed] $10
 * - Customer Credit - [Unconfirmed] $10
 *
 * 3) Adjust Order Payment Activity ($35 - ($10 + $10)) = $15
 * Order - Total = $35
 * - Order Payment (PayPal Express Checkout) - [Unconfirmed] $15
 * - Gift Card - [Unconfirmed] $10
 * - Customer Credit - [Unconfirmed] $10
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Component("blAdjustOrderPaymentsActivity")
public class AdjustOrderPaymentsActivity extends BaseActivity<ProcessContext<Order>> {
    
    public static final int ORDER = 9000;
    
    public AdjustOrderPaymentsActivity() {
        setOrder(ORDER);
    }

    @Override
    public ProcessContext<Order> execute(ProcessContext<Order> context) throws Exception {
        Order order = context.getSeedData();

        OrderPayment unconfirmedThirdPartyOrCreditCard = null;
        Money appliedPaymentsWithoutThirdPartyOrCC = Money.ZERO;
        for (OrderPayment payment : order.getPayments()) {
            if (payment.isActive()) {
                if (!payment.isConfirmed() && payment.isFinalPayment())  {
                    unconfirmedThirdPartyOrCreditCard = payment;
                } else if (payment.getAmount() != null) {
                    appliedPaymentsWithoutThirdPartyOrCC = appliedPaymentsWithoutThirdPartyOrCC.add(payment.getAmount());
                }
            }

        }

        if (unconfirmedThirdPartyOrCreditCard != null) {
            Money difference = order.getTotal().subtract(appliedPaymentsWithoutThirdPartyOrCC);
            unconfirmedThirdPartyOrCreditCard.setAmount(difference);
        }

        context.setSeedData(order);
        return context;
    }

}
