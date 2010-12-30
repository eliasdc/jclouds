/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.aws.ec2.compute.functions;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.testng.Assert.assertEquals;

import java.net.UnknownHostException;

import org.jclouds.aws.ec2.EC2Client;
import org.jclouds.aws.ec2.domain.KeyPair;
import org.jclouds.aws.ec2.services.KeyPairClient;
import org.testng.annotations.Test;

import com.google.common.base.Supplier;

/**
 * @author Adrian Cole
 */
@Test(groups = "unit")
public class CreateUniqueKeyPairTest {
   @SuppressWarnings( { "unchecked" })
   @Test
   public void testApply() throws UnknownHostException {
      EC2Client client = createMock(EC2Client.class);
      KeyPairClient keyClient = createMock(KeyPairClient.class);
      Supplier<String> uniqueIdSupplier = createMock(Supplier.class);

      KeyPair pair = createMock(KeyPair.class);

      expect(client.getKeyPairServices()).andReturn(keyClient).atLeastOnce();

      expect(uniqueIdSupplier.get()).andReturn("1");
      expect(keyClient.createKeyPairInRegion("region", "jclouds#tag#region#1")).andReturn(pair);

      replay(client);
      replay(keyClient);
      replay(uniqueIdSupplier);

      CreateUniqueKeyPair parser = new CreateUniqueKeyPair(client, uniqueIdSupplier);

      assertEquals(parser.createNewKeyPairInRegion("region", "tag"), pair);

      verify(client);
      verify(keyClient);
      verify(uniqueIdSupplier);
   }

   @SuppressWarnings( { "unchecked" })
   @Test
   public void testApplyWithIllegalStateException() throws UnknownHostException {
      EC2Client client = createMock(EC2Client.class);
      KeyPairClient keyClient = createMock(KeyPairClient.class);
      Supplier<String> uniqueIdSupplier = createMock(Supplier.class);

      KeyPair pair = createMock(KeyPair.class);

      expect(client.getKeyPairServices()).andReturn(keyClient).atLeastOnce();

      expect(uniqueIdSupplier.get()).andReturn("1");
      expect(keyClient.createKeyPairInRegion("region", "jclouds#tag#region#1")).andThrow(new IllegalStateException());
      expect(uniqueIdSupplier.get()).andReturn("2");
      expect(keyClient.createKeyPairInRegion("region", "jclouds#tag#region#2")).andReturn(pair);

      replay(client);
      replay(keyClient);
      replay(uniqueIdSupplier);

      CreateUniqueKeyPair parser = new CreateUniqueKeyPair(client, uniqueIdSupplier);

      assertEquals(parser.createNewKeyPairInRegion("region", "tag"), pair);

      verify(client);
      verify(keyClient);
      verify(uniqueIdSupplier);

   }
}
