package training.supportbank;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Transactions {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void readTransactions(String name, String CSVpath)  {
        String line = "";
        String splitBy = ",";
        int count = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(CSVpath));

            while ((line = reader.readLine()) != null) {
                count++;
                String[] transactions = line.split(splitBy);

                if (transactions[1].equals("From")) {
                    continue;
                }

                if(transactions[1].equals(name) || transactions[2].equals(name)) {
                    System.out.println("Date: " + transactions[0]
                            + "    |     Amount: " + transactions[4]
                            + "    |     From: " + transactions[1]
                            + "    |     To: " + transactions[2]
                            + "    |     Narrative: " + transactions[3]);
                }

                if(transactions[1].equals(name) || transactions[2].equals(name)) {
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    try {
                        LocalDate.parse(transactions[0], dateFormat);
                    } catch (DateTimeParseException e) {
                        String errorMessage = "DATE ERROR: not a valid type for 'Date': ERROR on line " + count + " of CSV file";
                        LOGGER.error(errorMessage);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void transactionsJSON(String name, String JSONpath) {
        Gson gson = new Gson();

        JsonObject[] array;

        try  {
            BufferedReader reader = new BufferedReader(new FileReader(JSONpath));

            array = gson.fromJson(reader, JsonObject[].class);

            for(JsonObject object: array) {

                String from = object.get("fromAccount").toString().replace("\"", "");
                String to = object.get("toAccount").toString().replace("\"", "");
                String narrative = object.get("narrative").toString().replace("\"", "");
                String date = object.get("date").toString().replace("\"", "");
                String amount = object.get("amount").toString();

                if(from.equals(name) || to.equals(name)) {
                    System.out.println("Date: " + date
                            + "    |     Amount: " + amount
                            + "    |     From: " +  from
                            + "    |     To: " + to
                            + "    |     Narrative: " + narrative);
                }
            }
        }
        catch (FileNotFoundException e) {
            LOGGER.error("File not found");
        }
    }

    public static void transactionsXML(String name, String XMLpath) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(XMLpath));

            NodeList list = doc.getElementsByTagName("SupportTransaction");

            for (int i = 0; i < list.getLength(); i++){

                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String from = element.getElementsByTagName("From").item(0).getTextContent();
                    String to = element.getElementsByTagName("To").item(0).getTextContent();
                    String narrative = element.getElementsByTagName("Description").item(0).getTextContent();
                    Long dateLong = Long.parseLong(element.getAttribute("Date")) - (365 * 70) - Math.floorDiv(70, 4);
                    LocalDate date = LocalDate.ofEpochDay(dateLong);
                    String amount = element.getElementsByTagName("Value").item(0).getTextContent();

                    if (from.equals(name) || to.equals(name)) {
                        System.out.println("Date: " + date
                                + "    |     Amount: " + amount
                                + "    |     From: " + from
                                + "    |     To: " + to
                                + "    |     Narrative: " + narrative);
                    }
                }
            }

        }  catch (ParserConfigurationException | IOException | org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
    }
}
