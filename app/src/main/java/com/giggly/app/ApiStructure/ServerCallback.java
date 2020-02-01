package com.giggly.app.ApiStructure;

import org.json.JSONObject;

/**
 * Created by M on 3/7/2018.
 */

public interface ServerCallback {
    void onSuccess(JSONObject result,String ERROR);

}
