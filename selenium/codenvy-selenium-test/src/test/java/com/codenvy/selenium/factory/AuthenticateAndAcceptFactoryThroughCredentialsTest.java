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
package com.codenvy.selenium.factory;

import com.google.inject.Inject;

import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.factory.FactoryTemplate;
import org.eclipse.che.selenium.core.factory.TestFactory;
import org.eclipse.che.selenium.core.factory.TestFactoryInitializer;
import org.eclipse.che.selenium.core.user.DefaultTestUser;
import org.eclipse.che.selenium.pageobject.Ide;
import org.eclipse.che.selenium.pageobject.NotificationsPopupPanel;
import org.eclipse.che.selenium.pageobject.Profile;
import org.eclipse.che.selenium.pageobject.ProjectExplorer;
import com.codenvy.selenium.pageobject.site.LoginAndCreateOnpremAccountPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Mihail Kuznyetsov
 */
public class AuthenticateAndAcceptFactoryThroughCredentialsTest {
    @Inject
    private Ide                             ide;
    @Inject
    private DefaultTestUser                 defaultUser;
    @Inject
    private ProjectExplorer                 projectExplorer;
    @Inject
    private LoginAndCreateOnpremAccountPage loginPage;
    @Inject
    private Profile                         profile;
    @Inject
    private NotificationsPopupPanel         notificationsPopupPanel;
    @Inject
    private TestFactoryInitializer          testFactoryInitializer;
    @Inject
    private SeleniumWebDriver               seleniumWebDriver;

    private TestFactory testFactory;

    @BeforeClass
    public void setUp() throws Exception {
        testFactory = testFactoryInitializer.fromTemplate(FactoryTemplate.MINIMAL).build();
    }

    @AfterClass
    public void tearDown() throws Exception {
        testFactory.delete();
    }


    @Test
    public void loginThroughEnteringCredentialsAndAcceptFactory() throws Exception {
        testFactory.open(ide.driver());
        loginPage.waitMainElementsOnLoginPage();

        loginPage.loginToDashboard(defaultUser.getName(), defaultUser.getPassword());

        profile.handleProfileOnboardingWithTestData();
        seleniumWebDriver.switchFromDashboardIframeToIde();

        projectExplorer.waitProjectExplorer();
        notificationsPopupPanel.waitExpectedMessageOnProgressPanelAndClosed("Project Spring imported");
    }
}
