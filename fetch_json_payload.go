package main

import "os"
import "fmt"
import "time"
import "net/http"
import "io/ioutil"

func fetch_json_payload(domain string) string {

	// Create the URL
	url := ("https://" + domain + "/api/v1/custom_emojis");

	// Create the client
	webClient := http.Client {
		Timeout: time.Second * 300}

	// Fetch the payload
	res, err := webClient.Get(url);

	if ( err != nil ) {
		fmt.Println("Error fetching json payload");
		os.Exit(1);
	}

	bodyBytes, err2 := ioutil.ReadAll(res.Body);

	if ( err2 != nil ) {
		fmt.Println("Error fetching json payload");
		os.Exit(1);
	}

	defer res.Body.Close();

	bodyString := string(bodyBytes);

	return bodyString;
}
