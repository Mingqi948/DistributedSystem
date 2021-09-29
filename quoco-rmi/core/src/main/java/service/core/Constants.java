package service.core;

import java.util.ArrayList;
import java.util.List;

public interface Constants {
	static final String BROKER_SERVICE = "bs-BrokerService";
	static final String GIRL_POWER_SERVICE = "qs-GirlPowerService";
	static final String AULD_FELLAS_SERVICE = "qs-AuldFellasService";
	static final String DODGY_DRIVERS_SERVICE = "qs-DodgyDriversService";

	static List<String> getALlCompanies() {
		return new ArrayList<>() {{
			add(GIRL_POWER_SERVICE);
			add(AULD_FELLAS_SERVICE);
			add(DODGY_DRIVERS_SERVICE);
		}};
	}

}