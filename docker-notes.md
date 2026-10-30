TASK 1.

1.

 docker images
                                                            i Info →   U  In Use
IMAGE                ID             DISK USAGE   CONTENT SIZE   EXTRA
hello-world:latest   5e2309035332       25.9kB         9.49kB    U
nginx:1.27           6784fb0834aa        282MB         75.5MB
nginx:1.27-alpine    65645c7bb6a0       74.5MB         21.9MB


The alpine image is smaller because It user lighter version of Linux distribution where as the other image uses much larger Debian based distro.

2.

 docker run -d --name web -p 8090:80 nginx

 docker ps
CONTAINER ID   IMAGE     COMMAND                  CREATED         STATUS         PORTS                                     NAMES
d5d7e9a67d03   nginx     "/docker-entrypoint.…"   2 minutes ago   Up 2 minutes   0.0.0.0:8090->80/tcp, [::]:8090->80/tcp   web


curl http://localhost:8090
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
html { color-scheme: light dark; }
body { width: 35em; margin: 0 auto;
font-family: Tahoma, Verdana, Arial, sans-serif; }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, nginx is successfully installed and working.
Further configuration is required for the web server, reverse proxy,
API gateway, load balancer, content cache, or other features.</p>

<p>For online documentation and support please refer to
<a href="https://nginx.org/">nginx.org</a>.<br/>
To engage with the community please visit
<a href="https://community.nginx.org/">community.nginx.org</a>.<br/>
For enterprise grade support, professional services, additional
security features and capabilities please refer to
<a href="https://f5.com/nginx">f5.com/nginx</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>

3.
docker run -d --name web2 -p 8091:80 nginx
6400b28972916fd7952fe1a09a35189e05124a2e51d176ddb4736f2bf559bdba
ubuntu@CI-SERVER1:~/assignment6$ docker ps
CONTAINER ID   IMAGE     COMMAND                  CREATED          STATUS          PORTS                                     NAMES
6400b2897291   nginx     "/docker-entrypoint.…"   11 seconds ago   Up 10 seconds   0.0.0.0:8091->80/tcp, [::]:8091->80/tcp   web2
d5d7e9a67d03   nginx     "/docker-entrypoint.…"   4 minutes ago    Up 4 minutes    0.0.0.0:8090->80/tcp, [::]:8090->80/tcp   web
ubuntu@CI-SERVER1:~/assignment6$ docker run -d --name web3 -p 8090:80 nginx
61ab9cec31677969df82ae66b710ad05bbe3dc5eb644a8f7b9d183ef25fb2979
docker: Error response from daemon: failed to set up container networking: driver failed programming external connectivity on endpoint web3 (d374d2a32c5537096742aff20e7a879701cb79fd06020a479e23e0adafb7bb9b): Bind for 0.0.0.0:8090 failed: port is already allocated


4.
Made three curl request and got the logs for it

172.17.0.1 - - [08/Oct/2026:07:12:43 +0000] "GET / HTTP/1.1" 200 896 "-" "curl/8.18.0" "-"
172.17.0.1 - - [08/Oct/2026:07:12:57 +0000] "GET / HTTP/1.1" 200 896 "-" "curl/8.18.0" "-"
172.17.0.1 - - [08/Oct/2026:07:13:00 +0000] "GET / HTTP/1.1" 200 896 "-" "curl/8.18.0" "-"
Each curl request generated new entry in the access log.

5.

 docker exec -it web /bin/bash

 vim /usr/share/nginx/html/index.html

curl http://localhost:8090
THIS IS MY TEST WEBSITE


6.
I stopped and started the container the content did survive

Started a new web container after deleting the old one.

