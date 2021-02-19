/*
ORNELLAS            12/02/2021 11:01            Executor comandos ( em geral, "java -jar ..." )
 */
package jexec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ornellas
 */
public class Runner extends ExecBase implements Runnable {

    private BufferedReader error;
    private BufferedReader op;
    private int exitVal;

    private String cmd = null;

    public Runner(String[] args) {

        cmd = args[0];

    }

    @Override
    public void run() {

        String arr[] = cmd.split(" ");
        List<String> args = new ArrayList<String>();

        String jarFilePath = null;

        for (int i = 0; i < arr.length; i++) {

            if (i == 0) {
                jarFilePath = arr[i];
            } else {
                args.add(arr[i]);
            }

        }

        progressMsg("Executando...");
        executeJar(jarFilePath, args);
        progressMsg("Processo concluído.");
        

    }

    public void executeJar(String jarFilePath, List<String> args) {
        // Create run arguments for the

        final List<String> actualArgs = new ArrayList<String>();
        actualArgs.add(0, "/usr/lib/jvm/jdk-15.0.2/bin/java");
        actualArgs.add(1, "-jar");
        actualArgs.add(2, jarFilePath);
        actualArgs.addAll(args);
        try {
            final Runtime re = Runtime.getRuntime();
            //final Process command = re.exec(cmdString, args.toArray(new String[0]));
            final Process command = re.exec(actualArgs.toArray(new String[0]));
            this.error = new BufferedReader(new InputStreamReader(command.getErrorStream()));
            this.op = new BufferedReader(new InputStreamReader(command.getInputStream()));
            // Wait for the application to Finish
            command.waitFor();
            this.exitVal = command.exitValue();
            if (this.exitVal != 0) {
                throw new IOException("Failed to execure jar, " + this.getExecutionLog());
            }

        } catch (Exception ex) {
            progressMsg(ex.getMessage());
        }
    }

    public String getExecutionLog() {
        String error = "";
        String line;
        try {
            while ((line = this.error.readLine()) != null) {
                error = error + "\n" + line;
            }
        } catch (final IOException e) {
        }
        String output = "";
        try {
            while ((line = this.op.readLine()) != null) {
                output = output + "\n" + line;
            }
        } catch (final IOException e) {
        }
        try {
            this.error.close();
            this.op.close();
        } catch (final IOException e) {
        }
        return "exitVal: " + this.exitVal + ", error: " + error + ", output: " + output;
    }

}
