FROM node:18-alpine as build-step

WORKDIR /app

COPY package.json . /app/

RUN npm install

COPY . /app

RUN npm run build --prod

#segunda etapa
FROM nginx:alpine

COPY --from=build-step /app/dist/frontend/browser /usr/share/nginx/html
COPY default.conf /etc/nginx/conf.d/default.conf