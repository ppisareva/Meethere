package com.example.polina.meethere;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.polina.meethere.Adapters.SimpleItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by polina on 10.03.16.
 */
public class Utils {

    private final static int WIDTH = 100;
    private final static int HEIGHT = 100;




    public static String getCurrentTime( ){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();

        return dateFormat.format(c.getTime());

    }

    public static String[]  getCurrentTimePlusHour(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 1);

        return new String []{new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()), new SimpleDateFormat("HH:mm").format(c.getTime())};

    }

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        return dateFormat.format(c.getTime());
    }


    public static List<SimpleItem> getAllCategory(Context context) {
        List<SimpleItem> allCategory= new ArrayList<>();
        String arr [] = context.getResources().getStringArray(R.array.category);
       TypedArray image = context.getResources().obtainTypedArray(R.array.category_images);
        for(int i =0; i<arr.length; i++){
            allCategory.add(new SimpleItem(image.getResourceId(i+1, R.drawable.ic_android), arr[i]));
        }
        return allCategory;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] prof = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, prof, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static InputStream getThumbnailImage(Uri uri, Context context) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            System.out.println(bitmap.toString());
            Bitmap resizedBitmap = getResizedBitmap(bitmap, HEIGHT, WIDTH);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            byte[] bitmapdata = bos.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bitmapdata);
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        Bitmap resizedBitmap;


        if (width >= height) {
            matrix.postScale(scaleHeight, scaleHeight);
            resizedBitmap = Bitmap.createBitmap(bm, width / 2 - height / 2, 0, height, height, matrix, false);
        } else {
            matrix.postScale(scaleWidth, scaleWidth);
            resizedBitmap = Bitmap.createBitmap(bm, 0, height / 2 - width / 2, width, width, matrix, false);
        }

        return resizedBitmap;

    }

    public static String convertDateToISO(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00.000'Z'");
        df.setTimeZone(tz);
        try {
            return df.format(dateFormat.parseObject(date));
        } catch (ParseException e) {
            return null;
        }

    }



}
