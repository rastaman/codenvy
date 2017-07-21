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
package com.codenvy.selenium.pageobject.site;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.pageobject.Loader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;

import static org.eclipse.che.selenium.core.constant.TestTimeoutsConstants.LOAD_PAGE_TIMEOUT_SEC;

/**
 * @author Dmytro Nochevnov
 */
@Singleton
public class AcceptFairSourceLicensePage {
    private final SeleniumWebDriver seleniumWebDriver;
    private final Loader            loader;

    @Inject
    public AcceptFairSourceLicensePage(SeleniumWebDriver seleniumWebDriver, Loader loader) {
        this.loader = loader;
        this.seleniumWebDriver = seleniumWebDriver;
        PageFactory.initElements(seleniumWebDriver, this);
    }

    private interface Locators {
        String ACCEPT_XPATH = "//input[@value='Accept']";
    }

    @FindBy(xpath = Locators.ACCEPT_XPATH)
    private WebElement acceptButton;

    /**
     * wait main elements on page
     */
    public void waitPage() {
        waitMainElementsOnPage();
    }

    /**
     * wait appearance main buttons and fields on page
     */
    public void waitMainElementsOnPage() {
        new WebDriverWait(seleniumWebDriver, LOAD_PAGE_TIMEOUT_SEC)
                .until(ExpectedConditions.visibilityOfAllElements(Arrays.asList(acceptButton)));
    }

    /**
     * wait main elements on page
     */
    public void waitPageClosed() {
        new WebDriverWait(seleniumWebDriver, LOAD_PAGE_TIMEOUT_SEC)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.ACCEPT_XPATH)));
    }

    /**
     * click on Accept button and wait closing of page
     */
    public void clickOnAcceptButtonAndGo() {
        acceptButton.click();
        loader.waitOnClosed();
    }

}
