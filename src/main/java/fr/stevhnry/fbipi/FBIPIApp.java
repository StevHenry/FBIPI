package fr.stevhnry.fbipi;

import fr.stevhnry.fbipi.networkmanager.ControlledInterfaceManager;
import fr.stevhnry.fbipi.networkmanager.ControlledNetworkInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FBIPIApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(FBIPIApp.class);

    public static void main(String[] args) throws SocketException {
        new FBIPIApp();
    }

    
    ControlledInterfaceManager manager;

    public FBIPIApp() throws SocketException {
        manager = new ControlledInterfaceManager();

        Set<ControlledNetworkInterface> interfaces = manager.getControlledNetworkInterfaces();
        LOGGER.debug("Network interfaces count: {}", interfaces.size());

        List<ControlledNetworkInterface> used = interfaces.stream().filter(ControlledNetworkInterface::isCurrentlyUsed).collect(Collectors.toList());
        LOGGER.debug("Used network interfaces: {}", used.size());

        used.forEach(this::displayInterfaceInformation);
    }
    
    private void displayInterfaceInformation(ControlledNetworkInterface controlledNetworkInterface) {
        LOGGER.info("----");
        LOGGER.info("NAME: {}", controlledNetworkInterface.getNetworkInterface().getName());
        LOGGER.info("HOSTNAMES: {}", controlledNetworkInterface.getHostNames());
        LOGGER.info("ADDRESSES: {}", controlledNetworkInterface.getAddresses());
        LOGGER.info("LOCAL ADDRESS: {}", controlledNetworkInterface.hasLocalAddress() ?
                controlledNetworkInterface.getLocalAddress().get() : "NONE");
        LOGGER.info("MAC ADDRESS: {}", controlledNetworkInterface.hasPhysicalAddress() ?
                controlledNetworkInterface.getPhysicalAddress().get() : "NONE");
    }
}
