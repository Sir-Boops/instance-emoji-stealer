package main

import "os"
import "time"
import "net/http"
import "io/ioutil"
import "path/filepath"

func download_emoji(url string, filename string, path string) {

	if _, err := os.Stat(filepath.Join(path, filename)); os.IsNotExist(err) {
		// File not there
		webClient := http.Client {
			Timeout: time.Second * 1}

			res, err := webClient.Get(url);

			if err == nil {
				// No error yet
				bodyBytes, err2 := ioutil.ReadAll(res.Body);
				if err2 == nil {
					// we goood
					ioutil.WriteFile(filepath.Join(path, filename), bodyBytes, os.ModePerm)
				}
				defer res.Body.Close();
			}
	}
}
