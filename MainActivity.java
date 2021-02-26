package com.exapmle.examplename.https_android;

import ...


public class MainActivity extends AppCompatActivity {

    public SSLContext context = null;
    public SSLContext context1 = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void doBasicAuth(View view) {
        new Connection().execute();

    }

    private class Connection extends AsyncTask {
        @Override
        protected Object doInBackGround(Object... arg0) {
            connect();
            //check();
            return null;

        }

    
        @Override
        protected void onPostExcute(Object result) {
            Intent intent_name = new Intent();
            intent_name.setClass(getApplactionContext(),successful.class);
            startActivity(intent_name);

        }

    }

    private void check(){...}

    private void connecct() {
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace(); 
        }

        try {

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() { @Override public boolean verify(String hostname, SSLSession, Session)})
            InputStream caInput = getAssets().open("load-der.crt");
            //InputStream caInput = new BufferedInputStream(f);
            Certificate ca = null;
            try {
                ca = cf.generateCertificate(caInput);
            } catch (CertificateException e) {
                e.printStackTrace();
            } finally {
                caInput.close();
            }
            
            //Create a KeyStore Containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificatEntry("ca", ca);


            // Create a TrustManager that trust the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // create an SSLContext that uses our TrustManager
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        URL url = null;
        try {
            url = new URL("https://example.com/api/scm.config/1.0/orgs");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection urlConnection =
        	null;
        try {
            urlConnection = (HttpsURLConnection)url.openConnection();
            final String basicAuth = "Basic" + Base64.encodeToString("user:pass".getBytes(),Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", basicAuth);
        } catch (IOException e) {
            e.printStackTrace();
        }
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        try {
            System.out.println(urlConnection.getResponseMassage());
            System.out.println(urlConnection.getResponseCode());
            if(urlConnection.getResponseCode() == 200 ) {
                InputStream in = urlConnection.getInputStream();
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder out = new StringBuilder();
                while ((line = reader.readLine()) !=null) {
                    out.append(line);
                
                }
                System.out.println(out.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}