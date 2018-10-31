package com.neotys.apposite.Util;

import com.apposite.api.Netropy;
import com.apposite.api.NetropyException;
import com.google.common.base.Strings;
import com.neotys.apposite.NLAPIClient.NLClient;
import com.neotys.apposite.codec.NetroyMonitoringReader;
import com.neotys.rest.error.NeotysAPIException;
import org.apache.olingo.odata2.api.exception.ODataException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

public class NetropySession {
    private String hostname;
    private String username;
    private String password;
    private String engine_idx;
    private String engine_path;
    private Netropy netropy;
    private int timeout;
    public static final String NORMAL="NORMAL";
    public static final String UNIFORM="UNIFORM";
    public static final String CONSTANT="CONSTANT";
    public static final String EXPONENTIAL="EXPONENTIAL";
    public static final String NONE="NONE";
    public static final String RANDOM="RANDOM";
    public static final String BURST="BURST";
    public static final String PERIODIC="PERIODIC";
    private boolean emulationOn=false;

    public NetropySession(String hostname, String username, String password,int timeout) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.timeout=timeout;
        this.netropy=new Netropy(hostname,username,password,timeout);
    }

    public void enableEmulation() throws NetropyException {
       if(!emulationOn) {
           netropy.setEmulationOn(engine_idx);
           emulationOn = true;
       }
    }

    public boolean isEmulationON()
    {
        return emulationOn;
    }

    public void disableEmulation() throws NetropyException {
        if(emulationOn) {
            this.netropy.setEmulationOff(this.engine_idx);
            emulationOn = false;
        }
    }

    public void setEngineInfo(String engine_idx, String engine_path)
    {
        this.engine_idx=engine_idx;
        this.engine_path=engine_path;
    }

    public void disconnect() throws NetropyException {

        if(emulationOn)
            disableEmulation();

        this.netropy.disconnectNetropy();

    }

    public void setBandwidth(String value, String unit,String port) throws NetropyException {
        netropy.setBandwidth(engine_idx,engine_path,"fixed "+value+" "+unit,port,"out");
    }

    public void disableBandwidth(String port) throws NetropyException {
        netropy.setDelay(engine_idx, engine_path, "none", port,"out");

    }

    public void setNetworkOutage(String intervalMin,String intervalMax,String durationmin,String durationmax,String port1, String port2) throws NetropyException {
        String function;
        function="periodic "+intervalMin;
        if (!Strings.isNullOrEmpty(intervalMax)){
            function+="-"+intervalMax;
        }
        function+=" duration min "+durationmin;
        if (!Strings.isNullOrEmpty(durationmax)){
        function+="-"+durationmax;
    }

        netropy.setNetworkOutage(engine_idx,engine_idx,function,port1,port2);
    }

    public void disableNetworkOutage(String port1, String port2) throws NetropyException {
        netropy.setNetworkOutage(engine_idx,engine_path,"none",port1,port2);

    }

    public void setLoss(String lossType,String lossPercent,String lossMin,String lossMax,String lossPeriod,String losscount,String netropy_PORT_1,String netropy_PORT_2) throws NetropyException {

            switch (lossType)
            {
                case RANDOM:
                    netropy.setLoss(engine_idx,engine_path,"random "+lossPercent,netropy_PORT_1,netropy_PORT_2);
                    break;
                case BURST:
                    netropy.setLoss(engine_idx,engine_path,"burst "+lossPercent+" min "+lossMin+" max "+lossMax,netropy_PORT_1,netropy_PORT_2);
                    break;
                case PERIODIC:
                    String function;
                    function="periodic "+lossPeriod;
                    if (!Strings.isNullOrEmpty(losscount)) {
                        function+=" burst "+losscount;
                    }
                    netropy.setLoss(engine_idx,engine_path,function,netropy_PORT_1,netropy_PORT_2);
                    break;
            }
    }

    public void disableLoss(String port1, String port2) throws NetropyException {
        netropy.setLoss(engine_idx,engine_path,"none",port1,port2);

    }

    public void setDelay(String typeOfDeLay,String delay_value,String stDev,String minDelay,String maxDelay,String netropy_PORT_1,String netropy_PORT_2) throws NetropyException {
        switch (typeOfDeLay)
        {
            case NORMAL:
                netropy.setDelay(engine_idx,engine_path,"normal "+delay_value+" "+stDev,netropy_PORT_1,netropy_PORT_2);
                break;
            case CONSTANT:
                netropy.setDelay(engine_idx,engine_path,"constant "+delay_value,netropy_PORT_1,netropy_PORT_2);
                break;
            case EXPONENTIAL:
                netropy.setDelay(engine_idx,engine_path,"exponential "+minDelay+" "+maxDelay,netropy_PORT_1,netropy_PORT_2);
                break;
            case UNIFORM:
                netropy.setDelay(engine_idx,engine_path,"uniform "+minDelay+" "+maxDelay,netropy_PORT_1,netropy_PORT_2);
                break;
        }
    }

    public void monitorNetropy(String neoloadUrl,String neoloadapiKey,String engine) throws NetropyException, IOException, GeneralSecurityException, ODataException, NeotysAPIException, URISyntaxException, ParseException {
        NLClient client=new NLClient(neoloadUrl,neoloadapiKey);
        client.sendData(NetroyMonitoringReader.monitoringReader(netropy.getTotalStats(engine)));
    }
    public void disableDelay(String port1, String port2) throws NetropyException {
        netropy.setDelay(engine_idx,engine_path,"none",port1,port2);
    }
}
