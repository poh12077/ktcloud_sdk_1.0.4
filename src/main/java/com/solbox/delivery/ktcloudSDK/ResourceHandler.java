package com.solbox.delivery.ktcloudSDK;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;


public class ResourceHandler {

    static void getToken() throws Exception {

    }

    static String getVm(String getVmUrl, String token, String serverName, String vmImageId, String specs, int timeout) throws Exception {
        String requestBody = RequestBody.getVm(serverName, vmImageId, specs);
        String result = RestAPI.post(getVmUrl, token, requestBody, timeout);
        String response = ResponseParser.statusCodeParser(result);
        String vmId = ResponseParser.VmCreateResponseParser(response);
        return vmId;
    }


    static String getVolume(String getVolumeUrl, String token, String volumeName, String volumeImageId, String projectId, int timeout) throws Exception {
        String requestBody = RequestBody.getVolume(volumeName, volumeImageId);
        String result = RestAPI.post(getVolumeUrl + projectId + "/volumes", token, requestBody, timeout);
        String response = ResponseParser.statusCodeParser(result);
        String volumeID = ResponseParser.volumeCreateResponseParser(response);
        return volumeID;
    }

    static void connectVmAndVolume(String connectVmAndVolumeUrl, String token, String vmId, String volumeId, int timeout) throws Exception {
        String requestBody = RequestBody.connectVmAndVolume(volumeId);
        String result = RestAPI.post(connectVmAndVolumeUrl + vmId + "/os-volume_attachments", token, requestBody, timeout);
        ResponseParser.statusCodeParser(result);
    }

    static String getPublicIp(String getPublicIpUrl, String token, int timeout) throws Exception {
        String publicIpJobId = "";
        String result = RestAPI.post(getPublicIpUrl, token, "", timeout);
        JSONObject fianlJsonObject = new JSONObject(result);
        String responseString = fianlJsonObject.getString("response");
        JSONObject response = new JSONObject(responseString);
        JSONObject nc_associateentpublicipresponse = response.getJSONObject("nc_associateentpublicipresponse");
        if (nc_associateentpublicipresponse.has("job_id")) {
            publicIpJobId = ResponseParser.IPCreateResponseParser(responseString);
            responseString = ResponseParser.lookupJobId(publicIpJobId, token, timeout);
            String publicIpId = ResponseParser.PublicIPJobIDlookupParser(responseString);
            return publicIpId;
        } else {
            System.out.println("public ip creation error");
            throw new Exception();
        }
    }

//    static String getPublicIp(String getPublicIpUrl, String token, int timeout) throws Exception {
//        String result = RestAPI.post(getPublicIpUrl, token, "", timeout);
//        String response = ResponseParser.statusCodeParser(result);
//        String publicIpJobId = ResponseParser.IPCreateResponseParser(response);
//        response = ResponseParser.lookupJobId(publicIpJobId, token, timeout);
//        String publicIpId = ResponseParser.PublicIPJobIDlookupParser(response);
//        return publicIpId;
//    }
//

    static String setStaticNat(String setStaticNatUrl, String token, String networkId, String vmPrivateIp, String publicIpId, int timeout) throws Exception {
        String requestBody = RequestBody.setStaticNat(vmPrivateIp, networkId, publicIpId);
        String result = RestAPI.post(setStaticNatUrl, token, requestBody, timeout);
        String response = ResponseParser.statusCodeParser(result);
        String staticNatId = ResponseParser.staticNATSettingResponseParser(response);
        return staticNatId;
    }

    static String openFirewall(String openFirewallUrl, String token, String startPort, String endPort, String staticNatId, String sourceNetworkId,
                               String destinationNetworkAddress, String protocol, String destinationNetworkId, int timeout) throws Exception {
        String requestBody = RequestBody.openFirewall(startPort, endPort, staticNatId, sourceNetworkId, destinationNetworkAddress, protocol, destinationNetworkId);
        String result = RestAPI.post(openFirewallUrl, token, requestBody, timeout);
        String response = ResponseParser.statusCodeParser(result);
        String firewallJobId = ResponseParser.firewallJobIdParser(response);
        return firewallJobId;
    }

