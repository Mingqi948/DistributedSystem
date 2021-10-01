package quote;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface StockQuoteService {
    @WebMethod public double getStockPrice(String name);
}
