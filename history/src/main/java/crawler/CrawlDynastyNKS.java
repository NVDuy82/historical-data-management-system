package crawler;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp dùng để thu thập triều đại lịch sử trên Wikipedia
 * @since: 15/01/2023
 */
public class CrawlDynastyNKS extends Crawler{
    public CrawlDynastyNKS() {
        super.setRoot("https://nguoikesu.com");
        super.setStart("https://nguoikesu.com/");
        super.setFolder("data\\Dynastynks.json");
    }

    /**
     * Thu thập dữ liệu ở trang web chỉ định
     * @param url
     * @throws IOException
     */
    @Override
    public void scrapePage(String url) throws IOException {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Jsoup client").timeout(20000).get();
            Elements link = doc.select("body > div:first-of-type > div:nth-of-type(4) > div > div > div:first-of-type > div > div:first-of-type > div > div > ul > li > a");
            for (Element element:link){
                // Name and ID
                String name = element.text();
                String id = super.getRoot() + element.attr("href");
                System.out.println(name);
                System.out.println(id);
                JSONObject object = new JSONObject();
                object.put("name",name);
                object.put("id", id);
                // Information
                String information = "";
                information += scrapeInformation(id,"div.category-desc.clearfix > p");
                object.put("info", information);
                // Connection
                doc = Jsoup.connect(id).userAgent("Jsoup client").timeout(20000).get();
                Elements connect = doc.select("body > div:first-of-type > div:nth-of-type(4) > div > div > main > div:nth-of-type(2) > div:nth-of-type(2) > div > div > div > h2 > a");
                List<String> urls = new ArrayList<>();
                for (Element element1:connect) urls.add(super.getRoot() + element1.attr("href"));
                object.put("connect",scrapeConnect(urls,"p > a"));

                super.getOutput().add(object);
                System.out.println(super.getOutput().size());
                //
            }
        } catch (UnknownHostException e){
            throw new UnknownHostException("Turn on your Internet please");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
