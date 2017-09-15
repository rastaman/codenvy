/*
 * Copyright (c) [2012] - [2017] Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package com.codenvy.api.permission.server;

import com.codenvy.auth.sso.client.SsoClientPrincipal;
import com.codenvy.auth.sso.client.TokenHandler;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.eclipse.che.commons.subject.Subject;
import org.eclipse.che.multiuser.api.permission.server.AuthorizedSubject;
import org.eclipse.che.multiuser.api.permission.server.PermissionChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sets up implementation of {@link Subject} that can check permissions by {@link PermissionChecker}
 * and delegates calls to injected {@link Named Named("delegated.handler")} {@link TokenHandler}
 *
 * @author Sergii Leschenko
 */
public class PermissionTokenHandler implements TokenHandler {
  private static final Logger LOG = LoggerFactory.getLogger(PermissionTokenHandler.class);

  private final PermissionChecker permissionChecker;
  private final TokenHandler delegate;

  @Inject
  public PermissionTokenHandler(
      PermissionChecker permissionChecker, @Named("delegated.handler") TokenHandler delegate) {
    this.permissionChecker = permissionChecker;
    this.delegate = delegate;
  }

  @Override
  public void handleValidToken(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      HttpSession session,
      SsoClientPrincipal principal)
      throws IOException, ServletException {
    delegate.handleValidToken(
        request,
        response,
        chain,
        session,
        new SsoClientPrincipal(
            principal.getToken(),
            principal.getClientUrl(),
            new AuthorizedSubject(principal.getUser(), permissionChecker)));
  }

  @Override
  public void handleBadToken(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain, String token)
      throws IOException, ServletException {
    delegate.handleBadToken(request, response, chain, token);
  }

  @Override
  public void handleMissingToken(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    delegate.handleMissingToken(request, response, chain);
  }
}
