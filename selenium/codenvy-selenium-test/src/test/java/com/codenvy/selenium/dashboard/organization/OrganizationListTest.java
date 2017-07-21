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

import com.codenvy.organization.shared.dto.OrganizationDto;
import com.codenvy.selenium.core.client.OnpremTestOrganizationServiceClient;
import org.eclipse.che.selenium.pageobject.dashboard.NavigationBar;
import com.codenvy.selenium.pageobject.dashboard.organization.OrganizationListPage;
import com.google.inject.Inject;

import org.eclipse.che.selenium.core.user.AdminTestUser;
import org.eclipse.che.selenium.pageobject.dashboard.Dashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Test validates the main UI elements on the organization list page.
 *
 * @author Ann Shumilova
 */
public class OrganizationListTest {
    private static final Logger LOG = LoggerFactory.getLogger(OrganizationListTest.class);

    private List<OrganizationDto> organizations;

    @Inject
    private OrganizationListPage                organizationListPage;
    @Inject
    private NavigationBar                       navigationBar;
    @Inject
    private Dashboard                           dashboard;
    @Inject
    private OnpremTestOrganizationServiceClient organizationServiceClient;
    @Inject
    private AdminTestUser                       adminTestUser;

    @BeforeClass
    public void setUp() throws Exception {
        dashboard.open(adminTestUser.getAuthToken());
        organizations = organizationServiceClient.getOrganizations(adminTestUser.getAuthToken());
    }

    @Test
    public void testOrganizationListComponents() {
        navigationBar.waitNavigationBar();
        int organizationsCount = organizations.size();
        navigationBar.clickOnMenu(NavigationBar.MenuItem.ORGANIZATIONS);
        organizationListPage.waitForOrganizationsToolbar();
        organizationListPage.waitForOrganizationsList();

        assertEquals(navigationBar.getMenuCounterValue(NavigationBar.MenuItem.ORGANIZATIONS), String.valueOf(organizationsCount));
        assertEquals(organizationListPage.getOrganizationsToolbarTitle(), "Organizations");
        assertEquals(navigationBar.getMenuCounterValue(NavigationBar.MenuItem.ORGANIZATIONS), String.valueOf(organizationsCount));
        assertEquals(organizationListPage.getOrganizationListItemCount(), organizationsCount);
        assertTrue(organizationListPage.isAddOrganizationButtonVisible());
        assertTrue(organizationListPage.isSearchInputVisible());
        //Check all headers are present:
        ArrayList<String> headers = organizationListPage.getOrganizationListHeaders();
        assertTrue(headers.contains(OrganizationListPage.OrganizationListHeader.NAME.getTitle()));
        assertTrue(headers.contains(OrganizationListPage.OrganizationListHeader.MEMBERS.getTitle()));
        assertTrue(headers.contains(OrganizationListPage.OrganizationListHeader.TOTAL_RAM.getTitle()));
        assertTrue(headers.contains(OrganizationListPage.OrganizationListHeader.AVAILABLE_RAM.getTitle()));
        assertTrue(headers.contains(OrganizationListPage.OrganizationListHeader.SUB_ORGANIZATIONS.getTitle()));
        assertTrue(headers.contains(OrganizationListPage.OrganizationListHeader.ACTIONS.getTitle()));
    }
}
