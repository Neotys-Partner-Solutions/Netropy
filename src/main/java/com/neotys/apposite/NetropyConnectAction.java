package com.neotys.apposite;

import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class NetropyConnectAction implements Action {
    public static final String Netropy_HOSTNAME = "Netropy_HOSTNAME";
    public static final String Netropy_USERNAME = "Netropy_USERNAME";
    public static final String Netropy_PASSWORD = "Netropy_PASSWORD";
    public static final String TIMEOUT = "TIMEOUT";
    private static final String BUNDLE_NAME = "com.neotys.apposite.bundle";
    private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayConnect");
    private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");

    private static final ImageIcon LOGO_ICON;

    @Override
    public String getType() {
        return "Netropy_Connect";
    }
    static {
        final URL iconURL = SetDelayAction.class.getResource("logo.png");
        if (iconURL != null) {
            LOGO_ICON = new ImageIcon(iconURL);
        }
        else {
            LOGO_ICON = null;
        }
    }
    @Override
    public List<ActionParameter> getDefaultActionParameters() {
        final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
        parameters.add(new ActionParameter(Netropy_HOSTNAME,"localhost"));
        parameters.add(new ActionParameter(Netropy_USERNAME,"user"));
        parameters.add(new ActionParameter(Netropy_PASSWORD,"password"));
        parameters.add(new ActionParameter(TIMEOUT,"20"));
             // TODO Add default parameters.
        return parameters;
    }

    @Override
    public Class<? extends ActionEngine> getEngineClass() {
        return null;
    }

    @Override
    public boolean getDefaultIsHit() {
        return false;
    }

    @Override
    public Icon getIcon() {
        return LOGO_ICON;
    }

    @Override
    public String getDescription() {
        final StringBuilder description = new StringBuilder();
        // TODO Add description
        description.append("Netropy_Connect will connect to the NetropyAppliance.\n");
        description.append("Netropy_Connect requires the following parameters\n");
        description.append("\tNetropy_HOSTNAME : HostName or Ip of the Netropy Appliance\n");
        description.append("\tNetropy_USERNAME : UserName to connect to the appliance\n");
        description.append("\tNetropy_PASSWORD : Password \n");
        description.append("\tTIMEOUT : maximum time is seconds allowed to connect to the appliance\n");



        return description.toString();
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public String getDisplayPath() {
        return DISPLAY_PATH;
    }

    @Override
    public Optional<String> getMinimumNeoLoadVersion() {
        return Optional.of("6.3");
    }

    @Override
    public Optional<String> getMaximumNeoLoadVersion() {
        return Optional.absent();
    }
}
