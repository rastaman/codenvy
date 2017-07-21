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
package com.codenvy.selenium.pageobject.dashboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.provider.TestDashboardUrlProvider;
import org.eclipse.che.selenium.core.provider.TestIdeUrlProvider;
import org.eclipse.che.selenium.core.user.DefaultTestUser;
import org.eclipse.che.selenium.pageobject.dashboard.Dashboard;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static java.util.Collections.singletonList;
import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.LOAD_PAGE_TIMEOUT_SEC;
import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.REDRAW_UI_ELEMENTS_TIMEOUT_SEC;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfAllElements;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

/**
 * Page object extends {@link Dashboard} page object to cover elements which are specific for Codenvy Admin dashboard content.
 *
 * @author Dmytro Nochevnov
 */
@Singleton
public class CodenvyAdminDashboard extends Dashboard {

    @Inject
    public CodenvyAdminDashboard(SeleniumWebDriver seleniumWebDriver,
                                 DefaultTestUser defaultUser,
                                 TestIdeUrlProvider testIdeUrlProvider,
                                 TestDashboardUrlProvider testDashboardUrlProvider) {
        super(seleniumWebDriver, defaultUser, testIdeUrlProvider, testDashboardUrlProvider);
        PageFactory.initElements(seleniumWebDriver, this);
    }

    private interface Locators {
        String SETTINGS_ITEM_XPATH = "//a[@href='#/onprem/administration']";
    }

    @FindBy(xpath = Locators.SETTINGS_ITEM_XPATH)
    private WebElement settingsItem;

    /**
     * Wait appearance of main elements on page
     */
    public void waitPageOpened() {
        new WebDriverWait(seleniumWebDriver, LOAD_PAGE_TIMEOUT_SEC)
                .until(visibilityOf(settingsItem));
    }

    /**
     * Wait disappearance of main elements on page
     */
    public void waitPageClosed() {
        new WebDriverWait(seleniumWebDriver, LOAD_PAGE_TIMEOUT_SEC)
                .until(invisibilityOfAllElements(singletonList(settingsItem)));
    }

    public void clickOnSettingsItem() {
        new WebDriverWait(seleniumWebDriver, REDRAW_UI_ELEMENTS_TIMEOUT_SEC)
                .until(visibilityOf(settingsItem))
                .click();
    }

}
