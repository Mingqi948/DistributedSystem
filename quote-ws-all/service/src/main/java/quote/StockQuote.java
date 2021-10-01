package quote;

import java.util.Map;
import java.util.TreeMap;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.*;

@WebService
@SOAPBinding(style=Style.RPC, use=Use.LITERAL)
public class StockQuote {
    private Map<String, Double> prices = new TreeMap<>();
    {
        prices.put("IBM", 143.79);
        prices.put("GOOGL", 1209.70);
        prices.put("MSFT", 137.44);
        prices.put("FB", 175.25);
        prices.put("TWTR", 40.22);
    }
    
    @WebMethod
    public double getStockPrice(String stockName) {
        Double price = prices.get(stockName);
        return price == null ? -1:price;
    }
}
