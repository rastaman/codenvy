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
import com.codenvy.selenium.pageobject.dashboard.CodenvyAdminDashboard;
import com.codenvy.selenium.pageobject.dashboard.DashboardSettings;
import com.google.inject.Inject;

import org.eclipse.che.selenium.core.client.TestAuthServiceClient;
import org.eclipse.che.selenium.core.client.TestUserServiceClient;
import org.eclipse.che.selenium.core.client.TestWorkspaceServiceClient;
import org.eclipse.che.selenium.core.user.AdminTestUser;
import org.eclipse.che.selenium.core.user.TestUser;
import org.eclipse.che.selenium.core.user.TestUserImpl;
import org.eclipse.che.selenium.core.user.TestUserNamespaceResolver;
import org.eclipse.che.selenium.pageobject.dashboard.DashboardPopup;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.inject.Named;

import static java.util.Optional.ofNullable;
import static org.eclipse.che.selenium.core.utils.WaitUtils.sleepQuietly;
import static org.testng.Assert.assertEquals;

/**
 * @author Dmytro Nochevnov
 */
public class UserLicenseNumberLimitReachedTest {

    // test users number = 2 = 4 - (<admin> + <default_user>)
    private TestUser user1;
    private TestUser user2;

    @Inject
    private AdminTestUser adminTestUser;
    @Inject
    @Named("license-for-4-seats-till-2020.01.11")
    private String        nonExpiredLicense;

    @Inject
    private DashboardSettings              dashboardPageSettingsPage;
    @Inject
    private CodenvyAdminDashboard          dashboardPage;
    @Inject
    private DashboardPopup                 dashboardPagePopup;
    @Inject
    private OnpremTestLicenseServiceClient licenseServiceClient;
    @Inject
    private TestUserServiceClient          testUserServiceClient;
    @Inject
    private TestWorkspaceServiceClient     workspaceServiceClient;
    @Inject
    private TestAuthServiceClient          authServiceClient;
    @Inject
    private TestUserNamespaceResolver      testUserNamespaceResolver;

    @BeforeClass
    public void start() throws Exception {
        licenseServiceClient.removeLicense();
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
        licenseServiceClient.removeLicense();
        ofNullable(user1).ifPresent(TestUser::delete);
        ofNullable(user2).ifPresent(TestUser::delete);
    }

    @Test
    public void shouldDisplayPopupMessage() throws Exception {
        // when
        licenseServiceClient.addLicense(nonExpiredLicense);
        registerUsers();

        dashboardPage.open(adminTestUser.getAuthToken());

        // then
        dashboardPagePopup.waitPopupDisplays();
        assertEquals(dashboardPagePopup.getPopupTitle(), "User License Has Reached Its Limit");
        assertEquals(dashboardPagePopup.getPopupMessage(), "Your user license has reached its limit. You cannot add more users.");
        dashboardPagePopup.closePopup();

        /* test dashboardPage doesn't display popup again */
        // given
        dashboardPage.driver().navigate().refresh();

        // when
        gotoSettingsPage();
        sleepQuietly(1);

        // then
        dashboardPagePopup.waitPopupClosed();
    }

    private void gotoSettingsPage() {
        dashboardPage.waitPageOpened();
        dashboardPage.clickOnSettingsItem();
        dashboardPageSettingsPage.waitPageOpened();
    }

    private void registerUsers() throws Exception {
        user1 = new TestUserImpl(testUserServiceClient, workspaceServiceClient, authServiceClient);
        user2 = new TestUserImpl(testUserServiceClient, workspaceServiceClient, authServiceClient);
    }

}
