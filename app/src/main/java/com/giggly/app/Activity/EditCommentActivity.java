package com.giggly.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditCommentActivity extends AppCompatActivity {

    String id,feed_id;
    public static int  position=0;
    RelativeLayout box_rel;
    EditText text_update;
    RelativeLayout back_rel,update,parent;
    TextView update_text;
    ProgressBar update_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);

        initializations();

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        box_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) EditCommentActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(text_update, InputMethodManager.SHOW_IMPLICIT);
            }
        });


        String comment = getIntent().getStringExtra("comment");
        position = getIntent().getIntExtra("position",0);
        id = getIntent().getStringExtra("id");
        feed_id = getIntent().getStringExtra("feed_id");
        text_update.setText(comment);
        text_update.setSelection(text_update.getText().length());



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(text_update.getText().toString().isEmpty()){
                    Toast.makeText(EditCommentActivity.this,"Enter Comment First",Toast.LENGTH_SHORT).show();
                }
                else {

                    update_progress.setVisibility(View.VISIBLE);
                    update_text.setVisibility(View.GONE);
                    // hit api............

                    SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                    String token = mPrefs.getString("USER_TOKEN", "");


                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("comment",text_update.getText().toString());


                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-sh-auth", token);

                    ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.EDIT_COMMENT+feed_id+"/"+id, EditCommentActivity.this, postParam, headers, new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result, String ERROR) {

                            if (ERROR.isEmpty()) {

                                try {


                                    JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                    int code = jsonObject.getInt("code");
                                    if (code == 200) {

                                        Intent inn=new Intent(EditCommentActivity.this, FeedDetailedActivity.class);
                                        inn.putExtra("KEYBOADR_STATUS", "update_comment");
                                        inn.putExtra("Feed_id", feed_id);
                                        inn.putExtra("Notification", "false");


                                        startActivity(inn);

                                        finish();

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                update_progress.setVisibility(View.GONE);
                                update_text.setVisibility(View.VISIBLE);

                            } else {
                                update_progress.setVisibility(View.GONE);
                                update_text.setVisibility(View.VISIBLE);

                                if (ERROR.equals("401")) {

                                    SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor mEditor = mPrefs.edit();
                                    mEditor.putString("USER_EMAIL", "");
                                    mEditor.putString("USER_PASSWORD", "");
                                    mEditor.putString("USER_TOKEN", "");
                                    mEditor.putString("USER_PIC", "");
                                    mEditor.apply();
                                    Intent intt = new Intent(EditCommentActivity.this, FirstActivity.class);
                                    startActivity(intt);
                                    finish();
                                } else {

                                    Toast.makeText(EditCommentActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                                }

                            }

                        }
                    });
                    // hit api--------------------


                }

            }
        });



        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inn=new Intent(EditCommentActivity.this, FeedDetailedActivity.class);
                inn.putExtra("KEYBOADR_STATUS", "feed_img");
                inn.putExtra("Feed_id", feed_id);
                inn.putExtra("Notification", "false");


                startActivity(inn);


                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    private void initializations() {
        back_rel=findViewById(R.id.back_rel);
        text_update=findViewById(R.id.text_update);
        update=findViewById(R.id.update);
        box_rel=findViewById(R.id.box_rel);
        parent=findViewById(R.id.parent);
        update_progress=findViewById(R.id.update_progress);
        update_text=findViewById(R.id.update_text);

    }

    protected void hideKeyboard(View view) {

//
//        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

}
