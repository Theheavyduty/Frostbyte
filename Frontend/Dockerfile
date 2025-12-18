FROM node:20-alpine

WORKDIR /app

RUN apk add --no-cache bash

# Install deps (cached layer)
COPY package*.json ./
RUN npm ci

# Copy the rest
COPY . .

# Expo dev tools / web
ENV EXPO_DEVTOOLS_LISTEN_ADDRESS=0.0.0.0

# Web will run on 8082
EXPOSE 8082

CMD ["npx", "expo", "start", "--web", "--port", "8082"]
