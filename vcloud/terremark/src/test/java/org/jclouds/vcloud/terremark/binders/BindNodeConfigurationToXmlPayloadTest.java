/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
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
package org.jclouds.vcloud.terremark.binders;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import org.jclouds.rest.internal.GeneratedHttpRequest;
import com.google.inject.name.Names;
import org.jclouds.vcloud.VCloudPropertiesBuilder;
import org.jclouds.vcloud.terremark.domain.NodeConfiguration;
import org.testng.annotations.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests behavior of {@code BindNodeConfigurationToXmlPayload}
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit", testName = "vcloud.BindNodeConfigurationToXmlPayloadTest")
public class BindNodeConfigurationToXmlPayloadTest {
   Injector injector = Guice.createInjector(new AbstractModule() {

      @Override
      protected void configure() {
         Properties props = new Properties();
         Names.bindProperties(binder(), checkNotNull(new VCloudPropertiesBuilder(props).build(),
                  "properties"));
      }
   });

   public void testChangeName() throws IOException {
      NodeConfiguration config = new NodeConfiguration().changeNameTo("willie");
      String expectedPayload = "<NodeService xmlns=\"urn:tmrk:vCloudExpress-1.0\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><Name>willie</Name></NodeService>";
      assertConfigMakesPayload(config, expectedPayload);
   }

   public void testChangeDescription() throws IOException {
      NodeConfiguration config = new NodeConfiguration().changeDescriptionTo("description");
      String expectedPayload = "<NodeService xmlns=\"urn:tmrk:vCloudExpress-1.0\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><Description>description</Description></NodeService>";
      assertConfigMakesPayload(config, expectedPayload);
   }

   public void testEnableTraffic() throws IOException {
      NodeConfiguration config = new NodeConfiguration().enableTraffic();
      String expectedPayload = "<NodeService xmlns=\"urn:tmrk:vCloudExpress-1.0\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><Enabled>true</Enabled></NodeService>";
      assertConfigMakesPayload(config, expectedPayload);
   }

   public void testDisableTraffic() throws IOException {
      NodeConfiguration config = new NodeConfiguration().disableTraffic();
      String expectedPayload = "<NodeService xmlns=\"urn:tmrk:vCloudExpress-1.0\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><Enabled>false</Enabled></NodeService>";
      assertConfigMakesPayload(config, expectedPayload);
   }

   public void testTwoOptions() throws IOException {
      NodeConfiguration config = new NodeConfiguration().disableTraffic().changeNameTo("willie");
      String expectedPayload = "<NodeService xmlns=\"urn:tmrk:vCloudExpress-1.0\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><Name>willie</Name><Enabled>false</Enabled></NodeService>";
      assertConfigMakesPayload(config, expectedPayload);
   }

   @Test(expectedExceptions = IllegalArgumentException.class)
   public void testNoOptions() throws IOException {
      NodeConfiguration config = new NodeConfiguration();
      String expectedPayload = "<NodeService xmlns=\"urn:tmrk:vCloudExpress-1.0\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><Name>willie</Name><Enabled>false</Enabled></NodeService>";
      assertConfigMakesPayload(config, expectedPayload);
   }

   private void assertConfigMakesPayload(NodeConfiguration config, String expectedPayload) {
      BindNodeConfigurationToXmlPayload binder = injector
               .getInstance(BindNodeConfigurationToXmlPayload.class);
      Multimap<String, String> headers = Multimaps.synchronizedMultimap(HashMultimap
               .<String, String> create());
      GeneratedHttpRequest<?> request = createMock(GeneratedHttpRequest.class);
      expect(request.getArgs()).andReturn(new Object[] { config }).atLeastOnce();
      expect(request.getEndpoint()).andReturn(URI.create("http://localhost/key")).anyTimes();
      expect(request.getFirstHeaderOrNull("Content-Type")).andReturn(null).atLeastOnce();
      expect(request.getHeaders()).andReturn(headers).atLeastOnce();
      request.setPayload(expectedPayload);
      replay(request);
      binder.bindToRequest(request, config);
      verify(request);
   }

}
