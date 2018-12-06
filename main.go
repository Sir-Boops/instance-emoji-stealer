package main

import "os"
import "fmt"
import "path/filepath"

func main() {
	// Get current run path
	dir, _ := filepath.Abs(filepath.Dir(os.Args[0]))

	// Make sure run args len is 2
	if ( len(os.Args) > 2 || len(os.Args) < 2 ) {
		fmt.Println("Invaild amount of args");
		os.Exit(1);
	}

	fmt.Println("Trying to fetch emoji list from: " + os.Args[1]);
	payload := fetch_json_payload(os.Args[1])

	// Create the instance folder
	emojiPath := filepath.Join(dir, os.Args[1])
	os.Mkdir(emojiPath, os.ModePerm)

	decode_json(os.Args[1], payload, emojiPath)
}
