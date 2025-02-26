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
package org.broadleafcommerce.common.extension;

import org.apache.commons.beanutils.BeanComparator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The ExtensionManager pattern is intended for out of box components to be extended by Broadleaf modules.
 * 
 * Each component that needs an extension should define an interface which is a descendant of ExtensionHandler.
 * The concrete ExtensionManager class will utilize that interface as a parameter (e.g. T below).   
 * 
 * The default extension manager pattern loops through all handlers and examines their {@link ExtensionResultStatusType} 
 * to determine whether or not to continue with other handlers.
 * 
 * @author bpolster
 *
 * @param <T>
 */
public abstract class ExtensionManager<T extends ExtensionHandler> implements InvocationHandler {

    protected boolean handlersSorted = false;
    protected static String LOCK_OBJECT = new String("EM_LOCK");
    
    protected T extensionHandler;
    protected List<T> handlers = new ArrayList<T>();

    /**
     * Should take in a className that matches the ExtensionHandler interface being managed.
     * @param className
     */
    @SuppressWarnings("unchecked")
    public ExtensionManager(Class<T> _clazz) {
        extensionHandler = (T) Proxy.newProxyInstance(_clazz.getClassLoader(),
                new Class[] { _clazz },
                this);
    }
    
    public T getProxy() {
        return extensionHandler;
    }

    /**
     * If you are attempting to register a handler with this manager and are invoking this outside of an {@link ExtensionManager}
     * subclass, consider using {@link #registerHandler(ExtensionHandler)} instead.
     * 
     * While the sorting of the handlers prior to their return is thread safe, adding directly to this list is not.
     * 
     * @return a list of handlers sorted by their priority
     * @see {@link #registerHandler(ExtensionHandler)}
     */
    public List<T> getHandlers() {
        if (!handlersSorted) {
            synchronized (LOCK_OBJECT) {
                sortHandlers();
            }
        }
        return handlers;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void sortHandlers() {
        if (!handlersSorted) {
            Comparator fieldCompare = new BeanComparator("priority");
            Collections.sort(handlers, fieldCompare);
            handlersSorted = true;
        }
    }
    
    /**
     * Intended to be invoked from the extension handlers themselves. This will add the given handler to this manager's list of
     * handlers. This also checks to ensure that the handler has not been already registered with this {@link ExtensionManager}
     * by checking the class names of the already-added handlers.
     * 
     * This method is thread safe.
     * 
     * @param handler the handler to register with this extension manager
     * @return true if the handler was successfully registered, false if this handler was already contained in the list of
     * handlers for this manager
     */
    public boolean registerHandler(T handler) {
        synchronized (LOCK_OBJECT) {
            boolean add = true;
            for (T item : this.handlers) {
                if (item.getClass().equals(handler.getClass())) {
                    add = false;
                }
            }
            if (add) {
                this.handlers.add(handler);
                handlersSorted = false;
            }
            
            return add;
        }
    }

    public void setHandlers(List<T> handlers) {
        this.handlers = handlers;
    }
    
    /**
     * Utility method that is useful for determining whether or not an ExtensionManager implementation
     * should continue after processing a ExtensionHandler call.
     * 
     * By default, returns true for CONTINUE
     * 
     * @return
     */
    public boolean shouldContinue(ExtensionResultStatusType result, ExtensionHandler handler,
            Method method, Object[] args) {
        if (result != null) {
            if (ExtensionResultStatusType.HANDLED_STOP.equals(result)) {
                return false;
            }
            
            if (ExtensionResultStatusType.HANDLED.equals(result) && ! continueOnHandled()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns whether or not this extension manager continues on {@link ExtensionResultStatusType}.HANDLED.   
     * 
     * @return
     */
    public boolean continueOnHandled() {
        return false;
    }

    /**
     * {@link ExtensionManager}s don't really need a priority but they pick up this property due to the 
     * fact that we want them to implement the same interface <T> as the handlers they are managing.   
     * 
     * @return
     */
    public int getPriority() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        boolean notHandled = true;
        for (ExtensionHandler handler : getHandlers()) {
            try {
                if (handler.isEnabled()) {
                    ExtensionResultStatusType result = (ExtensionResultStatusType) method.invoke(handler, args);
                    if (!ExtensionResultStatusType.NOT_HANDLED.equals(result)) {
                        notHandled = false;
                    }
                    if (!shouldContinue(result, handler, method, args)) {
                        break;
                    }
                }
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
        if (notHandled) {
            return ExtensionResultStatusType.NOT_HANDLED;
        } else {
            return ExtensionResultStatusType.HANDLED;
        }
    }

    /**
     * Provides a mechanism for executing multiple extension handler touchpoints without utilizing reflection. This is a reasonable
     * alternative when the ExtensionManager is used in an operation that is very sensitive to the time cost involved in reflection
     * (e.g. an operation that has a high volume of calls)
     *
     * @param operation
     * @param params
     * @return
     */
    protected ExtensionResultStatusType execute(ExtensionManagerOperation operation, Object... params) {
        boolean notHandled = true;
        for (ExtensionHandler handler : getHandlers()) {
            if (handler.isEnabled()) {
                ExtensionResultStatusType result = operation.execute(handler, params);
                if (!ExtensionResultStatusType.NOT_HANDLED.equals(result)) {
                    notHandled = false;
                }
                if (!shouldContinue(result, handler, null, null)) {
                    break;
                }
            }
        }
        if (notHandled) {
            return ExtensionResultStatusType.NOT_HANDLED;
        } else {
            return ExtensionResultStatusType.HANDLED;
        }
    }
}