docker run -d --name web -p 8090:80 nginx
7a9d1569335ddc875b0a6fa2c10e415cc0878b2e8388dcadc8c2f2bbc780c375
ubuntu@CI-SERVER1:~/assignment6$ docker ps
CONTAINER ID   IMAGE     COMMAND                  CREATED          STATUS          PORTS                                     NAMES
7a9d1569335d   nginx     "/docker-entrypoint.…"   3 seconds ago    Up 2 seconds    0.0.0.0:8090->80/tcp, [::]:8090->80/tcp   web
6400b2897291   nginx     "/docker-entrypoint.…"   21 minutes ago   Up 21 minutes   0.0.0.0:8091->80/tcp, [::]:8091->80/tcp   web2

The content did not survive after I created a web container from same image.

7.
docker inspect web

"Gateway": "172.17.0.1",
                    "IPAddress": "172.17.0.2",
                    "MacAddress": "ca:0f:7a:f5:92:ee",
                    "IPPrefixLen": 16,
                    "IPv6Gateway": "",
                    "GlobalIPv6Address": "",
                    "GlobalIPv6PrefixLen": 0,
                    "DNSNames": null

 docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' web
172.17.0.2


8.

 docker run --rm alpine date
Unable to find image 'alpine:latest' locally
latest: Pulling from library/alpine
e2de96513ba9: Pull complete
6d0606d1815c: Download complete
797dd00a0fc7: Download complete
Digest: sha256:294b683cb724975bec92580e1e685676bd4b50bda910ddb8c51d4cabeaec77e6
Status: Downloaded newer image for alpine:latest
Thu Oct  8 07:35:44 UTC 2026

 docker ps -a
CONTAINER ID   IMAGE         COMMAND                  CREATED          STATUS                   PORTS                                     NAMES
7a9d1569335d   nginx         "/docker-entrypoint.…"   9 minutes ago    Up 9 minutes             0.0.0.0:8090->80/tcp, [::]:8090->80/tcp   web
61ab9cec3167   nginx         "/docker-entrypoint.…"   29 minutes ago   Created                                                            web3
6400b2897291   nginx         "/docker-entrypoint.…"   30 minutes ago   Up 30 minutes            0.0.0.0:8091->80/tcp, [::]:8091->80/tcp   web2
f38491edd139   hello-world   "/hello"                 13 days ago      Exited (0) 13 days ago                                             keen_colden


The --rm flag is usefull when we need to run one time task.

9.

docker ps -a
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
ubuntu@CI-SERVER1:~$

TASK 2 A.

1.
 docker volume ls
DRIVER    VOLUME NAME
local     pgdata

 docker ps
CONTAINER ID   IMAGE         COMMAND                  CREATED         STATUS         PORTS      NAMES
a5a368614905   postgres:16   "docker-entrypoint.s…"   3 minutes ago   Up 3 minutes   5432/tcp   postgres


2.

 docker exec -it postgres psql -U postgres

postgres=# SELECT * FROM users;
 id |  name
----+--------
  1 | Naveen
(1 row)


3.

 docker volume ls
DRIVER    VOLUME NAME
local     pgdata

docker exec -it postgres1 psql -U postgres
psql (16.15 (Debian 16.15-1.pgdg13+2))
Type "help" for help.

postgres=# \dt
         List of relations
 Schema | Name  | Type  |  Owner
--------+-------+-------+----------
 public | users | table | postgres
(1 row)

postgres=# SELECT * FROM users;
 id |  name
----+--------
  1 | Naveen
(1 row)

4.
docker volume inspect pgdata
[
    {
        "CreatedAt": "2026-10-08T08:01:20Z",
        "Driver": "local",
        "Labels": null,
        "Mountpoint": "/var/lib/docker/volumes/pgdata/_data",
        "Name": "pgdata",
        "Options": null,
        "Scope": "local"
    }
]

5.

docker run --rm \
  -v pgdata:/source:ro \
  -v "$PWD":/backup \
  alpine \
  tar czf /backup/pgdata-backup.tar.gz -C /source .
