package main

import (
	"bytes"
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
)

const (
	API_KEY    =  "API KEY HERE"
	API_SECRET = "API SECRET HERE"
	API_PATH   = "https://dtpspartnerapi-development.fly.dev" // dont forget to change the url on production
)


// Function to generate HMAC-SHA256 signature
func generateSignature(secret, path string, data []byte) string {
	message := path
	if data != nil {
		message += string(data)
	}

	h := hmac.New(sha256.New, []byte(secret))
	h.Write([]byte(message))
	return hex.EncodeToString(h.Sum(nil))
}

// Function to send HTTP requests
func sendHTTPRequest(apiURL, method, signature string, requestBody []byte) ([]byte, error) {
	client := &http.Client{}
	req, err := http.NewRequest(method, apiURL, bytes.NewBuffer(requestBody))
	if err != nil {
		return nil, err
	}

	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("X-Api-Key", API_KEY)
	req.Header.Set("X-Api-Signature", signature)

	resp, err := client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	return ioutil.ReadAll(resp.Body)
}

// Create user function
func createUser() {
	apiURL := fmt.Sprintf("%s/api/v1/user/create", API_PATH)

	userData := map[string]interface{}{
		"birth_country":    "PN",
		"district":         "Mayerstead",
		"dob":              "08/04/1991",
		"first_name":      "Carmelo",
		"gender":          "Male",
		"isd_code":        1,
		"last_name":       "Smith",
		"mail":            "Uriel_Pouros@gmail.com",
		"occupation":      "Direct Branding Facilitator",
		"passportnumber":  "JPHF9AK1LRUT48250",
		"place_of_birth":  "Niue",
		"province":        "Gwynedd County",
		"telephone":       "(511) 612-5672",
		"title":           "Internal Response Representative",
		"village":         "Gloucestershire",
	}

	jsonUserData, _ := json.Marshal(userData)
	signature := generateSignature(API_SECRET, "/api/v1/user/create", jsonUserData)

	response, err := sendHTTPRequest(apiURL, "POST", signature, jsonUserData)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	fmt.Println("Create User Response:", string(response))
}

// Get card list function
func getCard() {
	apiURL := fmt.Sprintf("%s/api/v1/card/list", API_PATH)
	signature := generateSignature(API_SECRET, "/api/v1/card/list", nil)

	response, err := sendHTTPRequest(apiURL, "GET", signature, nil)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	fmt.Println("Get Card Response:", string(response))
}

// Main function
func main() {
	createUser()
	getCard()
}
