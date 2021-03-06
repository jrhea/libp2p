/*
 * Copyright 2019 BLK Technologies Limited (web3labs.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.web3j.libp2p.transport.tcp.util

import io.ipfs.multiformats.multiaddr.Multiaddr
import io.ipfs.multiformats.multiaddr.Protocol
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.SocketAddress

/**
 * A set of utility functions for working with the TCP transport library.
 */
object TCPUtil {

    /**
     * Converts the ipAddress IP address and port into an equivalent [Multiaddr].
     * @param ipAddress the IP address.
     * @param port the port number.
     * @return the equivalent multiaddr.
     */
    fun createMultiaddr(ipAddress: InetAddress, port: Int): Multiaddr {
        return if (ipAddress is Inet4Address) {
            TCPUtil.createMultiaddr(ipAddress, port)
        } else {
            TCPUtil.createMultiaddr(ipAddress as Inet6Address, port)
        }
    }

    /**
     * Converts the given multiaddr to the equivalent socket address.
     * @param multiaddr the multiaddr.
     * @return the socket address equivalent.
     */
    fun toSocketAddress(multiaddr: Multiaddr): SocketAddress {
        val ip = multiaddr.valueForProtocol(Protocol.IP4.code)
        val port = multiaddr.valueForProtocol(Protocol.TCP.code).toInt()
        return InetSocketAddress(InetAddress.getByName(ip), port)
    }

    /**
     * Creates a [Multiaddr] from the given socket address.
     * @param socketAddress the socket address.
     * @return the multiaddr created from this address.
     */
    fun createMultiaddr(socketAddress: InetSocketAddress): Multiaddr? {
        return socketAddress.address?.let {
            createMultiaddr(it, socketAddress.port)
        }
    }

    /**
     * Creates a [Multiaddr] that uses the loopback address on the given port.
     * @param port the desired port.
     * @return the multiaddr.
     */
    fun createLoopbackMultiaddr(port: Int): Multiaddr = createMultiaddr(InetAddress.getLoopbackAddress(), port)

    /**
     * Converts the given IPv4 address and port into an equivalent [Multiaddr].
     * @param ip4Address the IPv4 address.
     * @param port the port number.
     * @return the equivalent multiaddr.
     */
    private fun createMultiaddr(ip4Address: Inet4Address, port: Int): Multiaddr {
        return Multiaddr("/${Protocol.IP4.named}/${ip4Address.hostAddress}/tcp/$port")
    }

    /**
     * Converts the given IPv6 address and port into an equivalent [Multiaddr].
     * @param ip6address the IPv6 address.
     * @param port the port number.
     * @return the equivalent multiaddr.
     */
    private fun createMultiaddr(ip6address: Inet6Address, port: Int): Multiaddr {
        return Multiaddr("/${Protocol.IP6.named}/${ip6address.hostAddress}/tcp/$port")
    }
}
