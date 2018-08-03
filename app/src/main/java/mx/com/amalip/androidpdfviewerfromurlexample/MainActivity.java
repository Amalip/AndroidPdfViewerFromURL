package mx.com.amalip.androidpdfviewerfromurlexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.pdfView) PDFView pdfView;
    private String fileName = "kotlin-docs.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //New instance of PDFHelper
        new PDFHelper(this, fileName, new Callable<Void>() {
            @Override
            public Void call() {
                //Callable function if download is successful
                showPDF();
                return null;
            }
        }, new Callable<Void>() {
            @Override
            public Void call() {
                //Callable function if download isn't successful
                showError();
                return null;
            }
        });
    }

    public void showPDF(){
        //Getting the saved PDF
        File file = new File(this.getExternalFilesDir("pdfs") + File.separator + fileName);
        //Loading the PDF
        pdfView.fromFile(file)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    public void showError(){
        Toast.makeText(this, "Error downloading ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Deleting the PDF that was saved
        new File(this.getExternalFilesDir("pdfs")
                + File.separator + fileName).delete();
    }
}