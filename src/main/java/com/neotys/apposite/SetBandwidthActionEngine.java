package com.neotys.apposite;

import com.apposite.api.Netropy;
import com.apposite.api.NetropyException;
import com.google.common.base.Strings;
import com.neotys.apposite.Util.NetropySession;
import com.neotys.apposite.Util.Validator;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

import java.util.Arrays;
import java.util.List;

public class SetBandwidthActionEngine implements ActionEngine {
    private String bandwidthValue;
    private String bandwidthUnit ;
    private boolean bandwidthLimitation_Enabled;
    private String netropy_HOSTNAME;
    private String netropy_ENGINE_IDX;
    private String netropy_PATH_IDX;
    private String netropy_PORT;
    private NetropySession netropy;

    @Override
    public SampleResult execute(Context context, List<ActionParameter> list) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();
        String vSetBandwidth_enabled = null;

        for(ActionParameter parameter:list) {
            switch(parameter.getName()) {
                case SetBandwidthAction.BandwidthLimitation_Enabled:
                    vSetBandwidth_enabled = parameter.getValue();
                    break;
                case SetBandwidthAction.BandwidthUnit:
                    bandwidthUnit = parameter.getValue();
                    break;
                case SetBandwidthAction.BandwidthValue:
                    bandwidthValue = parameter.getValue();
                    break;
                case SetBandwidthAction.Netropy_ENGINE_IDX:
                    netropy_ENGINE_IDX = parameter.getValue();
                    break;
                case SetBandwidthAction.Netropy_PATH_IDX:
                    netropy_PATH_IDX = parameter.getValue();
                    break;
                case SetBandwidthAction.Netropy_HOSTNAME:
                    netropy_HOSTNAME = parameter.getValue();
                    break;
                case SetBandwidthAction.Netropy_PORT:
                    netropy_PORT = parameter.getValue();
                    break;
            }


            if (Strings.isNullOrEmpty(netropy_HOSTNAME)) {
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_HOSTNAME cannot be null "
                        + SetBandwidthAction.Netropy_HOSTNAME + ".", null);
            }
            if (Strings.isNullOrEmpty(netropy_ENGINE_IDX)) {
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_ENGINE_IDX cannot be null "
                        + SetBandwidthAction.Netropy_ENGINE_IDX + ".", null);
            }
            else
            {
                if(!Validator.isaDigit(netropy_ENGINE_IDX))
                    return getErrorResult(context, sampleResult, "Invalid argument: Netropy_ENGINE_IDX needs to be a digit "
                            + SetBandwidthAction.Netropy_ENGINE_IDX + ".", null);

            }
            if (Strings.isNullOrEmpty(netropy_PATH_IDX)) {
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PATH_IDX cannot be null "
                        + SetBandwidthAction.Netropy_PATH_IDX + ".", null);
            }
            if (Strings.isNullOrEmpty(netropy_PORT)) {
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT cannot be null "
                        + SetBandwidthAction.Netropy_PORT + ".", null);
            }
            else
            {
                if(!Validator.isaDigit(netropy_PORT))
                    return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT needs to be a digit "
                            + SetBandwidthAction.Netropy_PORT + ".", null);

            }
            if (Strings.isNullOrEmpty(vSetBandwidth_enabled)) {
                return getErrorResult(context, sampleResult, "Invalid argument: BandwidthLimitation_Enabled cannot be null "
                        + SetBandwidthAction.BandwidthLimitation_Enabled + ".", null);
            }
            else
            {
                if(!Validator.isaBoolan(vSetBandwidth_enabled))
                    return getErrorResult(context, sampleResult, "Invalid argument: BandwidthLimitation_Enabled needs to be a boolean "
                            + SetBandwidthAction.BandwidthLimitation_Enabled + ".", null);
                else
                {
                    bandwidthLimitation_Enabled=Validator.getBooleanValue(vSetBandwidth_enabled);
                    if(bandwidthLimitation_Enabled)
                    {
                        if (Strings.isNullOrEmpty(bandwidthValue)) {
                            return getErrorResult(context, sampleResult, "Invalid argument: BandwidthValue cannot be null "
                                    + SetBandwidthAction.BandwidthValue + ".", null);
                        }
                        else
                        {
                            if(!Validator.isaDigit(bandwidthValue))
                                return getErrorResult(context, sampleResult, "Invalid argument: BandwidthValue needs to be a digit "
                                        + SetBandwidthAction.BandwidthValue + ".", null);
                        }

                        if (Strings.isNullOrEmpty(bandwidthUnit)) {
                            return getErrorResult(context, sampleResult, "Invalid argument: BandwidthUnit cannot be null "
                                    + SetBandwidthAction.BandwidthUnit + ".", null);
                        }
                        else
                        {
                            //---check if the unit respect the unit list-----
                            if(!Validator.isaUnit(bandwidthUnit))
                                return getErrorResult(context, sampleResult, "Invalid argument: BandwidthUnit needs to be equal to bps|Kbps|Mbps|Gbps "
                                        + SetBandwidthAction.BandwidthUnit + ".", null);

                        }

                    }

                }
            }
            try {
                netropy = (NetropySession) context.getCurrentVirtualUser().get(Validator.netropyObjectname + netropy_HOSTNAME);
                sampleResult.sampleStart();
                if (netropy != null) {
                    netropy.setEngineInfo(netropy_ENGINE_IDX,netropy_PATH_IDX);

                    netropy.enableEmulation();
                    if(bandwidthLimitation_Enabled)
                    {
                        netropy.setBandwidth(bandwidthValue,bandwidthUnit,netropy_PORT);
                        responseBuilder.append("Bandwidth set on port "+netropy_PORT + "fixed "+bandwidthValue+" "+bandwidthUnit);
                    }
                    else
                    {
                        netropy.disableBandwidth(netropy_PORT);
                        responseBuilder.append("Bandwidth limitation removed");
                    }
                    sampleResult.sampleEnd();
                }
                else
                {
                    return getErrorResult(context, sampleResult, "Netropy connection Error: There is no existing connection on this appliance",null);
                }
            }
            catch(Exception e)
            {
                return getErrorResult(context, sampleResult, "Technical Error: ", e);

            }
        }
        sampleResult.setRequestContent(requestBuilder.toString());
        sampleResult.setResponseContent(responseBuilder.toString());
        return sampleResult;
    }

    @Override
    public void stopExecute() {

        if(netropy!=null)
        {
            try {
                netropy.disconnect();
            } catch (NetropyException e) {
                e.printStackTrace();
            }

        }
    }

    private void appendLineToStringBuilder(final StringBuilder sb, final String line){
        sb.append(line).append("\n");
    }

    /**
     * This method allows to easily create an error result and log exception.
     */
    private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
        result.setError(true);
        result.setStatusCode("NL-Netropy_SetBandwidth_ERROR");
        result.setResponseContent(errorMessage);
        if(exception != null){
            context.getLogger().error(errorMessage, exception);
        } else{
            context.getLogger().error(errorMessage);
        }
        return result;
    }
}
