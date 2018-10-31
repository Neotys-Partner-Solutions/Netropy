package com.neotys.apposite.NLAPIClient;

import com.neotys.apposite.data.NetropyData;
import com.neotys.rest.dataexchange.client.DataExchangeAPIClient;
import com.neotys.rest.dataexchange.client.DataExchangeAPIClientFactory;
import com.neotys.rest.dataexchange.model.ContextBuilder;
import com.neotys.rest.dataexchange.model.Entry;
import com.neotys.rest.dataexchange.model.EntryBuilder;
import com.neotys.rest.error.NeotysAPIException;

import org.apache.olingo.odata2.api.exception.ODataException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hrexed on 01/06/18.
 */
public class NLClient {
    private DataExchangeAPIClient client;
    private ContextBuilder Context;
    private String NLUrl;
    private String NLApiKey;
    private static final String NETROPY="Netropy";
    private static final String NETROPY_Location="Netropy_Appliance";
    private static final String NETROPY_Script="Netropy_NL_integration";
    public NLClient( String NLUrl, String NLApiKey) throws URISyntaxException, GeneralSecurityException, ODataException, NeotysAPIException, IOException {
        this.NLUrl = NLUrl;
        this.NLApiKey = NLApiKey;

        Context = new ContextBuilder();
        Context.hardware(NETROPY).location(NETROPY_Location).software(NETROPY)

                .script(NETROPY_Script + System.currentTimeMillis());

        client = DataExchangeAPIClientFactory.newClient(NLUrl, Context.build(), NLApiKey);


    }
    public void sendData(List<NetropyData> dataList) throws GeneralSecurityException, IOException, NeotysAPIException, ParseException, URISyntaxException {
        List<Entry> entryList=dataList.stream().map(data->  generateEntry(data)).collect(Collectors.toList());
        client.addEntries(entryList);
    }

    private Entry generateEntry(NetropyData data) {

        EntryBuilder entry=new EntryBuilder(Arrays.asList(NETROPY, data.getPaths(), data.getTimeIdx(),data.getFrom(),data.getType()));
        if(data.getUnit()!=null)
            entry.unit(data.getUnit());
        else
            entry.unit("");
        entry.url("");
        entry.value(Double.valueOf(data.getValue()));

        return entry.build();
    }




}
