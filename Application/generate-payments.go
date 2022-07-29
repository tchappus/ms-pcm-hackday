package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"math"
	"math/rand"
	"net/http"
	"os"
	"time"
)

type InitiatedPayment struct {
	InternalParty string  `json:"internalParty"`
	ExternalParty string  `json:"externalParty"`
	Direction     string  `json:"direction"`
	Currency      string  `json:"currency"`
	Timestamp     string  `json:"timestamp"`
	Amount        float64 `json:"amount"`
}

func calc(x int) float64 {
	return 200*math.Sin(float64(0.009)*float64(x)) + float64(0.2)*float64(x)
}

func main() {

	internalParties := []string{"MSNYUS33SWP", "MSNYUS33COM", "MSNYUS33PBS", "MSPRUS33FXO"}
	externalParties := []string{"CITIUS33GPC"}
	currencies := []string{"USD", "EUR", "CAD", "GBP", "JPY"}

	starttime := time.Date(2022, time.July, 25, 1, 0, 0, 0, time.UTC)

	ratio := math.Pow(10, float64(2))

	prev := float64(0)
	for i := 1; i < 168; i++ {
		y := float64(calc(20 * i))

		amount := float64(math.Round(((y-prev)*100)*ratio) / ratio)

		prev = y

		timestamp := starttime.Add(time.Hour * time.Duration((i))).Format("2006-01-02T15:04:05")
		fmt.Println(timestamp, amount)

		dir := "into"
		if amount < 0 {
			dir = "outfr"
		}

		body := &InitiatedPayment{
			InternalParty: internalParties[rand.Intn(len(internalParties))],
			ExternalParty: externalParties[rand.Intn(len(externalParties))],
			Direction:     dir,
			Currency:      currencies[rand.Intn(len(currencies))],
			Timestamp:     timestamp,
			Amount:        amount,
		}

		payloadBuf := new(bytes.Buffer)
		json.NewEncoder(payloadBuf).Encode(body)
		req, _ := http.NewRequest("POST", "http://payment-apps42-generator.westus.azurecontainer.io/send", payloadBuf)
		req.Header.Add("Content-Type", "application/json")

		client := &http.Client{}
		res, e := client.Do(req)

		if e != nil {
			fmt.Println(e)
			return
		}

		fmt.Println("Response status:", res.Status)
		io.Copy(os.Stdout, res.Body)

		res.Body.Close()
	}
}
