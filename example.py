import hmac
import hashlib
import requests
import json

API_KEY = "API KEY HERE"
API_SECRET = "API SECRET HERE"
API_PATH = "https://dtpspartnerapi-development.fly.dev" # dont forget to change the url for production

# Function to generate HMAC-SHA256 signature
def generate_signature(secret, path, data=None):
    message = path
    if data is not None:
        message += data
    return hmac.new(secret.encode(), message.encode(), hashlib.sha256).hexdigest()

# Function to send HTTP requests
def send_http_request(api_url, method, signature, request_body=None):
    headers = {
        "Content-Type": "application/json",
        "X-Api-Key": API_KEY,
        "X-Api-Signature": signature
    }
    
    if method == "POST":
        response = requests.post(api_url, headers=headers, data=request_body)
    else:  # GET method
        response = requests.get(api_url, headers=headers)

    response.raise_for_status()  # Raise an error for bad responses
    return response.json()  # Return JSON response

# Create user function
def create_user():
    api_url = f"{API_PATH}/api/v1/user/create"
    userdata = {
        "birth_country": "PN",
        "district": "Mayerstead",
        "dob": "08/04/1991",
        "first_name": "Carmelo",
        "gender": "Male",
        "isd_code": 1,
        "last_name": "Smith",
        "mail": "Uriel_Pouros@gmail.com",
        "occupation": "Direct Branding Facilitator",
        "passportnumber": "JPHF9AK1LRUT48250",
        "place_of_birth": "Niue",
        "province": "Gwynedd County",
        "telephone": "(511) 612-5672",
        "title": "Internal Response Representative",
        "village": "Gloucestershire"
    }

    json_user_data = json.dumps(userdata)
    signature = generate_signature(API_SECRET, "/api/v1/user/create", json_user_data)

    try:
        response = send_http_request(api_url, "POST", signature, json_user_data)
        print("Create User Response:", response)
    except requests.RequestException as e:
        print("Create User Error:", e)

# Get card list function
def get_card():
    api_url = f"{API_PATH}/api/v1/card/list"
    signature = generate_signature(API_SECRET, "/api/v1/card/list")

    try:
        response = send_http_request(api_url, "GET", signature)
        print("Get Card Response:", response)
    except requests.RequestException as e:
        print("Get Card Error:", e)


