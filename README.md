# disputekznruscrape

Simple Maven project demonstrating how to scrape pages of the site
`https://dispute.kzn.ru` using Selenium in headless mode.
By default it relies on the lightweight `HtmlUnitDriver` so no real
browser is required. You can optionally use headless Firefox by setting
the system property `browser=firefox` when running the program.

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

```
java -cp target/disputescraper-1.0-SNAPSHOT.jar com.example.App 5
```

Use `-Dbrowser=firefox` to run the scraper with headless Firefox instead of
HtmlUnit:

```
java -Dbrowser=firefox -cp target/disputescraper-1.0-SNAPSHOT.jar com.example.App 5
```

This will create files `dispute-1.txt` ... `dispute-5.txt` in the working
directory.

