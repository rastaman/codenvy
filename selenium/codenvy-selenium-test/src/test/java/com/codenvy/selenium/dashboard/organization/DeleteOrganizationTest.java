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
import com.codenvy.selenium.pageobject.dashboard.ConfirmDialog;
import org.eclipse.che.selenium.pageobject.dashboard.NavigationBar;
import com.codenvy.selenium.pageobject.dashboard.organization.OrganizationListPage;
import com.codenvy.selenium.pageobject.dashboard.organization.OrganizationPage;
import com.google.inject.Inject;

import org.eclipse.che.commons.lang.NameGenerator;
import org.eclipse.che.selenium.core.user.AdminTestUser;
import org.eclipse.che.selenium.core.user.DefaultTestUser;
import org.eclipse.che.selenium.pageobject.dashboard.Dashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Test validates organization deletion.
 *
 * @author Ann Shumilova
 */
public class DeleteOrganizationTest {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteOrganizationTest.class);

    private OrganizationDto parentOrganization;
    private OrganizationDto childOrganization;

    @Inject
    private OrganizationListPage                organizationListPage;
    @Inject
    private OrganizationPage                    organizationPage;
    @Inject
    private NavigationBar                       navigationBar;
    @Inject
    private ConfirmDialog                       confirmDialog;
    @Inject
    private Dashboard                           dashboard;
    @Inject
    private OnpremTestOrganizationServiceClient organizationServiceClient;
    @Inject
    private DefaultTestUser                     testUser;
    @Inject
    private AdminTestUser                       adminTestUser;

    @BeforeClass
    public void setUp() throws Exception {
        dashboard.open(adminTestUser.getAuthToken());
        parentOrganization = organizationServiceClient.createOrganization(NameGenerator.generate("organization", 5), adminTestUser.getAuthToken());
        childOrganization = organizationServiceClient
                .createOrganization(NameGenerator.generate("organization", 5), parentOrganization.getId(), adminTestUser.getAuthToken());

        organizationServiceClient.addOrganizationAdmin(parentOrganization.getId(), testUser.getId(), adminTestUser.getAuthToken());
        organizationServiceClient.addOrganizationAdmin(childOrganization.getId(), testUser.getId(), adminTestUser.getAuthToken());

        dashboard.open(testUser.getAuthToken());
    }

    @AfterClass
    public void tearDown() throws Exception {
        organizationServiceClient.deleteOrganizationById(childOrganization.getId(), adminTestUser.getAuthToken());
        organizationServiceClient.deleteOrganizationById(parentOrganization.getId(), adminTestUser.getAuthToken());
    }

    @Test(priority = 1)
    public void testSubOrganizationDelete() {
        navigationBar.waitNavigationBar();
        navigationBar.clickOnMenu(NavigationBar.MenuItem.ORGANIZATIONS);
        organizationListPage.waitForOrganizationsToolbar();
        organizationListPage.waitForOrganizationsList();

        organizationListPage.clickOnOrganization(childOrganization.getQualifiedName());

        organizationPage.waitOrganizationName(childOrganization.getName());
        organizationPage.clickDeleteOrganizationButton();
        confirmDialog.waitOpened();

        assertEquals(confirmDialog.getTitle(), "Delete organization");
        assertEquals(confirmDialog.getMessage(), "Would you like to delete organization '" + childOrganization.getName() + "'?");
        assertEquals(confirmDialog.getConfirmButtonTitle(), "Delete");

        confirmDialog.clickConfirm();
        confirmDialog.waitClosed();

        organizationListPage.waitForOrganizationsList();
        organizationListPage.waitForOrganizationIsRemoved(childOrganization.getQualifiedName());
        assertEquals(navigationBar.getMenuCounterValue(NavigationBar.MenuItem.ORGANIZATIONS), "1");
        assertEquals(organizationListPage.getOrganizationListItemCount(), 1);
    }

    @Test(priority = 2)
    public void testParentOrganizationDeletion() {
        navigationBar.waitNavigationBar();
        navigationBar.clickOnMenu(NavigationBar.MenuItem.ORGANIZATIONS);
        organizationListPage.waitForOrganizationsToolbar();
        organizationListPage.waitForOrganizationsList();

        organizationListPage.clickOnOrganization(parentOrganization.getName());

        organizationPage.waitOrganizationName(parentOrganization.getName());
        organizationPage.clickDeleteOrganizationButton();
        confirmDialog.waitOpened();

        assertEquals(confirmDialog.getTitle(), "Delete organization");
        assertEquals(confirmDialog.getMessage(), "Would you like to delete organization '" + parentOrganization.getName() + "'?");
        assertEquals(confirmDialog.getConfirmButtonTitle(), "Delete");

        confirmDialog.clickConfirm();
        confirmDialog.waitClosed();

        organizationListPage.waitForOrganizationsEmptyList();
        assertEquals(navigationBar.getMenuCounterValue(NavigationBar.MenuItem.ORGANIZATIONS), "");
    }
}
