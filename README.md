# mc-pcm-hackday

These components were developed as part of a hackathon project. The concept was to create a proof-of-concept payment system using the following technologies:
* Azure Service Bus
* Azure Cosmos DB
* Redis Cache
* Docker
* GitHub Actions
* Reactive Spring

A React front-end was also implemented: [ms-pcm-hackday-ui](https://github.com/tchappus/ms-pcm-hackday-ui)

The components included are:

* payment-generator, it exposes an API to initiate messages and put them on a Service Bus queue
* payment-enricher takes the messages off of the queue, fetches BIC data from Redis and enriches the content before putting the message on another queue
* payment-writer takes the message and persists it in Cosmos DB
* payment-api exposes an endpoint to create an event stream from the Cosmos changefeed. The stream can be subscribed to by the React front-end which displays realtime charts.

The components deployed in Docker containers using GitHub Actions.