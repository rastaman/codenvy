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
package com.codenvy.selenium.site;


import org.eclipse.che.selenium.pageobject.dashboard.Dashboard;
import com.codenvy.selenium.pageobject.site.AcceptFairSourceLicensePage;
import com.codenvy.selenium.pageobject.site.LoginAndCreateOnpremAccountPage;
import com.google.inject.Inject;

import org.eclipse.che.commons.json.JsonParseException;
import org.eclipse.che.selenium.core.provider.TestIdeUrlProvider;
import org.eclipse.che.selenium.core.user.AdminTestUser;
import org.eclipse.che.selenium.core.user.DefaultTestUser;
import org.eclipse.che.selenium.pageobject.Ide;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author Dmytro Nochevnov
 */
public class AcceptFairSourceLicenseTest {

    @Inject
    private Ide             ide;
    @Inject
    private DefaultTestUser user;

    @Inject
    private LoginAndCreateOnpremAccountPage loginPage;
    @Inject
    private AcceptFairSourceLicensePage     acceptFairSourceLicensePage;
    @Inject
    private Dashboard                       dashboard;
    @Inject
    private TestIdeUrlProvider              ideUrlProvider;
    @Inject
    private AdminTestUser                   adminTestUser;

    @Test
    public void acceptFairSourceLicenseTest() throws IOException, JsonParseException {
        ide.driver().get(ideUrlProvider.get() + "site/login");
        loginPage.waitLoginPage();
        loginPage.loginToDashboard(adminTestUser.getName(), adminTestUser.getPassword());
        loginPage.waitLoginPageClosed();

        acceptFairSourceLicensePage.waitPage();
        acceptFairSourceLicensePage.clickOnAcceptButtonAndGo();
        acceptFairSourceLicensePage.waitPageClosed();

        dashboard.waitDeveloperFaceImg();
    }

}
