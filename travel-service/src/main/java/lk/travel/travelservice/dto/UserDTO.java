package lk.travel.travelservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    int userID;
    String name;
    String email;
    String pwd;
   RoleDTO role;
}
