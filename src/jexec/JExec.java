/*
 * ORNELLAS                 12/02/2021 09:01                Executor de rotinas diversas desenvolvidas em JAVA...
 */
package jexec;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author ornellas
 */
public class JExec {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println("Executor ativo!");

        new Thread(new ExecJob()).start();
    }

    private static class ExecJob extends ExecBase implements Runnable {

        @Override
        public void run() {

            while (true) {
                getJob();
            }
        }

        private void getJob() {

            cls();
            progressMsg("JExec aguardando novos pedidos de processamento...");

            sleep(3000);

            //Executa consulta ao banco de dados através do serviço WEB "AERO SQLJSON"...
            String sql = "exec utilitarios.dbo.usp_dequeueFifo";

            String responseJSON = execSql(sql);

            parseSqlJson(responseJSON);

        }

        private void parseSqlJson(String responseJSON) {

            if (responseJSON == null || responseJSON.equals("")) {
                return;
            }

            try {

                JSONArray json = new JSONArray(responseJSON);

                for (int i = 0; i < json.length(); i++) {

                    JSONObject obj = json.getJSONObject(i);

                    /*
                    jarFile = args[0];
                    pathFile= getPath(args[1]);
                    params = args[2];
                    */
                    
                    String[] args = new String[1];
                    
                    args[0] = obj.get("Payload").toString();

                    progressMsg("Iniciando nova instância para " + args[0] + "...");

                    Runner runner = new Runner(args);

                    new Thread(runner).start();
                    
                    runner = null;
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }

    }

}

 
