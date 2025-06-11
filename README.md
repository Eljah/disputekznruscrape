# disputekznruscrape

Simple Maven project demonstrating how to scrape pages of the site
`https://dispute.kzn.ru` using Selenium in headless mode.

## Build

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
