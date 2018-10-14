package com.neotys.apposite;

import java.util.List;

import com.apposite.api.Netropy;
import com.apposite.api.NetropyException;
import com.google.common.base.Strings;
import com.neotys.apposite.NLAPIClient.NLClient;
import com.neotys.apposite.Util.NetropySession;
import com.neotys.apposite.Util.Validator;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

public final class SetDelayActionEngine implements ActionEngine {
	private String netropy_HostName;
	private String netropy_Engine_IDX;
	private String netropy_Path_IDX;
	private String netropy_PORT_1;
	private String netropy_PORT_2;
	private String delay_value;
	private String typeOfDeLay;
	private String minDelay;
	private String maxDelay;
	private String stDev;
	private NetropySession netropy;
	private static final String NORMAL="NORMAL";
	private static final String UNIFORM="UNIFORM";
	private static final String CONSTANT="CONSTANT";
	private static final String EXPONENTIAL="EXPONENTIAL";
	private static final String NONE="NONE";
	@Override
	public SampleResult execute(Context context, List<ActionParameter> parameters) {
		final SampleResult sampleResult = new SampleResult();
		final StringBuilder requestBuilder = new StringBuilder();
		final StringBuilder responseBuilder = new StringBuilder();

		for(ActionParameter parameter:parameters) {
			switch(parameter.getName()) {
				case SetDelayAction.DelayValue:
					delay_value = parameter.getValue();
					break;
				case SetDelayAction.MaxDelay:
					maxDelay = parameter.getValue();
					break;
				case SetDelayAction.MinDelay:
					minDelay = parameter.getValue();
					break;
				case SetDelayAction.Stdev:
					stDev = parameter.getValue();
					break;
				case SetDelayAction.Netropy_ENGINE_IDX:
					netropy_Engine_IDX = parameter.getValue();
					break;
				case SetDelayAction.Netropy_PATH_IDX:
					netropy_Path_IDX = parameter.getValue();
					break;
				case SetDelayAction.TypeOfDelay:
					typeOfDeLay = parameter.getValue();
					break;
				case SetDelayAction.Netropy_HOSTNAME:
					netropy_HostName = parameter.getValue();
					break;
				case SetDelayAction.Netropy_PORT_1:
					netropy_PORT_1 = parameter.getValue();
					break;
				case SetDelayAction.Netropy_PORT_2:
					netropy_PORT_2 = parameter.getValue();
					break;
			}
		}
		if (Strings.isNullOrEmpty(netropy_HostName)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Netropy_HOSTNAME cannot be null "
					+ SetDelayAction.Netropy_HOSTNAME + ".", null);
		}
		if (Strings.isNullOrEmpty(netropy_Engine_IDX)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Netropy_ENGINE_IDX cannot be null "
					+ SetDelayAction.Netropy_ENGINE_IDX + ".", null);
		}
		else
		{
			if(!Validator.isaDigit(netropy_Engine_IDX))
				return getErrorResult(context, sampleResult, "Invalid argument: Netropy_ENGINE_IDX needs to be a digit "
						+ SetDelayAction.Netropy_ENGINE_IDX + ".", null);

		}
		if (Strings.isNullOrEmpty(netropy_Path_IDX)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PATH_IDX cannot be null "
					+ SetDelayAction.Netropy_PATH_IDX + ".", null);
		}
		if (Strings.isNullOrEmpty(netropy_PORT_1)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_1 cannot be null "
					+ SetDelayAction.Netropy_PORT_1 + ".", null);
		}
		else
		{
			if(!Validator.isaDigit(netropy_PORT_1))
				return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_1 needs to be a digit "
						+ SetDelayAction.Netropy_PORT_1 + ".", null);

		}
		if (Strings.isNullOrEmpty(netropy_PORT_2)) {
			return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_2 cannot be null "
					+ SetDelayAction.Netropy_PORT_2 + ".", null);
		}
		else
		{
			if(!Validator.isaDigit(netropy_PORT_2))
				return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_2 needs to be a digit "
						+ SetDelayAction.Netropy_PORT_2 + ".", null);

		}
		if (Strings.isNullOrEmpty(typeOfDeLay)) {
			return getErrorResult(context, sampleResult, "Invalid argument: TypeOfDelay cannot be null "
					+ SetDelayAction.TypeOfDelay + ".", null);
		}


		switch (typeOfDeLay)
		{

			case  NetropySession.NORMAL:
				if (Strings.isNullOrEmpty(delay_value)) {
					return getErrorResult(context, sampleResult, "Invalid argument: delay_value cannot be null "
							+ SetDelayAction.DelayValue + ".", null);
				}
				else
				{
					if(Validator.isaDigit(delay_value))
						return getErrorResult(context, sampleResult, "Invalid argument: delay_value needs to be a digit "
								+ SetDelayAction.DelayValue + ".", null);

				}
				if (Strings.isNullOrEmpty(stDev)) {
					return getErrorResult(context, sampleResult, "Invalid argument: Stdev cannot be null "
							+ SetDelayAction.Stdev + ".", null);
				}
				else
				{
					if(!Validator.isaDigit(stDev))
						return getErrorResult(context, sampleResult, "Invalid argument: Stdev needs to be a digit "
								+ SetDelayAction.Stdev + ".", null);

				}
				break;
			case NetropySession.CONSTANT:
				if (Strings.isNullOrEmpty(delay_value)) {
					return getErrorResult(context, sampleResult, "Invalid argument: delay_value cannot be null "
							+ SetDelayAction.DelayValue + ".", null);
				}
				else
				{
					if(Validator.isaDigit(delay_value))
						return getErrorResult(context, sampleResult, "Invalid argument: delay_value needs to be a digit "
								+ SetDelayAction.DelayValue + ".", null);

				}
				break;
			case NetropySession.EXPONENTIAL:
				if (Strings.isNullOrEmpty(minDelay)) {
					return getErrorResult(context, sampleResult, "Invalid argument: MinDelay cannot be null "
							+ SetDelayAction.MinDelay + ".", null);
				}

				else
				{
					if(!Validator.isaDigit(minDelay))
						return getErrorResult(context, sampleResult, "Invalid argument: MinDelay needs to be digit "
								+ SetDelayAction.MinDelay + ".", null);

				}
				if (Strings.isNullOrEmpty(maxDelay)) {
					return getErrorResult(context, sampleResult, "Invalid argument: MaxDelay cannot be null "
							+ SetDelayAction.MaxDelay + ".", null);
				}
				else
				{
					if(!Validator.isaDigit(maxDelay))
						return getErrorResult(context, sampleResult, "Invalid argument: MaxDelay needs to be digit "
								+ SetDelayAction.MaxDelay + ".", null);

				}
				break;
			case NetropySession.UNIFORM:
				if (Strings.isNullOrEmpty(minDelay)) {
					return getErrorResult(context, sampleResult, "Invalid argument: MinDelay cannot be null "
							+ SetDelayAction.MinDelay + ".", null);
				}
				else
				{
					if(!Validator.isaDigit(minDelay))
						return getErrorResult(context, sampleResult, "Invalid argument: MinDelay needs to be digit "
								+ SetDelayAction.MinDelay + ".", null);

				}
				if (Strings.isNullOrEmpty(maxDelay)) {
					return getErrorResult(context, sampleResult, "Invalid argument: MaxDelay cannot be null "
							+ SetDelayAction.MaxDelay + ".", null);
				}
				else
				{
					if(!Validator.isaDigit(maxDelay))
						return getErrorResult(context, sampleResult, "Invalid argument: MaxDelay needs to be digit "
								+ SetDelayAction.MaxDelay + ".", null);

				}
				break;
			case NetropySession.NONE:
				break;
			default:
				return getErrorResult(context, sampleResult, "Invalid argument: TypeOfDelay can be equal to : NONE,UNIFORM,CONSTANT,NORMAL,EXPONENTIAL  "
							+ SetDelayAction.TypeOfDelay + ".", null);


		}
		try
		{
			sampleResult.sampleStart();
			netropy=(NetropySession)context.getCurrentVirtualUser().get(Validator.netropyObjectname+netropy_HostName);
			if(netropy!=null) {
				netropy.setEngineInfo(netropy_Engine_IDX,netropy_Path_IDX);
				netropy.enableEmulation();
				if(typeOfDeLay.equalsIgnoreCase("NONE"))
				{
					netropy.setDelay(typeOfDeLay,delay_value,stDev,minDelay,maxDelay,netropy_PORT_1,netropy_PORT_2);
					responseBuilder.append("Delay "+typeOfDeLay +" applied on port " + netropy_PORT_1+" and "+ netropy_PORT_2);
				}
				else
				{
					netropy.disableDelay(netropy_PORT_1,netropy_PORT_2);
					requestBuilder.append("Delay removed");
				}
			}
			else
				return getErrorResult(context, sampleResult, "Netropy connection Error: There is no existing connection on this appliance",null);

			sampleResult.sampleEnd();
		}
		catch(Exception e)
		{
			return getErrorResult(context, sampleResult, "Technical Error : "
					+ e.getMessage()+ ".", e);

		}


		sampleResult.setRequestContent(requestBuilder.toString());
		sampleResult.setResponseContent(responseBuilder.toString());
		return sampleResult;
	}

	private void appendLineToStringBuilder(final StringBuilder sb, final String line){
		sb.append(line).append("\n");
	}

	/**
	 * This method allows to easily create an error result and log exception.
	 */
	private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
		result.setError(true);
		result.setStatusCode("NL-Netropy_SetDelay_ERROR");
		result.setResponseContent(errorMessage);
		if(exception != null){
			context.getLogger().error(errorMessage, exception);
		} else{
			context.getLogger().error(errorMessage);
		}
		return result;
	}

	@Override
	public void stopExecute() {
		// TODO add code executed when the test have to stop.
		if(netropy!=null)
		{
			try {
				netropy.disconnect();
			} catch (NetropyException e) {
				e.printStackTrace();
			}
		}
	}

}
