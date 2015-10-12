Консолидированный дашборд-портал, изначально кастомный вариант активити-эксплорера, предназначенный для работы в нем именно работников гос-службы (обработка заявок граждан), со временем там-же будут выводиться для "продвинутых админов" 

## Install

### Pre-requisites

    sudo apt-get install npm
    sudo npm install -g bower grunt-cli

### Project dependencies

    npm install
    bower install

### Configuration

To use remote testing backend server, do the following

    cp server/config/local.env.sample.js server/config/local.env.js

To use your own backend server, follow the instruction:
https://github.com/e-government-ua/i/wiki/%D0%A3%D1%81%D1%82%D0%B0%D0%BD%D0%BE%D0%B2%D0%BA%D0%B0-%D1%80%D0%B5%D0%B3%D0%B8%D0%BE%D0%BD%D0%B0%D0%BB%D1%8C%D0%BD%D0%BE%D0%B3%D0%BE-%D0%B4%D0%B0%D1%88%D0%B1%D0%BE%D1%80%D0%B4%D0%B0-%D1%87%D0%B8%D0%BD%D0%BE%D0%B2%D0%BD%D0%B8%D0%BA%D0%B0-(dashboard-js,-frontend)

## Run

    grunt serve

