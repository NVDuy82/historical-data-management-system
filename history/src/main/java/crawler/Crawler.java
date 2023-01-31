package crawler;

import org.json.simple.JSONArray;
import org.jsoup.Jsoup;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Lớp chứa những thuộc tính và các phương thức phục vụ cho việc thu thập dữ liệu
 * @since: 15/01/2023
 */
public class Crawler {
    private String root; // Trang chủ của trang web muốn crawl
    private JSONArray output = new JSONArray(); // Dùng để xuất ra file json
    private String start; // Trang đầu tiên của phần chúng ta muốn crawl
    private String folder; // Đường tới thư mục lưu trữ dữ liệu
    private static final int CONNECTED_AMOUNT = 5; // Số lượng liên kết ta muốn thu thập cho mỗi thực thể

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public JSONArray getOutput() {
        return output;
    }

    public void scrapePage(String start) throws  IOException{
        return;
    }

    /**
     * Dùng để thu thập mô tả, thông tin về các thực thể lịch sử
     * @param url
     * @param css
     * @return info
     * @throws IOException
     */
    public String scrapeInformation(String url, String css) throws IOException{

        String info = "";
        Document doc;
        try {
            doc = Jsoup.connect(url).userAgent("Jsoup client").timeout(20000).get();
            Elements text = doc.select(css);
            for (Element element:text){
                info += element.text() + '\n';
            }
        }  catch (IOException e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     * Dùng để thu thập mô tả, thông tin về các thực thể lịch sử
     * @param urls
     * @param css
     * @return info
     * @throws IOException
     */
    public String scrapeInformation(List<String> urls, String css) throws IOException{
        String info = "";
        Document doc;
        try {
            for (String url:urls){
                info += scrapeInformation(url,css);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     * Dùng để thu thập các liên kết đối với thực thể
     * @param url
     * @param css
     * @return connect
     * @throws IOException
     */
    public List<String> scrapeConnect(String url, String css) throws IOException{
        List<String> connection = null;
        Document doc;
        try {
            doc = Jsoup.connect(url).userAgent("Jsoup client").timeout(20000).get();
            Elements connect = doc.select(css);
            List<String> text = new ArrayList<>();
            for (Element element:connect){
                if(!element.text().equals("")) text.add(element.text());
            }
            connection = kMostOccurrence(text,CONNECTED_AMOUNT);
        } catch (IOException e){
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Dùng để thu thập các liên kết đối với thực thể
     * @param urls
     * @param css
     * @return connect
     * @throws IOException
     */
    public List<String> scrapeConnect(List<String> urls, String css) throws IOException{
        List<String> connection = null;
        Document doc;
        try {
            List<String> text = new ArrayList<>();
            for(String url:urls){
                doc = Jsoup.connect(url).userAgent("Jsoup client").timeout(20000).get();
                Elements connect = doc.select(css);
                for (Element element:connect){
                    if(!element.text().equals("")) text.add(element.text());
                }
            }
            connection = kMostOccurrence(text,CONNECTED_AMOUNT);
        } catch (IOException e){
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Dùng để lưu trữ dữ liệu vào thư mục được chỉ định
     * @param folder
     * @throws IOException
     */
    public void saveData(String folder) throws IOException {
        try (FileWriter file = new FileWriter(folder)){
            file.write(output.toJSONString());
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Dùng để chọn ra k phần tử xuất hiện nhiều nhất trong một dãy
     * @param data
     * @param k
     * @return list_of_k_most_occurrence
     */
    public List<String> kMostOccurrence(List<String> data, int k){
        List<String> res = new ArrayList<>();
        Map<String, Integer> mp = new HashMap<>();

        // Lưu số lượng các phần tử ứng với số lần xuất hiện của chúng
        for (int i = 0; i < data.size(); i++) {
            mp.put(data.get(i), mp.getOrDefault(data.get(i), 0) + 1);
        }

        // Tạo ra một list sao cho mỗi phần tử là một cặp gồm phần tử của dãy ban đầu ứng với số
        // lần xuất hiện của phần tử
        List<Map.Entry<String, Integer> > list = new ArrayList<>(mp.entrySet());

        // Sắp xếp lại list vừa được tạo
        Collections.sort(list,
                new Comparator<>() {
                    public int compare(
                            Map.Entry<String, Integer> o1,
                            Map.Entry<String, Integer> o2)
                    {
                        if (o1.getValue() == o2.getValue())
                            return 0;
                        else
                            return o2.getValue()
                                    - o1.getValue();
                    }
                });
        // Chọn ra k phần tử có số lượng xuất hiện nhiều nhất
        for (int i = 0 ; i < Math.min(k, list.size()); i++){
            res.add(list.get(i).getKey());
        }
        return res;
    }

    /**
     * Dùng để thu thập và lưu trữ dữ liệu
     * @throws IOException
     */
    public void crawlAndSave() throws IOException{
        this.scrapePage(start);
        this.saveData(folder);
        System.out.println("Crawled " + output.size() + " objects");
    }

}
