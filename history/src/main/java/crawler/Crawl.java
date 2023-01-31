package crawler;

import java.io.File;
import java.io.IOException;


public class Crawl {
    public static void run() throws IOException {
        File f1 = new File("data");
        boolean bool = f1.mkdir();
        if(bool) System.out.println("Created folder");
        else System.out.println("Existed folder");
        CrawlCharacterNKS crawlCharacter = new CrawlCharacterNKS();
        crawlCharacter.crawlAndSave();
        CrawlDynastyNKS crawlDynasty = new CrawlDynastyNKS();
        crawlDynasty.crawlAndSave();
        CrawlEventNKS crawlEvent = new CrawlEventNKS();
        crawlEvent.crawlAndSave();
        CrawlPlaceNKS crawlPlace = new CrawlPlaceNKS();
        crawlPlace.crawlAndSave();
        CrawlPlaceWiki crawlPlaceWiki = new CrawlPlaceWiki();
        crawlPlaceWiki.crawlAndSave();
        CrawlFestivalWiki crawlFestivalWiki = new CrawlFestivalWiki();
        crawlFestivalWiki.crawlAndSave();
    }
}