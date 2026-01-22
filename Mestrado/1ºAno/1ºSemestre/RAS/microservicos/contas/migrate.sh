#!/bin/bash

sleep 90
python manage.py makemigrations
python manage.py migrate
python manage.py loaddata initialdatas.json
python manage.py runserver 0.0.0.0:8000