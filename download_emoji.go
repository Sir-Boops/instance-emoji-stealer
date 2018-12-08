package main

import "os"
import "time"
import "net/http"
import "io/ioutil"
import "path/filepath"

func download_emoji(url string, filename string, path string) {

	if _, err := os.Stat(filepath.Join(path, filename)); os.IsNotExist(err) {
		// File not there

		tries := 0
		is_done := false

		for tries < 2 && !is_done {
			is_done = dl(url, filepath.Join(path, filename))
			tries++
		}

	}
}

func dl(url string, fullpath string) bool {

	webClient := http.Client {
		Timeout: time.Second * 1}

		res, err := webClient.Get(url);

		is_good := false

		if err == nil {
			// No error yet
			bodyBytes, err2 := ioutil.ReadAll(res.Body);
			if err2 == nil {
				// we goood
				ioutil.WriteFile(fullpath, bodyBytes, os.ModePerm)
				is_good = true
			}
			defer res.Body.Close();
		}
		return is_good
}
