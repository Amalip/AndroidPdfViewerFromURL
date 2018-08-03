package mx.com.amalip.androidpdfviewerfromurlexample;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Amalip on 8/22/2017.
 */

public interface PDFServices {
    @GET
    Call<ResponseBody> downloadPDF(@Url String fileUrl);
}
