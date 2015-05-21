package ua.denysov.weather;



import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class Yahoo {

    private DefaultHttpClient httpclient = new DefaultHttpClient();

    public String getWeather()  {

        HttpResponse response;
        Document document = null;
        try {
            response = this.doRequest();
            document = this.makeDocument(response.getEntity().getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.renderData(document);

    }

    protected HttpResponse doRequest() throws IOException {

        HttpHost host = new HttpHost("weather.yahooapis.com", 80, "http");
        HttpGet request = new HttpGet("/forecastrss?w=918981&u=c");
        System.out.println("Executing requestt to " + host);
        return httpclient.execute(host, request);

    }

    protected Document makeDocument(InputStream xml) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(xml);

    }


    protected String renderData(Document document) {
        Node channel = document.getFirstChild().getFirstChild().getNextSibling();
        NodeList listOfChannelChild = channel.getChildNodes();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < listOfChannelChild.getLength(); i++) {
            Node child = listOfChannelChild.item(i);
            if (child.getNodeName().equals("item")) {
                NodeList data = child.getChildNodes();
                for (int j = 0; j < data.getLength(); j++) {
                    Node piece = data.item(j);
                    if(piece.getNodeName().equals("title")){
                        result.append(piece.getTextContent());
                        result.append("\n");
                    } else if(piece.getNodeName().equals("yweather:condition")) {
                        result.append("Current weather:   ");
                        result.append(piece.getAttributes().getNamedItem("text").getNodeValue()).append(", ");
                        result.append(piece.getAttributes().getNamedItem("temp")).append(", ");
                        result.append(piece.getAttributes().getNamedItem("date")).append(", ");
                        result.append("\n");
                    } else if (piece.getNodeName().equals("yweather:forecast")) {
                        result.append(piece.getAttributes().getNamedItem("day").getNodeValue()).append(", ");
                        result.append(piece.getAttributes().getNamedItem("date").getNodeValue()).append("  ");
                        result.append("low = ").append(piece.getAttributes().getNamedItem("low").getNodeValue()).append("C, high = ");
                        result.append(piece.getAttributes().getNamedItem("high").getNodeValue()).append("C, ");
                        result.append(piece.getAttributes().getNamedItem("text").getNodeValue());
                        result.append("\n");
                    }
                }
            }
        }
        return result.toString();
    }

}
