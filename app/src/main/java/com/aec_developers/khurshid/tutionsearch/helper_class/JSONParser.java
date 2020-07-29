package com.aec_developers.khurshid.tutionsearch.helper_class;

import com.aec_developers.khurshid.tutionsearch.model.Ad;
import com.aec_developers.khurshid.tutionsearch.model.Student;
import com.aec_developers.khurshid.tutionsearch.model.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khurshid on 1/27/2018.
 */

public class JSONParser {

    //for student
    private static final String ID = "s_id";
    private static final String NAME = "s_name";
    private static final String AGE = "s_age";
    private static final String GENDER = "s_gender";
    private static final String MOBILE = "s_mobile";
    private static final String EMAIL = "s_email";
    private static final String CITY = "city";
    private static final String STATE = "state";

    //for teacher
    private static final String TID = "tid";
    private static final String TNAME = "tname";
    private static final String TAGE = "tage";
    private static final String TGENDER = "tgender";
    private static final String TEMAIL = "temail";
    private static final String TMOBILE = "tphone";
    private static final String TQUALIFICATION = "tqualification";
    private static final String TFIELD = "tfield";
    private static final String TSUBJECT = "tsubject";
    private static final String TSPECIFIC_SUBJECT = "tspecific_subject";
    private static final String TCLASS = "tclass";
    private static final String TFEE = "tfee";
    private static final String TVERIFIED = "t_verified";
    private static final String TCITY = "t_city";
    private static final String TSTATE = "t_state";
    private static final String TLANG1 = "tlang_one";
    private static final String TLANG2 = "tlang_two";
    private static final String TLANG3 = "tlang_three";

    //ads
    private static final String AID = "aid";
    private static final String ATITLE = "atitle";
    private static final String ADESCRIPTION = "adescription";
    private static final String AADDRESS = "aaddress";
    private static final String AEMAIL = "aemail";
    private static final String AMOBILE1 = "aphone1";
    private static final String AMOBILE2 = "aphone2";
    private static final String AMOBILE3 = "aphone3";
    private static final String AMOBILE4 = "aphone4";
    private static final String ACITY = "acity";
    private static final String ASTATE = "astate";
    private static final String AIMAGEURL = "aimageurl";


    //new teacher
    private static final String TTID = "id";
    private static final String TTNAME = "name";
    private static final String TTAGE = "age";
    private static final String TTGENDER = "gender";
    private static final String TTEMAIL = "email";
    private static final String TTMOBILE = "mobile";
    private static final String TTQUALIFICATION = "qualification";
    private static final String TTFIELD = "field";
    private static final String TTSPECIFIC_SUBJECT = "subject";
    private static final String TTFEE = "fee";
    private static final String TTCITY = "city";
    private static final String TTSTATE = "state";


    //ends


