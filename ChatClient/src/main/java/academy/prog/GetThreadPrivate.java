package academy.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class GetThreadPrivate implements Runnable {
    private final Gson gson;

    private int  p;
    private String login;

    public GetThreadPrivate(String login) {
        this.login = login;
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }



    @Override
    public void run() { // WebSockets
        try {
            while ( ! Thread.interrupted()) {
                URL url = new URL(Utils.getURL() + "/get?prv=" + p + "&login=" + login);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();


                try (InputStream is = http.getInputStream()){
                    byte[] buf = responseBodyToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);

                    JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);


                    if (list != null) {
                        for (Message m : list.getList()) {
                            System.out.println(m);
                            p++;
                        }
                    }
                }
                Thread.sleep(500);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte[] responseBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }

    protected void printUsersList() {
        try {
            URL url = new URL(Utils.getURL() + "/users");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDoInput(true);
            try (InputStream is = http.getInputStream()) {
                byte[] buf = responseBodyToArray(is);
                String strBuf = new String(buf, StandardCharsets.UTF_8);
                Gson gson = new GsonBuilder().create();
                Set<String> set = gson.fromJson(strBuf, HashSet.class);
                System.out.println(set);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
