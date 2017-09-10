package com.tddp2.grupo2.linkup.service.impl;

import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.FacebookService;

public class FacebookServiceImpl extends FacebookService {

    private ClientService clientService;

    public FacebookServiceImpl(Database database, ClientService clientService) {
        super(database);
        this.clientService = clientService;
    }
}
