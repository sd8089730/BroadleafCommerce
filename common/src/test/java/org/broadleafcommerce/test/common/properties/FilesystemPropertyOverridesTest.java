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
/**
 * 
 */
package org.broadleafcommerce.test.common.properties;

import org.broadleafcommerce.common.config.BroadleafEnvironmentConfiguringApplicationListener;
import org.broadleafcommerce.common.config.BroadleafEnvironmentConfigurer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Validates that I can pass in a system argument pointing to a file on the filesystem to override any properties in the application
 * even when a profile is specified
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = BroadleafEnvironmentConfiguringApplicationListener.class)
@ActiveProfiles("production")
@DirtiesContext
public class FilesystemPropertyOverridesTest {
    
    @Autowired
    protected Environment env;
    
    // Inside of a static @BeforeClass to ensure this code executes before Spring starts the appctx
    @BeforeClass
    public static void setOverrideProperty() {
        String overridePropertiesPath = FilesystemPropertyOverridesTest.class.getClassLoader().getResource("overridestest.properties").getFile();
        overridePropertiesPath = overridePropertiesPath.replace("%20", " ");
        overridePropertiesPath = overridePropertiesPath.replace("%40", "@");
        System.setProperty(BroadleafEnvironmentConfigurer.PROPERTY_OVERRIDES_PROPERTY, overridePropertiesPath);
    }
    
    // don't impact other tests with my property override
    @AfterClass
    public static void clearOverrideProperty() {
        System.clearProperty(BroadleafEnvironmentConfigurer.PROPERTY_OVERRIDES_PROPERTY);
    }
    
    @Test
    // this is a customized version of the Spring appctx with an overridden Environment, make sure it gets reset
    // by dirtying the context
    @DirtiesContext
    public void testPropertiesWereOverridden() {
        Assert.assertEquals("overridevalue", env.getProperty(DefaultDevelopmentOverridePropertiesTest.TEST_PROPERTY));
        Assert.assertTrue(((ConfigurableEnvironment) env).getPropertySources().contains(BroadleafEnvironmentConfigurer.OVERRIDE_SOURCES_NAME));
    }
}
