import crypto from 'crypto';
import axios from 'axios';

const API_KEY = "API KEY HERE";
const API_SECRET = "API SECRET HERE";
const API_PATH = "https://dtpspartnerapi-development.fly.dev"; // dont forget to change the url for production

// Function to generate HMAC-SHA256 signature
const generateSignature = (secret: string, path: string, data?: string): string => {
    let message = path;
    if (data) {
        message += data;
    }
    return crypto.createHmac('sha256', secret).update(message).digest('hex');
};

// Function to send HTTP requests
const sendHttpRequest = async (apiUrl: string, method: 'POST' | 'GET', signature: string, requestBody?: string) => {
    const headers = {
        "Content-Type": "application/json",
        "X-Api-Key": API_KEY,
        "X-Api-Signature": signature
    };

    try {
        const response = method === 'POST' 
            ? await axios.post(apiUrl, requestBody, { headers }) 
            : await axios.get(apiUrl, { headers });

        return response.data;
    } catch (error) {
        console.error("HTTP Request Error:", error);
        throw error;
    }
};

// Create user function
const createUser = async () => {
    const apiUrl = `${API_PATH}/api/v1/user/create`;
    const userdata = {
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
    };

    const jsonUserData = JSON.stringify(userdata);
    const signature = generateSignature(API_SECRET, "/api/v1/user/create", jsonUserData);

    try {
        const response = await sendHttpRequest(apiUrl, 'POST', signature, jsonUserData);
        console.log("Create User Response:", response);
    } catch (error) {
        console.error("Create User Error:", error);
    }
};

// Get card list function
const getCard = async () => {
    const apiUrl = `${API_PATH}/api/v1/card/list`;
    const signature = generateSignature(API_SECRET, "/api/v1/card/list");

    try {
        const response = await sendHttpRequest(apiUrl, 'GET', signature);
        console.log("Get Card Response:", response);
    } catch (error) {
        console.error("Get Card Error:", error);
    }
};


