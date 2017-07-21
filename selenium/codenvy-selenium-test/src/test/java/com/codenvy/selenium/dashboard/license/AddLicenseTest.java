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
package com.codenvy.selenium.dashboard.license;

import com.codenvy.selenium.core.client.OnpremTestLicenseServiceClient;
import com.google.inject.Inject;

import org.eclipse.che.selenium.core.user.AdminTestUser;
import com.codenvy.selenium.pageobject.dashboard.CodenvyAdminDashboard;
import com.codenvy.selenium.pageobject.dashboard.DashboardSettings;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Named;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Dmytro Nochevnov
 */
public class AddLicenseTest {
    private static final String INVALID_LICENSE = "invalid-license";

    @Inject
    private DashboardSettings              dashboardSettingsPage;
    @Inject
    private CodenvyAdminDashboard          dashboardPage;
    @Inject
    private OnpremTestLicenseServiceClient licenseServiceClient;
    @Inject
    private AdminTestUser                  adminTestUser;

    @Inject
    @Named("license-for-15-seats-till-2016.05.25")
    private String expiredLicense;

    @Inject
    @Named("license-for-4-seats-till-2020.01.11")
    private String nonExpiredLicense;

    @BeforeClass
    public void setupClass() throws Exception {
        licenseServiceClient.removeLicense();
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        licenseServiceClient.removeLicense();
    }

    @Test
    public void addNonExpiredLicenseTest() {
        // given
        dashboardPage.open(adminTestUser.getAuthToken());
        gotoSettingsPage();
        assertTrue(dashboardSettingsPage.getLicenseInfo().matches("Codenvy Fair Source 3.*- 3 users, single server"),
                   "Actual content: " + dashboardSettingsPage.getLicenseInfo());
        assertEquals(dashboardSettingsPage.getLicenseExpiration(), "N/A");

        // when
        dashboardSettingsPage.applyLicense(nonExpiredLicense);

        // then
        dashboardSettingsPage.waitOnRemoveLicenseButtonAppears();
        assertEquals(dashboardSettingsPage.getLicenseInfo(), "Codenvy Enterprise - 4 users, unlimited servers");
        assertEquals(dashboardSettingsPage.getLicenseExpiration(), "01/11/2020");
    }

    @Test
    public void addExpiredLicenseTest() {
        // given
        dashboardPage.open(adminTestUser.getAuthToken());
        gotoSettingsPage();
        assertTrue(dashboardSettingsPage.getLicenseInfo().matches("Codenvy Fair Source 3.*- 3 users, single server"),
                   "Actual content: " + dashboardSettingsPage.getLicenseInfo());
        assertEquals(dashboardSettingsPage.getLicenseExpiration(), "N/A");

        // when
        dashboardSettingsPage.applyLicense(expiredLicense);

        // then
        dashboardSettingsPage.waitOnRemoveLicenseButtonAppears();
        assertTrue(dashboardSettingsPage.getLicenseInfo().matches("Codenvy Fair Source 3.*- 3 users, single server"),
                   "Actual content: " + dashboardSettingsPage.getLicenseInfo());
        assertEquals(dashboardSettingsPage.getLicenseExpiration(), "05/25/2016 -- expired");
    }

    @Test
    public void addInvalidLicenseTest() {
        // given
        dashboardPage.open(adminTestUser.getAuthToken());
        gotoSettingsPage();
        assertTrue(dashboardSettingsPage.getLicenseInfo().matches("Codenvy Fair Source 3.*- 3 users, single server"),
                   "Actual content: " + dashboardSettingsPage.getLicenseInfo());
        assertEquals(dashboardSettingsPage.getLicenseExpiration(), "N/A");

        // when
        dashboardSettingsPage.applyLicense(INVALID_LICENSE);

        // then
        dashboardPage.waitNotificationMessage("Failed\n"
                                              + "System license is not valid");

        assertTrue(dashboardSettingsPage.getLicenseInfo().matches("Codenvy Fair Source 3.*- 3 users, single server"),
                   "Actual content: " + dashboardSettingsPage.getLicenseInfo());
        assertEquals(dashboardSettingsPage.getLicenseExpiration(), "N/A");
    }

    private void gotoSettingsPage() {
        dashboardPage.waitPageOpened();
        dashboardPage.clickOnSettingsItem();
        dashboardSettingsPage.waitPageOpened();
    }

}
