//New instance of PDFHelper
new PDFHelper(ctx, fileName, 
	//Callable function if download is successful
	new Callable<Void>() {
            @Override
            public Void call() {
                showPDF();
                return null;
            }
        },
	//Callable function if download isn't successful
	 new Callable<Void>() {
            @Override
            public Void call() {
                showError();
                return null;
            }
        });
    }

    public void showPDF() {
	//Getting the saved PDF
        fragment.showPDF(new File(activity.getExternalFilesDir("pdfs")
                + File.separator + fileName));
    }

    public void showError() {
        fragment.showError();
    }

////////////////////////////////////////////////Fragment///////////////////////////////////////////////////
@BindView(R.id.pdfView) PDFView pdfView;

public void showPDF(File file) {
	//Loading the PDF
        pdfView.fromFile(file)
                .defaultPage(0)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(ctx))
                .load();
    }

    @Override
    public void showError() {
	Toast.makeText(ctx, "Error downloading ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
	//Deleting the PDF that was saved
        new File(ctx.getExternalFilesDir("pdfs")
                + File.separator + fileName).delete();
    }
