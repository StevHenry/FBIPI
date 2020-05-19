package fr.stevhnry.fbipi.networkmanager;

import com.sun.istack.internal.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ControlledNetworkInterface implements NetworkPropertyHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControlledNetworkInterface.class);

    private NetworkInterface networkInterface;
    private TreeSet<String> addresses;
    private HashSet<String> hostnames;

    /**
     * @param networkInterface {@link java.net.NetworkInterface} default OS provided interface
     */
    public ControlledNetworkInterface(@NotNull NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }

    @Override
    public NetworkInterface getNetworkInterface() {
        return networkInterface;
    }

    @Override
    public Set<String> getHostNames() {
        return hostnames;
    }

    @Override
    public TreeSet<String> getAddresses() {
        return addresses;
    }

    @Override
    public boolean updateAddresses() {
        addresses = Collections.list(getNetworkInterface().getInetAddresses()).stream().parallel()
                .map(add -> add.getHostAddress().split("%")[0]).collect(Collectors.toCollection(TreeSet::new));
        return addresses.size() > 0;
    }

    @Override
    public boolean updateHostNames(){
        hostnames = Collections.list(getNetworkInterface().getInetAddresses()).stream()
                .map(add -> add.getCanonicalHostName().split("%")[0]).collect(Collectors.toCollection(HashSet::new));
        return hostnames.size() > 0;
    }

    @Override
    public boolean isCurrentlyUsed() {
        try {
            return getNetworkInterface().isUp();
        } catch (SocketException exception) {
            LOGGER.warn("Can't check if interface is up ! Checking addresses number!");
        }
        return getNetworkInterface().getInterfaceAddresses().size() > 0;
    }
}
