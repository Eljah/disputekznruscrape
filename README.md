# disputekznruscrape

Simple Maven project demonstrating how to scrape pages of the site
`https://dispute.kzn.ru` using Selenium. By default it relies on the
lightweight `HtmlUnitDriver` so no real browser is required. You can
optionally use Chrome or headless Firefox by setting the `browser`
system property when running the program.

## Build

Make sure Maven is installed. On Debian-based systems run:

```
sudo apt-get update && sudo apt-get install -y maven
```
For convenience the repository includes a script `setup-maven.sh` which downloads Maven 3.9.9, configures proxy settings and preloads project dependencies. Run it if Maven is not already available:

```
./setup-maven.sh
```

If `apt-get` is not available you can manually download Maven from [Maven
Central](https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.6.3/),
unpack the archive and add the `bin` directory to your `PATH`.

```
mvn package
```

## Run

By default the program downloads the text of disputes with id from 1 to 3
and saves them to `dispute-<id>.txt`. You can specify a different upper
id as the first argument.

Two helper scripts are available to run the scraper with different
browsers:

* `./run-htmlunit.sh` – uses the lightweight HtmlUnit driver (default).
* `./run-chrome.sh` – launches Google Chrome in visible mode. If no
  display is available the script runs the browser via `xvfb-run`.

Example using HtmlUnit:

```
./run-htmlunit.sh 5
```

Example using Chrome:

```
./run-chrome.sh 5
```

Both the application and the tests automatically use the `HTTP_PROXY`
environment variable if it is present. In this environment network
access is only possible through the proxy at `http://proxy:8080`, so
ensure this variable is set when running the program or Maven tests.

You can still run with headless Firefox by specifying the system
property:

```
java -Dbrowser=firefox -cp target/disputescraper-1.0-SNAPSHOT.jar com.example.App 5
```

This will create files `dispute-1.txt` ... `dispute-5.txt` in the working
directory.

