#!/bin/bash
./gradlew clean build
cd ../sys-echo-ui; npm run build; cd ../sys-echo-rest
rm -rf ./build/sys-echo-ui || true; mkdir ./build/sys-echo-ui; cp -r ../sys-echo-ui/build/* ./build/sys-echo-ui
docker compose -f docker-compose-system.yaml up -d --build
