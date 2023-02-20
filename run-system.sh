#!/bin/bash
./gradlew clean build
docker compose -f docker-compose-system.yaml up -d --build
