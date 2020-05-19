package fr.stevhnry.fbipi.networkmanager;

import com.sun.istack.internal.NotNull;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public interface NetworkPropertyHolder {

    /**
     * @return {@link java.net.NetworkInterface} linked
     */
    @NotNull
    NetworkInterface getNetworkInterface();

    /**
     * @return whether the connection is used
     */
    boolean isCurrentlyUsed();

    /**
     * @return whether the host names update succeeded
     */
    boolean updateHostNames();

    /**
     * @return Hosts' names in a {@link java.util.TreeSet}
     */
    Set<String> getHostNames();

    /**
     * @return whether the addresses update succeeded
     */
    boolean updateAddresses();

    /**
     * @return Interface' addresses in a {@link java.util.TreeSet}
     */
    TreeSet<String> getAddresses();


    /**
     * @return whether this interface has a local address
     */
    default boolean hasLocalAddress() {
        return getAddresses().size() > 1;
    }

    /**
     * @return the local address if it exists
     */
    default Optional<String> getLocalAddress() {
        return Optional.ofNullable(getAddresses().first());
    }

    /**
     * @return whether hardware/physical address is specified
     */
    default boolean hasPhysicalAddress() {
        try {
            byte[] hardwareAddress = getNetworkInterface().getHardwareAddress();
            return hardwareAddress != null && hardwareAddress.length > 0;
        } catch (SocketException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * @return the hardware/physical address if it exists
     * Shape: XX:XX:XX...
     */
    default Optional<String> getPhysicalAddress() {
        try {
            byte[] hardwareAddress = getNetworkInterface().getHardwareAddress();
            String[] bytes = new String[hardwareAddress.length];
            for (int i = 0; i < hardwareAddress.length; i++) {
                bytes[i] = String.format("%02x", hardwareAddress[i]).toUpperCase();
            }
            return Optional.of(String.join(":", bytes));
        } catch (SocketException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }
}
