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

IMPORT 'macros.pig';

l = loadResources('$LOG', '$FROM_DATE', '$TO_DATE', '$USER', '$WS');
f = filterByEvent(l, '$EVENT');

a1 = FOREACH f GENERATE event;
a = countAll(a1);

result = FOREACH a GENERATE (long)'$TO_DATE', TOTUPLE('value', countAll);
STORE result INTO '$STORAGE_URL.$METRIC' USING MongoStorage();

r1 = FOREACH f GENERATE ws, user, LOWER(REGEX_EXTRACT(user, '.*@(.*)', 1)) AS domain;
r = FOREACH r1 GENERATE (long)'$TO_DATE', TOTUPLE('ws', ws), TOTUPLE('user', user), TOTUPLE('domain', domain);
STORE r INTO '$STORAGE_URL.$METRIC-raw' USING MongoStorage();