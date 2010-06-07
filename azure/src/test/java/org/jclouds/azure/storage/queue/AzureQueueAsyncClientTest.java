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
package org.jclouds.azure.storage.queue;

import static org.jclouds.azure.storage.options.CreateOptions.Builder.withMetadata;
import static org.jclouds.azure.storage.options.ListOptions.Builder.maxResults;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Properties;

import javax.ws.rs.HttpMethod;

import org.jclouds.azure.storage.filters.SharedKeyLiteAuthentication;
import org.jclouds.azure.storage.options.CreateOptions;
import org.jclouds.azure.storage.options.ListOptions;
import org.jclouds.azure.storage.queue.config.AzureQueueRestClientModule;
import org.jclouds.azure.storage.queue.options.PutMessageOptions;
import org.jclouds.http.functions.CloseContentAndReturn;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.http.functions.ReturnTrueIf2xx;
import org.jclouds.logging.config.NullLoggingModule;
import org.jclouds.rest.RestClientTest;
import org.jclouds.rest.functions.MapHttp4xxCodesToExceptions;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.rest.internal.RestAnnotationProcessor;
import com.google.inject.name.Names;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMultimap;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;

/**
 * Tests behavior of {@code AzureQueueAsyncClient}
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit", testName = "azurequeue.AzureQueueAsyncClientTest")
public class AzureQueueAsyncClientTest extends RestClientTest<AzureQueueAsyncClient> {
   private static final Class<? extends ListOptions[]> listOptionsVarargsClass = new ListOptions[] {}
            .getClass();
   private static final Class<? extends CreateOptions[]> createOptionsVarargsClass = new CreateOptions[] {}
            .getClass();

   public void testListQueues() throws SecurityException, NoSuchMethodException {
      Method method = AzureQueueAsyncClient.class.getMethod("listQueues", listOptionsVarargsClass);

      GeneratedHttpRequest<AzureQueueAsyncClient> httpMethod = processor.createRequest(method,
               new Object[] {});
      assertEquals(httpMethod.getEndpoint().getHost(), "myaccount.queue.core.windows.net");
      assertEquals(httpMethod.getEndpoint().getPath(), "/");
      assertEquals(httpMethod.getEndpoint().getQuery(), "comp=list");
      assertEquals(httpMethod.getMethod(), HttpMethod.GET);
      assertEquals(httpMethod.getHeaders().size(), 1);
      assertEquals(httpMethod.getHeaders().get("x-ms-version"), Collections
               .singletonList("2009-09-19"));
      assertEquals(processor.createResponseParser(method, httpMethod).getClass(), ParseSax.class);
      // TODO check generic type of response parser
      assertEquals(processor
               .createExceptionParserOrThrowResourceNotFoundOn404IfNoAnnotation(method).getClass(),
               MapHttp4xxCodesToExceptions.class);
   }

   public void testListQueuesOptions() throws SecurityException, NoSuchMethodException {
      Method method = AzureQueueAsyncClient.class.getMethod("listQueues", listOptionsVarargsClass);

      GeneratedHttpRequest<AzureQueueAsyncClient> httpMethod = processor.createRequest(method,
               new Object[] { maxResults(1).marker("marker").prefix("prefix") });
      assertEquals(httpMethod.getEndpoint().getHost(), "myaccount.queue.core.windows.net");
      assertEquals(httpMethod.getEndpoint().getPath(), "/");
      assert httpMethod.getEndpoint().getQuery().contains("comp=list");
      assert httpMethod.getEndpoint().getQuery().contains("marker=marker");
      assert httpMethod.getEndpoint().getQuery().contains("maxresults=1");
      assert httpMethod.getEndpoint().getQuery().contains("prefix=prefix");
      assertEquals(httpMethod.getMethod(), HttpMethod.GET);
      assertEquals(httpMethod.getHeaders().size(), 1);
      assertEquals(httpMethod.getHeaders().get("x-ms-version"), Collections
               .singletonList("2009-09-19"));
      assertEquals(processor.createResponseParser(method, httpMethod).getClass(), ParseSax.class);
      // TODO check generic type of response parser
      assertEquals(processor
               .createExceptionParserOrThrowResourceNotFoundOn404IfNoAnnotation(method).getClass(),
               MapHttp4xxCodesToExceptions.class);
   }

   public void testCreateQueue() throws SecurityException, NoSuchMethodException {
      Method method = AzureQueueAsyncClient.class.getMethod("createQueue", String.class,
               createOptionsVarargsClass);

      GeneratedHttpRequest<AzureQueueAsyncClient> httpMethod = processor.createRequest(method,
               new Object[] { "queue" });
      assertEquals(httpMethod.getEndpoint().getHost(), "myaccount.queue.core.windows.net");
      assertEquals(httpMethod.getEndpoint().getPath(), "/queue");
      assertEquals(httpMethod.getEndpoint().getQuery(), null);
      assertEquals(httpMethod.getMethod(), HttpMethod.PUT);
      assertEquals(httpMethod.getHeaders().size(), 2);
      assertEquals(httpMethod.getHeaders().get("x-ms-version"), Collections
               .singletonList("2009-09-19"));
      assertEquals(httpMethod.getHeaders().get("Content-Length"), Collections.singletonList("0"));
      assertEquals(processor.createResponseParser(method, httpMethod).getClass(),
               ReturnTrueIf2xx.class);
      // TODO check generic type of response parser
      assertEquals(processor
               .createExceptionParserOrThrowResourceNotFoundOn404IfNoAnnotation(method).getClass(),
               MapHttp4xxCodesToExceptions.class);
   }

   public void testCreateQueueOptions() throws SecurityException, NoSuchMethodException {
      Method method = AzureQueueAsyncClient.class.getMethod("createQueue", String.class,
               createOptionsVarargsClass);

      GeneratedHttpRequest<AzureQueueAsyncClient> httpMethod = processor.createRequest(method,
               new Object[] { "queue", withMetadata(ImmutableMultimap.of("foo", "bar")) });
      assertEquals(httpMethod.getEndpoint().getHost(), "myaccount.queue.core.windows.net");
      assertEquals(httpMethod.getEndpoint().getPath(), "/queue");
      assertEquals(httpMethod.getEndpoint().getQuery(), null);
      assertEquals(httpMethod.getMethod(), HttpMethod.PUT);
      assertEquals(httpMethod.getHeaders().size(), 3);
      assertEquals(httpMethod.getHeaders().get("x-ms-version"), Collections
               .singletonList("2009-09-19"));
      assertEquals(httpMethod.getHeaders().get("x-ms-meta-foo"), Collections.singletonList("bar"));
      assertEquals(httpMethod.getHeaders().get("Content-Length"), Collections.singletonList("0"));
      assertEquals(processor.createResponseParser(method, httpMethod).getClass(),
               ReturnTrueIf2xx.class);
      // TODO check generic type of response parser
      assertEquals(processor
               .createExceptionParserOrThrowResourceNotFoundOn404IfNoAnnotation(method).getClass(),
               MapHttp4xxCodesToExceptions.class);
   }

   public void testDeleteQueue() throws SecurityException, NoSuchMethodException {
      Method method = AzureQueueAsyncClient.class.getMethod("deleteQueue", String.class);

      GeneratedHttpRequest<AzureQueueAsyncClient> httpMethod = processor.createRequest(method,
               new Object[] { "queue" });
      assertEquals(httpMethod.getEndpoint().getHost(), "myaccount.queue.core.windows.net");
      assertEquals(httpMethod.getEndpoint().getPath(), "/queue");
      assertEquals(httpMethod.getEndpoint().getQuery(), null);
      assertEquals(httpMethod.getMethod(), HttpMethod.DELETE);
      assertEquals(httpMethod.getHeaders().size(), 1);
      assertEquals(httpMethod.getHeaders().get("x-ms-version"), Collections
               .singletonList("2009-09-19"));
      assertEquals(processor.createResponseParser(method, httpMethod).getClass(),
               CloseContentAndReturn.class);
      // TODO check generic type of response parser
      assertEquals(processor
               .createExceptionParserOrThrowResourceNotFoundOn404IfNoAnnotation(method).getClass(),
               MapHttp4xxCodesToExceptions.class);
   }

   public void testPutMessage() throws SecurityException, NoSuchMethodException, IOException {
      Method method = AzureQueueAsyncClient.class.getMethod("putMessage", String.class,
               String.class, Array.newInstance(PutMessageOptions.class, 0).getClass());
      GeneratedHttpRequest<AzureQueueAsyncClient> httpMethod = processor.createRequest(method,
               "queue", "message");

      assertRequestLineEquals(httpMethod,
               "POST https://myaccount.queue.core.windows.net/queue/messages HTTP/1.1");
      assertHeadersEqual(httpMethod,
               "Content-Length: 63\nContent-Type: application/unknown\nx-ms-version: 2009-09-19\n");
      assertPayloadEquals(httpMethod,
               "<QueueMessage><MessageText>message</MessageText></QueueMessage>");

      assertResponseParserClassEquals(method, httpMethod, CloseContentAndReturn.class);
      assertSaxResponseParserClassEquals(method, null);
      assertExceptionParserClassEquals(method, null);

      checkFilters(httpMethod);
   }

   public void testPutMessageOptions() throws SecurityException, NoSuchMethodException, IOException {
      Method method = AzureQueueAsyncClient.class.getMethod("putMessage", String.class,
               String.class, Array.newInstance(PutMessageOptions.class, 0).getClass());
      GeneratedHttpRequest<AzureQueueAsyncClient> httpMethod = processor.createRequest(method,
               "queue", "message", PutMessageOptions.Builder.withTTL(3));

      assertRequestLineEquals(httpMethod,
               "POST https://myaccount.queue.core.windows.net/queue/messages?messagettl=3 HTTP/1.1");
      assertHeadersEqual(httpMethod,
               "Content-Length: 63\nContent-Type: application/unknown\nx-ms-version: 2009-09-19\n");
      assertPayloadEquals(httpMethod,
               "<QueueMessage><MessageText>message</MessageText></QueueMessage>");

      assertResponseParserClassEquals(method, httpMethod, CloseContentAndReturn.class);
      assertSaxResponseParserClassEquals(method, null);
      assertExceptionParserClassEquals(method, null);

      checkFilters(httpMethod);
   }

   public void testClearMessages() throws SecurityException, NoSuchMethodException, IOException {
      Method method = AzureQueueAsyncClient.class.getMethod("clearMessages", String.class);
      GeneratedHttpRequest<AzureQueueAsyncClient> httpMethod = processor.createRequest(method,
               "queue");

      assertRequestLineEquals(httpMethod,
               "DELETE https://myaccount.queue.core.windows.net/queue/messages HTTP/1.1");
      assertHeadersEqual(httpMethod, "x-ms-version: 2009-09-19\n");
      assertPayloadEquals(httpMethod, null);

      assertResponseParserClassEquals(method, httpMethod, CloseContentAndReturn.class);
      assertSaxResponseParserClassEquals(method, null);
      assertExceptionParserClassEquals(method, null);

      checkFilters(httpMethod);
   }

   @Override
   protected void checkFilters(GeneratedHttpRequest<AzureQueueAsyncClient> httpMethod) {
      assertEquals(httpMethod.getFilters().size(), 1);
      assertEquals(httpMethod.getFilters().get(0).getClass(), SharedKeyLiteAuthentication.class);
   }

   @Override
   protected TypeLiteral<RestAnnotationProcessor<AzureQueueAsyncClient>> createTypeLiteral() {
      return new TypeLiteral<RestAnnotationProcessor<AzureQueueAsyncClient>>() {
      };
   }

   @Override
   protected Module createModule() {
      return new AzureQueueRestClientModule() {
         @Override
         protected void configure() {
            Names.bindProperties(binder(), new AzureQueuePropertiesBuilder(new Properties())
                     .withCredentials("myaccount", "key").build());
            install(new NullLoggingModule());
            super.configure();
         }

      };
   }
}
