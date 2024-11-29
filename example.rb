require 'uri'
require 'net/http'
require 'json'
require 'openssl'

API_KEY = "API KEY HERE"
API_SECRET = "API SECRET HERE"
API_PATH = "https://dtpspartnerapi-development.fly.dev" # dont forget to change the url on production

# Function to generate HMAC-SHA256 signature
def generate_signature(secret, path, data = nil)
  message = path
  message += data if data
  hmac = OpenSSL::HMAC.digest(OpenSSL::Digest.new('sha256'), secret, message)
  hmac.unpack1('H*') # Convert to hex string
end

# Function to send HTTP requests
def send_http_request(api_url, method, signature, request_body = nil)
  uri = URI(api_url)
  http = Net::HTTP.new(uri.host, uri.port)
  http.use_ssl = true # Use SSL/TLS

  request = case method
            when 'POST'
              req = Net::HTTP::Post.new(uri.path, { 'Content-Type' => 'application/json' })
              req.body = request_body
              req
            when 'GET'
              req = Net::HTTP::Get.new(uri.path)
              req
            end

  req['X-Api-Key'] = API_KEY
  req['X-Api-Signature'] = signature

  response = http.request(request)
  JSON.parse(response.body) if response.is_a?(Net::HTTPSuccess)
rescue => e
  puts "HTTP Request Error: #{e.message}"
end

# Create user function
def create_user
  api_url = "#{API_PATH}/api/v1/user/create"
  userdata = {
    birth_country: "PN",
    district: "Mayerstead",
    dob: "08/04/1991",
    first_name: "Carmelo",
    gender: "Male",
    isd_code: 1,
    last_name: "Smith",
    mail: "Uriel_Pouros@gmail.com",
    occupation: "Direct Branding Facilitator",
    passportnumber: "JPHF9AK1LRUT48250",
    place_of_birth: "Niue",
    province: "Gwynedd County",
    telephone: "(511) 612-5672",
    title: "Internal Response Representative",
    village: "Gloucestershire"
  }

  json_user_data = userdata.to_json
  signature = generate_signature(API_SECRET, "/api/v1/user/create", json_user_data)

  response = send_http_request(api_url, 'POST', signature, json_user_data)
  puts "Create User Response: #{response}"
end

# Get card list function
def get_card
  api_url = "#{API_PATH}/api/v1/card/list"
  signature = generate_signature(API_SECRET, "/api/v1/card/list")

  response = send_http_request(api_url, 'GET', signature)
  puts "Get Card Response: #{response}"
end
