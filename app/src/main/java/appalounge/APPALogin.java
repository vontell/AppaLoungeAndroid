package appalounge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import resources.Constructors;
import resources.exceptions.*;
import resources.infrastructure.EasyApiComponent;

/**
 * Gives login authentication for the APPA Lounge API
 * @author Aaron Vontell
 * @date 10/16/2015
 * @version 0.1
 */
public class APPALogin implements EasyApiComponent {

    private String url = "";
    private String result;
    private String key;

    private String username;
    private String password;

    /**
     * Create a login object
     */
    public APPALogin() {

        url = AppaLounge.BASE_URL + "login/";

    }

    /**
     * Sets the username and password to use in the request
     * @param username The username to log in with
     * @param password The corresponding password
     */
    public void setParameters(String username, String password){

        this.username = username;
        this.password = password;

    }

    @Override
    public void downloadData() throws ApiException, AuthRequiredException, BadConnectionException, BadRequestException, DataNotSetException {

        String request = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        System.out.println(url);
        JSONObject response = Constructors.postData(url, request);
        try{
            result = response.toString();
            key = response.getString("key");
        } catch (Exception e){
            throw new ApiException("There was an error logging in");
        }

    }

    public String getKey(){
        if(key != null){
            return key;
        } else {
            return "";
        }
    }

    @Override
    public String getRawData() {
        return result;
    }
}
