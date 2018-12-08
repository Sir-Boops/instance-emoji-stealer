package main

import "fmt"
import "encoding/json"

type PayloadArr struct {
	Emoji []EmojiPayload
}

type EmojiPayload struct {
	ShortCode string `json:"shortcode"`
	Url string `json:"url"`
	StaticURL string `json:"static_url"`
	Visable bool `json:"visible_in_picker"`
}

func decode_json(domain string, payload string, path string) {

	Emojis := make([]EmojiPayload, 0)
	_ = json.Unmarshal([]byte(payload), &Emojis)

	jobs := make(chan int, len(Emojis))
	results := make(chan int, len(Emojis))

	fmt.Println("Number of emojis to download:" ,len(Emojis))

	// Start the workers
	for w := 0; w <= 99; w++ {
		go worker(Emojis, path, w, jobs, results)
  }

	for i := 0; i < len(Emojis); i++ {
		//jobChan <- download_emoji(Emojis[i].StaticURL, (Emojis[i].ShortCode + ".png"), path)
		jobs <- i
	}

	close(jobs)

	for i := 1; i <= len(Emojis); i++ {
		<- results
	}
}

func worker(Emojis []EmojiPayload, path string, id int, jobs <-chan int, results chan<- int) {
	for j := range jobs {
		download_emoji(Emojis[j].StaticURL, (Emojis[j].ShortCode + ".png"), path)
    fmt.Println("Downloaded emoji", Emojis[j].ShortCode)
    results <- j * 2
  }
}
