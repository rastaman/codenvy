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
package com.codenvy.ldap.sync;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import com.codenvy.ldap.LdapUserIdNormalizer;
import com.codenvy.ldap.sync.LdapSynchronizer.SyncResult;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.model.user.User;
import org.eclipse.che.api.user.server.ProfileManager;
import org.eclipse.che.api.user.server.UserManager;
import org.eclipse.che.api.user.server.model.impl.ProfileImpl;
import org.eclipse.che.api.user.server.model.impl.UserImpl;
import org.eclipse.che.commons.lang.Pair;
import org.ldaptive.Connection;
import org.ldaptive.ConnectionFactory;
import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapEntry;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Tests {@link LdapSynchronizer}.
 *
 * @author Yevhenii Voevodin
 */
@Listeners(MockitoTestNGListener.class)
public class LdapSynchronizerTest {

  @Mock private ConnectionFactory connFactory;

  @Mock private Connection connection;

  @Mock private EntityManager entityManager;

  @Mock private LdapEntrySelector entrySelector;

  @Mock private LdapUserIdNormalizer idNormalizer;

  @Mock private UserManager userManager;

  @Mock private DBUserLinker userFinder;

  @Mock private ProfileManager profileManager;

  private LdapSynchronizer synchronizer;
  private Set<String> existingIds;

  @BeforeMethod
  @SuppressWarnings("unchecked") // synchronizer generic array of string pairs
  public void setUp() throws Exception {
    synchronizer =
        new LdapSynchronizer(
            connFactory,
            entrySelector,
            userManager,
            profileManager,
            idNormalizer,
            null,
            0,
            0,
            "uid",
            "cn",
            "mail",
            new Pair[] {Pair.of("firstName", "givenName")},
            true,
            true,
            userFinder);

    // mocking existing ids
    existingIds = new HashSet<>();
    when(userFinder.findIds()).thenReturn(existingIds);
    when(userFinder.extractId(any())).thenAnswer(inv -> ((User) inv.getArguments()[0]).getId());

    // mocking connection
    when(connFactory.getConnection()).thenReturn(connection);
  }

  @Test
  public void createsAllTheUsersWhenSynchronizedFirstTime() throws Exception {
    when(entrySelector.select(anyObject()))
        .thenReturn(
            asList(
                createUserEntry("user123"),
                createUserEntry("user234"),
                createUserEntry("user345")));

    final SyncResult syncResult = synchronizer.syncAll();

    assertEquals(syncResult.getProcessed(), 3);
    assertEquals(syncResult.getCreated(), 3);
    assertEquals(syncResult.getRemoved(), 0);
    assertEquals(syncResult.getUpdated(), 0);
    assertEquals(syncResult.getUpToDate(), 0);
    assertEquals(syncResult.getRemoved(), 0);
    assertEquals(syncResult.getSkipped(), 0);
    assertEquals(syncResult.getFetched(), 3);
    verify(userManager, times(3)).create(anyObject(), anyBoolean());
    verify(profileManager, times(3)).update(anyObject());
  }

  @Test
  public void removesThoseUsersWhoAreNotPresentInSelection() throws Exception {
    final Map<String, LdapEntry> users = new HashMap<>();
    users.put("user123", createUserEntry("user123"));
    users.put("user234", createUserEntry("user234"));
    when(entrySelector.select(anyObject())).thenReturn(users.values());

    existingIds.add("missed-in-selection");
    when(userFinder.findUser("missed-in-selection"))
        .thenReturn(new UserImpl("missed-in-selection", "email", "name"));

    final SyncResult syncResult = synchronizer.syncAll();

    assertEquals(syncResult.getProcessed(), 2);
    assertEquals(syncResult.getCreated(), 2);
    assertEquals(syncResult.getRemoved(), 1);
    assertEquals(syncResult.getUpdated(), 0);
    assertEquals(syncResult.getUpToDate(), 0);
    assertEquals(syncResult.getFailed(), 0);
    assertEquals(syncResult.getSkipped(), 0);
    assertEquals(syncResult.getFetched(), 2);
    verify(userManager, times(2)).create(anyObject(), anyBoolean());
    verify(profileManager, times(2)).update(anyObject());
    verify(userManager).remove("missed-in-selection");
  }

  @Test
  public void skipsUsersWhoAreAlreadyPresentInDatabaseAndNotModified() throws Exception {
    final Map<String, LdapEntry> users = new HashMap<>();
    users.put("user123", createUserEntry("user123"));
    users.put("user234", createUserEntry("user234"));
    users.put("user345", createUserEntry("user345"));
    when(entrySelector.select(anyObject())).thenReturn(users.values());
    when(profileManager.getById(any()))
        .thenAnswer(
            inv -> {
              final String id = inv.getArguments()[0].toString();
              return new ProfileImpl(id, ImmutableMap.of("firstName", "firstName-" + id));
            });

    final UserMapper mapper = new UserMapper("uid", "cn", "mail");
    when(userFinder.findUser(any()))
        .thenAnswer(
            inv -> {
              final String id = inv.getArguments()[0].toString();
              return mapper.apply(users.get(id));
            });

    existingIds.add("user123");
    existingIds.add("user345");

    final SyncResult syncResult = synchronizer.syncAll();

    assertEquals(syncResult.getProcessed(), 3);
    assertEquals(syncResult.getCreated(), 1);
    assertEquals(syncResult.getRemoved(), 0);
    assertEquals(syncResult.getUpToDate(), 2);
    assertEquals(syncResult.getUpdated(), 0);
    assertEquals(syncResult.getFailed(), 0);
    assertEquals(syncResult.getSkipped(), 0);
    assertEquals(syncResult.getFetched(), 3);
    verify(userManager).create(anyObject(), anyBoolean());
    verify(profileManager).update(anyObject());
    verify(userManager, never()).update(anyObject());
  }

