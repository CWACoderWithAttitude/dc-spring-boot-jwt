#!/bin/sh

curl --verbose --location 'http://localhost:8088/api/auth/signup' \
--header 'Content-Type: application/json' \
--data '{
"username": "testuser",
"password": "testpass"
}'