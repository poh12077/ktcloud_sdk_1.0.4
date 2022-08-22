package com.solbox.delivery.ktcloudSDK;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

class ResponseParser {

//	static String statusCodeParser(String result) throws Exception {
//		JSONObject jsonResult = new JSONObject(result);
//		if (jsonResult.getInt("statusCode") == HttpURLConnection.HTTP_CREATED
//				|| jsonResult.getInt("statusCode") == HttpURLConnection.HTTP_OK
//				|| jsonResult.getInt("statusCode") == HttpURLConnection.HTTP_ACCEPTED) {
//			return jsonResult.getString("response");
//		} else {
//			// fail logic should be here
//			System.out.println( jsonResult.getInt("statusCode") );
//			throw new Exception();
//		}
//	}

    static String statusCodeParser(String result) throws Exception {
        JSONObject jsonResult = new JSONObject(result);
        if (400 <= jsonResult.getInt("statusCode") && jsonResult.getInt("statusCode") < 600) {
            throw new Exception();
        } else {
            return jsonResult.getString("response");
        }
    }

    static boolean statusCodeParserInDeletion(String result, String successLog, String failLog) throws Exception {
        JSONObject jsonResult = new JSONObject(result);
        if (400 <= jsonResult.getInt("statusCode") && jsonResult.getInt("statusCode") < 600) {
            //System.out.println( jsonResult.getString("response") );
            System.out.println(failLog);
            //throw new Exception();
            return false;
        } else {
            System.out.println(successLog);
            //System.out.println( jsonResult.getString("response") );
            return true;
        }
    }


//	static String getProjectIdFromToken(String result) throws Exception {
//		JSONObject jsonResult = new JSONObject(result);
//		if (jsonResult.getInt("statusCode") == HttpURLConnection.HTTP_CREATED
//				|| jsonResult.getInt("statusCode") == HttpURLConnection.HTTP_OK
//				|| jsonResult.getInt("statusCode") == HttpURLConnection.HTTP_ACCEPTED) {
//			return jsonResult.getString("projectID");
//		} else {
//			// fail logic should be here
//			System.out.println( jsonResult.getInt("statusCode") );
//			throw new Exception();
//		}
//	}

    static String getProjectIdFromToken(String result) throws Exception {
        JSONObject jsonResult = new JSONObject(result);
        if (400 <= jsonResult.getInt("statusCode") && jsonResult.getInt("statusCode") < 600) {
            // fail logic should be here
            System.out.println(jsonResult.getInt("statusCode"));
            throw new Exception();
        } else {
            return jsonResult.getString("projectID");
        }
    }

    static String VmCreateResponseParser(String response) throws JSONException {
        JSONObject fianlJsonObject = new JSONObject(response);
        JSONObject server = fianlJsonObject.getJSONObject("server");
        String ID = server.getString("id");
        return ID;
    }

    static String VmDetailResponseParser(String response) throws JSONException {
        JSONObject fianlJsonObject = new JSONObject(response);
        JSONObject server = fianlJsonObject.getJSONObject("server");
        JSONObject addresses = server.getJSONObject("addresses");
        JSONArray Private = addresses.getJSONArray("Private");
        String privateIP = "";
        for (int i = 0; i < Private.length(); i++) {
            JSONObject jsonObject = Private.getJSONObject(i);
            privateIP = jsonObject.getString("addr");
        }
        return privateIP;
    }

    static String volumeCreateResponseParser(String response) throws JSONException {
        JSONObject fianlJsonObject = new JSONObject(response);
        JSONObject volume = fianlJsonObject.getJSONObject("volume");
        String ID = volume.getString("id");
        return ID;
    }

    static String IPCreateResponseParser(String response) throws JSONException {
        JSONObject fianlJsonObject = new JSONObject(response);
        JSONObject nc_associateentpublicipresponse = fianlJsonObject.getJSONObject("nc_associateentpublicipresponse");
        String job_id = nc_associateentpublicipresponse.getString("job_id");
        return job_id;
    }