ubuntu@CI-SERVER1:~/assignment6$ docker ps -a
CONTAINER ID   IMAGE         COMMAND                  CREATED          STATUS          PORTS      NAMES
6aec72763e50   postgres:16   "docker-entrypoint.s…"   32 minutes ago   Up 32 minutes   5432/tcp   postgres1
ubuntu@CI-SERVER1:~/assignment6$ docker ps
CONTAINER ID   IMAGE         COMMAND                  CREATED          STATUS          PORTS      NAMES
6aec72763e50   postgres:16   "docker-entrypoint.s…"   32 minutes ago   Up 32 minutes   5432/tcp   postgres1
ubuntu@CI-SERVER1:~/assignment6$ ls
docker-notes.md  pgdata-backup.tar.gz

6.
pg_dump understands PostgreSQL's database structure and creates a logical backup of tables, schemas, and data in a consistent way while PostgreSQL is running.
The tar approach copies the database's physical files directly, so if PostgreSQL is actively writing during the backup, the files may be captured in an inconsistent state.


PART B.
1.

  docker run -d --name webtest -p 8090:80 -v $PWD/webfold:/usr/share/nginx/html nginx
0a85ce778c2ba528f823980263d56b7fbaf0d3d5ea2655485a10e4ce714216c3


 docker ps
CONTAINER ID   IMAGE         COMMAND                  CREATED         STATUS         PORTS                                     NAMES
0a85ce778c2b   nginx         "/docker-entrypoint.…"   2 minutes ago   Up 2 minutes   0.0.0.0:8090->80/tcp, [::]:8090->80/tcp   webtest

ubuntu@CI-SERVER1:~/assignment6/webfold$ curl http://localhost:8090             TESTING BIND MOUNTS


2.
 vim index.html

UPDATING ON HOST FILE

updated and got the above change

Here we updated the host file(bind mount) where as in task 1 we had logged into the web container file system and changed the index.html file

3.
 docker rm -f webtest
webtest
ubuntu@CI-SERVER1:~/assignment6/webfold$ docker run -d --name webtest -p 8090:80 -v $PWD/webfold:/usr/share/nginx/html:ro nginx
ac51e5aceb9229ca4bf402324d91f403095314b22243d5bf1922c162954304a6
ubuntu@CI-SERVER1:~/assignment6/webfold$ docker ps
CONTAINER ID   IMAGE         COMMAND                  CREATED         STATUS         PORTS                                     NAMES
ac51e5aceb92   nginx         "/docker-entrypoint.…"   3 seconds ago   Up 2 seconds   0.0.0.0:8090->80/tcp, [::]:8090->80/tcp   webtest

 echo "Hello from inside the container!" > /usr/share/nginx/html/index.html
bash: /usr/share/nginx/html/index.html: Read-only file system


4.
Bind mount we use for development where we want to directly access the host so make changes .
Nmaed volume we use it in production where docker manages persistent data where new container can use the same volume if need be.

PART C.

1.
docker run -d --name web1 nginx
cb9ab39b486f4d03d06de52822025db4025ffe90436e3c24bb5698e1d593910e
ubuntu@CI-SERVER1:~/assignment6$ docker run -d --name web2 nginx
f32b6383f09ae9e40c568e48e58a525a726cf84f337b17cfe6b42b68f344abc7

 ping web1 -c 4
ping: web1: Name or service not known
root@cb9ab39b486f:/# ping 172.17.0.5 -c 4
PING 172.17.0.5 (172.17.0.5) 56(84) bytes of data.
64 bytes from 172.17.0.5: icmp_seq=1 ttl=64 time=0.082 ms
64 bytes from 172.17.0.5: icmp_seq=2 ttl=64 time=0.055 ms
64 bytes from 172.17.0.5: icmp_seq=3 ttl=64 time=0.041 ms
64 bytes from 172.17.0.5: icmp_seq=4 ttl=64 time=0.066 ms

--- 172.17.0.5 ping statistics ---
4 packets transmitted, 4 received, 0% packet loss, time 3105ms
rtt min/avg/max/mdev = 0.041/0.061/0.082/0.015 ms

2.
 docker network create mynetwork