    static boolean checkVmCreationStatus(String vmDetailUrl, String token, String vmId, int timeout, int maximumWaitingTime, int requestCycle) throws Exception {
        System.out.print("Server creation is in progress ");
        int count = 0;
        while (true) {
            String result = RestAPI.get(vmDetailUrl + vmId, token, timeout);
            String response = ResponseParser.statusCodeParser(result);
            JSONObject fianlJsonObject = new JSONObject(response);
            JSONObject server = fianlJsonObject.getJSONObject("server");
            int power_state = server.getInt("OS-EXT-STS:power_state");
            if (power_state == 1) {
                System.out.println("VM has been created");
                return true;
            }
            Thread.sleep(requestCycle * 1000);
            count++;
            System.out.print(count + " ");

            if (maximumWaitingTime <= count) {
                return false;
            }
        }
    }


    static boolean deleteVmOnly(String serverID, String token, int timeout) throws Exception {
        if (serverID.length() == 0) {
            return false;
        }
        String requestBody = RequestBody.forceDeleteVm();
        String result = RestAPI.post(KTCloudOpenAPI.forceDeleteVm_URL + serverID + "/action", token, requestBody,
                timeout);
        return ResponseParser.statusCodeParser(result, "Server deletion is in progress", "Server deletion failed");
//        JSONObject jsonResult = new JSONObject(result);
//        if (  100 <= jsonResult.getInt("statusCode") && jsonResult.getInt("statusCode") < 400) {
//            System.out.println("Server deletion is in progress");
//        } else {
//            System.out.println("Server deletion failed");
//        }
    }

    static boolean deleteVolume(String volumeID, String projectID, String token, int timeout, int maximumWaitingTime, int requestCycle) throws Exception {
        if (volumeID.length() == 0) {
            return false;
        }
        int count = 0;
        while (true) {
            String result = RestAPI.delete(KTCloudOpenAPI.deleteVolume_URL + projectID + "/volumes/" + volumeID, token,
                    timeout);
            JSONObject jsonResult = new JSONObject(result);
            if (100 <= jsonResult.getInt("statusCode") && jsonResult.getInt("statusCode") < 400) {
                System.out.println("Volume deletion is done");
                return true;
            } else {
                System.out.print(count + " ");
            }
            count++;
            Thread.sleep(requestCycle * 1000);

            if (maximumWaitingTime <= count) {
                System.out.print("Volume deletion error");
                return false;
            }
        }
    }

    static boolean deleteStaticNat(String staticNatId, String token, int timeout) throws Exception {
        if (staticNatId.length() == 0) {
            return false;
        }
        String result = RestAPI.delete(KTCloudOpenAPI.DeleteStaticNAT_URL + staticNatId, token, timeout);

        return ResponseParser.statusCodeParser(result, "static NAT has been disabled", "static NAT deletion has failed");
//        JSONObject jsonResult = new JSONObject(result);
//        if (100 <= jsonResult.getInt("statusCode") && jsonResult.getInt("statusCode") < 400) {
//            System.out.println("static NAT has been disabled");
//        } else {
//            System.out.println("static NAT deletion has failed");
//        }
    }

    static boolean deletePublicIp(String publicIpId, String token, int timeout) throws Exception {
        if (publicIpId.length() == 0) {
            return false;
        }
        String result = RestAPI.delete(KTCloudOpenAPI.deleteIP_URL + publicIpId, token, timeout);
        JSONObject fianlJsonObject = new JSONObject(result);
        String responseString = fianlJsonObject.getString("response");
        JSONObject response = new JSONObject(responseString);
        JSONObject nc_disassociateentpublicipresponse = response.getJSONObject("nc_disassociateentpublicipresponse");
        if (nc_disassociateentpublicipresponse.has("job_id")) {
            return true;
        } else {
            boolean isPublicIpDeleted = nc_disassociateentpublicipresponse.getBoolean("success");
            return isPublicIpDeleted;
        }
    }

    static boolean closeFirewall(String firewallJobId, String token, int timeout) throws Exception {
        if (firewallJobId.length() == 0) {
            return false;
        }
        String response = ResponseParser.lookupJobId(firewallJobId, token, timeout);
        String firewallId = ResponseParser.firewallIdParser(response);
        String result = RestAPI.delete(KTCloudOpenAPI.closeFirewall_URL + firewallId, token, timeout);
        return ResponseParser.statusCodeParser(result, "firewall has closed", "firewall still opened");
//        JSONObject jsonResult = new JSONObject(result);
//        if (100 <= jsonResult.getInt("statusCode") && jsonResult.getInt("statusCode") < 400) {
//            System.out.println("firewall has closed");
//        } else {
//            System.out.println("firewall still opened");
//        }
    }


}
