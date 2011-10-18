/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.compute.callable;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.jclouds.scriptbuilder.domain.Statements.exec;
import static org.testng.Assert.assertEquals;

import org.jclouds.compute.callables.InitScriptConfigurationForTasks;
import org.jclouds.compute.callables.RunScriptOnNodeAsInitScriptUsingSsh;
import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.domain.NodeState;
import org.jclouds.compute.options.RunScriptOptions;
import org.jclouds.domain.Credentials;
import org.jclouds.scriptbuilder.InitBuilder;
import org.jclouds.scriptbuilder.domain.OsFamily;
import org.jclouds.scriptbuilder.domain.Statement;
import org.jclouds.ssh.SshClient;
import org.testng.annotations.Test;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * @author Adrian Cole
 */
@Test(groups = "unit", singleThreaded = true, testName = "RunScriptOnNodeAsInitScriptUsingSshTest")
public class RunScriptOnNodeAsInitScriptUsingSshTest {

   @Test(expectedExceptions = IllegalStateException.class)
   public void testWithoutInitThrowsIllegalStateException() {
      Statement command = exec("doFoo");
      NodeMetadata node = new NodeMetadataBuilder().ids("id").state(NodeState.RUNNING).credentials(
               new Credentials("tester", "notalot")).build();

      SshClient sshClient = createMock(SshClient.class);

      replay(sshClient);

      RunScriptOnNodeAsInitScriptUsingSsh testMe = new RunScriptOnNodeAsInitScriptUsingSsh(Functions
               .forMap(ImmutableMap.of(node, sshClient)), InitScriptConfigurationForTasks.create()
               .appendIncrementingNumberToAnonymousTaskNames(), node, command, new RunScriptOptions());

      testMe.call();
   }

   public void testDefault() {
      Statement command = exec("doFoo");
      NodeMetadata node = new NodeMetadataBuilder().ids("id").state(NodeState.RUNNING).credentials(
               new Credentials("tester", "notalot")).build();

      SshClient sshClient = createMock(SshClient.class);

      InitBuilder init = new InitBuilder("jclouds-script-0", "/tmp/jclouds-script-0", "/tmp/jclouds-script-0",
               ImmutableMap.<String, String> of(), ImmutableSet.of(command));

      sshClient.connect();
      sshClient.put("/tmp/init-jclouds-script-0", init.render(OsFamily.UNIX));
      expect(sshClient.getUsername()).andReturn("tester").atLeastOnce();
      expect(sshClient.getHostAddress()).andReturn("somewhere.example.com").atLeastOnce();
      
      // setup script as default user
      expect(sshClient.exec("chmod 755 /tmp/init-jclouds-script-0")).andReturn(new ExecResponse("", "", 0));
      expect(sshClient.exec("ln -fs /tmp/init-jclouds-script-0 jclouds-script-0")).andReturn(new ExecResponse("", "", 0));
      expect(sshClient.exec("./jclouds-script-0 init")).andReturn(new ExecResponse("", "", 0));
      
      // start script as root via sudo, note that since there's no adminPassword we do a straight sudo
      expect(sshClient.exec("sudo ./jclouds-script-0 start")).andReturn(new ExecResponse("", "", 0));

      
      sshClient.disconnect();
      replay(sshClient);

      RunScriptOnNodeAsInitScriptUsingSsh testMe = new RunScriptOnNodeAsInitScriptUsingSsh(Functions
               .forMap(ImmutableMap.of(node, sshClient)), InitScriptConfigurationForTasks.create()
               .appendIncrementingNumberToAnonymousTaskNames(), node, command, new RunScriptOptions());

      assertEquals(testMe.getInitFile(), "/tmp/init-jclouds-script-0");
      assertEquals(testMe.getNode(), node);
      assertEquals(testMe.getStatement(), init);

      testMe.init();
      testMe.call();
      verify(sshClient);
   }


