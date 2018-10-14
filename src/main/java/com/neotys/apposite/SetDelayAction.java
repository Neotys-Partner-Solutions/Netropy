package com.neotys.apposite;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;

import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

public final class SetDelayAction implements Action{
	private static final String BUNDLE_NAME = "com.neotys.apposite.bundle";
	private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displaySetDelay");
	private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");
	public static final String Netropy_HOSTNAME = "Netropy_HOSTNAME";
	public static final String Netropy_ENGINE_IDX = "Netropy_ENGINE_IDX";
	public static final String Netropy_PATH_IDX = "Netropy_PATH_IDX";
	public static final String Netropy_PORT_1 = "Netropy_PORT_1";
	public static final String Netropy_PORT_2 = "Netropy_PORT_2";
	public static final String DelayValue = "DelayValue";
	public static final String TypeOfDelay = "TypeOfDelay";
	public static final String MinDelay = "MinDelay";
	public static final String MaxDelay = "MaxDelay";
	public static final String Stdev = "Stdev";



	private static final ImageIcon LOGO_ICON;

	@Override
	public String getType() {
		return "Netropy_SetDelay";
	}

	@Override
	public List<ActionParameter> getDefaultActionParameters() {
		final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
		parameters.add(new ActionParameter(Netropy_HOSTNAME,"URL of your WPT Server"));
		parameters.add(new ActionParameter(Netropy_ENGINE_IDX,"location:browser.connectivity"));
		parameters.add(new ActionParameter(Netropy_PATH_IDX,"2"));
		parameters.add(new ActionParameter(Netropy_PORT_1,"2"));
		parameters.add(new ActionParameter(Netropy_PORT_2,"2"));
		parameters.add(new ActionParameter(TypeOfDelay,"CONSTANT"));
		parameters.add(new ActionParameter(DelayValue,"20"));
		// TODO Add default parameters.
		return parameters;
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
	public Class<? extends ActionEngine> getEngineClass() {
		return SetDelayActionEngine.class;
	}

	@Override
	public Icon getIcon() {
		// TODO Add an icon
		return LOGO_ICON;
	}

	@Override
	public boolean getDefaultIsHit(){
		return false;
	}

	@Override
	public String getDescription() {
		final StringBuilder description = new StringBuilder();
		// TODO Add description
		description.append("Netropy_SetDelay will set a delay from port 1 to port 2.\n");
		description.append("Netropy_SetDelay requires the following parameters\n");
		description.append("\tNetropy_HOSTNAME : Url of your WPT Server\n");
		description.append("\tNetropy_ENGINE_IDX : Optionnal WPT api Key\n");
		description.append("\tNetropy_PATH_IDX : testing url to send to WPT\n");
		description.append("\tNetropy_PORT_1 : WPT Location used for the test execution\n");
		description.append("\tNetropy_PORT_2 : number of execution\n");
		description.append("\tTypeOfDelay : type of delay algorithm. Value Possible : NORMAL,UNIFORM,CONSTANT,EXPONENTIAL,NONE\n");
		description.append("\tDelayValue : (Optionnal) Value of the delay. Parameter required for the delay types : NORMAL,CONSTANT\n");
		description.append("\tMinDelay : (Optionnal) min value of the delay, value required for UNIFORM, EXPONENTIAL ");
		description.append("\tMaxDelay : (Optionnal) max value of the delay, value required for UNIFORM, EXPONENTIAL");
		description.append("\tStdev : (Optionnal) standard deviation of the delay, value required for NORMAL ");


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