  @Test
  public void updatesUsersWhoChangedInLdap() throws Exception {
    final Map<String, LdapEntry> users = new HashMap<>();
    users.put("user123", createUserEntry("user123"));
    users.put("user234", createUserEntry("user234"));
    when(entrySelector.select(anyObject())).thenReturn(users.values());

    when(profileManager.getById(any()))
        .thenAnswer(
            inv -> {
              final String id = inv.getArguments()[0].toString();
              return new ProfileImpl(id, ImmutableMap.of("firstName", "new-firstName-" + id));
            });

    final UserMapper mapper = new UserMapper("uid", "cn", "mail");
    when(userFinder.findUser(any()))
        .thenAnswer(
            inv -> {
              final String id = inv.getArguments()[0].toString();
              return mapper.apply(users.get(id));
            });

    existingIds.add("user123");
    existingIds.add("user234");

    final SyncResult syncResult = synchronizer.syncAll();

    assertEquals(syncResult.getProcessed(), 2);
    assertEquals(syncResult.getCreated(), 0);
    assertEquals(syncResult.getRemoved(), 0);
    assertEquals(syncResult.getUpToDate(), 0);
    assertEquals(syncResult.getUpdated(), 2);
    assertEquals(syncResult.getFailed(), 0);
    assertEquals(syncResult.getSkipped(), 0);
    verify(userManager, never()).update(anyObject());
    verify(profileManager, times(2)).update(anyObject());
  }

  @Test
  public void skipsUsersIfTheyAlreadyExistAndUpdateIfExistsAttributeIsSetToFalse()
      throws Exception {
    synchronizer =
        new LdapSynchronizer(
            connFactory,
            entrySelector,
            userManager,
            profileManager,
            idNormalizer,
            null,
            0,
            0,
            "uid",
            "cn",
            "mail",
            null,
            false, // <- don't update
            true,
            userFinder);
    when(entrySelector.select(anyObject()))
        .thenReturn(asList(createUserEntry("user123"), createUserEntry("user234")));
    existingIds.add("user123");
    existingIds.add("user234");

    final SyncResult syncResult = synchronizer.syncAll();

    assertEquals(syncResult.getProcessed(), 2);
    assertEquals(syncResult.getCreated(), 0);
    assertEquals(syncResult.getUpdated(), 0);
    assertEquals(syncResult.getUpToDate(), 0);
    assertEquals(syncResult.getRemoved(), 0);
    assertEquals(syncResult.getFailed(), 0);
    assertEquals(syncResult.getSkipped(), 2);
  }

  @Test
  public void updatesUserIfLinkingAttributeIsDifferentFromId() throws Exception {
    UserImpl existingUser = new UserImpl("user123", "user123@codenvy.com", "user_123");

    DBUserLinker emailFinder = mock(DBUserLinker.class);
    when(emailFinder.extractId(any(User.class)))
        .thenAnswer(inv -> ((User) inv.getArguments()[0]).getEmail());
    when(emailFinder.findIds())
        .thenReturn(new HashSet<>(Collections.singleton(existingUser.getEmail())));
    when(emailFinder.findUser(anyString()))
        .thenAnswer(
            inv -> {
              if (!existingUser.getEmail().equals(inv.getArguments()[0])) {
                throw new NotFoundException("no user " + inv.getArguments()[0]);
              }
              return existingUser;
            });
    when(profileManager.getById(existingUser.getId())).thenReturn(mock(ProfileImpl.class));

    synchronizer =
        new LdapSynchronizer(
            connFactory,
            entrySelector,
            userManager,
            profileManager,
            idNormalizer,
            null,
            0,
            0,
            "uid",
            "cn",
            "mail",
            null,
            true,
            true,
            emailFinder);

    ArrayList<LdapEntry> entries = new ArrayList<>(2);
    entries.add(
        createUserEntry(
            "different-id", // <- the id is different from user123
            "different-name", // <- the name is different from user_123
            existingUser.getEmail(),
            "first-name"));
    entries.add(createUserEntry("new-user"));
    when(entrySelector.select(anyObject())).thenReturn(entries);

    final SyncResult syncResult = synchronizer.syncAll();

    assertEquals(syncResult.getProcessed(), 2);
    assertEquals(syncResult.getCreated(), 1);
    assertEquals(syncResult.getUpdated(), 1);
    assertEquals(syncResult.getUpToDate(), 0);
    assertEquals(syncResult.getRemoved(), 0);
    assertEquals(syncResult.getFailed(), 0);
    assertEquals(syncResult.getSkipped(), 0);
    assertEquals(syncResult.getFetched(), 2);

    ArgumentCaptor<UserImpl> userCaptor = ArgumentCaptor.forClass(UserImpl.class);
    verify(userManager).update(userCaptor.capture());
    UserImpl user = userCaptor.getValue();
    assertEquals(user.getId(), existingUser.getId(), "id must be taken from existing user");
    assertEquals(user.getName(), "different-name", "name must be updated");
    assertEquals(user.getEmail(), existingUser.getEmail(), "linking attribute must be the same");
  }

  private static LdapEntry createUserEntry(String id) {
    return createUserEntry(id, "name-" + id, "email" + id, "firstName-" + id);
  }

  private static LdapEntry createUserEntry(String id, String name, String email, String firstName) {
    final LdapEntry newEntry = new LdapEntry("uid=" + id + ",dc=codenvy,dc=com");
    newEntry.addAttribute(new LdapAttribute("uid", id));
    newEntry.addAttribute(new LdapAttribute("cn", name));
    newEntry.addAttribute(new LdapAttribute("mail", email));
    newEntry.addAttribute(new LdapAttribute("givenName", firstName));
    return newEntry;
  }
}
