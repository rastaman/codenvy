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
package com.codenvy.selenium.core.provider;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.selenium.core.provider.TestDashboardUrlProvider;

import javax.inject.Named;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Anatolii Bazko
 */
@Singleton
public class OnpremTestDashboardUrlProvider implements TestDashboardUrlProvider {
    @Inject
    @Named("sys.protocol")
    private String protocol;
    @Inject
    @Named("sys.host")
    private String host;

    @Override
    public URL get() {
        try {
            return new URL(protocol, host, -1, "/dashboard/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
