package nijat.project.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppointmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentApplication.class, args);
		//System.out.println(System.getenv().get("JWT_SECRET"));
//		System.out.println(System.getenv().get("JWT_SECRET_KEY"));
	}

}
