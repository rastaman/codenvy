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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Named;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

/**
 * @author Dmytro Nochevnov
 */
public class RemoveLicenseTest {

    @Inject
    private DashboardSettings              dashboardSettingsPage;
    @Inject
    private CodenvyAdminDashboard          dashboardPage;
    @Inject
    private OnpremTestLicenseServiceClient licenseServiceClient;
    @Inject
    private AdminTestUser                  adminTestUser;

    @Inject
    @Named("license-for-4-seats-till-2020.01.11")
    private String nonExpiredLicense;

    @BeforeClass
    public void setupClass() throws Exception {
        licenseServiceClient.removeLicense();
    }

    @BeforeMethod
    public void setupMethod() throws Exception {
        licenseServiceClient.addLicense(nonExpiredLicense);
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        licenseServiceClient.removeLicense();
    }

    @Test
    public void removeLicenseTest() {
        // given
        dashboardPage.open(adminTestUser.getAuthToken());
        gotoSettingsPage();
        assertEquals(dashboardSettingsPage.getLicenseInfo(), "Codenvy Enterprise - 4 users, unlimited servers");
        assertEquals(dashboardSettingsPage.getLicenseExpiration(), "01/11/2020");

        // when
        dashboardSettingsPage.waitPageOpened();
        dashboardSettingsPage.removeLicense();

        // then
        dashboardSettingsPage.waitOnApplyLicenseButtonAppears();
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
