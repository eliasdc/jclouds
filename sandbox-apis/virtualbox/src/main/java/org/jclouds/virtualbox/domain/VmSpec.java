/*
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

package org.jclouds.virtualbox.domain;

import com.google.common.base.Objects;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A description of a Virtual Machine in VirtualBox.
 */
public class VmSpec {

   private final String vmName;
   private final String osTypeId;
   private final String vmId;
   private final boolean forceOverwrite;
   private final Map<Long, NatAdapter> natNetworkAdapters;
   private final Set<StorageController> controllers;

   public VmSpec(String vmId, String vmName, String osTypeId, boolean forceOverwrite, Set<StorageController> controllers, Map<Long, NatAdapter> natNetworkAdapters) {
      this.vmId = vmId;
      this.vmName = vmName;
      this.osTypeId = osTypeId;
      this.controllers = controllers;
      this.forceOverwrite = forceOverwrite;
      this.natNetworkAdapters = natNetworkAdapters;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static class Builder {

      private Set<StorageController> controllers = new HashSet<StorageController>();
      private String name;
      private String id;
      private String osTypeId = "";
      private boolean forceOverwrite;
      private Map<Long, NatAdapter> natNetworkAdapters = new HashMap<Long, NatAdapter>();

      public Builder controller(StorageController controller) {
         controllers.add(controller);
         return this;
      }

      public Builder name(String name) {
         this.name = name;
         return this;
      }

      public Builder id(String id) {
         this.id = id;
         return this;
      }

      public Builder osTypeId(String osTypeId) {
         this.osTypeId = osTypeId;
         return this;
      }

      public Builder forceOverwrite(boolean forceOverwrite) {
         this.forceOverwrite = forceOverwrite;
         return this;
      }

      public Builder natNetworkAdapter(int slot, NatAdapter adapter) {
         this.natNetworkAdapters.put((long) slot, adapter);
         return this;
      }


      public VmSpec build() {
         checkNotNull(name, "name");
         checkNotNull(id, "id");
         return new VmSpec(id, name, osTypeId, forceOverwrite, controllers, natNetworkAdapters);
      }
   }

   public String getVmName() {
      return vmName;
   }

   public String getOsTypeId() {
      return osTypeId;
   }

   public String getVmId() {
      return vmId;
   }

   public boolean isForceOverwrite() {
      return forceOverwrite;
   }

   public Set<StorageController> getControllers() {
      return Collections.unmodifiableSet(controllers);
   }

   public Map<Long, NatAdapter> getNatNetworkAdapters() {
      return Collections.unmodifiableMap(natNetworkAdapters);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o instanceof VmSpec) {
         VmSpec other = (VmSpec) o;
         return Objects.equal(vmId, other.vmId) &&
                 Objects.equal(vmName, other.vmName) &&
                 Objects.equal(osTypeId, other.osTypeId) &&
                 Objects.equal(forceOverwrite, other.forceOverwrite) &&
                 Objects.equal(natNetworkAdapters, other.natNetworkAdapters) &&
                 Objects.equal(controllers, other.controllers);
      }
      return false;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(vmId, vmName, osTypeId, forceOverwrite, natNetworkAdapters, controllers);
   }

   @Override
   public String toString() {
      return "VmSpecification{" +
              "vmName='" + vmName + '\'' +
              ", osTypeId='" + osTypeId + '\'' +
              ", vmId='" + vmId + '\'' +
              ", forceOverwrite=" + forceOverwrite +
              ", natNetworkAdapters=" + natNetworkAdapters +
              ", controllers=" + controllers +
              '}';
   }
}
