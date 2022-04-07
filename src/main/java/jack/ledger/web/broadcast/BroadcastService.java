package jack.ledger.web.broadcast;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringTokenizer;

/**
 * Class used for sending and listening to broadcast messages, we use broadcasting to get an idea of the nodes
 * around us so we can then talk http to them.
 */
@Service
public class BroadcastService {

    private static final String BROADCAST_ADDRESS = "255.255.255.255";
    private static final String ALL_BIND_ADDRESS = "0.0.0.0";
    private static final String DEFAULT = "default";
    private static final String DEFAULT_GATEWAY = "192.168.0.1";
    private static final String NET_STAT = "netstat -rn";

    @Value("ledger.broadcast.port")
    private int broadcastPort;

    public static void main(String[] args) throws IOException {
        BroadcastService broadcastUtil = new BroadcastService();
//        broadcastUtil.getIpAddresse();

        System.out.println(broadcastUtil.getGateway().orElse(DEFAULT_GATEWAY));
    }

    public Optional<String> getGateway() {
        try{
            Process result = Runtime.getRuntime().exec(NET_STAT);

            BufferedReader output = new BufferedReader(new InputStreamReader(result.getInputStream()));

            String line = output.readLine();
            while(line != null){
                if (line.trim().startsWith(DEFAULT) || line.trim().startsWith(ALL_BIND_ADDRESS))
                    break;
                line = output.readLine();
            }

            if (line == null) {
                return Optional.empty();
            }

            return Collections.list(new StringTokenizer( line ))
                    .stream()
                    .skip(1)
                    .map(token -> (String) token)
                    .findFirst();

        } catch( Exception e ) {
            return Optional.empty();
        }
    }

    public void broadcastAddress() {

    }

    public void broadcastString(String broadcastMessage) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(BROADCAST_ADDRESS), broadcastPort);
        socket.send(packet);
        socket.close();
    }

    private void getIpAddresse() throws SocketException {
        getNetworkInterfaces().forEach(networkInterface -> {
            System.out.println(networkInterface.getDisplayName());
            Enumeration<InetAddress> xx = networkInterface.getInetAddresses();
            while (xx.hasMoreElements()) {
                System.out.println(xx.nextElement());
            }
            System.out.println("\n");
        });
    }

    private Collection<NetworkInterface> getNetworkInterfaces() throws SocketException {
        Iterator<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces().asIterator();
        Collection<NetworkInterface> networkInterfacesCollection = new HashSet<>();
        networkInterfaces.forEachRemaining(networkInterfacesCollection::add);
        return networkInterfacesCollection;
    }

    private static List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces
                = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream()
                    .map(a -> a.getBroadcast())
                    .filter(Objects::nonNull)
                    .forEach(broadcastList::add);
        }
        return broadcastList;
    }
}