    static String PublicIPJobIDlookupParser(String response) throws JSONException {
        JSONObject fianlJsonObject = new JSONObject(response);
        JSONObject nc_associateentpublicipresponse = fianlJsonObject.getJSONObject("nc_queryasyncjobresultresponse");
        JSONObject result = nc_associateentpublicipresponse.getJSONObject("result");
        String IP_id = result.getString("id");
        return IP_id;
    }

    static String staticNATSettingResponseParser(String response) throws Exception {
        JSONObject fianlJsonObject = new JSONObject(response);
        JSONObject nc_enablestaticnatresponse = fianlJsonObject.getJSONObject("nc_enablestaticnatresponse");
        String staticNAT_ID = "";
        if (nc_enablestaticnatresponse.getBoolean("success") == true) {
            staticNAT_ID = nc_enablestaticnatresponse.getString("id");
            return staticNAT_ID;
        } else {
            throw new Exception();
        }
    }

    static String projectIDParser(String response) throws JSONException {
        JSONObject fianlJsonObject = new JSONObject(response);
        JSONObject token = fianlJsonObject.getJSONObject("token");
        JSONObject project = token.getJSONObject("project");
        String ID = project.getString("id");
        return ID;
    }

    static String firewallJobIdParser(String response) throws Exception {
        JSONObject fianlJsonObject = new JSONObject(response);
        JSONObject nc_createfirewallruleresponse = fianlJsonObject.getJSONObject("nc_createfirewallruleresponse");
        String jobId = nc_createfirewallruleresponse.getString("job_id");
        return jobId;
    }

    static String firewallIdParser(String response) throws JSONException {
        JSONObject fianlJsonObject = new JSONObject(response);
        JSONObject nc_queryasyncjobresultresponse = fianlJsonObject.getJSONObject("nc_queryasyncjobresultresponse");
        JSONObject result = nc_queryasyncjobresultresponse.getJSONObject("result");
        String firewallId = "";
        if (result.getBoolean("success") == true) {
            firewallId = result.getString("id");
            return firewallId;
        } else {
            return firewallId;
        }
    }

    static String lookupJobId(String jobId, String token, int timeout) throws Exception {
        String result = RestAPI.get(KTCloudOpenAPI.jobID_URL + jobId, token, timeout);
        String response = ResponseParser.statusCodeParser(result);
        return response;
    }

    static boolean lookupJobId(String jobId, String token, int timeout, int deletionCount, String log, int maximumWaitingTime, int requestCycle ) throws Exception {
        int waitingCount=0;

        while (true) {
            String result = RestAPI.get(KTCloudOpenAPI.jobID_URL + jobId, token, timeout);
            String response = ResponseParser.statusCodeParser(result);
            JSONObject fianlJsonObject = new JSONObject(response);
            JSONObject nc_queryasyncjobresultresponse = fianlJsonObject.getJSONObject("nc_queryasyncjobresultresponse");
            int state = nc_queryasyncjobresultresponse.getInt("state");
            if (state == 1) {
                KTCloudOpenAPI.LOGGER.trace(log + " has been deleted");
                return true;
            } else if (state == 0) {
                KTCloudOpenAPI.LOGGER.trace(waitingCount + " " + log + " deletion is in progress");
                waitingCount++;
                Thread.sleep(requestCycle * 1000);

            } else if (state == 2) {
                KTCloudOpenAPI.LOGGER.trace(deletionCount + " " + log + " deletion failed");
                return false;
            } else {
                KTCloudOpenAPI.LOGGER.trace(log + " error");
                return false;
            }

            if (maximumWaitingTime <= waitingCount) {
                System.out.print(log+" deletion has failed");
                return false;
            }

        }
    }


    static String lookupVmPrivateIp(String vmDetailUrl, String token, String vmId, int timeout) throws Exception {
        String result = RestAPI.get(vmDetailUrl + vmId, token, timeout);
        String response = ResponseParser.statusCodeParser(result);
        String vmPrivateIp = ResponseParser.VmDetailResponseParser(response);
        Etc.check(vmPrivateIp);
        return vmPrivateIp;
    }


}
