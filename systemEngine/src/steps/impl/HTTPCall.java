package steps.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import datadefinition.DataDefinitionRegistry;
import datadefinition.api.IO_NAMES;
import datadefinition.impl.list.FileListData;
import flow.execution.context.StepExecutionContext;
import okhttp3.*;
import steps.api.AbstractStepDefinition;
import steps.api.DataDefinitionDeclarationImpl;
import steps.api.DataNecessity;
import steps.api.StepResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Enumeration;

public class HTTPCall  extends AbstractStepDefinition {

    public HTTPCall() {
        super("HTTP Call", true);

        // step inputs
        addInput(new DataDefinitionDeclarationImpl("RESOURCE", DataNecessity.MANDATORY, "Resource Name (include query parameters)", DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("ADDRESS", DataNecessity.MANDATORY, "Domain:Port",DataDefinitionRegistry.STRING));
        addInput(new DataDefinitionDeclarationImpl("PROTOCOL", DataNecessity.MANDATORY, "protocol", DataDefinitionRegistry.STRING));//ENUMERATOR
        addInput(new DataDefinitionDeclarationImpl("METHOD", DataNecessity.OPTIONAL, "Method", DataDefinitionRegistry.STRING));//ENUMERATOR
        addInput(new DataDefinitionDeclarationImpl("BODY", DataNecessity.OPTIONAL, "Request Body", DataDefinitionRegistry.JSON));

        // step outputs
        addOutput(new DataDefinitionDeclarationImpl("CODE", DataNecessity.NA, "Response code", DataDefinitionRegistry.NUMBER));
        addOutput(new DataDefinitionDeclarationImpl("RESPONSE_BODY", DataNecessity.NA, "Response body", DataDefinitionRegistry.STRING));

    }


    @Override
    public StepResult invoke(StepExecutionContext context) {
        Instant start = Instant.now();

        String resource = context.getDataValue(IO_NAMES.RESOURCE, String.class);
        String address = context.getDataValue(IO_NAMES.ADDRESS, String.class);

        //checkkkkk how to read json and ENUMERATOR
        String protocol = context.getDataValue(IO_NAMES.PROTOCOL, String.class);
        String method = context.getDataValue(IO_NAMES.METHOD, String.class);
        String body = context.getDataValue(IO_NAMES.BODY, String.class); //need to be JSON?

        context.storeLogLine("About to execute http request " + protocol + "|" + method + "|" + address + "|" + resource);


        try {
            URL url = new URL(protocol + "://" + address + "/" + resource);
            System.out.println(url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (method != null) {
                connection.setRequestMethod(method);
            }
            connection.setDoOutput(true);

            if (body != null && !body.isEmpty()) {
                byte[] requestBodyBytes = body.getBytes(StandardCharsets.UTF_8);//converts the body string into bytes
                connection.setRequestProperty("Content-Type", "application/json");//sets the Content-Type for to show the request body contains JSON data. The value "application/json" for the media type of the request body.
                connection.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));//sets the Content-Length header of the request, specifying the length of the request body in bytes. It ensures that the server knows the exact size of the request body.

                //It sends the actual request body data to the server:
                connection.getOutputStream().write(requestBodyBytes);//writes the request body bytes to the output stream of the connection.
            }

            int statusCode = connection.getResponseCode();
            StringBuilder responseBodyBuilder = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBodyBuilder.append(line);
                }
            }

            String responseBody = responseBodyBuilder.toString();

            context.storeLogLine("Received Response. Status code: " + statusCode);

            context.storeDataValue("CODE", statusCode);
            context.storeDataValue("RESPONSE_BODY", responseBody);

            context.storeStepTotalTime(start);
            return StepResult.SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            context.storeLogLineAndSummaryLine("The HTTP request failed");
            return StepResult.FAILURE;
        }
    }

/*
    @Override
    public StepResult invoke(StepExecutionContext context) {
        Instant start = Instant.now();

        String resource = context.getDataValue(IO_NAMES.RESOURCE, String.class);
        String address = context.getDataValue(IO_NAMES.ADDRESS, String.class);

        //checkkkkk how to read json and ENUMERATOR
        String protocol = context.getDataValue(IO_NAMES.PROTOCOL, String.class);
        String method = context.getDataValue(IO_NAMES.METHOD, String.class);
        String body = context.getDataValue(IO_NAMES.BODY, String.class); //need to be JSON?

        context.storeLogLine("About to execute http request " + protocol + "|" + method + "|" + address + "|" + resource);

        try {
            OkHttpClient client = new OkHttpClient();
            String requestUrl = protocol + "://" + address + "/" + resource;

            Request.Builder requestBuilder; //set the method
            if(method.equals(null)){
                requestBuilder = new Request.Builder().url(requestUrl).method("get", null);
            }else {
               requestBuilder = new Request.Builder().url(requestUrl).method(method, null);
            }

            if (body != null && !body.isEmpty()) {
                MediaType mediaType = MediaType.parse("application/json");

              //need okio package
               RequestBody requestBody = RequestBody.create(mediaType, body);
               requestBuilder = requestBuilder.method(method, requestBody);
            }

            // Build the request
            Request request = requestBuilder.build();

            // Execute the request
            Response response = client.newCall(request).execute();

            int statusCode = response.code();
            String responseBody = response.body().string();

            context.storeLogLine("Received Response. Status code: " + statusCode);

            context.storeDataValue("CODE", statusCode);
            context.storeDataValue("RESPONSE_BODY", responseBody);

            context.storeStepTotalTime(start);
            return StepResult.SUCCESS;

        } catch (IOException e) {// Handle any exceptions that occurred during the HTTP request
            e.printStackTrace();
            context.storeLogLineAndSummaryLine("The HTTP request failed");
            return StepResult.FAILURE;

        }

    }*/

}
