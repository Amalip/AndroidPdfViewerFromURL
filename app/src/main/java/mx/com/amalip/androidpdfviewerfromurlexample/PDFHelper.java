package mx.com.amalip.androidpdfviewerfromurlexample;
import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Amalip on 8/22/2017.
 */

public class PDFHelper {
    private final Context ctx;
    //E.g.:"rules.pdf"
    private final String fileName;
    //Functions that will be called if the PDF was downloaded correctly or not
    private final Callable downloaded, error;
    //Retrofit service
    private PDFServices pdfServices;

    public PDFHelper(Context ctx, String fileName, Callable downloaded, Callable error) {
        this.ctx = ctx;
        this.fileName = fileName;
        this.downloaded = downloaded;
        this.error = error;


        pdfServices = RetrofitSettings.createRetrofitService(PDFServices.class, "https://kotlinlang.org/docs/");
        getPDF();
    }

    //Method to get PDF
    private void getPDF() {
        //Make the request to download the PDF
        Call<ResponseBody> call = pdfServices.downloadPDF(fileName);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                //Check if the download was successful
                if (response.isSuccessful())
                    result(writeResponseBodyToDisk(response.body()) ? downloaded : error);
                else
                    result(error);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                result(error);
            }
        });
    }

    //Method to save the PDF
    /*
     * body = The PDF stream
     * */
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            //Making the file and pathname where it will be saved
            File PDFFile = new File(ctx.getExternalFilesDir("pdfs")
                    + File.separator + fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                //Reading and writing the file
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(PDFFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                //If all ok then return true else false
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                //Close the reader and writer
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    //Method to execute the result function
    /*
     * fun = The function to execute
     * */
    private <T> void result(Callable<T> fun) {
        try {
            fun.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}