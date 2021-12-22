package com.asianaidt.ict.analyca.agent.stomp;

import com.asianaidt.ict.analyca.system.websocketcore.type.WebSocketHeader;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Configuration
public class StompClientConfig {
//    private final StompSessionHandler stompSessionHandler;
//
//    @Value("${analyca.websocket.ip}")
//    private String analycaIp;
//
//    @Value("${analyca.websocket.port}")
//    private String analycaPort;
//
//    @Value("${analyca.websocket.endpoint}")
//    private String analycaEndPoint;
//
//
//    public StompClientConfig(StompSessionHandler stompSessionHandler) {
//        this.stompSessionHandler = stompSessionHandler;
//    }
//
//    @Bean
//    public StompSession stompSession() {
//        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
//        Transport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
//        List<Transport> transports = Collections.singletonList(webSocketTransport);
//
//        SockJsClient sockJsClient = new SockJsClient(transports);
//
//        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//        StompHeaders headers = new StompHeaders();
//        headers.add(WebSocketHeader.HOSTNAME.getName(), getHostname());
//        headers.add(WebSocketHeader.IP.getName(), getHostIP());
//
//        final String url = "ws://{host}:{port}/{endpoint}";
//        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
//        webSocketHttpHeaders.add(WebSocketHeader.USER.getName(), getUser());
//        StompSession session = null;
//        try {
//            session = stompClient.connect(url, webSocketHttpHeaders, headers,
//                    stompSessionHandler, analycaIp, analycaPort, analycaEndPoint).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        return session;
//    }
//
//    private String getHostname() {
//        try {
//            return InetAddress.getLocalHost().getHostName();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return generateRandomUsername();
//    }
//
//    private String generateRandomUsername() {
//        RandomStringGenerator randomStringGenerator =
//                new RandomStringGenerator.Builder()
//                        .withinRange('0', 'z')
//                        .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
//                        .build();
//        return randomStringGenerator.generate(32);
//    }
//
//    private String getHostIP() {
//        try {
//            return InetAddress.getLocalHost().getHostAddress();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return "127.0.0.1";
//    }
//
//    private String getUser() {
//
//        try {
//            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
//                    .getNetworkInterfaces();
//            while (networkInterfaces.hasMoreElements()) {
//                NetworkInterface nic = networkInterfaces.nextElement();
//                byte[] hardwareAddress = nic.getHardwareAddress();
//                StringBuilder buffer = new StringBuilder();
//                if (null != hardwareAddress) {
//                    for (byte b : hardwareAddress) {
//                        buffer.append(String.format("%02X", b) + ":");
//                    }
//                    return buffer.toString().substring(0, buffer.toString().length()-1);
//                }
//            }
//            return Long.toHexString(System.currentTimeMillis());
//        } catch (SocketException e) {
//            return Long.toHexString(System.currentTimeMillis());
//        }
//        //return getHostname() + "-" + getHostIP();
//    }
}
