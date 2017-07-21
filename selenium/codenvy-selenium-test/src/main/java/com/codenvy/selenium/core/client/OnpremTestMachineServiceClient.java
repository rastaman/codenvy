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

import com.codenvy.machine.authentication.shared.dto.MachineTokenDto;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.eclipse.che.api.core.rest.HttpJsonRequestFactory;
import org.eclipse.che.api.core.rest.HttpJsonResponse;
import org.eclipse.che.selenium.core.client.TestMachineServiceClient;
import org.eclipse.che.selenium.core.provider.TestApiEndpointUrlProvider;

/**
 * @author Musienko Maxim
 */
@Singleton
public class OnpremTestMachineServiceClient implements TestMachineServiceClient {
    private final String                 apiEndpoint;
    private final HttpJsonRequestFactory requestFactory;

    @Inject
    public OnpremTestMachineServiceClient(TestApiEndpointUrlProvider apiEndpointProvider,
                                          HttpJsonRequestFactory requestFactory) {
        this.apiEndpoint = apiEndpointProvider.get().toString();
        this.requestFactory = requestFactory;
    }

    /**
     * Returns machine token for current workspace
     *
     * @param workspaceId
     *         the workspace id
     * @param authToken
     *         the authorization token
     * @return the machine token for current workspace
     */
    @Override
    public String getMachineApiToken(String workspaceId, String authToken) throws Exception {
        HttpJsonResponse response = requestFactory.fromUrl(apiEndpoint + "machine/token/" + workspaceId)
                                                  .setAuthorizationHeader(authToken)
                                                  .useGetMethod()
                                                  .request();
        return response.asDto(MachineTokenDto.class).getMachineToken();
    }
}

