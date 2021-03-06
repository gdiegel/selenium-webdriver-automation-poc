# POC for browser automation with Selenium Webdriver

## Usage

```
# Check out project
git clone git@github.com:gdiegel/selenium-webdriver-automation-poc.git
cd selenium-webdriver-automation-poc
# Build project
mvn package
# Execute tests
java -jar target/app.jar
```

The browser used defaults to Firefox but can be explicitely set via the environment variable `BROWSER`:

```
BROWSER=chrome java -jar target/app.jar
```

The base URL can be set by via the environment variable `BASE_URL`:

```
BASE_URL=https://www.thermomix.com java -jar target/app.jar
```

## Requirements

* Maven 3.x.x
* JDK >11
* Firefox/Chrome binaries have to be installed locally
