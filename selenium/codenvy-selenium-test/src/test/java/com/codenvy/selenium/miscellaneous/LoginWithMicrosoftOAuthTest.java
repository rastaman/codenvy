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
package com.codenvy.selenium.miscellaneous;

import com.codenvy.selenium.pageobject.MicrosoftOauthPage;
import com.codenvy.selenium.pageobject.site.LoginAndCreateOnpremAccountPage;
import com.google.inject.Inject;

import org.eclipse.che.selenium.pageobject.Ide;
import org.eclipse.che.selenium.pageobject.dashboard.Dashboard;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import javax.inject.Named;

import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.ELEMENT_TIMEOUT_SEC;

/**
 * @author Musienko Maxim
 */
public class LoginWithMicrosoftOAuthTest {

    @Inject
    private Ide                             ide;
    @Inject
    private LoginAndCreateOnpremAccountPage loginPage;
    @Inject
    private MicrosoftOauthPage              microsoftOauthPage;
    @Inject
    private Dashboard                       dashboard;

    @Inject
    @Named("vsts.user")
    private String vstsAccountEmail;
    @Inject
    @Named("vsts.password")
    private String vstsAccountPassword;

    @Test
    public void shouldLoginWithMicrosoftOAuth() {
        loginPage.open();

        loginPage.clickOnMicrosoftOauthBtn();
        microsoftOauthPage.loginToMicrosoftAccount(vstsAccountEmail, vstsAccountPassword);

        new WebDriverWait(ide.driver(), ELEMENT_TIMEOUT_SEC)
                .until(ExpectedConditions.urlContains("https://app.vssps.visualstudio.com/oauth2/authorize?client_id"));

        microsoftOauthPage.clickOnAcceptBtn();
        dashboard.waitDashboardToolbarTitle();
    }
}
