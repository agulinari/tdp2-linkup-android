package com.tddp2.grupo2.linkup.service.factory;

import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.service.api.*;
import com.tddp2.grupo2.linkup.service.impl.*;

import java.util.HashMap;
import java.util.Map;


public class ServiceFactory {

    private static Map<ServiceType, LinkupService> services = new HashMap<>();


    private ServiceFactory() {
    }

    public static void init(Database database) {
        buildServices(database);
    }

    private static void buildServices(Database database) {
        buildRealServices(database);
    }

    private static void buildRealServices(Database database) {
        ClientService clientService = new ClientServiceImpl();
        ProfileService profileService = new ProfileServiceImpl(database, clientService);
        LoginService LoginService = new LoginServiceImpl(database, clientService);
        FacebookService facebookService = new FacebookServiceImpl(database, clientService);
        LinksService linksService = new LinksServiceImpl(database, clientService);
        MyLinksService myLinksService = new MyLinksServiceImpl(database, clientService);
        NotificationService notificationService = new NotificationServiceImpl(database, clientService);
        LinkUserService linkUserService = new LinkUserServiceImpl(database, clientService);
        save(profileService);
        save(LoginService);
        save(facebookService);
        save(linksService);
        save(myLinksService);
        save(notificationService);
        save(linkUserService);
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

    public static FacebookService getFacebookService() {
        return (FacebookService) services.get(ServiceType.FACEBOOK);
    }

    public static LinksService getLinksService() {
        return (LinksService) services.get(ServiceType.LINKS);
    }

    public static MyLinksService getMyLinksService() {
        return (MyLinksService) services.get(ServiceType.MY_LINKS);
    }

    public static NotificationService getNotificationService(){
        return (NotificationService) services.get(ServiceType.FCM);
    }

    public static LinkUserService getUserService() {
        return (LinkUserService) services.get(ServiceType.LINK_USER);
    }
}
