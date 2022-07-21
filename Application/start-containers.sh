#!/bin/bash

az container start --name payment-generator --resource-group payment-apps
az container start --name payment-enricher --resource-group payment-apps
az container start --name payment-writer --resource-group payment-apps
az container start --name payment-api --resource-group payment-apps