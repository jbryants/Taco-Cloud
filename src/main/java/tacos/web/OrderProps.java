package tacos.web;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

// It’s also annotated with @Component
// so that Spring component scanning will automatically discover it 
// and create it as a bean in the Spring application context. 
// This is important, as the next step is to inject the
// bean into OrderController.OrderProps

@Component
@ConfigurationProperties(prefix = "taco.orders")
@Data
public class OrderProps {

	// There’s nothing particularly special about configuration property holders.
	// They’re beans that have their properties injected from the Spring
	// environment.
	// They can be injected into any other bean that needs those properties.

	@Min(value = 5, message = "must be between 5 and 25")
	@Max(value = 25, message = "must be between 5 and 25")
	private int pageSize = 20;

	// setPageSize is called when properties is injected from the Spring env.
//	public void setPageSize(int pageSize) {
//		this.pageSize = pageSize;
//	}

}
