package com.rapidops.salesmatechatsdk.data.webserivce;

import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartUtil {

    /* -------------------------------  Multipart Util method --------------------------------------- */
    @NonNull
    static RequestBody createPartFromString(String partValue) {
        /*return RequestBody.create(MediaType.parse("text/plain"), partValue);*/
        return RequestBody.create(partValue, MultipartBody.FORM);

    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, String selectedFilePath) {
        File file = new File(selectedFilePath);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse("application/octet-stream"));
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, File file) {

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse("application/octet-stream"));
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static MultipartBody.Part prepareFilePart(String partName, String fileName, byte[] toByteArray) {

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(toByteArray,MediaType.parse("application/octet-stream"));
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, fileName, requestFile);
    }
}
