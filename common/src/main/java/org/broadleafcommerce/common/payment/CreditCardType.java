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
package org.broadleafcommerce.common.payment;

import org.broadleafcommerce.common.BroadleafEnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of credit card types.
 * 
 * @author jfischer
 *
 */
public class CreditCardType implements Serializable, BroadleafEnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, CreditCardType> TYPES = new LinkedHashMap<String, CreditCardType>();

    public static final CreditCardType MASTERCARD  = new CreditCardType("MASTERCARD", "Master Card");
    public static final CreditCardType VISA  = new CreditCardType("VISA", "Visa");
    public static final CreditCardType AMEX  = new CreditCardType("AMEX", "American Express");
    public static final CreditCardType DINERSCLUB_CARTEBLANCHE  = new CreditCardType("DINERSCLUB_CARTEBLANCHE", "Diner's Club / Carte Blanche");
    public static final CreditCardType DISCOVER  = new CreditCardType("DISCOVER", "Discover");
    public static final CreditCardType ENROUTE  = new CreditCardType("ENROUTE", "En Route");
    public static final CreditCardType JCB  = new CreditCardType("JCB", "JCB");

    public static CreditCardType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public CreditCardType() {
        //do nothing
    }

    public CreditCardType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)){
            TYPES.put(type, this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!getClass().isAssignableFrom(obj.getClass()))
            return false;
        CreditCardType other = (CreditCardType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
