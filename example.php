<?php

$API_KEY ="API KEY HERE";
$API_SECRET ="API SECRET HERE";
$API_PATH = "https://dtpspartnerapi-development.fly.dev";  // dont forget to change the url on production


// Function to generate HMAC-SHA256 signature
function generateSignature($secret, $path, $data = null) {
    $message = $path;
    if ($data) {
        $message .= $data;
    }
    return strtolower(hash_hmac('sha256', $message, $secret));
}

// Function to send HTTP requests
function sendHttpRequest($apiUrl, $method, $signature, $requestBody = null) {
    $headers = [
        "Content-Type: application/json",
        "X-Api-Key: $GLOBALS[API_KEY]",
        "X-Api-Signature: $signature"
    ];

    $ch = curl_init($apiUrl);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    if ($method === 'POST') {
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, $requestBody);
    }

    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($httpCode >= 200 && $httpCode < 300) {
        return json_decode($response, true);
    } else {
        echo "HTTP Request Error: $httpCode\n";
        return null;
    }
}

// Create user function
function createUser() {
    global $API_PATH;

    $apiUrl = "$API_PATH/api/v1/user/create";
    $userdata = [
        "birth_country" => "PN",
        "district" => "Mayerstead",
        "dob" => "08/04/1991",
        "first_name" => "Carmelo",
        "gender" => "Male",
        "isd_code" => 1,
        "last_name" => "Smith",
        "mail" => "Uriel_Pouros@gmail.com",
        "occupation" => "Direct Branding Facilitator",
        "passportnumber" => "JPHF9AK1LRUT48250",
        "place_of_birth" => "Niue",
        "province" => "Gwynedd County",
        "telephone" => "(511) 612-5672",
        "title" => "Internal Response Representative",
        "village" => "Gloucestershire"
    ];

    $jsonUserData = json_encode($userdata);
    $signature = generateSignature($GLOBALS['API_SECRET'], "/api/v1/user/create", $jsonUserData);

    $response = sendHttpRequest($apiUrl, 'POST', $signature, $jsonUserData);
    echo "Create User Response: " . print_r($response, true);
}

// Get card list function
function getCard() {
    global $API_PATH;

    $apiUrl = "$API_PATH/api/v1/card/list";
    $signature = generateSignature($GLOBALS['API_SECRET'], "/api/v1/card/list");

    $response = sendHttpRequest($apiUrl, 'GET', $signature);
    echo "Get Card Response: " . print_r($response, true);
}


?>