    public static List<Student> jsonParseStudent(String rawJsonData) {

        try {
            JSONArray array = new JSONArray(rawJsonData);
            List<Student> list = new ArrayList<>(0);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Student student = new Student();

                //extract all data to model class
                student.setId(object.getInt(ID));
                student.setName(object.getString(NAME));
                student.setAge(object.getString(AGE));
                student.setGender(object.getString(GENDER));
                student.setEmail(object.getString(EMAIL));
                student.setMobile(object.getString(MOBILE));
                student.setCity(object.getString(CITY));
                student.setState(object.getString(STATE));

                //adding to list
                list.add(student);


            }
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Teacher> jsonParseTeacherOld(String rawJsonData) {

        try {
            JSONArray array = new JSONArray(rawJsonData);
            List<Teacher> list = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {

                //get JSON object
                JSONObject object = array.getJSONObject(i);
                Teacher teacher = new Teacher();

                //extract all data to model class
                teacher.setId(object.getInt(TID));
                teacher.setName(object.getString(TNAME));
                teacher.setAge(object.getString(TAGE));
                teacher.setGender(object.getString(TGENDER));
                teacher.setEmail(object.getString(TEMAIL));
                teacher.setPhone(object.getString(TMOBILE));
                teacher.setQualification(object.getString(TQUALIFICATION));
                teacher.setField(object.getString(TFIELD));
                teacher.setSubject(object.getString(TSUBJECT));
                teacher.setSpecific_subject(object.getString(TSPECIFIC_SUBJECT));
                teacher.setTclass(object.getString(TCLASS));
                teacher.setFee(object.getString(TFEE));
//                teacher.setVerified(object.getString(TVERIFIED));
                teacher.setCity(object.getString(TCITY));
                teacher.setState(object.getString(TSTATE));

                //add to list
                list.add(teacher);


            }
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


//    public static List<Teacher> jsonParseTeacher(String rawJsonData) {
//
//        try {
//            JSONArray array = new JSONArray(rawJsonData);
//            List<Teacher> list = new ArrayList<>();
//
//            for (int i = 0; i < array.length(); i++) {
//
//                //get JSON object
//                JSONObject object = array.getJSONObject(i);
//                Teacher teacher = new Teacher();
//
//                //extract all data to model class
//                teacher.setId(object.getInt("id"));
//                teacher.setName(object.getString("title"));
//                teacher.setField(object.getString("url"));
//                teacher.setSubject(object.getString("url"));
//                teacher.setSpecific_subject(object.getString("url"));
//                teacher.setCity(object.getString("url"));
//                teacher.setState(object.getString("url"));
//
//                //add to list
//                list.add(teacher);
//
//
//            }
//            return list;
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }


    //check for null pointer exception
    public static List<Teacher> jsonParseTeacher(String rawJsonData) {

        String totalCount = "";
        int success = 0;
        List<Teacher> list;
        try {
            //custom parsing
            JSONObject object = new JSONObject(rawJsonData);
//            Log.v("rawdata", String.valueOf(object.length()));
            success = object.getInt("success");
            if (success == 1)
                totalCount = object.getString("count");
            JSONArray teacherArray = object.getJSONArray("records");
//            Log.v("rawdata", String.valueOf(teacherArray.length()));
            list = new ArrayList<>();

            for (int i = 0; i < teacherArray.length(); i++) {
                JSONObject teacherObject = teacherArray.getJSONObject(i);
                Teacher teacher = new Teacher();
                teacher.setTotalCount(totalCount);
                teacher.setName(teacherObject.getString(TTNAME));
                teacher.setGender(teacherObject.getString(TTGENDER));
                teacher.setAge(teacherObject.getString(TTAGE));
                teacher.setEmail(teacherObject.getString(TTEMAIL));
                teacher.setPhone(teacherObject.getString(TTMOBILE));
                teacher.setQualification(teacherObject.getString(TTQUALIFICATION));
                teacher.setField(teacherObject.getString(TTFIELD));
                teacher.setSpecific_subject(teacherObject.getString(TTSPECIFIC_SUBJECT));
                teacher.setFee(teacherObject.getString(TTFEE));
                teacher.setCity(teacherObject.getString(TTCITY));
                teacher.setState(teacherObject.getString(TTSTATE));
                list.add(teacher);
            }
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<Ad> jsonParseAd(String rawJsonData) {

        try {
            JSONArray array = new JSONArray(rawJsonData);
            List<Ad> list = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {

                //get JSON object
                JSONObject object = array.getJSONObject(i);
                Ad ad = new Ad();

                //extract all data to model class
                ad.setName(object.getString(ATITLE));
                ad.setDescription(object.getString(ADESCRIPTION));
                ad.setPhone1(object.getString(AMOBILE1));
                ad.setPhone2(object.getString(AMOBILE2));
                ad.setPhone3(object.getString(AMOBILE3));
                ad.setPhone4(object.getString(AMOBILE4));
                ad.setEmail(object.getString(AEMAIL));
                ad.setAddress(object.getString(AADDRESS));
                ad.setCity(object.getString(ACITY));
                ad.setState(object.getString(ASTATE));
                ad.setAdImageUrl(object.getString(AIMAGEURL));


                //add to list
                list.add(ad);


            }
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
