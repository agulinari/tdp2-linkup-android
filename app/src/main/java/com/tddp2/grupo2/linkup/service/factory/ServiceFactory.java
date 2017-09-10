package com.tddp2.grupo2.linkup.service.factory;

import com.tddp2.grupo2.linkup.controller.LoginController;
import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.service.api.ClientService;
import com.tddp2.grupo2.linkup.service.api.LinkupService;
import com.tddp2.grupo2.linkup.service.api.LoginService;
import com.tddp2.grupo2.linkup.service.api.ProfileService;
import com.tddp2.grupo2.linkup.service.api.ServiceType;
import com.tddp2.grupo2.linkup.service.impl.ClientServiceImpl;
import com.tddp2.grupo2.linkup.service.impl.LoginServiceImpl;
import com.tddp2.grupo2.linkup.service.impl.ProfileServiceImpl;

import java.util.HashMap;
import java.util.Map;


public class ServiceFactory {

    private static Map<ServiceType, LinkupService> services = new HashMap<>();


    private ServiceFactory() {
    }

    public static void init(Database database) {

        //chatMapper = new MatchChatMapper();
        buildServices(database);
    }

    private static void buildServices(Database database) {
        buildRealServices(database);
        /*if (type.equals(Services.REAL)) {
            buildRealServices(database);
        } else {
            buildMockServices(database);
        }*/
    }

    private static void buildRealServices(Database database) {
        ClientService clientService = new ClientServiceImpl();
        ProfileService profileService = new ProfileServiceImpl(database, clientService);
        LoginService LoginService = new LoginServiceImpl(database, clientService);
        save(profileService);
        save(LoginService);
    }

    private static void buildMockServices() {

    }

    private static void save(LinkupService service) {
        services.put(service.getType(), service);
    }

    public static ProfileService getProfileService() {
        return (ProfileService) services.get(ServiceType.PROFILE);
    }

    public static LoginService getLoginService() {
        return (LoginService) services.get(ServiceType.LOGIN);
    }
}