0918a694e56d6721c1ba292676b530dd804a49fe9edea344f24276083b1ccf34
ubuntu@CI-SERVER1:~/assignment6$ docker network ls
NETWORK ID     NAME        DRIVER    SCOPE
82208bea3922   bridge      bridge    local
ef0bcbde0ac3   host        host      local
0918a694e56d   mynetwork   bridge    local
496b4a66ab6e   none        null      local


docker run -d --name web1 --network mynetwork nginx
d442650983323d8a4a93fda4918ef50433cc11bbc280c528eb9f54144acd6c80
ubuntu@CI-SERVER1:~/assignment6$ docker run -d --name web2 --network mynetwork nginx
293b7f16d339a22bfb57fdabb9362697020aacbc6189eeee2d35efc61058386e


 ping 172.18.0.3 -c 4
PING 172.18.0.3 (172.18.0.3) 56(84) bytes of data.
64 bytes from 172.18.0.3: icmp_seq=1 ttl=64 time=0.078 ms
64 bytes from 172.18.0.3: icmp_seq=2 ttl=64 time=0.065 ms
64 bytes from 172.18.0.3: icmp_seq=3 ttl=64 time=0.068 ms
64 bytes from 172.18.0.3: icmp_seq=4 ttl=64 time=0.053 ms

--- 172.18.0.3 ping statistics ---
4 packets transmitted, 4 received, 0% packet loss, time 3066ms
rtt min/avg/max/mdev = 0.053/0.066/0.078/0.008 ms
root@d44265098332:/# ping web2 -c 4
PING web2 (172.18.0.3) 56(84) bytes of data.
64 bytes from web2.mynetwork (172.18.0.3): icmp_seq=1 ttl=64 time=0.042 ms
64 bytes from web2.mynetwork (172.18.0.3): icmp_seq=2 ttl=64 time=0.074 ms
64 bytes from web2.mynetwork (172.18.0.3): icmp_seq=3 ttl=64 time=0.071 ms
64 bytes from web2.mynetwork (172.18.0.3): icmp_seq=4 ttl=64 time=0.068 ms

--- web2 ping statistics ---
4 packets transmitted, 4 received, 0% packet loss, time 3047ms
rtt min/avg/max/mdev = 0.042/0.063/0.074/0.012 ms


A user defined bridge network provides automatic container-name DNS resolution but the default bridge network does not provide automatic container name resolution.

3.
docker network ls
NETWORK ID     NAME        DRIVER    SCOPE
5dc691234494   appnet      bridge    local
82208bea3922   bridge      bridge    local
ef0bcbde0ac3   host        host      local
0918a694e56d   mynetwork   bridge    local
496b4a66ab6e   none        null      local


 docker run -d --name DB --network appnet -e POSTGRES_PASSWORD=pass123 postgres:16
8d1ab57a834b02459e6f8fd93f752d8a7ef154a5e7cdc2008cc509364ec1f804

 docker run -d --name APP --network appnet -p 9090:80 nginx
e8c2a2a74006476e32a784be23ee059f7515b203ce2e1afcfa742d2197064bbb

4.
 nc -zv localhost 5432
nc: connect to localhost (127.0.0.1) port 5432 (tcp) failed: Connection refused

The connection was refused when trying to connect from host.


 psql -h DB -U postgres -W
Password:
psql (17.11 (Debian 17.11-0+deb13u1), server 16.15 (Debian 16.15-1.pgdg13+2))
Type "help" for help.

postgres=#

I was able to connect to DB container from APP container using postgres client.


TASK 3.

1.

  cat Dockerfile
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .
CMD ["java", "-jar", "target/invoice-service-1.0-SNAPSHOT.jar"]


-------------

 docker images
                                                            i Info →   U  In Use
IMAGE                     ID             DISK USAGE   CONTENT SIZE   EXTRA
invoice-service:1.0-fat   c86291eebe79        734MB          228MB


2.
 docker run -d --name invoice -p 8090:80 invoice-service:1.0-fat

 docker logs invoice
Customer name: Naveen
Is name empty? false


3.

