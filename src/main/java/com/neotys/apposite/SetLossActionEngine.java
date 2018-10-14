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

import java.util.List;

public class SetLossActionEngine implements ActionEngine {
    private String lossPercent;
    private String lossMin;
    private String lossMax;
    private String lossType;
    private String lossPeriod;
    private String losscount;

    private String netropy_HOSTNAME;
    private String netropy_ENGINE_IDX ;
    private String netropy_PATH_IDX ;
    private String netropy_PORT_1 ;
    private String netropy_PORT_2;
    private NetropySession netropy;

    private static final String RANDOM="RANDOM";
    private static final String NONE="NONE";
    private static final String BURST="BURST";
    private static final String PERIODIC="PERIODIC";


    @Override
    public SampleResult execute(Context context, List<ActionParameter> list)
    {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();

        for(ActionParameter parameter:list) {
            switch(parameter.getName()) {
                case SetLossAction.Losscount:
                    losscount = parameter.getValue();
                    break;
                case SetLossAction.LossMax:
                    lossMax = parameter.getValue();
                    break;
                case SetLossAction.LossMin:
                    lossMin = parameter.getValue();
                    break;
                case SetLossAction.LossPercent:
                    lossPercent = parameter.getValue();
                    break;
                case SetLossAction.Netropy_ENGINE_IDX:
                    netropy_ENGINE_IDX = parameter.getValue();
                    break;
                case SetLossAction.Netropy_PATH_IDX:
                    netropy_PATH_IDX = parameter.getValue();
                    break;
                case SetLossAction.LossType:
                    lossType = parameter.getValue();
                    break;
                case SetLossAction.LossPeriod:
                    lossPeriod = parameter.getValue();
                    break;
                case SetLossAction.Netropy_HOSTNAME:
                    netropy_HOSTNAME = parameter.getValue();
                    break;
                case SetLossAction.Netropy_PORT_1:
                    netropy_PORT_1 = parameter.getValue();
                    break;
                case SetLossAction.Netropy_PORT_2:
                    netropy_PORT_2 = parameter.getValue();
                    break;
            }
            if (Strings.isNullOrEmpty(netropy_HOSTNAME)) {
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_HOSTNAME cannot be null "
                        + SetLossAction.Netropy_HOSTNAME + ".", null);
            }
            if (Strings.isNullOrEmpty(netropy_ENGINE_IDX)) {
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_ENGINE_IDX cannot be null "
                        + SetLossAction.Netropy_ENGINE_IDX + ".", null);
            }
            else
            {
                if(!Validator.isaDigit(netropy_ENGINE_IDX))
                    return getErrorResult(context, sampleResult, "Invalid argument: Netropy_ENGINE_IDX needs to be a digit "
                            + SetLossAction.Netropy_ENGINE_IDX + ".", null);

            }
            if (Strings.isNullOrEmpty(netropy_PORT_1)) {
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_1 cannot be null "
                        + SetLossAction.Netropy_PORT_1 + ".", null);
            }
            else
            {
                if(!Validator.isaDigit(netropy_PORT_1))
                    return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_1 needs to be a digit "
                            + SetLossAction.Netropy_PORT_1 + ".", null);

            }
            if (Strings.isNullOrEmpty(netropy_PORT_2)) {
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_2 cannot be null "
                        + SetLossAction.Netropy_PORT_2 + ".", null);
            }
            else
            {
                if(!Validator.isaDigit(netropy_PORT_2))
                    return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_2 needs to be a digit "
                            + SetLossAction.Netropy_PORT_2 + ".", null);

            }



            if (Strings.isNullOrEmpty(lossType)) {
                return getErrorResult(context, sampleResult, "Invalid argument: LossType cannot be null "
                        + SetLossAction.LossType + ".", null);
            }
            else
            {
                switch (lossType)
                {
                    case NetropySession.RANDOM:
                        if (Strings.isNullOrEmpty(lossPercent)) {
                            return getErrorResult(context, sampleResult, "Invalid argument: LossPercent cannot be null "
                                    + SetLossAction.LossPercent + ".", null);
                        }
                        else
                        {
                            if(!Validator.isaDigit(lossPercent))
                                return getErrorResult(context, sampleResult, "Invalid argument: LossPercent needs to be a digit  "
                                        + SetLossAction.LossPercent + ".", null);

                        }
                        break;
                    case NetropySession.BURST:
                        if (Strings.isNullOrEmpty(lossPercent)) {
                            return getErrorResult(context, sampleResult, "Invalid argument: LossPercent cannot be null "
                                    + SetLossAction.LossPercent + ".", null);
                        }
                        else
                        {
                            if(!Validator.isaDigit(lossPercent))
                                return getErrorResult(context, sampleResult, "Invalid argument: LossPercent needs to be a digit  "
                                        + SetLossAction.LossPercent + ".", null);

                        }
                        if (Strings.isNullOrEmpty(lossMin)) {
                            return getErrorResult(context, sampleResult, "Invalid argument: LossMin cannot be null "
                                    + SetLossAction.LossMin + ".", null);
                        }
                        else
                        {
                            if(!Validator.isaDigit(lossMin))
                                return getErrorResult(context, sampleResult, "Invalid argument: LossMin needs to be a digit  "
                                        + SetLossAction.LossMin + ".", null);

                        }
                        if (Strings.isNullOrEmpty(lossMax)) {
                            return getErrorResult(context, sampleResult, "Invalid argument: LossMax cannot be null "
                                    + SetLossAction.LossMax + ".", null);
                        }
                        else
                        {
                            if(!Validator.isaDigit(lossMax))
                                return getErrorResult(context, sampleResult, "Invalid argument: LossMax needs to be a digit  "
                                        + SetLossAction.LossMax + ".", null);

                        }
                        break;
                    case NetropySession.PERIODIC:
                        if (Strings.isNullOrEmpty(lossPeriod)) {
                            return getErrorResult(context, sampleResult, "Invalid argument: LossPeriod cannot be null "
                                    + SetLossAction.LossPeriod + ".", null);
                        }
                        else
                        {
                            if(!Validator.isaDigit(lossPeriod))
                                return getErrorResult(context, sampleResult, "Invalid argument: LossPeriod needs to be a digit  "
                                        + SetLossAction.LossPeriod + ".", null);

                        }
                        if (!Strings.isNullOrEmpty(losscount)) {
                            if(!Validator.isaDigit(losscount))
                                return getErrorResult(context, sampleResult, "Invalid argument: Losscount needs to be a digit  "
                                        + SetLossAction.Losscount + ".", null);

                        }
                        break;
                    case NetropySession.NONE:
                        break;
                    default:
                        return getErrorResult(context, sampleResult, "Invalid argument: LossType can only be equal to NONE|RANDOM|BURST|PERIODIC "
                                + SetLossAction.LossType + ".", null);

                }
            }
            try
            {
                sampleResult.sampleStart();
                netropy=(NetropySession)context.getCurrentVirtualUser().get(Validator.netropyObjectname+netropy_HOSTNAME);
                if(netropy!=null)
                {
                    netropy.setEngineInfo(netropy_ENGINE_IDX,netropy_PATH_IDX);
                    netropy.enableEmulation();
                    if(!lossType.equalsIgnoreCase("NONE"))
                    {
                        netropy.setLoss(lossType,lossPercent,lossMin,lossMax,lossPeriod,losscount,netropy_PORT_1,netropy_PORT_2);
                        responseBuilder.append("Loss "+ lossType+ " has been applied between "+netropy_PORT_1 +" and "+netropy_PORT_2);
                    }
                    else
                    {
                        netropy.disableLoss(netropy_PORT_1,netropy_PORT_2);
                        responseBuilder.append("LOSS has been removed between port "+netropy_PORT_1 +" and "+ netropy_PORT_2);
                    }
                    sampleResult.sampleEnd();
                }
                else
                {
                    return getErrorResult(context, sampleResult, "Netropy connection Error: There is no existing connection on this appliance",null);
                }
            }
            catch (Exception e) {
                return getErrorResult(context, sampleResult, "Technical Error:", e);
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
        result.setStatusCode("NL-Netropy_SetLoss_ERROR");
        result.setResponseContent(errorMessage);
        if(exception != null){
            context.getLogger().error(errorMessage, exception);
        } else{
            context.getLogger().error(errorMessage);
        }
        return result;
    }
}
