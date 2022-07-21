#!/bin/bash

az container stop --name payment-generator --resource-group payment-apps
az container stop --name payment-enricher --resource-group payment-apps
az container stop --name payment-writer --resource-group payment-apps
az container stop --name payment-api --resource-group payment-apps