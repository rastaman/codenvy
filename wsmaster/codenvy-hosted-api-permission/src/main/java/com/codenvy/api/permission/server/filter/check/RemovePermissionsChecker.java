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
package com.codenvy.api.permission.server.filter.check;

import org.eclipse.che.api.core.ForbiddenException;

/**
 * Defines contract for domain specific checks, before remove permissions.
 *
 * @author Anton Korneta
 */
public interface RemovePermissionsChecker {

    /**
     * Checks if the current user is allowed to remove permissions.
     *
     * @param user
     *         user identifier
     * @param domainId
     *         permissions domain
     * @param instance
     *         instance associated with the permissions to be removed
     * @throws ForbiddenException
     *         when it is not allowed to remove permissions
     */
    void check(String user, String domainId, String instance) throws ForbiddenException;

}
