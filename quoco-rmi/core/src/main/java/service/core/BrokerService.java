package service.core;

import java.rmi.NotBoundException;
import java.util.List;
import java.rmi.RemoteException;
import java.rmi.Remote;

/**
 * Interface for defining the behaviours of the broker service
 * @author Rem
 *
 */
public interface BrokerService extends Remote {
	public List<Quotation> getQuotations(ClientInfo info) throws RemoteException, NotBoundException;
}
