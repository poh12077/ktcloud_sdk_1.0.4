package com.solbox.delivery.ktcloudSDK;

import org.json.JSONObject;

public class KTCloudOpenAPI {

    static final String getVm_URL = "https://api.ucloudbiz.olleh.com/d1/server/servers";
    static final String forceDeleteVm_URL = "https://api.ucloudbiz.olleh.com/d1/server/servers/";
    static final String VmList_URL = "https://api.ucloudbiz.olleh.com/d1/server/servers/detail";
    static final String VmDetail_URL = "https://api.ucloudbiz.olleh.com/d1/server/servers/";

    static final String getVolume_URL = "https://api.ucloudbiz.olleh.com/d1/volume/";
    static final String connectVmAndVolume_URL = "https://api.ucloudbiz.olleh.com/d1/server/servers/";
    static final String deleteAllVolume_URL = "https://api.ucloudbiz.olleh.com/d1/volume/";
    static final String deleteVolume_URL = "https://api.ucloudbiz.olleh.com/d1/volume/";
    static final String getIP_URL = "https://api.ucloudbiz.olleh.com/d1/nc/IpAddress";
    static final String deleteIP_URL = "https://api.ucloudbiz.olleh.com/d1/nc/IpAddress/";
    static final String IPList_URL = "https://api.ucloudbiz.olleh.com/d1/nc/IpAddress";

    static final String setStaticNAT_URL = "https://api.ucloudbiz.olleh.com/d1/nc/StaticNat";
    static final String staticNATList_URL = "https://api.ucloudbiz.olleh.com/d1/nc/StaticNat";
    static final String DeleteStaticNAT_URL = "https://api.ucloudbiz.olleh.com/d1/nc/StaticNat/";

    static final String openFirewall_URL = "https://api.ucloudbiz.olleh.com/d1/nc/Firewall";
    static final String firewall_List_URL = "https://api.ucloudbiz.olleh.com/d1/nc/Firewall";
    static final String closeFirewall_URL = "https://api.ucloudbiz.olleh.com/d1/nc/Firewall/";

    static final String getToken_URL = "https://api.ucloudbiz.olleh.com/d1/identity/auth/tokens";
    static final String jobID_URL = "https://api.ucloudbiz.olleh.com/d1/nc/Etc?command=queryAsyncJob&jobid=";

    static final String GET = "GET";
    static final String DELETE = "DELETE";
    static final String POST = "POST";
    //static final int timeout = 10; //sec

    public static ServerInformation createServer(String serverName, String serverImage, String specs, int timeout, String accountId, String accountPassword,
                                                 String networkId, String destinationNetworkId, String destinationNetworkAddress  ) throws Exception {
        try {
            String result;
            String protocol = "ALL";
            String startPort="0";
            String endPort="65535";
            String sourceNetworkId="6b812762-c6bc-4a6d-affb-c469af1b4342";

            //String VmImage_nginx = "fab16e16-5d53-4e00-892f-bec4b10079bb";

//            //read config
//            String confString = Etc.read(confPath);
//            JSONObject conf = new JSONObject(confString);
//            JSONObject http = conf.getJSONObject("http");
//            int timeout = http.getInt("timeout");
//            JSONObject image = conf.getJSONObject("image");
//            String VmImage_complete1 = image.getString("vm");
//            JSONObject specs = conf.getJSONObject("specs");
//            String core8_ram16 = specs.getString("core8_ram16");
//            //firewall parameter
//            JSONObject firewall = conf.getJSONObject("firewall");
//            String startPort = firewall.getString("startPort");
//            String endPort = firewall.getString("endPort");
//            String sourceNetworkId = firewall.getString("sourceNetworkId");
//            String destinationNetworkAddress = firewall.getString("destinationNetworkAddress");
//            String protocol = firewall.getString("protocol");
//            String destinationNetworkId = firewall.getString("destinationNetworkId");

            System.out.println("Server creation has started");

            ServerInformation serverInformation = new ServerInformation();
            serverInformation.initLog();
            // token
            result = RestAPI.post(getToken_URL, RequestBody.getToken(accountId, accountPassword), timeout);
            String token = ResponseParser.statusCodeParser(result);
            String projectId = ResponseParser.getProjectIdFromToken(result);
            serverInformation.setProjectID(projectId);
            String vmId = ResourceHandler.getVm(getVm_URL, token, serverName, serverImage, specs, timeout);
            serverInformation.setVmId(vmId);
            String publicIpId = ResourceHandler.getPublicIp(getIP_URL, token, timeout);
            serverInformation.setPublicIP_ID(publicIpId);
            boolean isVmCreated = ResourceHandler.checkVmCreationStatus(VmDetail_URL, token, vmId, timeout, 500, 1);
            String vmPrivateIp = "";
            if (isVmCreated) {
                vmPrivateIp = ResponseParser.lookupVmPrivateIp(VmDetail_URL, token, vmId, timeout);
            } else {
                System.out.println("vm creation error");
                deleteServer(serverInformation, timeout, accountId, accountPassword);
                throw new Exception();
            }
            String staticNatId = ResourceHandler.setStaticNat(setStaticNAT_URL, token, networkId, vmPrivateIp, publicIpId, timeout);
            serverInformation.setStaticNAT_ID(staticNatId);
            String firewallJobId = ResourceHandler.openFirewall(openFirewall_URL, token, startPort, endPort, staticNatId, sourceNetworkId,
                    destinationNetworkAddress, protocol, destinationNetworkId, timeout);
            serverInformation.setFirewallJobId(firewallJobId);

            System.out.println("server creation is done");
            return serverInformation;
        } catch (Exception e) {
            ServerInformation serverInformation = new ServerInformation();
            KTCloudOpenAPI.deleteServer(serverInformation, timeout, accountId, accountPassword);
            throw new Exception();
        }
    }

