FROM node:20.19

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 8081

CMD ["npx", "expo", "start", "--web", "--host", "lan", "--port", "8081"]