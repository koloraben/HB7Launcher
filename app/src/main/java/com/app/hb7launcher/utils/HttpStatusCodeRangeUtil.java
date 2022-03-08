package com.app.hb7launcher.utils;

/*
 * Created by Issam ELGUERCH on 3/8/2022.
 * mail: issamelguerch@gmail.com
 * All rights reserved.
 */


public class HttpStatusCodeRangeUtil {

    public static HttpStatusCodeRange getRange(int code) {
        if (code >= 200 && code < 300) {
            return HttpStatusCodeRange.SUCCESS_RANGE;
        }
        if (code >= 400 && code < 500) {
            return HttpStatusCodeRange.CLIENT_ERROR_RANGE;
        }
        if (code >= 500 && code < 600) {
            return HttpStatusCodeRange.SERVER_ERROR_RANGE;
        }
        return HttpStatusCodeRange.UNKNOWN;
    }
}