    public static ServerInformation createServer(String serverName, String volumeName, String serverImage, String volumeImage, String specs, int timeout, String accountId, String accountPassword,
                                                 String networkId, String destinationNetworkId, String destinationNetworkAddress) throws Exception {
        try {
            String result;
            String protocol = "ALL";
            String startPort="0";
            String endPort="65535";
            String sourceNetworkId="6b812762-c6bc-4a6d-affb-c469af1b4342";

            //String VmImage_nginx = "fab16e16-5d53-4e00-892f-bec4b10079bb";
//            //read conf
//            String confString = Etc.read(confPath);
//            JSONObject conf = new JSONObject(confString);
//            JSONObject http = conf.getJSONObject("http");
//            int timeout = http.getInt("timeout");
//            JSONObject image = conf.getJSONObject("image");
//            String VmImage_complete1 = image.getString("vm");
//            String volumeImageId = image.getString("volume");
//            JSONObject specs = conf.getJSONObject("specs");
//            String core8_ram16 = specs.getString("core8_ram16");
//            //firewall parameter
//            JSONObject firewall = conf.getJSONObject("firewall");
//            String startPort = firewall.getString("startPort");
//            String endPort = firewall.getString("endPort");
//            String sourceNetworkId = firewall.getString("sourceNetworkId");
//            String destinationNetworkAddress = firewall.getString("destinationNetworkAddress");
//            String protocol = firewall.getString("protocol");
//            String destinationNetworkId = firewall.getString("destinationNetworkId");

            System.out.println("Server creation has started");

            ServerInformation serverInformation = new ServerInformation();
            // token
            result = RestAPI.post(getToken_URL, RequestBody.getToken(accountId,accountPassword), timeout);
            String token = ResponseParser.statusCodeParser(result);
            String projectId = ResponseParser.getProjectIdFromToken(result);
            serverInformation.setProjectID(projectId);
            String vmId = ResourceHandler.getVm(getVm_URL, token, serverName, serverImage, specs, timeout);
            serverInformation.setVmId(vmId);
            String volumeId = ResourceHandler.getVolume(getVolume_URL, token, volumeName, volumeImage, projectId, timeout);
            serverInformation.setVolumeID(volumeId);
            String publicIpId = ResourceHandler.getPublicIp(getIP_URL, token, timeout);
            serverInformation.setPublicIP_ID(publicIpId);
            boolean isVmCreated = ResourceHandler.checkVmCreationStatus(VmDetail_URL, token, vmId, timeout, 500, 1);
            if (isVmCreated) {
                ResourceHandler.connectVmAndVolume(connectVmAndVolume_URL, token, vmId, volumeId, timeout);
            } else {
                System.out.println("vm creation error");
                deleteServer(serverInformation, timeout, accountId, accountPassword);
                throw new Exception();
            }

            String vmPrivateIp = ResponseParser.lookupVmPrivateIp(VmDetail_URL, token, vmId, timeout);
            String staticNatId = ResourceHandler.setStaticNat(setStaticNAT_URL, token, networkId, vmPrivateIp, publicIpId, timeout);
            serverInformation.setStaticNAT_ID(staticNatId);
            String firewallJobId = ResourceHandler.openFirewall(openFirewall_URL, token, startPort, endPort, staticNatId, sourceNetworkId,
                    destinationNetworkAddress, protocol, destinationNetworkId, timeout);
            serverInformation.setFirewallJobId(firewallJobId);

            System.out.println("server creation is done");
            return serverInformation;
        } catch (Exception e) {
            ServerInformation serverInformation = new ServerInformation();
            KTCloudOpenAPI.deleteServer(serverInformation, timeout,accountId,accountPassword);
            throw new Exception();
        }
    }


