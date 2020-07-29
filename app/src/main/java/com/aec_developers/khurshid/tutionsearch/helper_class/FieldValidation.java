package com.aec_developers.khurshid.tutionsearch.helper_class;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Khurshid on 1/27/2018.
 */

public class FieldValidation {

    //Regex patterns
    //Pattern for email validation
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    //email validation method
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    //phone validation
    public static boolean phoneValidate(String phone) {
        if (phone.matches("\\d{10}"))
            return true;
        else
            return false;
    }

    //username validation
    public static boolean usernameValidate(String username, String url) {

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        String encodedParams = HttpManager.getEncodedParams(map);
        String response = HttpManager.readWrite(encodedParams, url);
        String tResponse = response.trim();
        if (tResponse.equals("0"))
            return true;
        else
            return false;
    }

    //password validation
    public static boolean passwordValidate(String password) {
        return false;
    }

}
