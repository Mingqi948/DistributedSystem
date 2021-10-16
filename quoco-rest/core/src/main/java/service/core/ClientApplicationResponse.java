package service.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientApplicationResponse {

    private ClientInfo info;
    private List<Quotation> quotations;

}
