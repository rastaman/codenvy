/*******************************************************************************
 * Copyright (c) [2012] - [2017] Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 *******************************************************************************/
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
