package nijat.project.appointment.utils.common;

import nijat.project.appointment.model.enums.UserRole;

public class EnumUtils {
    public static String formatRole(UserRole userRole){
        return userRole.toString().charAt(0) +
                userRole.toString().substring(1).toLowerCase();
    }
}