    public static String deleteServer(ServerInformation serverInformation, int timeout, String accountId, String accountPassword) throws Exception {
        //read conf
//        String confString = Etc.read(confPath);
//        JSONObject conf = new JSONObject(confString);
//        JSONObject http = conf.getJSONObject("http");
//        int timeout = http.getInt("timeout");
        serverInformation.initLog();
        System.out.println("Server deletion has started");
        // token
        String response = RestAPI.post(getToken_URL, RequestBody.getToken(accountId,accountPassword), 10);
        String token = ResponseParser.statusCodeParser(response);
        boolean isVmDeleleted = false;
        boolean isVolumeDeleleted = false;
        boolean isFirewallCloseed = false;
        boolean isStaticNatDisabled = false;
        boolean isPublicIpDeleleted = false;

        isVmDeleleted = ResourceHandler.deleteVmOnly(serverInformation.getVmId(), token, timeout);
        isVolumeDeleleted = ResourceHandler.deleteVolume(serverInformation.getVolumeID(), serverInformation.getProjectID(), token, timeout, 500, 1);
        isFirewallCloseed = ResourceHandler.closeFirewall(serverInformation.getFirewallJobId(), token, timeout);
        isStaticNatDisabled = ResourceHandler.deleteStaticNat(serverInformation.getStaticNAT_ID(), token, timeout);
        isPublicIpDeleleted = ResourceHandler.deletePublicIp(serverInformation.getPublicIP_ID(), token, timeout);

        System.out.println("server deletion is done");

        JSONObject result = new JSONObject();
        result.put("isVmDeleleted", isVmDeleleted);
        result.put("isPublicIpDeleleted", isPublicIpDeleleted);
        result.put("isFirewallCloseed", isFirewallCloseed);
        result.put("isStaticNatDisabled", isStaticNatDisabled);
        result.put("isVolumeDeleleted", isVolumeDeleleted);
        System.out.println(result);
        return result.toString();
    }

    public static void init(String confPath, String accountId, String accountPassword) throws Exception {
        //read conf
        String confString = Etc.read(confPath);
        JSONObject conf = new JSONObject(confString);
        JSONObject http = conf.getJSONObject("http");
        int timeout = http.getInt("timeout");

        String result;
        String response;
        result = RestAPI.request(getToken_URL, POST, RequestBody.getToken(accountId,accountPassword) );
        String token = ResponseParser.statusCodeParser(result);
        String projectID = ResponseParser.getProjectIdFromToken(result);

        // close firewall
        result = RestAPI.request(firewall_List_URL, GET, token, "");
        response = ResponseParser.statusCodeParser(result);
        Initialization.closeAllFirewall(response, token);

        // unlock static NAT
        result = RestAPI.request(staticNATList_URL, GET, token, "");
        response = ResponseParser.statusCodeParser(result);
        Initialization.deleteAllStaticNAT(response, token);

        // delete ip
        result = RestAPI.request(IPList_URL, GET, token, "");
        response = ResponseParser.statusCodeParser(result);
        Initialization.deleteAllIP(response, token);

        // delete vm
        result = RestAPI.request(VmList_URL, GET, token, "");
        response = ResponseParser.statusCodeParser(result);
        Initialization.deleteAllVm(response, token);

        //delete volume
        result = RestAPI.get(deleteAllVolume_URL + projectID + "/volumes/detail", token, timeout);
        response = ResponseParser.statusCodeParser(result);
        Initialization.deleteAllVolume(response, token, projectID, timeout);

        System.out.println("initialization is done");
    }

    public static void lookup() throws Exception {
    }

}
