FROM python:3.10.6

ENV JWT secret
ENV MYSQL_HOST GestaoDeContasDB
ENV MYSQL_NAME GestaoDeContasDB
ENV MYSQL_USER gestao
ENV MYSQL_PASSWORD password
ENV MYSQL_PORT 3306

RUN apt-get update \
	&& apt-get install -y default-mysql-client\
	&& rm -rf /var/lib/apt/lists/*

WORKDIR /usr/src/app
COPY requirements.txt ./
COPY migrate.sh ./
RUN pip install -r requirements.txt
COPY ./app/GestaoDeContas ./
COPY initialdata.json ./


EXPOSE 8000
CMD ["bash", "./migrate.sh"]
