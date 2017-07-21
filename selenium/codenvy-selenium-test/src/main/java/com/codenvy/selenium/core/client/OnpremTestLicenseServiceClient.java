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
package com.codenvy.selenium.core.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.rest.HttpJsonRequestFactory;
import org.eclipse.che.selenium.core.provider.TestApiEndpointUrlProvider;
import org.eclipse.che.selenium.core.user.AdminTestUser;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.util.Optional.ofNullable;

/**
 * @author Dmytro Nochevnov
 */
@Singleton
public class OnpremTestLicenseServiceClient {
    private final String                 apiEndpoint;
    private final AdminTestUser          adminTestUser;
    private final HttpJsonRequestFactory requestFactory;

    @Inject
    public OnpremTestLicenseServiceClient(TestApiEndpointUrlProvider apiEndpointProvider,
                                          AdminTestUser adminTestUser,
                                          HttpJsonRequestFactory requestFactory) {
        this.apiEndpoint = apiEndpointProvider.get().toString();
        this.adminTestUser = adminTestUser;
        this.requestFactory = requestFactory;
    }

    public void removeLicense() throws Exception {
        try {
            requestFactory.fromUrl(apiEndpoint + "license/system")
                          .setAuthorizationHeader(adminTestUser.getAuthToken())
                          .useDeleteMethod()
                          .request();
        } catch (NotFoundException e) {
            // ignore, because it's correct to have 404 NotFound error if license hasn't been registered on server yet.
        }
    }

    public void addLicense(String license) throws Exception {
        HttpURLConnection httpConnection = null;

        try {
            String apiUrl = apiEndpoint + "license/system";
            URL url = new URL(apiUrl);
            httpConnection = (HttpURLConnection)url.openConnection();
            httpConnection.setRequestMethod("POST");
            httpConnection.addRequestProperty("Content-Type", "text/plain");
            httpConnection.addRequestProperty("Authorization", adminTestUser.getAuthToken());

            httpConnection.setDoOutput(true);
            try (OutputStream output = httpConnection.getOutputStream()) {
                output.write(license.getBytes("UTF-8"));
            }

            if (httpConnection.getResponseCode() != 201) {
                throw new RuntimeException("Can't add license: " + httpConnection.getResponseCode());
            }

        } finally {
            ofNullable(httpConnection).ifPresent(HttpURLConnection::disconnect);
        }
    }
}
