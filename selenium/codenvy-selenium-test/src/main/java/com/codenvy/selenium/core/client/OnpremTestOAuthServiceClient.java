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

import org.eclipse.che.api.core.rest.HttpJsonRequestFactory;
import org.eclipse.che.selenium.core.provider.TestApiEndpointUrlProvider;

/**
 * @author Andrey Chizhikov
 */
@Singleton
public class OnpremTestOAuthServiceClient {
    private final String                 apiEndpoint;
    private final HttpJsonRequestFactory requestFactory;

    @Inject
    public OnpremTestOAuthServiceClient(TestApiEndpointUrlProvider apiEndpointProvider,
                                        HttpJsonRequestFactory requestFactory) {
        this.apiEndpoint = apiEndpointProvider.get().toString();
        this.requestFactory = requestFactory;
    }

    /**
     * Invalidate OAuth provider
     *
     * @param provider
     *         the name of OAuth provider
     */
    public synchronized void invalidateOAuthProvider(String provider, String token) throws Exception {
        requestFactory.fromUrl(apiEndpoint + "oauth/token?oauth_provider=" + provider)
                      .useDeleteMethod()
                      .setAuthorizationHeader(token)
                      .request();
    }

    /**
     * Check if OAuth provider is valid.
     *
     * @param provider
     *         the name of OAuth provider
     * @return {@code true} if OAuth provider is founded
     */
    public boolean isOAuthProviderValid(String provider, String token) {
        try {
            requestFactory.fromUrl(apiEndpoint + "oauth/token?oauth_provider=" + provider)
                          .useGetMethod()
                          .setAuthorizationHeader(token)
                          .request();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
