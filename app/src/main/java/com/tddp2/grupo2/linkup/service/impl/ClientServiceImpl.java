package com.tddp2.grupo2.linkup.service.impl;

import com.tddp2.grupo2.linkup.infrastructure.LinkupClient;
import com.tddp2.grupo2.linkup.service.api.ClientService;



public class ClientServiceImpl implements ClientService{


    @Override
    public LinkupClient getClient() {
        LinkupClient linkupClient = new LinkupClient();
        linkupClient.build();
        return linkupClient;
    }
}
