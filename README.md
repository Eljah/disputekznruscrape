# disputekznruscrape

Simple Maven project demonstrating how to scrape pages of the site
`https://dispute.kzn.ru` using Selenium in headless mode.

## Build

Make sure Maven is installed. On Debian-based systems run:

```
sudo apt-get update && sudo apt-get install -y maven
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

This will create files `dispute-1.txt` ... `dispute-5.txt` in the working
directory.

## Troubleshooting

If you encounter a `SessionNotCreatedException` mentioning that the current
Chrome browser version does not match the downloaded ChromeDriver, clean the
cached driver and let WebDriverManager download the correct one:

```java
WebDriverManager.chromedriver().clearDriverCache().setup();
```

Alternatively make sure the installed Chrome browser and the driver versions
match (e.g. update Chrome or specify a driver version using
`WebDriverManager.chromedriver().browserVersion("<major>").setup();`).
