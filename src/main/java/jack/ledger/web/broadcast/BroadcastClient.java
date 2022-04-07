package jack.ledger.web.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadcastClient {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(4445, InetAddress.getByName("0.0.0.0"));
        socket.setBroadcast(true);
        System.out.println("Listen on " + socket.getLocalAddress() + " from " + socket.getInetAddress() + " port " + socket.getBroadcast());
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (true) {
            System.out.println("Waiting for data");
            socket.receive(packet);
            String str = new String(buf);
            System.out.println(str);
            System.out.println("Data received");
        }
    }
}
