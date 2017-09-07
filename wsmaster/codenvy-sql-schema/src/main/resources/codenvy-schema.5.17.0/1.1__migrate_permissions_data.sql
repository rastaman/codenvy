--
--  [2012] - [2017] Codenvy, S.A.
--  All Rights Reserved.
--
-- NOTICE:  All information contained herein is, and remains
-- the property of Codenvy S.A. and its suppliers,
-- if any.  The intellectual and technical concepts contained
-- herein are proprietary to Codenvy S.A.
-- and its suppliers and may be covered by U.S. and Foreign Patents,
-- patents in process, and are protected by trade secret or copyright law.
-- Dissemination of this information or reproduction of this material
-- is strictly forbidden unless prior written permission is obtained
-- from Codenvy S.A..
--

-- System permissions migration --------------------------------------------------
INSERT INTO che_systempermissions(id, userid)
SELECT                            id, userid
FROM systempermissions;
----------------------------------------------------------------------------------

-- System permissions actions migration ------------------------------------------
INSERT INTO che_systempermissions_actions (systempermissions_id, actions)
SELECT                                     systempermissions_id, actions
FROM systempermissions_actions;
----------------------------------------------------------------------------------


-- Workers migration -------------------------------------------------------------
INSERT INTO che_worker(id, userid, workspaceid)
SELECT                 id, userid, workspaceid
FROM worker;
----------------------------------------------------------------------------------


-- Worker actions migration ------------------------------------------------------
INSERT INTO che_worker_actions (worker_id, actions)
SELECT                          worker_id, actions
FROM worker_actions;
----------------------------------------------------------------------------------


-- Stack permissions migration ---------------------------------------------------
INSERT INTO che_stackpermissions(id, stackid, userid)
SELECT                           id, stackid, userid
FROM stackpermissions;
----------------------------------------------------------------------------------


-- Stack permission actions migration --------------------------------------------
INSERT INTO che_stackpermissions_actions (stackpermissions_id, actions)
SELECT                                    stackpermissions_id, actions
FROM stackpermissions_actions;
----------------------------------------------------------------------------------


-- Recipe permissions migration --------------------------------------------------
INSERT INTO che_recipepermissions(id, recipeid, userid)
SELECT                            id, recipeid, userid
FROM recipepermissions;
----------------------------------------------------------------------------------


-- Recipe permission actions migration -------------------------------------------
INSERT INTO che_recipepermissions_actions (recipepermissions_id, actions)
SELECT                                     recipepermissions_id, actions
FROM recipepermissions_actions;
----------------------------------------------------------------------------------
