/*
 * ORNELLAS                 12/02/2021 09:01                Executor de rotinas diversas desenvolvidas em JAVA...
 */
package jexec;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alexandre Ornellas
 */
public class ExecBase {

    protected static void sleep(int miliseconds) {

        //20201124 
        try {
            Thread.sleep(miliseconds);
        } catch (Exception e) {
            //
        }
    }

    protected void exec(String cmd) {

        try {

            ProcessBuilder builder = new ProcessBuilder(cmd);
            Process process = builder.start();

        } catch (Exception ex) {

            progressMsg(ex.getMessage());
        }

    }

    protected  String execSql(String sql) {
        
        Map<String, String> params = new HashMap<>();

        params.put("login", "FFFACDDOOD");
        params.put("senha", "1EEEEEEdddDDD514B");
        params.put("sql", sql);

        return getURL(
                 "sqljson.aspx",
                params);

    }

    private String getURL(String url, Map params) {

        StringBuffer content;

        try {

            URL aurl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) aurl.openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(getParamsString(params));
            out.flush();
            out.close();

            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();

            Reader streamReader = null;

            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }

            BufferedReader in = new BufferedReader(
                    streamReader);

            String inputLine;

            content = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            con.disconnect();

        } catch (Exception e) {

            return e.getMessage();
        }

        return content.toString();

    }

    private String getParamsString(Map<String, String> params) {

        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {

            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            } catch (Exception e) {

            }

        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public static void cls() {
        try {

            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c",
                        "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ex) {
        }
    }

    public static void progressMsg(String msg) {

        if (msg == null || msg.equals("")) {
            return;
        }

        if (msg.equals("CLEAR")) {
            cls();
        } else {

            String aux = new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date());
            System.out.println(aux + " " + msg + "\n");

        }

    }
}
