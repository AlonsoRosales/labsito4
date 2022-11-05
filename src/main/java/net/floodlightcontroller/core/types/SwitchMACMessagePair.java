/**
 *    Copyright 2011, Big Switch Networks, Inc.
 *    Originally created by David Erickson, Stanford University
 *
 *    Licensed under the Apache License, Version 2.0 (the "License"); you may
 *    not use this file except in compliance with the License. You may obtain
 *    a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *    License for the specific language governing permissions and limitations
 *    under the License.
 **/

package net.floodlightcontroller.core.types;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.floodlightcontroller.MACTracker.SwitchMACMessagePairSerializer;
import org.projectfloodlight.openflow.protocol.OFMessage;

import net.floodlightcontroller.core.IOFSwitch;

@JsonSerialize(using= SwitchMACMessagePairSerializer.class)
public class SwitchMACMessagePair {
    private IOFSwitch sw;
    private OFMessage msg;
    private Long mac;

    public Long getMac() {
        return mac;
    }

    public IOFSwitch getSwitch() {
        return this.sw;
    }

    public OFMessage getMessage() {
        return this.msg;
    }

    public SwitchMACMessagePair(IOFSwitch sw, OFMessage msg, Long mac) {
        this.sw = sw;
        this.msg = msg;
        this.mac = mac;
    }
}