   public void testWithSudoPassword() {
      Statement command = exec("doFoo");
      NodeMetadata node = new NodeMetadataBuilder().ids("id").state(NodeState.RUNNING).credentials(
               new Credentials("tester", "notalot")).adminPassword("rootme").build();

      SshClient sshClient = createMock(SshClient.class);

      InitBuilder init = new InitBuilder("jclouds-script-0", "/tmp/jclouds-script-0", "/tmp/jclouds-script-0",
               ImmutableMap.<String, String> of(), ImmutableSet.of(command));

      sshClient.connect();
      sshClient.put("/tmp/init-jclouds-script-0", init.render(OsFamily.UNIX));
      expect(sshClient.getUsername()).andReturn("tester").atLeastOnce();
      expect(sshClient.getHostAddress()).andReturn("somewhere.example.com").atLeastOnce();
      
      // setup script as default user
      expect(sshClient.exec("chmod 755 /tmp/init-jclouds-script-0")).andReturn(new ExecResponse("", "", 0));
      expect(sshClient.exec("ln -fs /tmp/init-jclouds-script-0 jclouds-script-0")).andReturn(new ExecResponse("", "", 0));
      expect(sshClient.exec("./jclouds-script-0 init")).andReturn(new ExecResponse("", "", 0));
      
      // since there's an adminPassword we must pass this in
      expect(sshClient.exec("echo 'rootme'|sudo -S ./jclouds-script-0 start")).andReturn(new ExecResponse("", "", 0));

      
      sshClient.disconnect();
      replay(sshClient);

      RunScriptOnNodeAsInitScriptUsingSsh testMe = new RunScriptOnNodeAsInitScriptUsingSsh(Functions
               .forMap(ImmutableMap.of(node, sshClient)), InitScriptConfigurationForTasks.create()
               .appendIncrementingNumberToAnonymousTaskNames(), node, command, new RunScriptOptions());

      assertEquals(testMe.getInitFile(), "/tmp/init-jclouds-script-0");
      assertEquals(testMe.getNode(), node);
      assertEquals(testMe.getStatement(), init);

      testMe.init();
      testMe.call();
      verify(sshClient);
   }



   public void testNotRoot() {
      Statement command = exec("doFoo");
      NodeMetadata node = new NodeMetadataBuilder().ids("id").state(NodeState.RUNNING).credentials(
               new Credentials("tester", "notalot")).adminPassword("rootme").build();

      SshClient sshClient = createMock(SshClient.class);

      InitBuilder init = new InitBuilder("jclouds-script-0", "/tmp/jclouds-script-0", "/tmp/jclouds-script-0",
               ImmutableMap.<String, String> of(), ImmutableSet.of(command));

      sshClient.connect();
      sshClient.put("/tmp/init-jclouds-script-0", init.render(OsFamily.UNIX));
      expect(sshClient.getUsername()).andReturn("tester").atLeastOnce();
      expect(sshClient.getHostAddress()).andReturn("somewhere.example.com").atLeastOnce();
      
      // setup script as default user
      expect(sshClient.exec("chmod 755 /tmp/init-jclouds-script-0")).andReturn(new ExecResponse("", "", 0));
      expect(sshClient.exec("ln -fs /tmp/init-jclouds-script-0 jclouds-script-0")).andReturn(new ExecResponse("", "", 0));
      expect(sshClient.exec("./jclouds-script-0 init")).andReturn(new ExecResponse("", "", 0));
      
      // kick off as current user
      expect(sshClient.exec("./jclouds-script-0 start")).andReturn(new ExecResponse("", "", 0));
      
      sshClient.disconnect();
      replay(sshClient);

      RunScriptOnNodeAsInitScriptUsingSsh testMe = new RunScriptOnNodeAsInitScriptUsingSsh(Functions
               .forMap(ImmutableMap.of(node, sshClient)), InitScriptConfigurationForTasks.create()
               .appendIncrementingNumberToAnonymousTaskNames(), node, command, new RunScriptOptions().runAsRoot(false));

      assertEquals(testMe.getInitFile(), "/tmp/init-jclouds-script-0");
      assertEquals(testMe.getNode(), node);
      assertEquals(testMe.getStatement(), init);

      testMe.init();
      testMe.call();
      verify(sshClient);
   }
}
