package fr.stevhnry.fbipi.networkmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ControlledInterfaceManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControlledInterfaceManager.class);

    /**
     * Set containing all controlledNetworkInterfaces
     */
    private Set<ControlledNetworkInterface> interfaces = new HashSet<>();

    public ControlledInterfaceManager() throws SocketException {
        updateInterfacesInstances();
        updateInterfacesValues();
    }

    /**
     * Updates the {@link ControlledNetworkInterface} set by keeping existing
     * connections and removing destroyed ones
     */
    public void updateInterfacesInstances() throws SocketException {

        Set<ControlledNetworkInterface> newInterfaces = new HashSet<>();
        for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            newInterfaces.add(getControlledInterface(ni).orElse(new ControlledNetworkInterface(ni)));
        }
        interfaces = newInterfaces;
        LOGGER.info("ControlledNetworkInterfaces' set updated!");
    }

    /**
     * Updates addresses and hostnames provided interface
     *
     * @param ctrlNetInterface provided interface
     */
    public void updateInterfaceValues(ControlledNetworkInterface ctrlNetInterface) {
        ctrlNetInterface.updateAddresses();
        ctrlNetInterface.updateHostNames();
        LOGGER.debug("Successfully updated ControlledNetworkInterface named {}.", ctrlNetInterface.getNetworkInterface().getName());
    }

    /**
     * Calls {@link #updateInterfaceValues(ControlledNetworkInterface)} for each
     * {@link ControlledNetworkInterface} in the Set
     */
    public void updateInterfacesValues() {
        interfaces.forEach(this::updateInterfaceValues);
        LOGGER.info("Updated all ControlledNetworkInterfaces!");
    }

    /**
     * Returns a {@link ControlledNetworkInterface} if a provided {@link NetworkInterface}
     * is linked to, {@link Optional#empty()} else
     *
     * @param networkInterface interface that should be linked to a {@link ControlledNetworkInterface}
     * @return {@link java.util.Optional} of {@link ControlledNetworkInterface}
     * associated to the provided {@link NetworkInterface}
     */
    private Optional<ControlledNetworkInterface> getControlledInterface(NetworkInterface networkInterface) {
        return interfaces.stream().filter(ni -> ni.getNetworkInterface() == networkInterface).findFirst();
    }

    /**
     * @return an unmodifiable Set of the currently existing {@link ControlledNetworkInterface}
     */
    public Set<ControlledNetworkInterface> getControlledNetworkInterfaces() {
        return Collections.unmodifiableSet(interfaces);
    }
}
