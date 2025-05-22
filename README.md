# CloudNativeApp_Backend

## MongoDB for developmnent

### Create container

```shell
    docker run -d --name cloudnative-mongo -p 27017:27017 -v mongo_data:/data/db -e MONGO_INITDB_DATABASE=cloudnativeapp_dev mongo:latest
```

Or to make sure the data is deleted each time the container is stopped

```shell
    docker run -d --name cloudnative-mongo \
    -p 27017:27017 \
    -e MONGO_INITDB_DATABASE=cloudnativeapp_dev \
    mongo:latest

```


### Basic commands

```shell
    docker start cloudnative-mongo

    docker stop cloudnative-mongo

    docker ps -a

    docker rm cloudnative-mongo
```

### Accessing MongoDB

```shell
    docker exec -it cloudnative-mongo mongosh
```


### Basic MongoDB Commands

```shell
    show dbs

    use cloudnativeapp_dev

    show collections

    db.<collectionName>.find()

    db.<collectionName>.insertOne({ username: "test", email: "test@example.com" })

    db.<collectionName>.deleteOne({ username: "test" })
```

## Testing/running Azure functions Locally (for development)

Install Azure Core Tools

Compile the backend functions

```shell
    mvn clean package
```
Change the OS in the pom.xml depending on your OS.

Run the functions in the backend folder with the command:

```shell
    func start
```
This will start the Azure Functions at [http://localhost:7071/api](http://localhost:7071/api)
