package com.tddp2.grupo2.linkup.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.tddp2.grupo2.linkup.R;


public class ErrorUtils {

    public static AlertDialog createNoConnectionDialog(final String title, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        //builder.setIcon(R.drawable.ic_no_connection);
        builder.setMessage(context.getResources().getString(R.string.no_connection_to_server))
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        return alert;
    }

    /*public static APIError parseError(Response<?> response) {
        APIError error;
        if (sessionExpired(response)) {
            error = new APIError();
            error.setSessionExpired(Boolean.TRUE);
        } else {
            Converter<ResponseBody, APIError> converter =
                    ServiceGenerator.defaultRetrofit()
                            .responseBodyConverter(APIError.class, new Annotation[0]);
            try {
                error = converter.convert(response.errorBody());
            } catch (IOException e) {
                return new APIError(response.raw().toString());
            }
        }


        return error;
    }*/

    /*public static Boolean sessionExpired(Response<?> response) {
        return response.code() == Configuration.HTTP_CODE_FORBIDDEN;
    }*/
}