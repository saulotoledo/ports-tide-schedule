# Ports tide schedule

The "Ports tide schedule" is a small Java Spring Boot application created for
demonstration purposes. It presents a list of endpoints with data regarding
a ports tide schedule.

## How to run the application

You will need [Docker](https://www.docker.com/) and [docker-compose](https://docs.docker.com/compose/).
If you do not have them already installed, please follow
[these](https://docs.docker.com/engine/install/) and [these](https://docs.docker.com/compose/install/)
setup instructions.

### 1. Clone this repository in your local machine

```sh
git clone REPO_URL
```

### 2. Setup environment configuration

You may set up your environment variables by creating a file `.env` in your project root.
You may copy the file `.env.example` and define your own values for the informed variables.

Please remember to update the Spring Boot configuration `.yml` files at `src/main/resources/`
accordingly.

### 3. Build and start the development containers

The following command will download the required images, prepare, and start the development
containers. The command will run in the background.

```sh
docker-compose up -d
```

From now on, we will execute commands in the application container:

```sh
docker exec -it smart-hardware-shop-app echo "running in container"
```

If you are running a Unix compatible environment, you can use the `ric.sh` script:

```sh
sh ric.sh echo "running in container"
```

or:

```sh
chmod +x ric.sh
./ric.sh echo "running in container"
```

We will use the `ric.sh` script in the following examples.

### 4. Test the application (optional)

```sh
sh ric.sh mvn clean test
```

### 5. Install the application

```sh
sh ric.sh mvn clean install
```

### 6. Start the application

```sh
sh ric.sh mvn spring-boot:run
```

## Accessing the application

The application is available at [http://localhost:8081/](http://localhost:8081/).

Some endpoints are paginated. For those cases, the following URL attributes are available:

- `page`: a zero-index value for the page
- `size`: the max number of elements per page

Example: `/tides/summary?page=2&size=5`.

### Available endpoints

#### `/tides/summary`

Returns a paginated summary of tides in the format below:

```json
{
  "items": [
    {
      "amplitude": 3.2,
      "datetime": "10-11-2021 01:20",
      "numberOfShipments": 1
    },
    ...
  ],
  "pageable": { ... },
  "last": false,
  "totalPages": 16,
  "totalElements": 305,
  "size": 20,
  "number": 0,
  "sort": { ... },
  "numberOfElements": 20,
  "first": true,
  "empty": false
}
```

#### `/tides/report`

Returns a paginated weekly report of the tides in the format below:

```json
{
  "items": [
    {
      "year": 2019,
      "week": 33,
      "numberOfShipments": 2,
      "totalQuantity": 75000
    },
    {
      "year": 2019,
      "week": 34,
      "numberOfShipments": 13,
      "totalQuantity": 521000
    },
    ...
  ],
  "pageable": { ... },
  "last": true,
  "totalPages": 1,
  "totalElements": 12,
  "size": 20,
  "number": 0,
  "sort": { ... },
  "numberOfElements": 12,
  "first": true,
  "empty": false
}
```
#### `/tides/report`

Returns an object containing KPI statistics regarding the tides in the format below:

```json
{
  "low": {
    "amplitude": {
      "average": 0.98601305
    }
  },
  "high": {
    "amplitude": {
      "average": 5.45875
    },
    "forShipments": {
      "total": 129,
      "assigned": 64
    }
  }
}
```

Shipments can only arrive on high tides with amplitude above a certain threshold.
This threshold can be defined at the [application.yml](src/main/resources/application.yml)
file at `tides.high.shipments.threshold`.
