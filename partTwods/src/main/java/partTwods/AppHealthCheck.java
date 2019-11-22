package partTwods;

import com.codahale.metrics.health.HealthCheck;

public class AppHealthCheck extends HealthCheck {

	@Override
	protected Result check() throws Exception {
		// TODO Auto-generated method stub
		return Result.healthy();
	}

}