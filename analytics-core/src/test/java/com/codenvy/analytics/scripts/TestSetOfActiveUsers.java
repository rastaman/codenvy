/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 * NOTICE: All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */

package com.codenvy.analytics.scripts;


import com.codenvy.analytics.BaseTest;
import com.codenvy.analytics.metrics.Utils;
import com.codenvy.analytics.metrics.value.MapStringSetValueData;
import com.codenvy.analytics.metrics.value.SetStringValueData;
import com.codenvy.analytics.scripts.util.Event;
import com.codenvy.analytics.scripts.util.LogGenerator;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


/** @author <a href="mailto:abazko@codenvy.com">Anatoliy Bazko</a> */
public class TestSetOfActiveUsers extends BaseTest {

    @Test
    public void testSetOfActiveUsers() throws Exception {
        List<Event> events = new ArrayList<>();
        events.add(Event.Builder.createProjectCreatedEvent("user1@gmail.com", "ws1", "session", "project1", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("user2@gmail.com", "ws2", "session", "project1", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("user2@gmail.com", "ws2", "session", "project2", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("", "", "session", "project1", "type").withDate("2010-10-01").build());
        File log = LogGenerator.generateLog(events);

        Map<String, String> context = new HashMap<>();
        Utils.putFromDate(context, "20101001");
        Utils.putToDate(context, "20101001");

        SetStringValueData valueData = (SetStringValueData) executeAndReturnResult(ScriptType.SET_ACTIVE_USERS, log, context);
        assertEquals(valueData.size(), 2);
        assertTrue(valueData.getAll().contains("user1@gmail.com"));
        assertTrue(valueData.getAll().contains("user2@gmail.com"));
    }

    @Test
    public void testSetOfActiveUsersByWs() throws Exception {
        List<Event> events = new ArrayList<>();
        events.add(Event.Builder.createProjectCreatedEvent("user1@gmail.com", "ws1", "session", "project1", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("user2@gmail.com", "ws2", "session", "project1", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("user2@gmail.com", "ws2", "session", "project2", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("user3@gmail.com", "ws2", "session", "project3", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("", "", "session", "project1", "type").withDate("2010-10-01").build());
        File log = LogGenerator.generateLog(events);

        Map<String, String> context = new HashMap<>();
        Utils.putFromDate(context, "20101001");
        Utils.putToDate(context, "20101001");

        MapStringSetValueData valueData = (MapStringSetValueData) executeAndReturnResult(ScriptType.SET_ACTIVE_USERS_BY_WS, log, context);

        assertEquals(valueData.size(), 2);
        assertTrue(valueData.getAll().containsKey("ws1"));
        assertTrue(valueData.getAll().containsKey("ws2"));

        SetStringValueData setValueData = valueData.getAll().get("ws1");
        assertEquals(setValueData.size(), 1);
        assertTrue(setValueData.getAll().contains("user1@gmail.com"));

        setValueData = valueData.getAll().get("ws2");
        assertEquals(setValueData.size(), 2);
        assertTrue(setValueData.getAll().contains("user2@gmail.com"));
        assertTrue(setValueData.getAll().contains("user3@gmail.com"));
    }

    @Test
    public void testSetOfActiveUsersByDomains() throws Exception {
        List<Event> events = new ArrayList<>();
        events.add(Event.Builder.createProjectCreatedEvent("user1@gmail.com", "ws1", "session", "project1", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("user2@gmail.com", "ws2", "session", "project1", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("user2@gmail.com", "ws2", "session", "project2", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("user3@gmail.com", "ws2", "session", "project3", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("", "", "session", "project1", "type").withDate("2010-10-01").build());
        File log = LogGenerator.generateLog(events);

        Map<String, String> context = new HashMap<>();
        Utils.putFromDate(context, "20101001");
        Utils.putToDate(context, "20101001");

        MapStringSetValueData valueData = (MapStringSetValueData) executeAndReturnResult(ScriptType.SET_ACTIVE_USERS_BY_DOMAINS, log, context);

        assertEquals(valueData.size(), 1);
        assertTrue(valueData.getAll().containsKey("gmail.com"));

        SetStringValueData setValueData = valueData.getAll().get("gmail.com");
        assertEquals(setValueData.size(), 3);
        assertTrue(setValueData.getAll().contains("user1@gmail.com"));
        assertTrue(setValueData.getAll().contains("user2@gmail.com"));
        assertTrue(setValueData.getAll().contains("user3@gmail.com"));
    }

    @Test
    public void testSetOfActiveUsersByUsers() throws Exception {
        List<Event> events = new ArrayList<>();
        events.add(Event.Builder.createProjectCreatedEvent("user1@gmail.com", "ws1", "session", "project1", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("user2@gmail.com", "ws2", "session", "project1", "type").withDate("2010-10-01").build());
        events.add(Event.Builder.createProjectCreatedEvent("", "", "session", "project1", "type").withDate("2010-10-01").build());
        File log = LogGenerator.generateLog(events);

        Map<String, String> context = new HashMap<>();
        Utils.putFromDate(context, "20101001");
        Utils.putToDate(context, "20101001");

        MapStringSetValueData valueData = (MapStringSetValueData) executeAndReturnResult(ScriptType.SET_ACTIVE_USERS_BY_USERS, log, context);

        assertEquals(valueData.size(), 2);
        assertTrue(valueData.getAll().containsKey("user1@gmail.com"));
        assertTrue(valueData.getAll().containsKey("user2@gmail.com"));

        SetStringValueData setValueData = valueData.getAll().get("user1@gmail.com");
        assertEquals(setValueData.size(), 1);
        assertTrue(setValueData.getAll().contains("user1@gmail.com"));

        setValueData = valueData.getAll().get("user2@gmail.com");
        assertEquals(setValueData.size(), 1);
        assertTrue(setValueData.getAll().contains("user2@gmail.com"));
    }
}
