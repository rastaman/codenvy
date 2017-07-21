/*
 *  [2012] - [2017] Codenvy, S.A.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.selenium.dashboard.organization;

import org.eclipse.che.selenium.pageobject.dashboard.NavigationBar;
import com.codenvy.selenium.pageobject.dashboard.organization.OrganizationListPage;
import com.google.inject.Inject;

import org.eclipse.che.selenium.core.user.DefaultTestUser;
import org.eclipse.che.selenium.pageobject.dashboard.Dashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * Test validates organization views for simple user being a member of any organization.
 *
 * @author Ann Shumilova
 */
public class UserEmptyOrganizationTest {
    private static final Logger LOG = LoggerFactory.getLogger(UserEmptyOrganizationTest.class);

    @Inject
    private OrganizationListPage organizationListPage;
    @Inject
    private NavigationBar        navigationBar;
    @Inject
    private Dashboard            dashboard;
    @Inject
    private DefaultTestUser      testUser;

    @BeforeClass
    public void setUp() {
        dashboard.open(testUser.getAuthToken());
    }

    @Test
    public void testOrganizationListComponents() {
        navigationBar.waitNavigationBar();
        navigationBar.clickOnMenu(NavigationBar.MenuItem.ORGANIZATIONS);
        organizationListPage.waitForOrganizationsToolbar();
        organizationListPage.waitForOrganizationsEmptyList();

        assertEquals(navigationBar.getMenuCounterValue(NavigationBar.MenuItem.ORGANIZATIONS), "");
        assertEquals(organizationListPage.getOrganizationsToolbarTitle(), "Organizations");
        assertFalse(organizationListPage.isAddOrganizationButtonVisible());
        assertFalse(organizationListPage.isSearchInputVisible());
    }
}