cat .dockerignore
.git
.gitignore
pom.xml
src/
target/*
!target/invoice-service-1.0-SNAPSHOT.jar

Before we included .dockerignore the docker builder was getting all the files from the current directory after including .dockerignore it just got the jar file for build.

4.

 cat Dockerfile

FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -B clean package


FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /build/target/invoice-service-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


 docker images
                                                            i Info →   U  In Use
IMAGE                     ID             DISK USAGE   CONTENT SIZE   EXTRA
invoice-service:1.0       4783d6f8cfb5        438MB          113MB
invoice-service:1.0-fat   f6be08bd5115        734MB          228MB

In single stage we used FROM image as jdk which is larger but in multistage we used jre image which is lighter so we could reduce the size of the image.

5.
Disk usage reduction: (734−438)/734×100=40.3%

Content size reduction: (228−113)/228×100=50.4%

Asmaller image is easier to pull,push and deploy lesser cpu times and also it reduces the attack surfaces by excluding unnecessary files and security vulnerabilities.

6.
cat Dockerfile
# Stage 1: Build the application using Maven and JDK
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package


# Stage 2: Run the application using JRE only
FROM eclipse-temurin:17-jre

# Create a dedicated non-root group and user
RUN groupadd --system appgroup \
    && useradd --system --uid 10001 --gid appgroup --no-create-home appuser

WORKDIR /app

# Copy the executable JAR from the builder stage
COPY --from=builder --chown=appuser:appgroup /build/target/invoice-service-1.0-SNAPSHOT.jar app.jar

# Run as a non-root user
USER appuser:appgroup

ENTRYPOINT ["java", "-jar", "app.jar"]


 docker run -d --name invoice-nonroot-check --entrypoint sleep invoice-service:1.0 infinity
fbda89d37259c93c59d0a7922aff69744f6cbca0f345d4f1923570c3a6f3d0bd

ubuntu@CI-SERVER1:~/assignment6/invoice-service$ docker ps
CONTAINER ID   IMAGE                 COMMAND            CREATED         STATUS         PORTS     NAMES
fbda89d37259   invoice-service:1.0   "sleep infinity"   4 seconds ago   Up 3 seconds             invoice-nonroot-check
ubuntu@CI-SERVER1:~/assignment6/invoice-service$ docker exec invoice-nonroot-check id
uid=10001(appuser) gid=999(appgroup) groups=999(appgroup)

7.
 docker images
                                                            i Info →   U  In Use
IMAGE                     ID             DISK USAGE   CONTENT SIZE   EXTRA
invoice-service:1.0       631fec010e85        438MB          113MB
invoice-service:1.0-fat   f6be08bd5115        734MB          228MB
ubuntu@CI-SERVER1:~/assignment6/invoice-service$ docker run -d \
  --name invoice-health \
  -p 8090:8080 \
  invoice-service:1.0
3914a8e5e89146deac77ec7e429c610612f689eb714c0ad0f9ab34b87610ec57
ubuntu@CI-SERVER1:~/assignment6/invoice-service$ sleep 12
ubuntu@CI-SERVER1:~/assignment6/invoice-service$ docker ps --filter name=invoice-health
CONTAINER ID   IMAGE                 COMMAND               CREATED              STATUS                        PORTS                                         NAMES
3914a8e5e891   invoice-service:1.0   "java -jar app.jar"   About a minute ago   Up About a minute (healthy)   0.0.0.0:8090->8080/tcp, [::]:8090->8080/tcp   invoice-health
ubuntu@CI-SERVER1:~/assignment6/invoice-service$


8.
The steps rebuilt were

COPY src ./src
RUN mvn -B clean package
COPY --from=builder ... app.jar

#10 [builder 4/5] COPY src ./src
#10 DONE

#11 [builder 5/5] RUN mvn -B clean package
#11 DONE

#14 [stage-1 4/4] COPY --from=builder ... app.jar
#14 DONE


TASK 4 A.
1.

 cat compose.yaml

services:
  app:
    build:
      context: ../invoice-service
      dockerfile: ../compose/Dockerfile.compose
    image: invoice-service:task4
    ports:
      - "8091:8080"
    environment:
      DB_HOST: postgres
      DB_PORT: "5432"
      DB_NAME: ${POSTGRES_DB}
      DB_USER: ${POSTGRES_USER}
      DB_PASSWORD: ${POSTGRES_PASSWORD}
    depends_on:
      - postgres
    networks:
      - app-network

  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    volumes:
      - pgdata:/var/lib/postgresql/data
    networks:
      - app-network

networks:
  app-network:
    driver: bridge

volumes:
  pgdata:


2.
created .env file and .gitignore file

3.
docker compose up --build -d

curl http://localhost:8091/health
OK

docker run --rm --net compose_app-network --env-file .env --entrypoint sh postgres:16-alpine -c 'PGPASSWORD="$POSTGRES_PASSWORD" psql -h postgres -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "SELECT current_database(), current_user;"'

 docker compose up -d postgres
[+] up 1/1
 ✔ Container compose-postgres-1 Started                                     0.1s
ubuntu@CI-SERVER1:~/assignment6/compose$ docker compose ps
NAME                 IMAGE                COMMAND                  SERVICE    CREATED       STATUS         PORTS
compose-postgres-1   postgres:16-alpine   "docker-entrypoint.s…"   postgres   3 hours ago   Up 7 seconds   5432/tcp
ubuntu@CI-SERVER1:~/assignment6/compose$ docker run --rm --net compose_app-network --env-file .env --entrypoint sh postgres:16-alpine -c 'PGPASSWORD="$POSTGRES_PASSWORD" psql -h postgres -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "SELECT current_database(), current_user;"'
 current_database | current_user
------------------+--------------
 invoice_db       | invoice_user
(1 row)


6.

 cat compose.yaml

services:
  app:
    build:
      context: ../invoice-service
      dockerfile: ../compose/Dockerfile.compose
    image: invoice-service:task4
    ports:
      - "8091:8080"
    environment:
      DB_HOST: postgres
      DB_PORT: "5432"
      DB_NAME: ${POSTGRES_DB}
      DB_USER: ${POSTGRES_USER}
      DB_PASSWORD: ${POSTGRES_PASSWORD}
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - app-network

  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    volumes:
      - pgdata:/var/lib/postgresql/data
    healthcheck:
      healthcheck:
      test: ["CMD-SHELL", "pg_isready -U $${POSTGRES_USER} -d $${POSTGRES_DB}"]
      interval: 5s
      timeout: 3s
      retries: 10
      start_period: 5s
    networks:
      - app-network

networks:
  app-network:
    driver: bridge

volumes:
  pgdata:


4.

The data survived because the volume was a named volume and it is a persistent one pg_data.

docker compose down -v

this is would have destroyed the volume the -v flag deletes the volume when we use compose down with -v.

5.

The application was changed and used the command
docker compose up --build -d

the compose rebuilt the image and used the cache wherever there was no change.

 => resolving provenance for metadata file                                 0.0s
[+] up 4/4
 ✔ Image invoice-service:task4  Built                                      12.3s
 ✔ Network compose_app-network  Created                                     0.0s
 ✔ Container compose-postgres-1 Started                                     0.2s
 ✔ Container compose-app-1      Started

6.
services:
  app:
    build:
      context: ../invoice-service
      dockerfile: ../compose/Dockerfile.compose
    image: invoice-service:task4
    ports:
      - "8091:8080"
    environment:
      DB_HOST: postgres
      DB_PORT: "5432"
      DB_NAME: ${POSTGRES_DB}
      DB_USER: ${POSTGRES_USER}
      DB_PASSWORD: ${POSTGRES_PASSWORD}
    depends_on:
      postgres:
        condition: service_healthy
    networks:
      - app-network

  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    volumes:
      - pgdata:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U $${POSTGRES_USER} -d $${POSTGRES_DB}"]
      interval: 5s
      timeout: 3s
      retries: 10
      start_period: 5s
    networks:
      - app-network

networks:
  app-network:
    driver: bridge

volumes:
  pgdata:


 docker compose up -d
[+] up 3/3
 ✔ Network compose_app-network  Created                                     0.0s
 ✔ Container compose-postgres-1 Healthy                                     5.3s
 ✔ Container compose-app-1      Started

depends_on alone controls the order in which Compose starts services. It does not guarantee that a dependency is ready to accept connections.

PART B.
1.
 docker stack deploy --resolve-image never -c compose.swarm.yaml invoice-stack
Since --detach=false was not specified, tasks will be created in the background.
In a future release, --detach=false will become the default.
Creating network invoice-stack_app-network
Creating service invoice-stack_app
Creating service invoice-stack_postgres


 docker stack ls
NAME            SERVICES
invoice-stack   2

 docker stack services invoice-stack
ID             NAME                     MODE         REPLICAS   IMAGE                   PORTS
txcqk08w5bh5   invoice-stack_app        replicated   1/1        invoice-service:task4   *:8092->8080/tcp
zgmwnnzml4lo   invoice-stack_postgres   replicated   1/1        postgres:16-alpine

2.
 docker stack ps invoice-stack
ID             NAME                       IMAGE                   NODE         DESIRED STATE   CURRENT STATE           ERROR     PORTS
njuz2rnu4h36   invoice-stack_app.1        invoice-service:task4   CI-SERVER1   Running         Running 3 minutes ago
985gu6u86pli   invoice-stack_postgres.1   postgres:16-alpine      CI-SERVER1   Running         Running 3 minutes ago


3.
docker service scale invoice-stack_app=3
invoice-stack_app scaled to 3
overall progress: 3 out of 3 tasks
1/3: running
2/3: running
3/3: running
verify: Service invoice-stack_app converged

 docker stack services invoice-stack
ID             NAME                     MODE         REPLICAS   IMAGE                   PORTS
txcqk08w5bh5   invoice-stack_app        replicated   3/3        invoice-service:task4   *:8092->8080/tcp
zgmwnnzml4lo   invoice-stack_postgres   replicated   1/1        postgres:16-alpine


4.

 docker ps --filter label=com.docker.swarm.service.name=invoice-stack_app
CONTAINER ID   IMAGE                   COMMAND               CREATED         STATUS         PORTS      NAMES
4f7b85d1d882   invoice-service:task4   "java -jar app.jar"   2 minutes ago   Up 2 minutes   8080/tcp   invoice-stack_app.2.w3opygdhbvk30oanar77yjq0e
e44501ec4814   invoice-service:task4   "java -jar app.jar"   2 minutes ago   Up 2 minutes   8080/tcp   invoice-stack_app.3.xlijm15wm2bq1js02uc2ae1tg
fe4f0281e831   invoice-service:task4   "java -jar app.jar"   8 minutes ago   Up 8 minutes   8080/tcp   invoice-stack_app.1.njuz2rnu4h36inrwq33mvbet4
ubuntu@CI-SERVER1:~/assignment6/compose$ docker rm -f 4f7b85d1d882
4f7b85d1d882


 docker service ps invoice-stack_app
ID             NAME                      IMAGE                   NODE         DESIRED STATE   CURRENT STATE            ERROR                         PORTS
njuz2rnu4h36   invoice-stack_app.1       invoice-service:task4   CI-SERVER1   Running         Running 10 minutes ago
d0lqiyd20awb   invoice-stack_app.2       invoice-service:task4   CI-SERVER1   Running         Running 54 seconds ago
w3opygdhbvk3    \_ invoice-stack_app.2   invoice-service:task4   CI-SERVER1   Shutdown        Failed 59 seconds ago    "task: non-zero exit (137)"
xlijm15wm2bq   invoice-stack_app.3       invoice-service:task4   CI-SERVER1   Running         Running 4 minutes ago

I had forcibly deleted one running application container docker swarm detected the desired replica as 3  and recreated a new running container to maintain the desired count.

5.

docker service ps invoice-stack_app
docker stack services invoice-stack
ID             NAME                      IMAGE                      NODE         DESIRED STATE   CURRENT STATE                 ERROR                         PORTS
ozmjv1lugdgl   invoice-stack_app.1       invoice-service:task4-v2   CI-SERVER1   Running         Running about a minute ago
njuz2rnu4h36    \_ invoice-stack_app.1   invoice-service:task4      CI-SERVER1   Shutdown        Shutdown about a minute ago
10646e0dkgab   invoice-stack_app.2       invoice-service:task4-v2   CI-SERVER1   Running         Running about a minute ago
d0lqiyd20awb    \_ invoice-stack_app.2   invoice-service:task4      CI-SERVER1   Shutdown        Shutdown about a minute ago
w3opygdhbvk3    \_ invoice-stack_app.2   invoice-service:task4      CI-SERVER1   Shutdown        Failed 8 minutes ago          "task: non-zero exit (137)"
2d7m54wvklex   invoice-stack_app.3       invoice-service:task4-v2   CI-SERVER1   Running         Running about a minute ago
xlijm15wm2bq    \_ invoice-stack_app.3   invoice-service:task4      CI-SERVER1   Shutdown        Shutdown about a minute ago
ID             NAME                     MODE         REPLICAS   IMAGE                      PORTS
txcqk08w5bh5   invoice-stack_app        replicated   3/3        invoice-service:task4-v2   *:8092->8080/tcp
zgmwnnzml4lo   invoice-stack_postgres   replicated   1/1        postgres:16-alpine


docker service rollback invoice-stack_app
invoice-stack_app
rollback: manually requested rollback
overall progress: rolling back update: 3 out of 3 tasks
1/3: running
2/3: running
3/3: running
verify: Service invoice-stack_app converged


 docker service ps invoice-stack_app
ID             NAME                      IMAGE                      NODE         DESIRED STATE   CURRENT STATE                 ERROR                         PORTS
xnd1nlobz29i   invoice-stack_app.1       invoice-service:task4      CI-SERVER1   Running         Running about a minute ago
ozmjv1lugdgl    \_ invoice-stack_app.1   invoice-service:task4-v2   CI-SERVER1   Shutdown        Shutdown about a minute ago
njuz2rnu4h36    \_ invoice-stack_app.1   invoice-service:task4      CI-SERVER1   Shutdown        Shutdown 4 minutes ago
h32ho1kg9qg0   invoice-stack_app.2       invoice-service:task4      CI-SERVER1   Running         Running about a minute ago
10646e0dkgab    \_ invoice-stack_app.2   invoice-service:task4-v2   CI-SERVER1   Shutdown        Shutdown about a minute ago
d0lqiyd20awb    \_ invoice-stack_app.2   invoice-service:task4      CI-SERVER1   Shutdown        Shutdown 4 minutes ago
w3opygdhbvk3    \_ invoice-stack_app.2   invoice-service:task4      CI-SERVER1   Shutdown        Failed 11 minutes ago         "task: non-zero exit (137)"
6oy1lm29xoi2   invoice-stack_app.3       invoice-service:task4      CI-SERVER1   Running         Running about a minute ago
2d7m54wvklex    \_ invoice-stack_app.3   invoice-service:task4-v2   CI-SERVER1   Shutdown        Shutdown about a minute ago
xlijm15wm2bq    \_ invoice-stack_app.3   invoice-service:task4      CI-SERVER1   Shutdown        Shutdown 4 minutes ago

Rolling update showed : Recorded the old tasks, replacement tasks, their current states, and the node shown in the output

Rollback showed : Record the replacement tasks using the original image and the shutdown tasks from the newer version


6.

Docker Compose allows us to define and run multiple containers together using a single YAML file, instead of starting each container individually with docker run commands.

Docker Swarm helps in container orchestration across a cluster of nodes, providing features like replica management, desired-state reconciliation, rolling updates, and rollbacks.

PART C.
