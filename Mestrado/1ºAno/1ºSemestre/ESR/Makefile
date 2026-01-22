PYTHON = python3
SRC = src/
NEIGHBORS = neighbors/
CENARIO = cenario3.txt


bootstrapper:
	$(PYTHON) $(SRC)bootstrapper.py 10.0.2.2:4000 $(NEIGHBORS)$(CENARIO)

client1:
	export DISPLAY=:0.0; \
	$(PYTHON) $(SRC)clientLauncher.py 10.0.2.2:4000 10.0.17.21:3002 movie.Mjpeg

client2:
	export DISPLAY=:0.0; \
	$(PYTHON) $(SRC)clientLauncher.py 10.0.2.2:4000 10.0.17.20:3002 movie.Mjpeg

client3:
	export DISPLAY=:0.0; \
	$(PYTHON) $(SRC)clientLauncher.py 10.0.2.2:4000 10.0.20.22:3002 movie.Mjpeg

client4:
	export DISPLAY=:0.0; \
	$(PYTHON) $(SRC)clientLauncher.py 10.0.2.2:4000 10.0.20.20:3002 movie.Mjpeg

client5:
	export DISPLAY=:0.0; \
	$(PYTHON) $(SRC)clientLauncher.py 10.0.2.2:4000 10.0.18.22:3002 movie.Mjpeg


server1:
	$(PYTHON) $(SRC)serverLauncher.py 10.0.19.10:3001 ./videos/videos.txt

server2:
	$(PYTHON) $(SRC)serverLauncher.py 10.0.16.10:3001 ./videos/videos.txt

rp:
	$(PYTHON) $(SRC)rpLauncher.py 10.0.2.2:4000 10.0.20.1:3001 3002


node2:
	$(PYTHON) $(SRC)nodeLauncher.py 10.0.2.2:4000 10.0.1.2:3000 3001 3002

node3:
	$(PYTHON) $(SRC)nodeLauncher.py 10.0.2.2:4000 10.0.19.1:3000 3001 3002

node5: 	
	$(PYTHON) $(SRC)nodeLauncher.py 10.0.2.2:4000 10.0.16.1:3000 3001 3002

node6:	
	$(PYTHON) $(SRC)nodeLauncher.py 10.0.2.2:4000 10.0.17.1:3000 3001 3002
