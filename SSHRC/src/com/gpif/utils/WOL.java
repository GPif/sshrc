package com.gpif.utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WOL {
    public static final int PORT = 9;
    public String ip;
    public String mac;

    public WOL(String _ip, String _mac) {
      ip  = _ip;
      mac = _mac;
    }

    public int send() {
      String ipStr = this.ip;
      String macStr = this.mac;

      try {
          byte[] macBytes = getMacBytes(macStr);
          byte[] bytes = new byte[6 + 16 * macBytes.length];
          for (int i = 0; i < 6; i++) {
              bytes[i] = (byte) 0xff;
          }
          for (int i = 6; i < bytes.length; i += macBytes.length) {
              System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
          }

          InetAddress address = InetAddress.getByName(ipStr);
          DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
          DatagramSocket socket = new DatagramSocket();
          socket.send(packet);
          socket.close();

          System.out.println("Wake-on-LAN packet sent.");
      }
      catch (Exception e) {
          System.out.println("Failed to send Wake-on-LAN packet:" + e);
          System.exit(1);
      }
      return 0;
    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }

}
