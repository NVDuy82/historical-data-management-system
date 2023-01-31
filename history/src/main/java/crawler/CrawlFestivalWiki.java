package crawler;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * Lớp dùng để thu thập lễ hội lịch sử trên Wikipedia
 * @since: 15/01/2023
 */
public class CrawlFestivalWiki extends Crawler {
    public CrawlFestivalWiki() {
        super.setRoot("https://vi.wikipedia.org");
        super.setFolder("data\\Festivalwiki.json");
        super.setStart("https://vi.wikipedia.org/wiki/L%E1%BB%85_h%E1%BB%99i_Vi%E1%BB%87t_Nam");
    }

    /**
     * Thu thập dữ liệu ở trang web chỉ định
     * @param url
     * @throws IOException
     */
    public void scrapePage(String url) throws IOException {
        Document doc;
        try {
            doc = Jsoup.connect(url).userAgent("Jsoup client").timeout(20000).get();
            Elements table = doc.select("table.prettytable.wikitable > tbody > tr");
            for (int i = 1; i < table.size(); i++) {
                JSONObject obj = new JSONObject();
                Elements names = table.get(i).select("td:nth-of-type(3)");
                String name = "";
                for (Element element : names) name += element.text();
                String id = table.get(i).select("td:nth-of-type(3) > a").attr("href");
                id = super.getRoot() + id;
                System.out.println(name);
                System.out.println(id);
                obj.put("name", name);
                obj.put("id", id);
                String description = "";
                // Scrape the organized day
                if (table.get(i).select("td:nth-of-type(1)").text() != null)
                    description += "Ngày bắt đầu (âm lịch): " + table.get(i).select("td:nth-of-type(1)").text() + "\n";
                // Scrape the position
                if (table.get(i).select("td:nth-of-type(2)") != null) {
                    String position = "";
                    for (Element element : table.get(i).select("td:nth-of-type(2)")) {
                        position += element.text();
                    }
                    description += "Vị trí: " + position + "\n";
                }
                // Scrape the first time
                String start = null;
                if (table.get(i).select("td:nth-of-type(4) > a") != null) {
                    start = table.get(i).select("td:nth-of-type(4) > a").text();
                } else {
                    if (table.get(i).select("td:nth-of-type(4)").text() != null) {
                        start = table.get(i).select("td:nth-of-type(4)").text();
                    }
                }
                if (start != null) description += "Lần đầu tổ chức: " + start + "\n";
                // Scrape the connected character
                String connect = null;
                if (table.get(i).select("td:nth-of-type(5)").text() != null) {
                    connect = table.get(i).select("td:nth-of-type(5)").text();
                }
                if (connect != null) description += "Nhân vật liên quan: " + connect + "\n";
                if (scrapeInformation(id, "div.mw-body-content.mw-content-ltr > div > p:first-of-type") != null)
                    description += scrapeInformation(id, "div.mw-body-content.mw-content-ltr > div > p:first-of-type");
                System.out.println(description);
                obj.put("info", description);
                super.getOutput().add(obj);
            }
        } catch (UnknownHostException e){
            throw new UnknownHostException("Turn on your Internet please");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
