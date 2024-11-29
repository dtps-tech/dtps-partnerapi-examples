import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Main {
    final String API_KEY = "API KEY HERE";
     final String API_SECRET = "API SECRET HERE";
     final String API_PATH = "https://sandbox.dtpspartnerapi.backofficeportal.app/api/v1"; // dont forget to change the url for production

    // Method to generate HMAC-SHA256 signature
    public  String generateSignature(String secret, String path, String data) throws Exception {
        String message = path + (data != null ? data : "");
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] hash = sha256Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        // Convert to Hex
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // Generic method to handle HTTP requests
    public  String sendHttpRequest(String apiUrl, String method, String signature, String requestBody) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-Api-Key", API_KEY);
        connection.setRequestProperty("X-Api-Signature", signature);

        // Handle POST method with request body
        if ("POST".equals(method) && requestBody != null) {
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        // Get the response
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            }
        } else {
            throw new Exception("HTTP request failed with response code: " + responseCode);
        }
    }

    // Create user method
    public  void createUser() {
        String apiUrl = API_PATH + "/user/create";
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("birth_country", "PN");
        userdata.put("district", "Mayerstead");
        userdata.put("dob", "08/04/1991");
        userdata.put("first_name", "Carmelo");
        userdata.put("gender", "Male");
        userdata.put("isd_code", 1);
        userdata.put("last_name", "Smith");
        userdata.put("mail", "Uriel_Pouros@gmail.com");
        userdata.put("occupation", "Direct Branding Facilitator");
        userdata.put("passportnumber", "JPHF9AK1LRUT48250");
        userdata.put("place_of_birth", "Niue");
        userdata.put("province", "Gwynedd County");
        userdata.put("telephone", "(511) 612-5672");
        userdata.put("title", "Internal Response Representative");
        userdata.put("village", "Gloucestershire");

        String jsonUserData = new com.google.gson.Gson().toJson(userdata);

        try {
            String signature = generateSignature(API_SECRET, new URL(apiUrl).getPath(), jsonUserData);
            String response = sendHttpRequest(apiUrl, "POST", signature, jsonUserData);
            System.out.println("Create User Response: " + response);
        } catch (Exception e) {
            System.err.println("Create User Error: " + e.getMessage());
        }
    }

    // Get card list method
    public  void getCard() {
        String apiUrl = API_PATH + "/card/list";
        try {
            String signature = generateSignature(API_SECRET, new URL(apiUrl).getPath(), null);
            String response = sendHttpRequest(apiUrl, "GET", signature, null);
            System.out.println("Get Card Response: " + response);
        } catch (Exception e) {
            System.err.println("Get Card Error: " + e.getMessage());
        }
    }


}
